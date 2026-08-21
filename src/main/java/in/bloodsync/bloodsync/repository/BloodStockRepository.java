package in.bloodsync.bloodsync.repository;

import in.bloodsync.bloodsync.entity.BloodStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BloodStockRepository extends JpaRepository<BloodStock, Integer> {

    Optional<BloodStock> findByBloodGroup(String bloodGroup);
}