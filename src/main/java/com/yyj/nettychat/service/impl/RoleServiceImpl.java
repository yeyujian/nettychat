package com.yyj.nettychat.service.impl;

import com.yyj.nettychat.mapper.PermissionMapper;
import com.yyj.nettychat.mapper.RoleMapper;
import com.yyj.nettychat.model.Role;
import com.yyj.nettychat.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public Role getUserRole(String roleid) {
        return roleMapper.selectByRoleid(roleid);
    }

    @Override
    public Role getRoleByName(String name) {
        return roleMapper.selectByRolename(name);
    }
}
