package sondev.indentityservice.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        var result = userService.createUser(request);
        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                .result(result)
                .build();
        return apiResponse;
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping
    public ApiResponse<List<UserResponse>> getUsers() {
        //Lấy thông tin User đang đăng nhập
        var authentication = SecurityContextHolder.getContext();
        var result = userService.getUsers();
        ApiResponse<List<UserResponse>> apiResponse = ApiResponse.<List<UserResponse>>builder()
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

    @GetMapping("/myInfo")
    public ApiResponse<UserResponse> getInfo(){
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
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
