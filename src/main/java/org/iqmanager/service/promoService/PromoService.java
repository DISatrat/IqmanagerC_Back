package org.iqmanager.service.promoService;

import org.iqmanager.dto.PromoDTO;
import org.iqmanager.models.Promo;
import java.util.List;
import java.util.Optional;

public interface PromoService {
    List<PromoDTO> getAllPromo();
    Optional<Promo> getPromoById(Long id);

}
