package com.postrowski.ecommerceapp.validation;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ExactlyOneNotNullValidator implements ConstraintValidator<ExactlyOneNotNull, Object> {

    private Set<String> fields;

    @Override
    public void initialize(ExactlyOneNotNull constraintAnnotation) {
        fields = new HashSet<>(Arrays.asList(constraintAnnotation.fields()));
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(value);

        Set<String> nonNullFields = fields.stream()
                .filter(f -> beanWrapper.getPropertyValue(f) != null)
                .collect(Collectors.toSet());

        boolean valid = nonNullFields.size() == 1;

        if (!valid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    String.format("Exactly one of {%s} must be non-null but {%s} are non-null",
                            String.join(", ", fields),
                            String.join(", ", nonNullFields)))
                    .addConstraintViolation();
        }

        return valid;
    }
}
