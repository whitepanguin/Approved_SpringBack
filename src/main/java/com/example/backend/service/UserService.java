package com.example.backend.service;

import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User register(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }
        if (userRepository.findByUserid(user.getUserid()).isPresent()) {
            throw new RuntimeException("이미 존재하는 유져입니다.");
        }

        // 비밀번호 암호화
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);

        // 기본값 설정
        user.setProvider("local");
        user.setProfile(""); // 비어있는 기본 프로필
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        if (user.getPhone() == null) user.setPhone("");
        if (user.getBusinessType() == null) user.setBusinessType("");

        return userRepository.save(user);
    }

    public void modifyUser(User userData) {
        String email = userData.getEmail();

        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("이메일이 필요합니다.");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당 이메일의 유저를 찾을 수 없습니다."));

        user.setName(userData.getName());
        user.setUserid(userData.getUserid());
        user.setAddress(userData.getAddress());
        user.setBirthDate(userData.getBirthDate());
        user.setPhone(userData.getPhone());
        user.setBusinessType(userData.getBusinessType());
        user.setUpdatedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));


        userRepository.save(user); // 업데이트
    }

    public void deleteUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당 이메일의 유저를 찾을 수 없습니다."));

        userRepository.delete(user);
    }

    public String findUserByNameAndBirthDate(String name, String birthDate) {
        User user = userRepository.findByNameAndBirthDate(name, birthDate)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        return user.getEmail();
    }

    public String resetPassword(String email, String name, String birthDate) {
        User user = userRepository.findByEmailAndNameAndBirthDate(email, name, birthDate)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        String tempPassword = generateTempPassword();
        String hashedPassword = BCrypt.hashpw(tempPassword, BCrypt.gensalt());

        user.setPassword(hashedPassword);
        userRepository.save(user);

        return tempPassword;
    }

    private String generateTempPassword() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 6);
    }

    public void updatePassword(String email, String currentPassword, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        if (!BCrypt.checkpw(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 올바르지 않습니다.");
        }

        String hashedNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        user.setPassword(hashedNewPassword);
        userRepository.save(user);
    }

    public boolean isUseridDuplicated(String userid) {
        return userRepository.existsByUserid(userid);
    }

    public List<User> getAllUsers() {
        return userRepository.findAllByOrderByCreatedAtDesc();
    }

    public long getUserCount() {
        return userRepository.count();
    }


}
