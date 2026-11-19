
package model;

public class LabRequest {

    private final int requestId;
    private final Patient patient;
    private final String testName;

    private String result;
    private boolean completed;

    // Constructor
    public LabRequest(int requestId, Patient patient, String testName) {

        this.requestId = requestId;
        this.patient = patient;
        this.testName = testName;

        this.result = "Pending";
        this.completed = false;
    }

    public int getRequestId() {
        return requestId;
    }

    public Patient getPatient() {
        return patient;
    }

    public String getTestName() {
        return testName;
    }

    public synchronized void completeTest(String result) {

        this.result = result;
        this.completed = true;

        patient.addLabResult(testName + ": " + result);
    }

    public synchronized String getResult() {
        return result;
    }

    public synchronized boolean isCompleted() {
        return completed;
    }

    @Override
    public String toString() {

        return "Request ID: " + requestId +
               "\nPatient: " + patient.getName() +
               "\nTest: " + testName +
               "\nResult: " + result +
               "\nCompleted: " + completed;
    }
}

