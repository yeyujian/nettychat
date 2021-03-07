package com.yyj.nettychat.service;

import com.yyj.nettychat.model.Role;

public interface RoleService {

    Role getUserRole(String roleid);

    Role getRoleByName(String name);
}
