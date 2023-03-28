package ru.yandex.practicum.filmorate.CustomValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class ReleaseDateValidator implements ConstraintValidator<ReleaseDateConstraint, LocalDate> {
    private final LocalDate initialReleaseDate = LocalDate.of(1895, 12, 28);

    @Override
    public void initialize(ReleaseDateConstraint releaseDate) {
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        return localDate != null && localDate.isAfter(initialReleaseDate);
    }
}
