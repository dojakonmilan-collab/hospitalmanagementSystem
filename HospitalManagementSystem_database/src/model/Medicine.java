
package model;

public class Medicine {

    private final int medicineId;
    private final String name;
    private final String dosage;

    // Constructor
    public Medicine(int medicineId, String name, String dosage) {

        this.medicineId = medicineId;
        this.name = name;
        this.dosage = dosage;
    }

    // Get medicine ID
    public int getMedicineId() {
        return medicineId;
    }

    // Get medicine name
    public String getName() {
        return name;
    }

    // Get dosage
    public String getDosage() {
        return dosage;
    }

    // Display medicine information
    @Override
    public String toString() {

        return "Medicine ID: " + medicineId +
               "\nName: " + name +
               "\nDosage: " + dosage;
    }
}
