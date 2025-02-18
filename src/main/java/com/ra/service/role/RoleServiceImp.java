package com.ra.service.role;

import com.ra.model.entity.Role;
import com.ra.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImp implements RoleService {
    @Autowired
    private RoleRepository roleRepository;
    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }
}
