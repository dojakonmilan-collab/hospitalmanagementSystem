/*the main program that brings everything together using ExecutorService.
 */

package app;

import model.Patient;
import model.Medicine;
import service.PatientRepository;
import service.MedicineInventory;
import service.LaboratoryService;
import workers.Doctor;
import workers.Nurse;
import workers.LaboratoryTechnician;
import workers.Pharmacist;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class HospitalApplication {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("==================================");
        System.out.println(" HOSPITAL MANAGEMENT SYSTEM");
        System.out.println("==================================");

        // 1. Create shared hospital services
        PatientRepository repository = new PatientRepository();

        Medicine medicine =
            new Medicine(1, "Paracetamol", "500mg");

        MedicineInventory inventory =
            new MedicineInventory(medicine, 50);

        LaboratoryService laboratoryService =
            new LaboratoryService();

        // 2. Register 100 patients
        for (int i = 1; i <= 100; i++) {

            Patient patient =
                new Patient(i, "Patient " + i, 20 + (i % 40));

            repository.addPatient(patient);
        }

        System.out.println("\nTotal registered patients: "
                           + repository.getPatientCount());

        // 3. Create an ExecutorService with 20 worker threads
        ExecutorService executor =
            Executors.newFixedThreadPool(20);

        // 4. Create and submit 5 doctors
        for (int i = 1; i <= 5; i++) {

            Doctor doctor =
                new Doctor("Doctor " + i, repository);

            final int patientId = i;

            executor.submit(() -> {

                doctor.updateMedicalRecord(
                    patientId,
                    "Medical examination completed."
                );

                doctor.reviewPatient(patientId);
            });
        }

        // 5. Create and submit 10 nurses
        for (int i = 1; i <= 10; i++) {

            Nurse nurse =
                new Nurse(
                    "Nurse " + i,
                    repository,
                    laboratoryService
                );

            final int patientId = i;
            final int requestId = i;

            executor.submit(() -> {

                nurse.updateVitalSigns(
                    patientId,
                    "Blood pressure: 120/80, Temperature: 37 C"
                );

                nurse.requestLabTest(
                    requestId,
                    patientId,
                    "Blood Test"
                );
            });
        }

        // 6. Create and submit 3 laboratory technicians
        int[] requestsPerTechnician = {4, 3, 3};

        for (int i = 1; i <= 3; i++) {

            LaboratoryTechnician technician =
                new LaboratoryTechnician(
                    "Technician " + i,
                    laboratoryService,
                    requestsPerTechnician[i - 1]
                );

            executor.submit(technician);
        }

        // 7. Create and submit 2 pharmacists
        for (int i = 1; i <= 2; i++) {

            Pharmacist pharmacist =
                new Pharmacist(
                    "Pharmacist " + i,
                    inventory,
                    30
                );

            executor.submit(pharmacist);
        }

        // 8. Stop accepting new tasks and wait for completion
        executor.shutdown();

        if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {

            System.out.println("Tasks taking too long. Stopping workers.");

            executor.shutdownNow();
        }

        // 9. Display final hospital statistics
        System.out.println("\n==================================");
        System.out.println(" FINAL HOSPITAL REPORT");
        System.out.println("==================================");

        System.out.println(
            "Registered patients: " + repository.getPatientCount()
        );

        System.out.println(
            "Remaining medicine doses: "
            + inventory.getAvailableQuantity()
        );

        System.out.println(
            "Waiting laboratory requests: "
            + laboratoryService.getWaitingRequests()
        );

        // Display the laboratory results for the first 10 patients
        System.out.println("\nLABORATORY RESULTS");

        for (int i = 1; i <= 10; i++) {

            Patient patient = repository.getPatient(i);

            System.out.println(
                patient.getName() + ": " + patient.getLabResults()
            );
        }

        System.out.println("\nHospital operations completed.");
    }
}
