package SMP.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import SMP.dto.TeacherProfileResponse;
import SMP.service.TeacherProfileService;

@RestController
@RequestMapping("/api/teacher")
public class TeacherProfileController {

    private final TeacherProfileService teacherProfileService;

    public TeacherProfileController(TeacherProfileService teacherProfileService) {
        this.teacherProfileService = teacherProfileService;
    }

    @GetMapping("/profile")
    public ResponseEntity<TeacherProfileResponse> getTeacherProfile(
            Authentication authentication) {

        return ResponseEntity.ok(
                teacherProfileService.getTeacherProfile(authentication)
        );
    }
}