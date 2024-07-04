package sondev.indentityservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sondev.indentityservice.dto.request.RoleRequest;
import sondev.indentityservice.dto.response.RoleResponse;
import sondev.indentityservice.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "permissions", ignore = true)
    // Set<String> permissions; =/=> Set<Permission> permissions;
    Role toRole(RoleRequest request);
    RoleResponse toRoleResponse(Role role );
}
