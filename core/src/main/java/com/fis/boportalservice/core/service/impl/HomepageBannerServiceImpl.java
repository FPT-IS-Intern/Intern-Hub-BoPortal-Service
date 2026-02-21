package com.fis.boportalservice.core.service.impl;

import com.fis.boportalservice.core.domain.model.HomepageBanner;
import com.fis.boportalservice.core.domain.repository.HomepageBannerRepository;
import com.fis.boportalservice.core.service.HomepageBannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HomepageBannerServiceImpl implements HomepageBannerService {

    private final HomepageBannerRepository bannerRepository;

    @Override
    public List<HomepageBanner> getActiveBanners() {
        return bannerRepository.findActiveBanners();
    }

    @Override
    public List<HomepageBanner> getAllBanners() {
        return bannerRepository.findAll();
    }

    @Override
    public HomepageBanner getBannerById(UUID id) {
        return bannerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Banner not found with id: " + id));
    }

    @Override
    public HomepageBanner createBanner(HomepageBanner banner) {
        return bannerRepository.save(banner);
    }

    @Override
    public HomepageBanner updateBanner(UUID id, HomepageBanner banner) {
        HomepageBanner existing = getBannerById(id);

        // Update fields
        existing.setTitle(banner.getTitle());
        existing.setDescription(banner.getDescription());
        existing.setDisplayOrder(banner.getDisplayOrder());
        existing.setIsActive(banner.getIsActive());
        existing.setDesktopImageUrl(banner.getDesktopImageUrl());
        existing.setMobileImageUrl(banner.getMobileImageUrl());
        existing.setImageAltText(banner.getImageAltText());
        existing.setActionType(banner.getActionType());
        existing.setActionTarget(banner.getActionTarget());
        existing.setOpenInNewTab(banner.getOpenInNewTab());
        existing.setStartDate(banner.getStartDate());
        existing.setEndDate(banner.getEndDate());

        return bannerRepository.save(existing);
    }

    @Override
    public void deleteBanner(UUID id) {
        bannerRepository.deleteById(id);
    }
}
