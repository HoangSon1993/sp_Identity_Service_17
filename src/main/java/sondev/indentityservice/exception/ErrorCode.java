package sondev.indentityservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(999, "Uncategorized exception error"),

    INVALID_KEY(1001, "Invalid message key"),
    USER_EXISTED(1002, "User existed"),
    INVALID_PASSWORD(1004, "Password must be at least 8 character")
    ;

    int code;
    String message;


}