package com.banking.customer.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Customer {

    private final UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private CustomerStatus status;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Customer(UUID id, String firstName, String lastName, String email,
                     String phone, CustomerStatus status,
                     LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Customer create(String firstName, String lastName, String email, String phone) {
        validateName(firstName, "First name");
        validateName(lastName, "Last name");
        validateEmail(email);
        LocalDateTime now = LocalDateTime.now();
        return new Customer(UUID.randomUUID(), firstName, lastName, email, phone,
                CustomerStatus.ACTIVE, now, now);
    }

    /** Reconstitute from persistence — bypasses creation validation. */
    public static Customer reconstitute(UUID id, String firstName, String lastName,
                                        String email, String phone, CustomerStatus status,
                                        LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new Customer(id, firstName, lastName, email, phone, status, createdAt, updatedAt);
    }

    public void updateProfile(String firstName, String lastName, String phone) {
        validateName(firstName, "First name");
        validateName(lastName, "Last name");
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.updatedAt = LocalDateTime.now();
    }

    public void deactivate() {
        if (this.status == CustomerStatus.INACTIVE) {
            throw new IllegalStateException("Customer is already inactive");
        }
        this.status = CustomerStatus.INACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        this.status = CustomerStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

    private static void validateName(String name, String fieldLabel) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(fieldLabel + " is required");
        }
    }

    private static void validateEmail(String email) {
        if (email == null || !email.contains("@") || email.isBlank()) {
            throw new IllegalArgumentException("A valid email address is required");
        }
    }

    public UUID getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public CustomerStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
