package in.bloodsync.bloodsync.controller;

import in.bloodsync.bloodsync.entity.BloodRequest;
import in.bloodsync.bloodsync.service.BloodRequestService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blood-requests")
public class BloodRequestController {

    private final BloodRequestService bloodRequestService;

    public BloodRequestController(BloodRequestService bloodRequestService) {
        this.bloodRequestService = bloodRequestService;
    }

    @PostMapping
    public ResponseEntity<BloodRequest> createRequest(@Valid @RequestBody BloodRequest request) {
        BloodRequest saved = bloodRequestService.createRequest(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<BloodRequest>> getAllRequests() {
        return ResponseEntity.ok(bloodRequestService.getAllRequests());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BloodRequest> getRequestById(@PathVariable Integer id) {
        return ResponseEntity.ok(bloodRequestService.getRequestById(id));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<BloodRequest>> getRequestsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(bloodRequestService.getRequestsByStatus(status));
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<BloodRequest> approveRequest(@PathVariable Integer id) {
        return ResponseEntity.ok(bloodRequestService.approveRequest(id));
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<BloodRequest> rejectRequest(@PathVariable Integer id) {
        return ResponseEntity.ok(bloodRequestService.rejectRequest(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequest(@PathVariable Integer id) {
        bloodRequestService.deleteRequest(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check-availability")
    public ResponseEntity<Boolean> checkAvailability(
            @RequestParam String bloodGroup,
            @RequestParam Integer units) {
        return ResponseEntity.ok(bloodRequestService.checkAvailability(bloodGroup, units));
    }
}