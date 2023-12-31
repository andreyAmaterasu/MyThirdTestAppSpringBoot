package ru.kostromin.MyThirdTestAppSpringBoot.controller;

import jakarta.validation.Valid;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.kostromin.MyThirdTestAppSpringBoot.exception.UnsupportedCodeException;
import ru.kostromin.MyThirdTestAppSpringBoot.exception.ValidationFailedException;
import ru.kostromin.MyThirdTestAppSpringBoot.model.Codes;
import ru.kostromin.MyThirdTestAppSpringBoot.model.ErrorCodes;
import ru.kostromin.MyThirdTestAppSpringBoot.model.ErrorMessages;
import ru.kostromin.MyThirdTestAppSpringBoot.model.Request;
import ru.kostromin.MyThirdTestAppSpringBoot.model.Response;
import ru.kostromin.MyThirdTestAppSpringBoot.service.ModifyResponseService;
import ru.kostromin.MyThirdTestAppSpringBoot.service.ValidationService;
import ru.kostromin.MyThirdTestAppSpringBoot.util.DateTimeUtil;

@Slf4j
@RestController
public class MyController {

  private final ValidationService validationService;

  private final ModifyResponseService modifyResponseService;

  public MyController(ValidationService validationService,
      @Qualifier("modifySystemTimeResponseService") ModifyResponseService modifyResponseService) {

    this.validationService = validationService;
    this.modifyResponseService = modifyResponseService;
  }

  @PostMapping(value = "/feedback")
  public ResponseEntity<Response> feedback(
      @Valid @RequestBody Request request, BindingResult bindingResult) throws ParseException {

    log.info("request: {}", request);

    Date firstServiceSystemTime = DateTimeUtil.getCustomFormat().parse(request.getSystemTime());
    log.info("Прошло миллисекунд между получением запросов: " +
        (new Date().getTime() - firstServiceSystemTime.getTime()));

    Response response = Response.builder()
        .uid(request.getUid())
        .operationUid(request.getOperationUid())
        .code(Codes.SUCCESS)
        .errorCode(ErrorCodes.EMPTY)
        .errorMessage(ErrorMessages.EMPTY)
        .build();

    try {
      validationService.isValid(bindingResult);
    } catch (ValidationFailedException | UnsupportedCodeException e) {
      log.error("Ошибка валидации, установка значений для полей code, errorCode и errorMessage");
      response.setCode(Codes.FAILED);
      response.setErrorCode(ErrorCodes.VALIDATION_EXCEPTION);
      response.setErrorMessage(ErrorMessages.VALIDATION);
      return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
      log.error("Непредвиденная ошибка, установка значений для полей code, errorCode и errorMessage");
      response.setCode(Codes.FAILED);
      response.setErrorCode(ErrorCodes.UNKNOWN_EXCEPTION);
      response.setErrorMessage(ErrorMessages.UNKNOWN);
      return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    modifyResponseService.modify(response);

    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
