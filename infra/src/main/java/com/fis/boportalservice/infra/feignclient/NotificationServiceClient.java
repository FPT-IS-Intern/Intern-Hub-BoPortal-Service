package com.fis.boportalservice.infra.feignclient;

import com.fis.boportalservice.common.dto.ResponseApi;
import com.fis.boportalservice.infra.configuration.FeignClientCommonConfiguration;
import com.fis.boportalservice.infra.feignclient.dto.template.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
    name = "notification-service",
    url = "${feign.client.config.notification-service.url:http://notification-service:8080}",
    configuration = FeignClientCommonConfiguration.class)
public interface NotificationServiceClient {

  @GetMapping("/noti/internal/templates")
  ResponseApi<List<TemplateResponse>> listTemplates(
      @RequestParam(required = false) String code,
      @RequestParam(required = false) String channel,
      @RequestParam(required = false) String locale,
      @RequestParam(required = false) Boolean active,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size);

  @GetMapping("/noti/internal/templates/summary")
  ResponseApi<TemplateSummaryPageResponse> listTemplateSummaries(
      @RequestParam(required = false) String code,
      @RequestParam(required = false) String channel,
      @RequestParam(required = false, name = "lang") String lang,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size);

  @GetMapping("/noti/internal/templates/{code}/channels")
  ResponseApi<TemplateChannelAvailabilityResponse> getTemplateChannels(
      @PathVariable String code,
      @RequestParam(required = false, name = "lang") String lang);

  @GetMapping("/noti/internal/templates/{code}/definition")
  ResponseApi<TemplateDefinitionResponse> getTemplateDefinition(@PathVariable String code);

  @PostMapping("/noti/internal/templates/definition")
  ResponseApi<TemplateDefinitionResponse> createTemplateDefinition(
      @RequestBody TemplateDefinitionCreateRequest request);

  @PutMapping("/noti/internal/templates/{code}/definition")
  ResponseApi<TemplateDefinitionResponse> updateTemplateDefinition(
      @PathVariable String code,
      @RequestBody TemplateDefinitionUpdateRequest request);

  @DeleteMapping("/noti/internal/templates/definition/{code}")
  ResponseApi<Boolean> deleteTemplateDefinition(@PathVariable String code);

  @GetMapping("/noti/internal/templates/{code}/history")
  ResponseApi<List<TemplateResponse>> getTemplateHistory(
      @PathVariable String code,
      @RequestParam String channel,
      @RequestParam(required = false, name = "lang") String lang);

  @PutMapping("/noti/internal/templates/{code}/restore")
  ResponseApi<TemplateResponse> restoreTemplate(
      @PathVariable String code,
      @RequestBody TemplateRestoreRequest request);

  @PostMapping("/noti/internal/templates")
  ResponseApi<TemplateResponse> createTemplate(@RequestBody TemplateUpsertRequest request);

  @PutMapping("/noti/internal/templates/{id}")
  ResponseApi<TemplateResponse> updateTemplate(
      @PathVariable Long id,
      @RequestBody TemplateUpsertRequest request);
}
