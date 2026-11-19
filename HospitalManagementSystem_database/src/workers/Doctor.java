
package workers;

import model.Patient;
import service.PatientRepository;

public class Doctor {

    private final String name;
    private final PatientRepository repository;

    public Doctor(String name, PatientRepository repository) {
        this.name = name;
        this.repository = repository;
    }

    // Review patient information
    public void reviewPatient(int patientId) {

        Patient patient = repository.getPatient(patientId);

        if (patient == null) {
            System.out.println("Patient not found.");
            return;
        }

        System.out.println("\nDoctor " + name + " is reviewing:");
        System.out.println(patient);
    }

    // Save medical records to MySQL
    public void updateMedicalRecord(int patientId, String record) {

        Patient patient = repository.getPatient(patientId);

        if (patient == null) {
            System.out.println("Patient not found.");
            return;
        }

        repository.saveMedicalRecord(patientId, record);

        System.out.println(
            "Doctor " + name + " updated " + patient.getName()
            + "'s medical record."
        );
    }

    // View laboratory results
    public void viewLabResults(int patientId) {

        Patient patient = repository.getPatient(patientId);

        if (patient == null) {
            System.out.println("Patient not found.");
            return;
        }

        System.out.println(
            "Lab results for " + patient.getName() + ": "
            + patient.getLabResults()
        );
    }

    public String getName() {
        return name;
    }
}
