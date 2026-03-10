package com.fis.boportalservice.core.util;

import com.fis.boportalservice.core.constant.CoreConstant;
import com.fis.boportalservice.core.exception.ErrorCode;
import com.fis.boportalservice.core.exception.ServerSideException;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class UtilityHelper {

  private UtilityHelper() {
  }

  private static final AtomicInteger sequence = new AtomicInteger(1);

  private static final int MAX_VALUE = 99_999_999;

  public static String generateTimestamp() {
    LocalDateTime currentTime = LocalDateTime.now();
    ZoneOffset offset = ZoneOffset.UTC;
    OffsetDateTime offsetDateTime = currentTime.atOffset(offset);
    DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    return offsetDateTime.format(formatter);
  }

  private static String padLeft(String input) {
    StringBuilder output = new StringBuilder(input);
    for (int i = 1; i < 3 - input.length(); i++) {
      output.insert(0, "0");
    }
    return output.toString();
  }

  //    public static <T> T deepClone(ObjectMapper objectMapper, T object, Class<T> clazz) {
  //        try {
  //            return objectMapper.readValue(objectMapper.writeValueAsString(object), clazz);
  //        } catch (Exception e) {
  //            throw new ServerSideException(
  //                    ErrorCode.RESPONSE_ERROR.getCode(), "Error occurred when deep cloning
  // object", e);
  //        }
  //    }

  public static String signNo(String userId, String message) {
    log.info("[signNo] Start signing message with userId: {}, message: {}", userId, message);
    byte[] hmacSha256 = null;
    if (userId != null) {
      message = userId + message;
    }
    try {
      Mac mac = Mac.getInstance(CoreConstant.MAC_INSTANCE);
      SecretKeySpec secretKeySpec =
          new SecretKeySpec(
              CoreConstant.SALT_SIGN.getBytes(StandardCharsets.UTF_8), CoreConstant.MAC_INSTANCE);
      mac.init(secretKeySpec);
      hmacSha256 = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
    } catch (Exception e) {
      log.error("[signNo] Error signing message: {}", e.getMessage(), e);
      throw new ServerSideException(ErrorCode.RESPONSE_ERROR.getCode(), e.getMessage());
    }
    return Base64.getEncoder().encodeToString(hmacSha256);
  }

  //    public static void verifySignNo(String clientNo, String signNo, String... additionalFields)
  // {
  //        if (UtilityHelper.isInvalidSignNo(clientNo, signNo, additionalFields)) {
  //            log.error("Business - Invalid request data");
  //            throw new ClientSideException(ErrorCode.BAD_REQUEST.getCode(), "Invalid request
  // data");
  //        }
  //    }

  //    private static boolean isInvalidSignNo(
  //            String clientNo, String signNo, String... additionalFields) {
  //        StringBuilder data = new StringBuilder();
  //
  //        for (String field : additionalFields) {
  //            if (StringUtils.isBlank(field)) {
  //                return true;
  //            }
  //            data.append(field);
  //        }
  //
  //        String expectedSign = UtilityHelper.signNo(clientNo, data.toString());
  //        return !StringUtils.equals(expectedSign, signNo);
  //    }
}
