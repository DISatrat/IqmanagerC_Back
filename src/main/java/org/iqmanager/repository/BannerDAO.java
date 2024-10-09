package org.iqmanager.repository;

import org.iqmanager.models.Banner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerDAO extends JpaRepository<Banner, Long> {
    Banner findBannerById(long id);
}