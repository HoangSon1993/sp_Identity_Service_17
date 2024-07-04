package sondev.indentityservice.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import sondev.indentityservice.dto.request.PermissionRequest;
import sondev.indentityservice.dto.request.RoleRequest;
import sondev.indentityservice.dto.response.ApiResponse;
import sondev.indentityservice.dto.response.PermissionResponse;
import sondev.indentityservice.dto.response.RoleResponse;
import sondev.indentityservice.service.PermissionService;
import sondev.indentityservice.service.RoleService;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    RoleService roleService;
    @PostMapping
    public ApiResponse<RoleResponse> create (@RequestBody RoleRequest request){
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.create(request))
                .build();
    }
    @GetMapping
    public ApiResponse<List<RoleResponse>> getAll(){
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getAll())
                .build();
    }
    @DeleteMapping("/{role}")
    public ApiResponse<Void> delete (@PathVariable String role){
        roleService.delete(role);
        return ApiResponse.<Void>builder().build();
    }
}
