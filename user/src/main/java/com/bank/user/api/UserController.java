package com.bank.user.api;

import com.bank.rabbit.RegistrationResponseDto;
import com.bank.user.jpa.dto.UserRequestDto;
import com.bank.user.jpa.dto.UserResponseDto;
import com.bank.user.jpa.dto.UserUpdateRequestDto;
import com.bank.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/registry")
    public ResponseEntity<RegistrationResponseDto> registry(@Valid @RequestBody UserRequestDto dto) {
        RegistrationResponseDto registrationResponseDto = userService.registry(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(registrationResponseDto);
    }

    @GetMapping("/me/{id}")
    public ResponseEntity<UserResponseDto> me(@PathVariable Long id) {
        UserResponseDto returnedUser = userService.getPersonalData(id);
        return ResponseEntity.status(HttpStatus.OK).body(returnedUser);
    }

    @GetMapping("/activation/{email}")
    public ResponseEntity<String> activateUser(@PathVariable String email) {
        userService.activateUser(email);
        return  ResponseEntity.status(HttpStatus.OK).body("User with email: " + email + " has been activated");
    }

    @PostMapping("/{id}")
    public ResponseEntity<UserResponseDto> updatePersonalData(
            @PathVariable Long id, @RequestBody @Valid UserUpdateRequestDto  dto) {
        UserResponseDto responseDto = userService.updatePersonalData(id,dto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
