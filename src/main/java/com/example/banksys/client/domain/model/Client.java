package com.example.banksys.client.domain.model;

import com.example.banksys.client.infrastructure.NationalIdConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID bankId;

    @NotBlank
    @Column(nullable = false)
    private String firstName;

    @NotBlank
    @Column(nullable = false)
    private String lastName;

    @NotNull
    @Column(nullable = false)
    @Convert(converter = NationalIdConverter.class)
    private NationalId nationalId;

    @Column(unique = true)
    @Email
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClientStatus status = ClientStatus.ACTIVE;

    @ElementCollection
    @CollectionTable(joinColumns = @JoinColumn)
    private Set<PhoneNumber> phoneNumbers = new HashSet<>();

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Version
    private Long version;

    public Client() {
    }

    public Client(String firstName, String lastName, NationalId nationalId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.nationalId = nationalId;
    }

    @PrePersist
    private void onCreate() {
        this.createdAt = LocalDateTime.now(ZoneId.of("Africa/Cairo"));
    }


    public void addPhoneNumber(PhoneNumber phoneNumber){
        phoneNumbers.add(phoneNumber);
    }

    public void removePhoneNumber(PhoneNumber phoneNumber){
        phoneNumbers.remove(phoneNumber);
    }

    public Set<PhoneNumber> getPhoneNumbers(){
        return Collections.unmodifiableSet(phoneNumbers);
    }

    public UUID getBankId() {
        return bankId;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public NationalId getNationalId() {
        return nationalId;
    }
    public void setNationalId(NationalId nationalId) {
        this.nationalId = nationalId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public ClientStatus getStatus() {
        return status;
    }

    public void setStatus(ClientStatus status) {
        this.status = status;
    }

    public Long getVersion() {
        return version;
    }


}
