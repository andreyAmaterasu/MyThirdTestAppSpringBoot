package ru.kostromin.MyThirdTestAppSpringBoot.service;

import org.springframework.validation.BindingResult;
import ru.kostromin.MyThirdTestAppSpringBoot.exception.UnsupportedCodeException;
import ru.kostromin.MyThirdTestAppSpringBoot.exception.ValidationFailedException;

public interface ValidationService {

  void isValid(BindingResult bindingResult) throws ValidationFailedException, UnsupportedCodeException;
}
