package in.bloodsync.bloodsync.controller;

import in.bloodsync.bloodsync.entity.BloodStock;
import in.bloodsync.bloodsync.service.BloodStockService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blood-stock")
public class BloodStockController {

    private final BloodStockService bloodStockService;

    public BloodStockController(BloodStockService bloodStockService) {
        this.bloodStockService = bloodStockService;
    }

    @PostMapping
    public ResponseEntity<BloodStock> addStock(@Valid @RequestBody BloodStock bloodStock) {
        BloodStock saved = bloodStockService.addStock(bloodStock);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<BloodStock>> getAllStock() {
        return ResponseEntity.ok(bloodStockService.getAllStock());
    }

    @GetMapping("/{bloodGroup}")
    public ResponseEntity<BloodStock> getStockByBloodGroup(@PathVariable String bloodGroup) {
        return ResponseEntity.ok(bloodStockService.getStockByBloodGroup(bloodGroup));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BloodStock> updateStock(@PathVariable Integer id, @Valid @RequestBody BloodStock bloodStock) {
        return ResponseEntity.ok(bloodStockService.updateStock(id, bloodStock));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStock(@PathVariable Integer id) {
        bloodStockService.deleteStock(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/increase")
    public ResponseEntity<BloodStock> increaseStock(
            @RequestParam String bloodGroup,
            @RequestParam Integer units) {
        return ResponseEntity.ok(bloodStockService.increaseStock(bloodGroup, units));
    }

    @PatchMapping("/decrease")
    public ResponseEntity<BloodStock> decreaseStock(
            @RequestParam String bloodGroup,
            @RequestParam Integer units) {
        return ResponseEntity.ok(bloodStockService.decreaseStock(bloodGroup, units));
    }
}