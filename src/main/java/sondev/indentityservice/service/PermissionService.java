package sondev.indentityservice.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sondev.indentityservice.dto.request.PermissionRequest;
import sondev.indentityservice.dto.response.PermissionResponse;
import sondev.indentityservice.entity.Permission;
import sondev.indentityservice.mapper.PermissionMapper;
import sondev.indentityservice.repository.PermissionRepository;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service

public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public PermissionResponse create (PermissionRequest request){
        Permission permission = permissionMapper.toPermission(request);
        permission = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    public List<PermissionResponse> getAll(){
        var permissions = permissionRepository.findAll();
        return permissions.stream().map(permissionMapper::toPermissionResponse)
                .toList();
    }

    public void  delete (String permission){
        permissionRepository.deleteById(permission);
    }
}
