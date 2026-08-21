package in.bloodsync.bloodsync.repository;

import in.bloodsync.bloodsync.entity.Donor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DonorRepository extends JpaRepository<Donor, Integer> {

    List<Donor> findByBloodGroup(String bloodGroup);

    List<Donor> findByCity(String city);
}