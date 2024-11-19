package org.iqmanager.service.bannerService;

import org.iqmanager.dto.BannerToShowDTO;
import org.iqmanager.models.Banner;
import org.iqmanager.repository.BannerDAO;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BannerServiceImpl implements BannerService {
    private final BannerDAO bannerDAO;
    private final ModelMapper modelMapper;

    public BannerServiceImpl(BannerDAO bannerDAO, ModelMapper modelMapper) {
        this.bannerDAO = bannerDAO;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<BannerToShowDTO> getAllBanners() {
        List<Banner> banners = bannerDAO.findAll(Sort.by(Sort.Direction.ASC, "serialNumber"));
        return banners.stream()
                .filter(Banner::isBannerVisible)
                .map(banner -> {
                    BannerToShowDTO dto = modelMapper.map(banner, BannerToShowDTO.class);
                    dto.setBannerBackgroundImage(banner.getBannerBackground());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
