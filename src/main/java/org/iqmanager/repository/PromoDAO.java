package org.iqmanager.repository;
import org.iqmanager.models.Promo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromoDAO extends JpaRepository<Promo, Long> {
    Promo findPromoById(long id);
}
