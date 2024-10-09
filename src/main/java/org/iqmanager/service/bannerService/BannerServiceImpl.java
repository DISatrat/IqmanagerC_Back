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
    private final BannerDAO bannerRepository;
    private final ModelMapper modelMapper;

    public BannerServiceImpl(BannerDAO bannerRepository, ModelMapper modelMapper) {
        this.bannerRepository = bannerRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<BannerToShowDTO> getAllBanners() {
        List<Banner> banners = bannerRepository.findAll(Sort.by(Sort.Direction.ASC, "serialNumber"));
        return banners.stream()
                .filter(Banner::isBannerVisible)
                .map(banner -> modelMapper.map(banner, BannerToShowDTO.class))
                .collect(Collectors.toList());
    }
}
