package sondev.indentityservice.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import sondev.indentityservice.dto.request.UserCreationRequest;
import sondev.indentityservice.dto.request.UserUpdateRequest;
import sondev.indentityservice.dto.response.ApiResponse;
import sondev.indentityservice.dto.response.UserResponse;
import sondev.indentityservice.entity.User;
import sondev.indentityservice.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    // Injection Service
    UserService userService;

    @PostMapping
    public ApiResponse<User> createUser(@RequestBody @Valid UserCreationRequest request) {
        var result = userService.createUser(request);
        ApiResponse<User> apiResponse = ApiResponse.<User>builder()
                .result(result)
                .build();
        return apiResponse;
    }

    @GetMapping
    public ApiResponse<List<User>> getUsers() {
        var result =userService.getUsers();
        ApiResponse<List<User>> apiResponse = ApiResponse.<List<User>>builder()
                .result(result)
                .build();
        return apiResponse;
    }

    @GetMapping("/{userId}")
    public ApiResponse<UserResponse> getUser(@PathVariable String userId) {
        var result = userService.getUser(userId);
        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                .result(result)
                .build();
        return apiResponse;
    }

    @PutMapping("/{userId}")
    public ApiResponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request) {
        var result = userService.updateUser(userId, request);
        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                .result(result)
                .build();
        return apiResponse;
    }

    @DeleteMapping("/{userId}")
    public ApiResponse<String> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        ApiResponse<String> apiResponse = ApiResponse.<String>builder()
                .result("User delete successful")
                .build();
        return apiResponse;
    }
}
