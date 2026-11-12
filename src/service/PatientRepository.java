
package service;

import model.Patient;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class PatientRepository {

    private final Map<Integer, Patient> patients;

    private final ReentrantLock lock = new ReentrantLock();

    // Constructor
    public PatientRepository() {
        patients = new HashMap<>();
    }

    // Add a patient
    public void addPatient(Patient patient) {

        lock.lock();

        try {
            patients.put(patient.getPatientId(), patient);

            System.out.println(
                "Patient added: " + patient.getName()
            );

        } finally {
            lock.unlock();
        }
    }

    // Retrieve a patient
    public Patient getPatient(int patientId) {

        lock.lock();

        try {
            return patients.get(patientId);

        } finally {
            lock.unlock();
        }
    }

    // Get total number of patients
    public int getPatientCount() {

        lock.lock();

        try {
            return patients.size();

        } finally {
            lock.unlock();
        }
    }
}
