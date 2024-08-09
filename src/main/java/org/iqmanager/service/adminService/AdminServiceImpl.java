package org.iqmanager.service.adminService;

import org.iqmanager.models.Admin;
import org.iqmanager.repository.AdminDataDAO;
import org.iqmanager.service.adminService.AdminService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {
    private final AdminDataDAO adminDataDAO;

    public AdminServiceImpl(AdminDataDAO adminDataDAO) {
        this.adminDataDAO = adminDataDAO;
    }

    /** @deprecated Переделать*/
    @Override
    public List<String> adminsRequesterPhones() {
        Admin admin = adminDataDAO.getOne(10L);
        return adminDataDAO.findAll().stream().filter(x -> x.getRoles().contains(admin)).map(Admin::getPhone).collect(Collectors.toList());
    }

    /** @deprecated Переделать*/
    @Override
    public List<String> adminsOrderTakerPhones() {
        Admin admin = adminDataDAO.getOne(5L);
        return adminDataDAO.findAll().stream().filter(x -> x.getRoles().contains(admin)).map(Admin::getPhone).collect(Collectors.toList());
    }
}
