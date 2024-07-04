package sondev.indentityservice.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sondev.indentityservice.dto.request.UserCreationRequest;
import sondev.indentityservice.dto.request.UserUpdateRequest;
import sondev.indentityservice.dto.response.UserResponse;
import sondev.indentityservice.entity.User;
import sondev.indentityservice.enums.Role;
import sondev.indentityservice.exception.AppException;
import sondev.indentityservice.exception.ErrorCode;
import sondev.indentityservice.mapper.UserMapper;
import sondev.indentityservice.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        // Map data from UserCreationRequest to User with userMapper
        User user = userMapper.toUser(request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Create Role Default = USER
        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());
        //user.setRoles(roles);

        // Save User into Database
        // Return User was created successful
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers() {
        // return all User in the Database
        log.info("In method get Users");
        List<User> users =userRepository.findAll();
        return users.stream().map(userMapper::toUserResponse).collect(Collectors.toList());
    }

    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getUser(String userId) {
        // get User from DB with userId
        var authentication = SecurityContextHolder.getContext();
        log.info("In method get User by Id");
        var user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toUserResponse(user);
    }

    public UserResponse getMyInfo(){
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByUsername(name).orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.toUserResponse(user);
    }

    public UserResponse updateUser(String userId, UserUpdateRequest request){
        // get User from DB with userId
        User user =userRepository.findById(userId).orElseThrow(()->new RuntimeException("User not found"));

        // map data from UserUpdateRequest to User by UserMapper
        userMapper.updateUser(user, request);

        // Update database: result is a User after Updated Successful
        User result = userRepository.save(user);

        // Return UserResponse (using UseMapper)
        return userMapper.toUserResponse(result);
    }

    public void deleteUser(String userId){
        userRepository.deleteById(userId);
    }
}
