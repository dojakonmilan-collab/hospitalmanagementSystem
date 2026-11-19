
package workers;

import model.LabRequest;
import service.LaboratoryService;
import service.PatientRepository;

public class LaboratoryTechnician implements Runnable {

    private final String name;
    private final LaboratoryService laboratoryService;
    private final PatientRepository repository;
    private final int requestsToProcess;

    public LaboratoryTechnician(
            String name,
            LaboratoryService laboratoryService,
            PatientRepository repository,
            int requestsToProcess) {

        this.name = name;
        this.laboratoryService = laboratoryService;
        this.repository = repository;
        this.requestsToProcess = requestsToProcess;
    }

    @Override
    public void run() {

        for (int i = 0; i < requestsToProcess; i++) {

            boolean machineAcquired = false;

            try {

                LabRequest request = laboratoryService.takeRequest();

                laboratoryService.acquireMachine();
                machineAcquired = true;

                System.out.println(
                    name + " is processing " + request.getTestName()
                );

                Thread.sleep(1000);

                request.completeTest("Normal");

                // Save completed result in MySQL
                repository.saveLabResult(
                    request.getPatient().getPatientId(),
                    request.getTestName(),
                    request.getResult()
                );

                System.out.println(
                    name + " completed request "
                    + request.getRequestId()
                );

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();

                System.out.println(name + " was interrupted.");
                break;

            } finally {

                if (machineAcquired) {
                    laboratoryService.releaseMachine();
                }
            }
        }
    }

    public String getName() {
        return name;
    }
}
