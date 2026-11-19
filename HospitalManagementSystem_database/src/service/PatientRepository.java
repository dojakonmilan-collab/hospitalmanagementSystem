
package service;

import model.Patient;
import database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;

public class PatientRepository {

    private final ReentrantLock lock = new ReentrantLock();

    // Add or update a patient in MySQL
    public void addPatient(Patient patient) {

        String sql = """
            INSERT INTO patients (patient_id, name, age)
            VALUES (?, ?, ?)
            ON DUPLICATE KEY UPDATE
                name = ?, age = ?
            """;

        lock.lock();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, patient.getPatientId());
            statement.setString(2, patient.getName());
            statement.setInt(3, patient.getAge());

            statement.setString(4, patient.getName());
            statement.setInt(5, patient.getAge());

            statement.executeUpdate();

            System.out.println("Patient saved: " + patient.getName());

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save patient.", e);

        } finally {
            lock.unlock();
        }
    }

    // Retrieve a patient and all associated medical information
    public Patient getPatient(int patientId) {

        String patientSQL =
            "SELECT patient_id, name, age FROM patients WHERE patient_id = ?";

        lock.lock();

        try (Connection connection = DatabaseConnection.getConnection()) {

            Patient patient;

            // Retrieve basic patient information
            try (PreparedStatement statement =
                     connection.prepareStatement(patientSQL)) {

                statement.setInt(1, patientId);

                try (ResultSet result = statement.executeQuery()) {

                    if (!result.next()) {
                        return null;
                    }

                    patient = new Patient(
                        result.getInt("patient_id"),
                        result.getString("name"),
                        result.getInt("age")
                    );
                }
            }

            // Retrieve the latest vital signs
            String vitalSQL = """
                SELECT vital_signs
                FROM vital_signs
                WHERE patient_id = ?
                ORDER BY recorded_at DESC, vital_id DESC
                LIMIT 1
                """;

            try (PreparedStatement statement =
                     connection.prepareStatement(vitalSQL)) {

                statement.setInt(1, patientId);

                try (ResultSet result = statement.executeQuery()) {

                    if (result.next()) {
                        patient.loadVitalSigns(
                            result.getString("vital_signs")
                        );
                    }
                }
            }

            // Retrieve medical history
            String medicalSQL = """
                SELECT record
                FROM medical_records
                WHERE patient_id = ?
                ORDER BY record_id ASC
                """;

            try (PreparedStatement statement =
                     connection.prepareStatement(medicalSQL)) {

                statement.setInt(1, patientId);

                try (ResultSet result = statement.executeQuery()) {

                    while (result.next()) {
                        patient.loadMedicalRecord(
                            result.getString("record")
                        );
                    }
                }
            }

            // Retrieve laboratory results
            String labSQL = """
                SELECT test_name, result
                FROM lab_results
                WHERE patient_id = ?
                ORDER BY result_id ASC
                """;

            try (PreparedStatement statement =
                     connection.prepareStatement(labSQL)) {

                statement.setInt(1, patientId);

                try (ResultSet result = statement.executeQuery()) {

                    while (result.next()) {

                        String labResult =
                            result.getString("test_name") + ": "
                            + result.getString("result");

                        patient.loadLabResult(labResult);
                    }
                }
            }

            return patient;

        } catch (SQLException e) {
            throw new RuntimeException(
                "Failed to retrieve patient information.", e
            );

        } finally {
            lock.unlock();
        }
    }

    // Save new vital signs
    public void saveVitalSigns(int patientId, String vitalSigns) {

        String sql = """
            INSERT INTO vital_signs (patient_id, vital_signs)
            VALUES (?, ?)
            """;

        lock.lock();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, patientId);
            statement.setString(2, vitalSigns);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save vital signs.", e);

        } finally {
            lock.unlock();
        }
    }

    // Save a medical record
    public void saveMedicalRecord(int patientId, String record) {

        String sql = """
            INSERT INTO medical_records (patient_id, record)
            VALUES (?, ?)
            """;

        lock.lock();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, patientId);
            statement.setString(2, record);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save medical record.", e);

        } finally {
            lock.unlock();
        }
    }

    // Save a laboratory result
    public void saveLabResult(
            int patientId,
            String testName,
            String result) {

        String sql = """
            INSERT INTO lab_results (patient_id, test_name, result)
            VALUES (?, ?, ?)
            """;

        lock.lock();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, patientId);
            statement.setString(2, testName);
            statement.setString(3, result);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save laboratory result.", e);

        } finally {
            lock.unlock();
        }
    }

    // Count patients stored in MySQL
    public int getPatientCount() {

        String sql = "SELECT COUNT(*) FROM patients";

        lock.lock();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                 connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            if (result.next()) {
                return result.getInt(1);
            }

            return 0;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to count patients.", e);

        } finally {
            lock.unlock();
        }
    }
}
