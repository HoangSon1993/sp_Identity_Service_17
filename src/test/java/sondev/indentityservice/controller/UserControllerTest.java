package sondev.indentityservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import sondev.indentityservice.dto.request.UserCreationRequest;
import sondev.indentityservice.dto.response.UserResponse;
import sondev.indentityservice.service.UserService;

import java.time.LocalDate;
import java.util.Date;

@AutoConfigureMockMvc
@Slf4j
@SpringBootTest //init framework spring boot (connect dbms, init beans)
@TestPropertySource("/test.properties")
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    // Khai báo các dữ liệu dùng chung cho các Test Case
    private UserCreationRequest userCreationRequest;
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
    }

    @Test
        /*
         *
         **/
    void createUser_validRequest_success() throws Exception {
        // log.info("Hello Test");

        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(userCreationRequest);

        //Khi gọi tới userService với Any Argument thì trả ra userResponse
        // Nên userService sẽ không được chạy ở đây
        Mockito.when(userService.createUser(ArgumentMatchers.any()))
                .thenReturn(userResponse);

        // WHEN and THEN
        mockMvc.perform(MockMvcRequestBuilders // create request
                        .post("/users") // Send Request to Controller
                        .contentType(MediaType.APPLICATION_JSON_VALUE) //"application/json"
                        .content(content))
                // Then
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code")
                        .value(1000));
    }

    @Test
    /*
     *
     */
    void createUser_usernameInvalid_fail() throws Exception {
        // GIVEN

        // Điểu chỉnh username thành 3 ký tự để fail
        userCreationRequest.setUsername("Son");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(userCreationRequest);

        Mockito.when(userService.createUser(ArgumentMatchers.any()))
                .thenReturn(userResponse);
        // WHEN and THEN
        mockMvc.perform(MockMvcRequestBuilders
                .post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
                //THEN
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("code")
                        .value(1003))
                .andExpect(MockMvcResultMatchers.jsonPath("message")
                        .value("Username must be at least 4 characters"));
    }
}
