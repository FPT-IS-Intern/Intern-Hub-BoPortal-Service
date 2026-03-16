package com.fis.boportalservice.infra.feignclient.dto.template;

import lombok.Data;

import java.util.List;

@Data
public class TemplateChannelAvailabilityResponse {
  private String code;
  private String locale;
  private List<String> activeChannels;
  private List<String> availableChannels;
}
