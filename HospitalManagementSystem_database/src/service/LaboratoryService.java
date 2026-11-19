
package service;

import model.LabRequest;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

public class LaboratoryService {

    // Shared queue for laboratory requests
    private final BlockingQueue<LabRequest> requestQueue;

    // Only two laboratory machines are available
    private final Semaphore machineSemaphore;

    // Constructor
    public LaboratoryService() {

        requestQueue = new ArrayBlockingQueue<>(100, true);

        machineSemaphore = new Semaphore(2, true);
    }

    // Nurses submit laboratory requests
    public void submitRequest(LabRequest request)
            throws InterruptedException {

        requestQueue.put(request);

        System.out.println(
            "Laboratory request submitted: " + request.getRequestId()
        );
    }

    // Technicians retrieve laboratory requests
    public LabRequest takeRequest() throws InterruptedException {

        return requestQueue.take();
    }

    // Technicians acquire a laboratory machine
    public void acquireMachine() throws InterruptedException {

        machineSemaphore.acquire();
    }

    // Technicians release a laboratory machine
    public void releaseMachine() {

        machineSemaphore.release();
    }

    // Check the number of waiting requests
    public int getWaitingRequests() {

        return requestQueue.size();
    }
}
