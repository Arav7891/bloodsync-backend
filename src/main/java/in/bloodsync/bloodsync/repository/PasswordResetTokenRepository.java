package in.bloodsync.bloodsync.repository;

import in.bloodsync.bloodsync.entity.PasswordResetToken;
import in.bloodsync.bloodsync.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Integer> {
    Optional<PasswordResetToken> findByToken(String token);
    Optional<PasswordResetToken> findByUser(User user);
}