package com.example.backend.controllers.user;

import com.example.backend.exceptions.UserNotFoundException;
import com.example.backend.models.user.UserDto;
import com.example.backend.services.user.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/find")
    public UserDto getUserByEmail(@RequestParam String email) {
        try {
            return userService.getUserByEmail(email);
        } catch (UserNotFoundException e) {
            return null;
        }
    }

    @GetMapping("/check-email")
    public boolean checkEmailExistence(@RequestParam String email) {
        return userService.isEmailExists(email);
    }

    @GetMapping("/all")
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateUser(@Valid @RequestBody UserDto userDto) {
        userService.updateUser(userDto);
        return ResponseEntity.ok("User updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}
