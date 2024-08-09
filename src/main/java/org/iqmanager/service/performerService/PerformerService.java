package org.iqmanager.service.performerService;

import org.iqmanager.models.PerformerData;

public interface PerformerService {
    String getPerformerPhone(long id);
    PerformerData getPerformer(long id);
}
