package com.fis.boportalservice.infra.util;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.Nullable;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import static com.fis.boportalservice.infra.constant.FeignConstant.VI;

public class UtilityInfraHelper {

  private JdbcTemplate jdbcTemplate;

  private UtilityInfraHelper() {
  }

  public static String generateTimestamp() {
    LocalDateTime currentTime = LocalDateTime.now();
    ZoneOffset offset = ZoneOffset.UTC;
    OffsetDateTime offsetDateTime = currentTime.atOffset(offset);
    DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    return offsetDateTime.format(formatter);
  }

  public String getErrorDesc(String errorCode, @Nullable String language) {
    // Kiểm tra ngôn ngữ có hay không nếu không lấy mặc định
    if (language == null || language.isEmpty()) {
      language = VI;
    }
    try (Connection connection = jdbcTemplate.getDataSource().getConnection();
         CallableStatement callableStatement =
             connection.prepareCall("{ ? = call GET_ERROR_DESC(?, ?) }")) {
      callableStatement.registerOutParameter(1, Types.VARCHAR);
      callableStatement.setString(2, errorCode);
      callableStatement.setString(3, language);
      callableStatement.execute();
      return callableStatement.getString(1);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return null;
  }
}
