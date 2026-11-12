/*A doctor should be able to:
1. Access a patient's medical record.
2. Review the patient's vital signs.
3. View completed laboratory results.
4. Add new information to the patient's medical history. */


package workers;

import model.Patient;
import service.PatientRepository;

public class Doctor {

    private final String name;
    private final PatientRepository repository;

    // Constructor
    public Doctor(String name, PatientRepository repository) {
        this.name = name;
        this.repository = repository;
    }

    // Access and review a patient's record
    public void reviewPatient(int patientId) {

        Patient patient = repository.getPatient(patientId);

        if (patient == null) {
            System.out.println("Patient not found.");
            return;
        }

        System.out.println("\nDoctor " + name + " is reviewing:");
        System.out.println(patient);
    }

    // Add information to a patient's medical history
    public void updateMedicalRecord(int patientId, String record) {

        Patient patient = repository.getPatient(patientId);

        if (patient == null) {
            System.out.println("Patient not found.");
            return;
        }

        patient.addMedicalRecord(record);

        System.out.println(
            "Doctor " + name + " updated " + patient.getName() + "'s record."
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
