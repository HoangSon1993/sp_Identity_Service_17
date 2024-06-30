package sondev.indentityservice.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import sondev.indentityservice.dto.request.AuthenticationRequest;
import sondev.indentityservice.dto.response.ApiResponse;
import sondev.indentityservice.dto.response.AuthenticationResponse;
import sondev.indentityservice.service.AuthenticationService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
    AuthenticationService authenticationService;
    @PostMapping("/log-in")
    public ApiResponse<AuthenticationResponse> authenticate (@RequestBody AuthenticationRequest request){
        boolean result = authenticationService.authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(AuthenticationResponse.builder()
                        .authenticated(result)
                        .build())
                .build();
    }
}
