/*Nurses update patient record vital signs and submits laboratory requests through 
blocking queues. */

package workers;

import model.Patient;
import model.LabRequest;
import service.PatientRepository;
import service.LaboratoryService;

public class Nurse {

    private final String name;
    private final PatientRepository repository;
    private final LaboratoryService laboratoryService;

    // Constructor
    public Nurse(
            String name,
            PatientRepository repository,
            LaboratoryService laboratoryService) {

        this.name = name;
        this.repository = repository;
        this.laboratoryService = laboratoryService;
    }

    // Update patient vital signs
    public void updateVitalSigns(int patientId, String vitalSigns) {

        Patient patient = repository.getPatient(patientId);

        if (patient == null) {
            System.out.println("Patient not found.");
            return;
        }

        patient.updateVitalSigns(vitalSigns);

        System.out.println(
            "Nurse " + name + " updated vital signs for "
            + patient.getName()
        );
    }

    // Submit a laboratory request
    public void requestLabTest(
            int requestId,
            int patientId,
            String testName) {

        Patient patient = repository.getPatient(patientId);

        if (patient == null) {
            System.out.println("Patient not found.");
            return;
        }

        LabRequest request =
            new LabRequest(requestId, patient, testName);

        try {

            laboratoryService.submitRequest(request);

            System.out.println(
                "Nurse " + name + " requested " + testName
                + " for " + patient.getName()
            );

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            System.out.println(
                "Nurse " + name + " was interrupted while submitting a request."
            );
        }
    }

    public String getName() {
        return name;
    }
}
