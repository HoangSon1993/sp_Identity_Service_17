package sondev.indentityservice.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class DobValidator implements ConstraintValidator<DobConstraint, LocalDate> {
    private int min;
    /**
     * Khởi tạo mỗi khi Constraint được khởi tạo
     * Có thể get thông số của Annotation đó
     * Vd ở đây get min value mà người dùng nhập.
     * */
    @Override
    public void initialize(DobConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.min = constraintAnnotation.min();
        // (min = 18)
    }
    /**
     * isValid: Hàm xử lý data này có đúng hay không
     * */
    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext constraintValidatorContext) {
       if(Objects.isNull(value)){
           return true;
       }
       long years = ChronoUnit.YEARS.between(value, LocalDate.now());

        return years >=  min;
    }
}
