package sondev.indentityservice.exception;

import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import sondev.indentityservice.dto.response.ApiResponse;

import java.util.Map;
import java.util.Objects;

@Slf4j
@ControllerAdvice
//Khai báo cho Spring biết khi có 1 Exception xảy ra th class này sẽ chịu trách nhiệm
public class GlobalExceptionHandle {
    private static final String MIN_ATTRIBUTE = "min";

    // Bắt lỗi tổng quát nhất: Exception
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(Exception exception) {
        ApiResponse apiResponse = ApiResponse.builder().code(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode()).message(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage()).build();
        return ResponseEntity.status(ErrorCode.INVALID_KEY.getStatusCode()).body(apiResponse);
    }

    // Catch Error : RuntimeException
    // AppException extends RuntimeException
    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();

        ApiResponse apiResponse = ApiResponse.builder().code(errorCode.getCode()).message(errorCode.getMessage()).build();
        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> handlingAcessDeniedException(AccessDeniedException exception) {
        ApiResponse apiResponse = ApiResponse.builder().code(ErrorCode.UNAUTHORIZED.getCode()).message(ErrorCode.UNAUTHORIZED.getMessage()).build();
        return ResponseEntity.status(ErrorCode.UNAUTHORIZED.getStatusCode()).body(apiResponse);
    }

    // Catch Error: MethodArgumentNotValidException
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingValidation(MethodArgumentNotValidException exception) {
        String enumKey = exception.getFieldError().getDefaultMessage();

        // Gán nó cho 1 enum error mặc định
        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        Map<String, Object> attributes = null;

        //Bắt trường hợp enum key không đúng
        try {
            errorCode = ErrorCode.valueOf(enumKey);
            var constrainViolation = exception.getBindingResult()
                    .getAllErrors().get(0).unwrap(ConstraintViolation.class);
                    attributes= constrainViolation.getConstraintDescriptor().getAttributes();
                   log.info("Thong tin attributes");
                    log.info(attributes.toString());
        } catch (IllegalArgumentException e) {
            //Xử lý lỗi tại đây
        }

        // Nếu không có lỗi xảy ra
        ApiResponse apiResponse = ApiResponse.builder()
                .code(errorCode.getCode())
                .message(
                        Objects.nonNull(attributes) ? mapAttribute( errorCode.getMessage(), attributes) :errorCode.getMessage()
                )
                .build();

        return ResponseEntity.badRequest().body(apiResponse);
    }
    private String mapAttribute(String message, Map<String, Object> attribute) {
        String minValue = String.valueOf(attribute.get(MIN_ATTRIBUTE));
        return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
    }
}
