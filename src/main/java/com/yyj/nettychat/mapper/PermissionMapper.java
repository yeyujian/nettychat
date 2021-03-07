package com.yyj.nettychat.mapper;

import com.yyj.nettychat.model.Permission;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PermissionMapper {

    int insert(Permission permission);

    int delete(String id);

    int selectByPermissionname(String name);
}
