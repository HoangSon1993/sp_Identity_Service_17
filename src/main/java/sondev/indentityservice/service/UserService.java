package sondev.indentityservice.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import sondev.indentityservice.dto.request.UserCreationRequest;
import sondev.indentityservice.dto.request.UserUpdateRequest;
import sondev.indentityservice.dto.response.UserResponse;
import sondev.indentityservice.entity.User;
import sondev.indentityservice.exception.AppException;
import sondev.indentityservice.exception.ErrorCode;
import sondev.indentityservice.mapper.UserMapper;
import sondev.indentityservice.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;

    public User createUser(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        // Map data from UserCreationRequest to User with userMapper
        User user = userMapper.toUser(request);
        // Save User into Database
        // Return User was created successful
        return userRepository.save(user);
    }

    public List<User> getUsers() {
        // return all User in the Database
        return userRepository.findAll();
    }

    public UserResponse getUser(String userId) {
        // get User from DB with userId
        var user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
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
