package com.fis.boportalservice.api.controller;

import com.fis.boportalservice.common.dto.ResponseApi;
import com.fis.boportalservice.infra.feignclient.NotificationServiceClient;
import com.fis.boportalservice.infra.feignclient.dto.template.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "9. Admin - Notification Templates")
@RestController
@RequestMapping("/bo-portal/templates")
@RequiredArgsConstructor
@Validated
public class AdminTemplateController {

  private final NotificationServiceClient notificationServiceClient;

  @GetMapping
  @Operation(summary = "List templates")
  public ResponseApi<List<TemplateResponse>> listTemplates(
      @RequestParam(required = false) String code,
      @RequestParam(required = false) String channel,
      @RequestParam(required = false) String locale,
      @RequestParam(required = false) Boolean active,
      @RequestParam(defaultValue = "0") @Min(0) int page,
      @RequestParam(defaultValue = "20") @Min(1) @Max(200) int size) {
    log.info("Request to list templates: code={}, channel={}, locale={}, active={}, page={}, size={}",
        code, channel, locale, active, page, size);
    return notificationServiceClient.listTemplates(code, channel, locale, active, page, size);
  }

  @GetMapping("/summary")
  @Operation(summary = "List templates summary by code")
  public ResponseApi<TemplateSummaryPageResponse> listTemplateSummaries(
      @RequestParam(required = false) String code,
      @RequestParam(required = false) String channel,
      @RequestParam(required = false, name = "lang") String lang,
      @RequestParam(defaultValue = "0") @Min(0) int page,
      @RequestParam(defaultValue = "20") @Min(1) @Max(200) int size) {
    log.info("Request to list template summaries: code={}, channel={}, lang={}, page={}, size={}",
        code, channel, lang, page, size);
    return notificationServiceClient.listTemplateSummaries(code, channel, lang, page, size);
  }

  @GetMapping("/{code}/channels")
  @Operation(summary = "Get active and available channels for template code")
  public ResponseApi<TemplateChannelAvailabilityResponse> getTemplateChannels(
      @PathVariable String code,
      @RequestParam(required = false, name = "lang") String lang) {
    log.info("Request to get template channels: code={}, lang={}", code, lang);
    return notificationServiceClient.getTemplateChannels(code, lang);
  }

  @GetMapping("/{code}/definition")
  @Operation(summary = "Get template definition by code")
  public ResponseApi<TemplateDefinitionResponse> getTemplateDefinition(@PathVariable String code) {
    log.info("Request to get template definition: code={}", code);
    return notificationServiceClient.getTemplateDefinition(code);
  }

  @PostMapping("/definition")
  @Operation(summary = "Create template definition")
  public ResponseApi<TemplateDefinitionResponse> createTemplateDefinition(
      @Valid @RequestBody TemplateDefinitionCreateRequest request) {
    log.info("Request to create template definition: {}", request);
    return notificationServiceClient.createTemplateDefinition(request);
  }

  @PutMapping("/{code}/definition")
  @Operation(summary = "Update template definition by code")
  public ResponseApi<TemplateDefinitionResponse> updateTemplateDefinition(
      @PathVariable String code,
      @Valid @RequestBody TemplateDefinitionUpdateRequest request) {
    log.info("Request to update template definition: code={}, request={}", code, request);
    return notificationServiceClient.updateTemplateDefinition(code, request);
  }

  @DeleteMapping("/definition/{code}")
  @Operation(summary = "Delete template definition by code")
  public ResponseApi<Boolean> deleteTemplateDefinition(@PathVariable String code) {
    log.info("Request to delete template definition: code={}", code);
    return notificationServiceClient.deleteTemplateDefinition(code);
  }

  @GetMapping("/{code}/history")
  @Operation(summary = "Get template version history")
  public ResponseApi<List<TemplateResponse>> getTemplateHistory(
      @PathVariable String code,
      @RequestParam String channel,
      @RequestParam(required = false, name = "lang") String lang) {
    log.info("Request to get template history: code={}, channel={}, lang={}", code, channel, lang);
    return notificationServiceClient.getTemplateHistory(code, channel, lang);
  }

  @PutMapping("/{code}/restore")
  @Operation(summary = "Restore template version")
  public ResponseApi<TemplateResponse> restoreTemplate(
      @PathVariable String code,
      @Valid @RequestBody TemplateRestoreRequest request) {
    log.info("Request to restore template: code={}, request={}", code, request);
    return notificationServiceClient.restoreTemplate(code, request);
  }

  @PostMapping
  @Operation(summary = "Create template")
  public ResponseApi<TemplateResponse> createTemplate(@Valid @RequestBody TemplateUpsertRequest request) {
    log.info("Request to create template: {}", request);
    return notificationServiceClient.createTemplate(request);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update template")
  public ResponseApi<TemplateResponse> updateTemplate(
      @PathVariable Long id,
      @Valid @RequestBody TemplateUpsertRequest request) {
    log.info("Request to update template: id={}, request={}", id, request);
    return notificationServiceClient.updateTemplate(id, request);
  }
}
