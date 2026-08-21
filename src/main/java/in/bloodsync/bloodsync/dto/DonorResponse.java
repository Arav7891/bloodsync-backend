package in.bloodsync.bloodsync.dto;

import in.bloodsync.bloodsync.entity.Donor;

public class DonorResponse {

    private Integer id;
    private String name;
    private String bloodGroup;
    private String city;
    private String phone;
    private boolean available;
    private UserResponse user;

    public DonorResponse(Donor donor) {
        this.id = donor.getId();
        this.name = donor.getName();
        this.bloodGroup = donor.getBloodGroup();
        this.city = donor.getCity();
        this.phone = donor.getPhone();
        this.available = donor.isAvailable();
        this.user = donor.getUser() != null ? new UserResponse(donor.getUser()) : null;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }
}
