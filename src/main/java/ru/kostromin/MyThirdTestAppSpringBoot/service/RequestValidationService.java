package ru.kostromin.MyThirdTestAppSpringBoot.service;

import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import ru.kostromin.MyThirdTestAppSpringBoot.exception.UnsupportedCodeException;
import ru.kostromin.MyThirdTestAppSpringBoot.exception.ValidationFailedException;

@Slf4j
@Service
public class RequestValidationService implements ValidationService {

  @Override
  public void isValid(BindingResult bindingResult) throws ValidationFailedException, UnsupportedCodeException {

    if (bindingResult.hasErrors()) {
      log.error("Ошибки в bindingResult: {}", bindingResult.getAllErrors()
          .stream()
          .map(ObjectError::getDefaultMessage)
          .collect(Collectors.joining(", ")));
      throw new ValidationFailedException(bindingResult.getFieldError().toString());
    } else if ("123".equals(bindingResult.getFieldValue("uid"))) {
      bindingResult.addError(new ObjectError("request", "uid не должен быть равен 123"));
      throw new UnsupportedCodeException("uid");
    }
  }
}
