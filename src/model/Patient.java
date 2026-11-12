/*Represents an individual patient.
Stores patient information.
Allows nurses to update vital signs.
Allows doctors to add medical records.
Allows laboratory technicians to add test results.
Protects these individual updates using synchronized. */
package model;

import java.util.ArrayList;
import java.util.List;

public class Patient {

    // Patient information
    private final int patientId;
    private final String name;
    private final int age;

    // Medical information
    private String vitalSigns;
    private final List<String> medicalHistory;
    private final List<String> labResults;

    // Constructor
    public Patient(int patientId, String name, int age) {

        this.patientId = patientId;
        this.name = name;
        this.age = age;

        this.vitalSigns = "Not recorded";

        this.medicalHistory = new ArrayList<>();
        this.labResults = new ArrayList<>();
    }

    // Get patient ID
    public int getPatientId() {
        return patientId;
    }

    // Get patient name
    public String getName() {
        return name;
    }

    // Get patient age
    public int getAge() {
        return age;
    }

    // Update vital signs safely
    public synchronized void updateVitalSigns(String vitalSigns) {
        this.vitalSigns = vitalSigns;

        System.out.println(
            "Vital signs updated for " + name
        );
    }

    // Add medical history safely
    public synchronized void addMedicalRecord(String record) {
        medicalHistory.add(record);
    }

    // Add laboratory results safely
    public synchronized void addLabResult(String result) {
        labResults.add(result);
    }

    // Get vital signs safely
    public synchronized String getVitalSigns() {
        return vitalSigns;
    }

    // Get a safe copy of medical history
    public synchronized List<String> getMedicalHistory() {
        return List.copyOf(medicalHistory);
    }

    // Get a safe copy of laboratory results
    public synchronized List<String> getLabResults() {
        return List.copyOf(labResults);
    }

    // Display patient information
    @Override
    public synchronized String toString() {

        return "Patient ID: " + patientId +
               "\nName: " + name +
               "\nAge: " + age +
               "\nVital Signs: " + vitalSigns +
               "\nMedical History: " + medicalHistory +
               "\nLab Results: " + labResults;
    }
}
