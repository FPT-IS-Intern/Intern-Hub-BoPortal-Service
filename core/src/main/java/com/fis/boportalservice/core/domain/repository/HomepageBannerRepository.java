package com.fis.boportalservice.core.domain.repository;

import com.fis.boportalservice.core.domain.model.HomepageBanner;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HomepageBannerRepository {
  List<HomepageBanner> findActiveBanners();

  List<HomepageBanner> findAll();

  Optional<HomepageBanner> findById(UUID id);

  HomepageBanner save(HomepageBanner banner);

  void deleteById(UUID id);
}
