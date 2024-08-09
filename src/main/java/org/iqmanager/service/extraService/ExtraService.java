package org.iqmanager.service.extraService;

import org.iqmanager.dto.ExtraForOrderElementDTO;
import org.iqmanager.models.Extra;
import org.iqmanager.models.OrderedExtras;

import java.util.List;
import java.util.Set;

public interface ExtraService {
    Set<OrderedExtras> getExtras(Set<ExtraForOrderElementDTO> extra);
}
