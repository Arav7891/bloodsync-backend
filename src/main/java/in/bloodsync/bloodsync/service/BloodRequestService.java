package in.bloodsync.bloodsync.service;
import in.bloodsync.bloodsync.exception.ResourceNotFoundException;
import in.bloodsync.bloodsync.exception.BadRequestException;

import in.bloodsync.bloodsync.entity.BloodRequest;
import in.bloodsync.bloodsync.entity.BloodStock;
import in.bloodsync.bloodsync.repository.BloodRequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BloodRequestService {

    private final BloodRequestRepository bloodRequestRepository;
    private final BloodStockService bloodStockService;

    public BloodRequestService(BloodRequestRepository bloodRequestRepository,
                               BloodStockService bloodStockService) {
        this.bloodRequestRepository = bloodRequestRepository;
        this.bloodStockService = bloodStockService;
    }

    public BloodRequest createRequest(BloodRequest request) {
        request.setStatus("PENDING");
        return bloodRequestRepository.save(request);
    }

    public List<BloodRequest> getAllRequests() {
        return bloodRequestRepository.findAll();
    }

    public BloodRequest getRequestById(Integer id) {
        return bloodRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blood request not found with id: " + id));
    }

    public List<BloodRequest> getRequestsByStatus(String status) {
        return bloodRequestRepository.findByStatus(status);
    }

    public BloodRequest approveRequest(Integer id) {
        BloodRequest request = getRequestById(id);

        if (!"PENDING".equals(request.getStatus())) {
            throw new RuntimeException("Only PENDING requests can be approved. Current status: " + request.getStatus());
        }

        // This will throw an error if stock is insufficient — request stays PENDING in that case
        bloodStockService.decreaseStock(request.getBloodGroup(), request.getUnitsRequested());

        request.setStatus("APPROVED");
        return bloodRequestRepository.save(request);
    }

    public BloodRequest rejectRequest(Integer id) {
        BloodRequest request = getRequestById(id);

        if (!"PENDING".equals(request.getStatus())) {
            throw new RuntimeException("Only PENDING requests can be rejected. Current status: " + request.getStatus());
        }

        request.setStatus("REJECTED");
        return bloodRequestRepository.save(request);
    }

    public void deleteRequest(Integer id) {
        if (!bloodRequestRepository.existsById(id)) {
            throw new RuntimeException("Blood request not found with id: " + id);
        }
        bloodRequestRepository.deleteById(id);
    }

    public boolean checkAvailability(String bloodGroup, Integer unitsNeeded) {
        BloodStock stock = bloodStockService.getStockByBloodGroup(bloodGroup);
        return stock.getUnitsAvailable() >= unitsNeeded;
    }
}