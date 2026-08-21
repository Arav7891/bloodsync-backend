package in.bloodsync.bloodsync.controller;

import in.bloodsync.bloodsync.dto.DonorResponse;
import in.bloodsync.bloodsync.entity.Donor;
import in.bloodsync.bloodsync.service.DonorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/donors")
public class DonorController {

    private final DonorService donorService;

    public DonorController(DonorService donorService) {
        this.donorService = donorService;
    }

    @PostMapping
    public ResponseEntity<DonorResponse> createDonor(@Valid @RequestBody Donor donor) {
        Donor savedDonor = donorService.createDonor(donor);
        return ResponseEntity.status(HttpStatus.CREATED).body(new DonorResponse(savedDonor));
    }

    @GetMapping
    public ResponseEntity<List<DonorResponse>> getAllDonors() {
        List<DonorResponse> response = donorService.getAllDonors().stream()
                .map(DonorResponse::new)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DonorResponse> getDonorById(@PathVariable Integer id) {
        return ResponseEntity.ok(new DonorResponse(donorService.getDonorById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DonorResponse> updateDonor(@PathVariable Integer id, @Valid @RequestBody Donor donor) {
        return ResponseEntity.ok(new DonorResponse(donorService.updateDonor(id, donor)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDonor(@PathVariable Integer id) {
        donorService.deleteDonor(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search/blood-group")
    public ResponseEntity<List<DonorResponse>> searchByBloodGroup(@RequestParam String bloodGroup) {
        List<DonorResponse> response = donorService.searchByBloodGroup(bloodGroup).stream()
                .map(DonorResponse::new)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/city")
    public ResponseEntity<List<DonorResponse>> searchByCity(@RequestParam String city) {
        List<DonorResponse> response = donorService.searchByCity(city).stream()
                .map(DonorResponse::new)
                .toList();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{donorId}/link-user/{userId}")
    public ResponseEntity<DonorResponse> linkUserToDonor(
            @PathVariable Integer donorId,
            @PathVariable Integer userId) {
        return ResponseEntity.ok(new DonorResponse(donorService.linkUserToDonor(donorId, userId)));
    }
}