package com.batch.batchalibou;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class CSVGenerator {

    private static final int NUM_ROWS = 400000;
    private static final String FILE_NAME = "students.csv";
    private static final String SEPARATOR = "|";
    private static final float MIN_SALARY = 90000.00f;
    private static final float MAX_SALARY = 3500000.00f;

    // Sample names for realistic data
    private static final String[] FIRST_NAMES = {"Alice", "Bob", "Charlie", "Diana", "Ethan", "Fiona", "George", "Hannah", "Isaac", "Jasmine", "Kevin", "Laura", "Marcus", "Nora", "Oscar", "Penny", "Quinn", "Rachel", "Sam", "Tina"};
    private static final String[] LAST_NAMES = {"Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez", "Hernandez", "Lopez", "Gonzales", "Wilson", "Anderson", "Thomas", "Taylor", "Moore", "Jackson", "Martin"};
    private static final String[] EMAIL_DOMAINS = {"@example.com", "@university.edu", "@techcorp.org", "@mailservice.net", "@students.ac.uk"};
    
    private final Random random = new Random();

    public static void main(String[] args) {
        new CSVGenerator().generateCSV();
    }

    private void generateCSV() {
        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            // 1. Write Header
            String header = "id" + SEPARATOR + "firstName" + SEPARATOR + "lastName" + SEPARATOR + "email" + SEPARATOR + "result" + SEPARATOR + "salary" + "\n";
            writer.write(header);

            // 2. Write Data Rows
            for (int i = 0; i < NUM_ROWS; i++) {
                String row = generateRow(i + 1);
                writer.write(row);
            }

            System.out.println("✅ Successfully generated " + NUM_ROWS + " rows in " + FILE_NAME);

        } catch (IOException e) {
            System.err.println("❌ An error occurred while writing the CSV file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String generateRow(int index) {
        // ID: unique Long
        long id = ThreadLocalRandom.current().nextLong(1000000000L, 9999999999L) + index;

        // Names
        String firstName = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
        String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
        
        // Realistic Email: f.lastName[0-9]@domain
        String emailPrefix = firstName.toLowerCase().charAt(0) + "." + lastName.toLowerCase();
        if (random.nextBoolean()) { // 50% chance to add a number
            emailPrefix += random.nextInt(100);
        }
        String email = emailPrefix + EMAIL_DOMAINS[random.nextInt(EMAIL_DOMAINS.length)];
        
        // Result: empty (blank cell)
        String result = ""; 
        
        // Salary: float between 90000.00 and 3500000.00
        float salary = generateRandomSalary();

        // Format the float to two decimal places
        String salaryFormatted = String.format("%.2f", salary);

        // Build the pipe-separated row
        return id + SEPARATOR + 
               firstName + SEPARATOR + 
               lastName + SEPARATOR + 
               email + SEPARATOR + 
               result + SEPARATOR + 
               salaryFormatted + "\n";
    }

    /**
     * Generates a random float salary within the specified range, 
     * skewed towards the lower end for realism (as most employees aren't maximum earners).
     */
    private float generateRandomSalary() {
        // Use a power distribution (e.g., cube root) to skew results towards the lower end.
        // random.nextFloat() is uniform between 0.0 and 1.0
        float randomFactor = (float) Math.pow(random.nextFloat(), 3); 
        
        return MIN_SALARY + (randomFactor * (MAX_SALARY - MIN_SALARY));
    }
}