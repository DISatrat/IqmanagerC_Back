package org.iqmanager.service.performerService;

import org.iqmanager.models.PerformerData;
import org.iqmanager.repository.PerformerDataDAO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PerformerServiceImpl implements PerformerService {

    private final PerformerDataDAO performerDataDAO;

    public PerformerServiceImpl(PerformerDataDAO performerDataDAO) {
        this.performerDataDAO = performerDataDAO;
    }


    @Override
    public String getPerformerPhone(long id) {
        return performerDataDAO.getOne(id).getPhone();
    }

    @Override
    public PerformerData getPerformer(long id) {
        return performerDataDAO.getOne(id);
    }
}
