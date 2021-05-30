package ru.olegr.accountapp.model.validation.validators;

import ru.olegr.accountapp.model.validation.annotations.MaxScale;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

/**
 * Валидация количества знаков после точки в дробных числах
 */
public class ScaleValidator implements ConstraintValidator<MaxScale, BigDecimal> {

    /**
     * Максимальное количество знаков после запятой в размере транзакции
     */
    private int maxScale;

    @Override
    public void initialize(MaxScale maxScale) {
        this.maxScale = maxScale.value();
    }

    @Override
    public boolean isValid(BigDecimal bigDecimal, ConstraintValidatorContext constraintValidatorContext) {
        int amountScale = bigDecimal.stripTrailingZeros().scale();
        return amountScale <= maxScale;
    }
}
