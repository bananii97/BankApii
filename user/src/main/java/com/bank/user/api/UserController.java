package com.bank.user.api;

import com.bank.user.jpa.dto.UserRequestDto;
import com.bank.user.jpa.dto.UserResponseDto;
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
    public ResponseEntity<UserResponseDto> registry(@Valid @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto returnedUser = userService.registry(userRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(returnedUser);
    }
}
