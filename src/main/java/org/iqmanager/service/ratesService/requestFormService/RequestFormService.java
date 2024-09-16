package org.iqmanager.service.ratesService.requestFormService;

import org.iqmanager.models.RequestForm;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


public interface RequestFormService {
    void save(RequestForm requestForm);
}
