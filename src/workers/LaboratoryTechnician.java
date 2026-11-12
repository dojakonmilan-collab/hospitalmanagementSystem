/*BlockingQueue — allows technicians to receive laboratory requests.
Semaphore — ensures that no more than two technicians use laboratory machines simultaneously. */

package workers;

import model.LabRequest;
import service.LaboratoryService;

public class LaboratoryTechnician implements Runnable {

    private final String name;
    private final LaboratoryService laboratoryService;
    private final int requestsToProcess;

    // Constructor
    public LaboratoryTechnician(
            String name,
            LaboratoryService laboratoryService,
            int requestsToProcess) {

        this.name = name;
        this.laboratoryService = laboratoryService;
        this.requestsToProcess = requestsToProcess;
    }

    @Override
    public void run() {

        for (int i = 0; i < requestsToProcess; i++) {

            boolean machineAcquired = false;

            try {

                // Retrieve a request from the shared queue
                LabRequest request = laboratoryService.takeRequest();

                // Acquire permission to use a laboratory machine
                laboratoryService.acquireMachine();
                machineAcquired = true;

                System.out.println(
                    name + " is processing " + request.getTestName()
                );

                // Simulate laboratory test processing
                Thread.sleep(1000);

                // Save the test result
                request.completeTest("Normal");

                System.out.println(
                    name + " completed request "
                    + request.getRequestId()
                );

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();

                System.out.println(name + " was interrupted.");

                break;

            } finally {

                // Release the machine only if it was acquired
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
