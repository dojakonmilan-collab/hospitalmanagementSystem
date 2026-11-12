
package workers;

import service.MedicineInventory;

public class Pharmacist implements Runnable {

    private final String name;
    private final MedicineInventory inventory;
    private final int quantityToDispense;

    // Constructor
    public Pharmacist(
            String name,
            MedicineInventory inventory,
            int quantityToDispense) {

        this.name = name;
        this.inventory = inventory;
        this.quantityToDispense = quantityToDispense;
    }

    @Override
    public void run() {

        System.out.println(name + " is dispensing medicine.");

        boolean successful =
            inventory.dispenseMedicine(quantityToDispense);

        if (successful) {
            System.out.println(name + " successfully dispensed medicine.");
        } else {
            System.out.println(name + " could not dispense medicine.");
        }
    }

    public String getName() {
        return name;
    }
}
