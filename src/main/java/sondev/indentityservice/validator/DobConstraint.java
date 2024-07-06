package sondev.indentityservice.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD}) //Validate các biến trong Object
@Retention(RUNTIME) // Annotation được xử lý lúc Runtime
@Constraint(validatedBy = {DobValidator.class})
public @interface DobConstraint {
    String message() default "Invalid date of birth";

    int min(); // Custom

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default {};
}
