package com.yyj.nettychat.mapper;

import com.yyj.nettychat.model.Permission;
import com.yyj.nettychat.model.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoleMapper {

    int insert(Role role);

    int delete(String roleid);

    int deleteRolePermission(String roleid, String permissionid);

    int updateRolenameByRoleid(String roleid, String name);

    int insertRolePermission(String roleid, String permissionid);

    Role selectByRolename(String name);

    Role selectByRoleid(String id);

}
