package com.example.webDemo3.service.impl.manageClassServiceImpl;

import com.example.webDemo3.constant.Constant;
import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.dto.manageClassResponseDto.RoleListResponseDTO;
import com.example.webDemo3.entity.Role;
import com.example.webDemo3.repository.RoleRepository;
import com.example.webDemo3.service.manageClassService.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    /**
     * kimpt142
     * 25/6
     * @return
     */
    @Override
    public RoleListResponseDTO getAllRole() {
        RoleListResponseDTO roleListResponseDTO = new RoleListResponseDTO();
        List<Role> roles = new ArrayList<>();
        MessageDTO message = new MessageDTO();

        try {
             roles = roleRepository.findAll();
        }
        catch (Exception e)
        {
            message.setMessageCode(1);
            message.setMessage(e.toString());
            roleListResponseDTO.setMessage(message);
            return roleListResponseDTO;
        }

        message = Constant.SUCCESS;
        roleListResponseDTO.setListRole(roles);
        roleListResponseDTO.setMessage(message);
        return roleListResponseDTO;
    }
}
