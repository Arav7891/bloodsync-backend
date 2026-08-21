package in.bloodsync.bloodsync.service;
import in.bloodsync.bloodsync.exception.ResourceNotFoundException;
import in.bloodsync.bloodsync.entity.User;
import in.bloodsync.bloodsync.repository.UserRepository;
import in.bloodsync.bloodsync.exception.ResourceNotFoundException;

import in.bloodsync.bloodsync.entity.Donor;
import in.bloodsync.bloodsync.entity.User;
import in.bloodsync.bloodsync.repository.DonorRepository;
import in.bloodsync.bloodsync.repository.UserRepository;
import in.bloodsync.bloodsync.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DonorService {

    private final DonorRepository donorRepository;
    private final UserRepository userRepository;

    public DonorService(DonorRepository donorRepository, UserRepository userRepository) {
        this.donorRepository = donorRepository;
        this.userRepository = userRepository;
    }

    public Donor createDonor(Donor donor) {
        return donorRepository.save(donor);
    }

    public List<Donor> getAllDonors() {
        return donorRepository.findAll();
    }

    public Donor getDonorById(Integer id) {
        return donorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donor not found with id: " + id));
    }

    public Donor updateDonor(Integer id, Donor updatedDonor) {
        Donor existingDonor = donorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donor not found with id: " + id));

        existingDonor.setName(updatedDonor.getName());
        existingDonor.setBloodGroup(updatedDonor.getBloodGroup());
        existingDonor.setCity(updatedDonor.getCity());
        existingDonor.setPhone(updatedDonor.getPhone());
        existingDonor.setAvailable(updatedDonor.isAvailable());

        return donorRepository.save(existingDonor);
    }

    public void deleteDonor(Integer id) {
        if (!donorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Donor not found with id: " + id);
        }
        donorRepository.deleteById(id);
    }

    public List<Donor> searchByBloodGroup(String bloodGroup) {
        return donorRepository.findByBloodGroup(bloodGroup);
    }

    public List<Donor> searchByCity(String city) {
        return donorRepository.findByCity(city);
    }

    public Donor linkUserToDonor(Integer donorId, Integer userId) {
        Donor donor = donorRepository.findById(donorId)
                .orElseThrow(() -> new ResourceNotFoundException("Donor not found with id: " + donorId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        donor.setUser(user);
        return donorRepository.save(donor);
    }
}