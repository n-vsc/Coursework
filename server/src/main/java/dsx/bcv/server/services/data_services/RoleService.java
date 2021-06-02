package dsx.bcv.server.services.data_services;

import dsx.bcv.server.data.models.Role;
import dsx.bcv.server.data.repositories.RoleRepository;
import dsx.bcv.server.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role save(Role role) {
        return roleRepository.save(role);
    }

    public Role findByName(String name) {
        return roleRepository.findByName(name).orElseThrow(NotFoundException::new);
    }
}
