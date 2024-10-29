package org.iqmanager.service.promoService;


import org.iqmanager.dto.PromoDTO;
import org.iqmanager.models.Promo;
import org.iqmanager.repository.PromoDAO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PromoServiceImpl implements PromoService{

    private final PromoDAO promoDAO;
    private final ModelMapper modelMapper;

    public PromoServiceImpl(PromoDAO promoDAO, ModelMapper modelMapper) {
        this.promoDAO = promoDAO;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<PromoDTO> getAllPromo() {
        List<Promo> promos = promoDAO.findAll();
        return promos.stream().map(promo -> modelMapper.map(promo, PromoDTO.class)).collect(Collectors.toList());
    }
    @Override
    public Optional<Promo> getPromoById(Long id) {
        return promoDAO.findById(id);
    }







}


