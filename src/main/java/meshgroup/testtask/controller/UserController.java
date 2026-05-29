package meshgroup.testtask.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import meshgroup.testtask.dto.request.EmailRequest;
import meshgroup.testtask.dto.request.PhoneRequest;
import meshgroup.testtask.dto.response.UserResponse;
import meshgroup.testtask.service.UserService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/email")
    public ResponseEntity<Void> addEmail(@Valid @RequestBody EmailRequest request,
                                         Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        userService.addEmail(userId, request.getEmail());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/email")
    public ResponseEntity<Void> deleteEmail(@Valid @RequestBody EmailRequest request,
                                             Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        userService.deleteEmail(userId, request.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/phone")
    public ResponseEntity<Void> addPhone(@Valid @RequestBody PhoneRequest request,
                                          Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        userService.addPhone(userId, request.getPhone());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/phone")
    public ResponseEntity<Void> deletePhone(@Valid @RequestBody PhoneRequest request,
                                             Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        userService.deletePhone(userId, request.getPhone());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Page<UserResponse>> searchUsers(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @ParameterObject @PageableDefault(size = 10, sort = "name") Pageable pageable) {
        return ResponseEntity.ok(userService.searchUsers(dateOfBirth, phone, name, email, pageable));
    }
}
