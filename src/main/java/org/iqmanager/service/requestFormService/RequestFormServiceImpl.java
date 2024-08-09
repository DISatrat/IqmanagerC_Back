package org.iqmanager.service.requestFormService;

import lombok.SneakyThrows;
import org.iqmanager.models.RequestForm;
import org.iqmanager.repository.RequestFormDAO;
import org.iqmanager.service.adminService.AdminService;
import org.iqmanager.service.requestFormService.RequestFormService;
import org.iqmanager.util.SendSMS;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Service
public class RequestFormServiceImpl implements RequestFormService {

    private final RequestFormDAO requestFormDAO;
    private final AdminService adminService;

    public RequestFormServiceImpl(RequestFormDAO requestFormDAO, AdminService adminService) {
        this.requestFormDAO = requestFormDAO;
        this.adminService = adminService;
    }

    /** Сохранение заявки */
    @SneakyThrows
    @Override
    public void save(RequestForm requestForm) {
        requestFormDAO.save(requestForm);
//        SendSMS.receivedRequest(requestForm.getName(), requestForm.getPhone(), adminService.adminsRequesterPhones() );
    }
}
