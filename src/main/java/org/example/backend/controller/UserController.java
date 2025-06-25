package org.example.backend.controller;

import org.example.backend.model.User;
import org.example.backend.repository.UserRepository;
import org.example.backend.security.CustomUserDetails;
import org.example.backend.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.List;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Map;


@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/getUserInfo")
    public ResponseEntity<?> getUserInfo() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof CustomUserDetails user) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "사용자 인증 실패"));
        }
    }


    @GetMapping("/check-duplicate")
    public ResponseEntity<?> checkDuplicate(@RequestParam(required = false) String userid) {

        if (userid == null || userid.trim().isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "닉네임이 필요합니다."));
        }

        boolean exists = userService.isUseridDuplicated(userid);

        return ResponseEntity.ok(Map.of("exists", exists));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            userService.register(user);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "registerSuccess", true,
                            "message", "축하합니다. 회원가입이 완료되었습니다."
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of(
                            "registerSuccess", false,
                            "message", e.getMessage()  // "이미 존재하는 이메일입니다."
                    ));
        }
    }



    @PutMapping("/modify")
    public ResponseEntity<?> modify(@RequestBody User user) {
        try {
            userService.modifyUser(user);
            return ResponseEntity.ok(Map.of(
                    "updateSuccess", true,
                    "message", "회원정보가 수정되었습니다."
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "updateSuccess", false,
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "updateSuccess", false,
                    "message", "회원정보 수정 중 에러가 발생했습니다."
            ));
        }
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> remove(@RequestBody Map<String, String> body) {
        String email = body.get("email");

        try {
            userService.deleteUserByEmail(email);
            return ResponseEntity.ok(Map.of(
                    "updateSuccess", true,
                    "message", "회원탈퇴 완료. 다음생에 만나요"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "updateSuccess", false,
                    "message", "회원 탈퇴 중 오류 발생"
            ));
        }
    }

    @PostMapping("/findUser")
    public ResponseEntity<?> findUser(@RequestBody Map<String, String> body) {
        String name = body.get("name");
        String birthDate = body.get("birthDate");

        try {
            String email = userService.findUserByNameAndBirthDate(name, birthDate);
            return ResponseEntity.ok(Map.of("message", "이메일를 찾았습니다: " + email));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "서버 오류"));
        }
    }

    @PostMapping("/findPass")
    public ResponseEntity<?> findPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String name = body.get("name");
        String birthDate = body.get("birthDate");

        try {
            String tempPassword = userService.resetPassword(email, name, birthDate);
            return ResponseEntity.ok(Map.of("message", "임시 비밀번호: " + tempPassword));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "서버 오류"));
        }
    }

    @PutMapping("/updatePassword")
    public ResponseEntity<?> updatePassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String currentPassword = body.get("currentPassword");
        String newPassword = body.get("newPassword");

        try {
            userService.updatePassword(email, currentPassword, newPassword);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "비밀번호가 성공적으로 변경되었습니다."
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "서버 오류로 인해 비밀번호 변경에 실패했습니다."
            ));
        }
    }

    @GetMapping("/allUsers")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "전체 사용자 조회 실패"));
        }
    }

    @GetMapping("/UserCount")
    public ResponseEntity<?> getUserCount() {
        try {
            long count = userService.getUserCount();
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "유저 수 조회 성공",
                    "count", count
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "유저 수 조회 실패"));
        }
    }



    @PostMapping(value = "/picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadProfilePicture(@RequestParam("picture") MultipartFile file,
                                                  @RequestParam("email") String email) {
        try {
            String uploadDir = "uploads/profiles";
            Path uploadPath = Path.of(uploadDir);
            Files.createDirectories(uploadPath);

            String filename = System.currentTimeMillis() + "-" + file.getOriginalFilename();
            Path target = uploadPath.resolve(filename);
            file.transferTo(target);

            User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
            String filePath = "/uploads/profiles/" + filename;
            user.setProfile(filePath);
            userRepository.save(user);

            return ResponseEntity.ok(Map.of("uploadSuccess", true, "filePath", filePath));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("uploadSuccess", false, "message", "파일 업로드 실패: " + e.getMessage()));
        }
    }

    @PostMapping("/certifyRequest")
    public ResponseEntity<?> uploadCertifyImages(@RequestParam("imageUrls") List<MultipartFile> files) {
        try {
            String uploadDir = "uploads/certify";
            File folder = new File(uploadDir);
            if (!folder.exists()) folder.mkdirs();

            List<String> savedPaths = new ArrayList<>();
            for (MultipartFile file : files) {
                String filename = System.currentTimeMillis() + "-" + file.getOriginalFilename();
                File dest = new File(folder, filename);
                file.transferTo(dest);
                savedPaths.add("/uploads/certify/" + filename);
            }

            return ResponseEntity.ok(Map.of("uploadSuccess", true, "message", "강사 인증 이미지 업로드 성공", "filePaths", savedPaths));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("uploadSuccess", false, "message", "파일 업로드 실패"));
        }
    }



    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }
}