package ru.kostromin.MyThirdTestAppSpringBoot.service;

import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.kostromin.MyThirdTestAppSpringBoot.model.Response;
import ru.kostromin.MyThirdTestAppSpringBoot.util.DateTimeUtil;

@Slf4j
@Service
public class ModifySystemTimeResponseService implements ModifyResponseService {

  @Override
  public Response modify(Response response) {

    log.info("Модификация поля systemTime в response");
    response.setSystemTime(DateTimeUtil.getCustomFormat().format(new Date()));
    return response;
  }
}
