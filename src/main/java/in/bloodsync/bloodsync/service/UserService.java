package in.bloodsync.bloodsync.service;
import in.bloodsync.bloodsync.exception.BadRequestException;
import in.bloodsync.bloodsync.entity.User;
import in.bloodsync.bloodsync.exception.ResourceNotFoundException;
import in.bloodsync.bloodsync.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Register User
    public User registerUser(User user) {
        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
            throw new BadRequestException("You cannot self-register as an admin.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User registerAdmin(User user) {
        user.setRole("ADMIN");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // Get User
    public User getUserById(Integer id) {

        return userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + id));
    }

    // Update User
    public User updateUser(Integer id, User updatedUser) {

        User existingUser = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + id));

        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());

        // Hash the new password before saving
        existingUser.setPassword(
                passwordEncoder.encode(updatedUser.getPassword())
        );

        existingUser.setRole(updatedUser.getRole());

        return userRepository.save(existingUser);
    }

    // Delete User
    public void deleteUser(Integer id) {

        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "User not found with id: " + id);
        }

        userRepository.deleteById(id);
    }
}