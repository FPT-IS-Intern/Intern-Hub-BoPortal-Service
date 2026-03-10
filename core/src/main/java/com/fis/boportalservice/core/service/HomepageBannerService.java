package com.fis.boportalservice.core.service;

import com.fis.boportalservice.core.domain.model.HomepageBanner;

import java.util.List;
import java.util.UUID;

public interface HomepageBannerService {
  List<HomepageBanner> getActiveBanners();

  List<HomepageBanner> getAllBanners();

  HomepageBanner getBannerById(UUID id);

  HomepageBanner createBanner(HomepageBanner banner);

  HomepageBanner updateBanner(UUID id, HomepageBanner banner);

  void deleteBanner(UUID id);
}
