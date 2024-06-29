package sondev.indentityservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import sondev.indentityservice.dto.request.UserCreationRequest;
import sondev.indentityservice.dto.request.UserUpdateRequest;
import sondev.indentityservice.dto.response.UserResponse;
import sondev.indentityservice.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
