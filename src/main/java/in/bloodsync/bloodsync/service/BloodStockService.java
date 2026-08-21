package in.bloodsync.bloodsync.service;
import in.bloodsync.bloodsync.exception.ResourceNotFoundException;
import in.bloodsync.bloodsync.exception.BadRequestException;

import in.bloodsync.bloodsync.entity.BloodStock;
import in.bloodsync.bloodsync.repository.BloodStockRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BloodStockService {

    private final BloodStockRepository bloodStockRepository;

    public BloodStockService(BloodStockRepository bloodStockRepository) {
        this.bloodStockRepository = bloodStockRepository;
    }

    public BloodStock addStock(BloodStock bloodStock) {
        return bloodStockRepository.save(bloodStock);
    }

    public List<BloodStock> getAllStock() {
        return bloodStockRepository.findAll();
    }

    public BloodStock getStockByBloodGroup(String bloodGroup) {
        return bloodStockRepository.findByBloodGroup(bloodGroup)
                .orElseThrow(() -> new RuntimeException("No stock record found for blood group: " + bloodGroup));
    }

    public BloodStock updateStock(Integer id, BloodStock updatedStock) {
        BloodStock existingStock = bloodStockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock not found with id: " + id));

        existingStock.setBloodGroup(updatedStock.getBloodGroup());
        existingStock.setUnitsAvailable(updatedStock.getUnitsAvailable());

        return bloodStockRepository.save(existingStock);
    }

    public void deleteStock(Integer id) {
        if (!bloodStockRepository.existsById(id)) {
            throw new RuntimeException("Stock not found with id: " + id);
        }
        bloodStockRepository.deleteById(id);
    }

    public BloodStock increaseStock(String bloodGroup, Integer units) {
        BloodStock stock = bloodStockRepository.findByBloodGroup(bloodGroup)
                .orElseThrow(() -> new RuntimeException("No stock record found for blood group: " + bloodGroup));

        stock.setUnitsAvailable(stock.getUnitsAvailable() + units);
        return bloodStockRepository.save(stock);
    }

    public BloodStock decreaseStock(String bloodGroup, Integer units) {
        BloodStock stock = bloodStockRepository.findByBloodGroup(bloodGroup)
                .orElseThrow(() -> new RuntimeException("No stock record found for blood group: " + bloodGroup));

        if (stock.getUnitsAvailable() < units) {
            throw new RuntimeException("Insufficient stock for blood group: " + bloodGroup);
        }

        stock.setUnitsAvailable(stock.getUnitsAvailable() - units);
        return bloodStockRepository.save(stock);
    }
}