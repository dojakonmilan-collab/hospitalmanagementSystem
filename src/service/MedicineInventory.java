
package service;

import model.Medicine;

public class MedicineInventory {

    private final Medicine medicine;
    private int quantity;

    // Constructor
    public MedicineInventory(Medicine medicine, int initialQuantity) {
        this.medicine = medicine;
        this.quantity = initialQuantity;
    }

    // Dispense medicine safely
    public synchronized boolean dispenseMedicine(int requestedQuantity) {

        if (requestedQuantity <= 0) {
            System.out.println("Invalid quantity requested.");
            return false;
        }

        if (quantity < requestedQuantity) {
            System.out.println("Insufficient stock of " + medicine.getName());
            return false;
        }

        quantity -= requestedQuantity;

        System.out.println(
            requestedQuantity + " dose(s) dispensed. Remaining stock: " + quantity
        );

        return true;
    }

    // Check remaining stock
    public synchronized int getAvailableQuantity() {
        return quantity;
    }

    public Medicine getMedicine() {
        return medicine;
    }
}
