package in.bloodsync.bloodsync.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "blood_requests")
public class BloodRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Requester name is required")
    private String requesterName;

    @NotBlank(message = "Blood group is required")
    private String bloodGroup;

    @NotNull(message = "Units requested is required")
    @Min(value = 1, message = "Units requested must be at least 1")
    private Integer unitsRequested;

    @NotBlank(message = "City is required")
    private String city;

    private String status;

    public BloodRequest() {
    }

    public BloodRequest(String requesterName, String bloodGroup, Integer unitsRequested, String city) {
        this.requesterName = requesterName;
        this.bloodGroup = bloodGroup;
        this.unitsRequested = unitsRequested;
        this.city = city;
        this.status = "PENDING";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public Integer getUnitsRequested() {
        return unitsRequested;
    }

    public void setUnitsRequested(Integer unitsRequested) {
        this.unitsRequested = unitsRequested;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}