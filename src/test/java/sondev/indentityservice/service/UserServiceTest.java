package sondev.indentityservice.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import sondev.indentityservice.dto.request.UserCreationRequest;
import sondev.indentityservice.dto.response.UserResponse;
import sondev.indentityservice.entity.User;
import sondev.indentityservice.exception.AppException;
import sondev.indentityservice.repository.UserRepository;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static sondev.indentityservice.exception.ErrorCode.USER_EXISTED;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRepository;
    // Khai báo các dữ liệu dùng chung cho các Test Case
    private UserCreationRequest userCreationRequest;
    private User user;
    private UserResponse userResponse;

    @BeforeEach  // method initData được chạy trước để chuẩn bị dữ liệu cho các Test Case
    public void initData() {
        LocalDate dob = LocalDate.of(1992, 9, 2);
        userCreationRequest = UserCreationRequest.builder()
                .username("Sonlh2")
                .firstName("son")
                .lastName("lyhoang")
                .password("12345678")
                .dob(dob)
                .build();

        userResponse = UserResponse.builder()
                .id("f068579e6e35")
                .username("Sonlh2")
                .firstName("son")
                .lastName("lyhoang")
                .dob(dob)
                .build();
        user = User.builder()
                .id("f068579e6e35")
                .username("Sonlh2")
                .firstName("son")
                .lastName("lyhoang")
                .dob(dob)
                .build();
    }

    @Test
    void createUser_valiedRequest_success() {
        //GIVE
        // Sử dụng Mockito để trả về giá trị luôn chứ k gọi các method của layer Repository
        // Cho nên các Method ở Layer Repository sẽ không thật sự được gọi.
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(user);

        //WHEN
        var response = userService.createUser(userCreationRequest);

        //THEN
        Assertions.assertThat(response.getId()).isEqualTo("f068579e6e35");
        Assertions.assertThat(response.getUsername()).isEqualTo("Sonlh2");
    }

    @Test
    void createUser_userExists_fail() {
        //GIVE
        // Sử dụng Mockito để trả về giá trị luôn chứ k gọi các method của layer Repository
        // Cho nên các Method ở Layer Repository sẽ không thật sự được gọi.
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        //Đoạn này không cần thiết nữa
        //when(userRepository.save(any())).thenReturn(user);

        //WHEN
        var exception = assertThrows(AppException.class,
                () -> userService.createUser(userCreationRequest));

        //THEN
        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1002);
        Assertions.assertThat(exception.getErrorCode()).isEqualTo(USER_EXISTED);

    }
}
