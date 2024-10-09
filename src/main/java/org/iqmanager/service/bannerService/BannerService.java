package org.iqmanager.service.bannerService;


import org.iqmanager.dto.BannerToShowDTO;

import java.util.List;

public interface BannerService {
    List<BannerToShowDTO> getAllBanners();
}