package org.iqmanager.repository;

import org.iqmanager.models.Contract;
import org.iqmanager.models.PerformerData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractDAO extends JpaRepository<Contract, Long> {
    Contract findContractByPerformerData(PerformerData performerData);

}
