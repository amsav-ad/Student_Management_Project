package SMP.controller;

import SMP.dto.StudentProfileResponse;
import SMP.service.StudentProfileService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/student")
public class StudentProfileController {

    private final StudentProfileService studentProfileService;

    public StudentProfileController(StudentProfileService studentProfileService) {
        this.studentProfileService = studentProfileService;
    }

    @GetMapping("/profile")
    public ResponseEntity<StudentProfileResponse> getStudentProfile(
            Authentication authentication) {

        StudentProfileResponse response =
                studentProfileService.getStudentProfile(authentication);

        return ResponseEntity.ok(response);
    }
}