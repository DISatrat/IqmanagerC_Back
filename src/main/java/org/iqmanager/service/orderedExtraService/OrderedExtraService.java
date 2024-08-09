package org.iqmanager.service.orderedExtraService;

import org.iqmanager.models.OrderedExtras;

public interface OrderedExtraService {
    OrderedExtras save(OrderedExtras orderedExtras);
    void delete(long id);
}
