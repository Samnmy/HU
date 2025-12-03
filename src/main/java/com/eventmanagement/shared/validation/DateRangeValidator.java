package com.eventmanagement.shared.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.time.LocalDateTime;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, Object> {

    private String startDateField;
    private String endDateField;

    @Override
    public void initialize(ValidDateRange constraintAnnotation) {
        this.startDateField = constraintAnnotation.startDate();
        this.endDateField = constraintAnnotation.endDate();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        BeanWrapper beanWrapper = new BeanWrapperImpl(value);
        Object startDateObj = beanWrapper.getPropertyValue(startDateField);
        Object endDateObj = beanWrapper.getPropertyValue(endDateField);

        if (startDateObj == null || endDateObj == null) {
            return true;
        }

        if (!(startDateObj instanceof LocalDateTime) || !(endDateObj instanceof LocalDateTime)) {
            return true;
        }

        LocalDateTime startDate = (LocalDateTime) startDateObj;
        LocalDateTime endDate = (LocalDateTime) endDateObj;

        if (startDate.isAfter(endDate)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode(endDateField)
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}