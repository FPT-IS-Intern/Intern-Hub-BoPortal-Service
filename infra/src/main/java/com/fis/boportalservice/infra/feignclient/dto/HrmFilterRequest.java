package com.fis.boportalservice.infra.feignclient.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HrmFilterRequest {
  String keyword;
  List<String> sysStatuses;
  List<String> roles;
  List<String> positions;
}
