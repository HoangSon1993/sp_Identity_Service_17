package sondev.indentityservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import sondev.indentityservice.dto.request.PermissionRequest;
import sondev.indentityservice.dto.request.UserCreationRequest;
import sondev.indentityservice.dto.request.UserUpdateRequest;
import sondev.indentityservice.dto.response.PermissionResponse;
import sondev.indentityservice.dto.response.UserResponse;
import sondev.indentityservice.entity.Permission;
import sondev.indentityservice.entity.User;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);

    void updatePermissionn(@MappingTarget Permission permission, UserUpdateRequest request);
}
