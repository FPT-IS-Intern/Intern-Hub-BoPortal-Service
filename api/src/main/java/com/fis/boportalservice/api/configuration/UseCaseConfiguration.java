package com.fis.boportalservice.api.configuration;

import com.fis.boportalservice.core.domain.port.PasswordHasher;
import com.fis.boportalservice.core.domain.repository.BoAdminUserRepository;
import com.fis.boportalservice.core.domain.repository.BoRefreshTokenRepository;
import com.fis.boportalservice.core.domain.repository.BoTokenProvider;
import com.fis.boportalservice.core.domain.usecase.BoAuthUsecase;
import com.fis.boportalservice.core.domain.usecase.impl.BoAuthUsecaseImpl;
import com.fis.boportalservice.core.domain.repository.AllowedIpRangeRepository;
import com.fis.boportalservice.core.domain.repository.AttendanceLocationRepository;
import com.fis.boportalservice.core.domain.repository.BranchRepository;
import com.fis.boportalservice.core.domain.repository.HomepageBannerRepository;
import com.fis.boportalservice.core.domain.repository.PortalMenuRepository;
import com.fis.boportalservice.core.domain.repository.SecurityConfigRepository;
import com.fis.boportalservice.core.domain.repository.SystemConfigRepository;
import com.fis.boportalservice.core.service.AllowedIpRangeService;
import com.fis.boportalservice.core.service.AttendanceLocationService;
import com.fis.boportalservice.core.service.BranchService;
import com.fis.boportalservice.core.service.HomepageBannerService;
import com.fis.boportalservice.core.service.PortalMenuService;
import com.fis.boportalservice.core.service.SecurityConfigService;
import com.fis.boportalservice.core.service.SystemConfigService;
import com.fis.boportalservice.core.service.impl.AllowedIpRangeServiceImpl;
import com.fis.boportalservice.core.service.impl.AttendanceLocationServiceImpl;
import com.fis.boportalservice.core.service.impl.BranchServiceImpl;
import com.fis.boportalservice.core.service.impl.HomepageBannerServiceImpl;
import com.fis.boportalservice.core.service.impl.PortalMenuServiceImpl;
import com.fis.boportalservice.core.service.impl.SecurityConfigServiceImpl;
import com.fis.boportalservice.core.service.impl.SystemConfigServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfiguration {

  @Bean
  public BoAuthUsecase boAuthUsecase(
      BoAdminUserRepository boAdminUserRepository,
      BoRefreshTokenRepository boRefreshTokenRepository,
      BoTokenProvider boTokenProvider,
      PasswordHasher passwordHasher
  ) {
    return new BoAuthUsecaseImpl(
        boAdminUserRepository,
        boRefreshTokenRepository,
        boTokenProvider,
        passwordHasher
    );
  }

  @Bean
  public AllowedIpRangeService allowedIpRangeService(AllowedIpRangeRepository allowedIpRangeRepository) {
    return new AllowedIpRangeServiceImpl(allowedIpRangeRepository);
  }

  @Bean
  public AttendanceLocationService attendanceLocationService(
      AttendanceLocationRepository attendanceLocationRepository) {
    return new AttendanceLocationServiceImpl(attendanceLocationRepository);
  }

  @Bean
  public BranchService branchService(BranchRepository branchRepository) {
    return new BranchServiceImpl(branchRepository);
  }

  @Bean
  public HomepageBannerService homepageBannerService(HomepageBannerRepository homepageBannerRepository) {
    return new HomepageBannerServiceImpl(homepageBannerRepository);
  }

  @Bean
  public PortalMenuService portalMenuService(PortalMenuRepository portalMenuRepository) {
    return new PortalMenuServiceImpl(portalMenuRepository);
  }

  @Bean
  public SecurityConfigService securityConfigService(SecurityConfigRepository securityConfigRepository) {
    return new SecurityConfigServiceImpl(securityConfigRepository);
  }

  @Bean
  public SystemConfigService systemConfigService(SystemConfigRepository systemConfigRepository) {
    return new SystemConfigServiceImpl(systemConfigRepository);
  }
}
