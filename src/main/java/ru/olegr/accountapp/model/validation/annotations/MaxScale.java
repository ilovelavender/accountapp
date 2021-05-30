package ru.olegr.accountapp.model.validation.annotations;

import ru.olegr.accountapp.model.validation.validators.ScaleValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ScaleValidator.class)
public @interface MaxScale {

    int value();

    String message() default "Number should have no more than {value} fractional numbers";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default { };
}
