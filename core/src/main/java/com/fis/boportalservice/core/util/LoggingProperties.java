package com.fis.boportalservice.core.util;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class LoggingProperties {
  private List<String> sensitiveFields = new ArrayList<>();
}
