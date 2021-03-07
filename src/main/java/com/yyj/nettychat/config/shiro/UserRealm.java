package com.yyj.nettychat.config.shiro;

import com.yyj.nettychat.model.Permission;
import com.yyj.nettychat.model.Role;
import com.yyj.nettychat.model.User;
import com.yyj.nettychat.service.RoleService;
import com.yyj.nettychat.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRealm extends AuthorizingRealm {


    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        //获取登录用户名
        User user = (User) principalCollection.getPrimaryPrincipal();
        // System.out.println(user);
        //添加角色和权限
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        Role role = roleService.getUserRole(user.getRoleid());
        // System.out.println(role);
        //添加角色
        simpleAuthorizationInfo.addRole(role.getRolename());
        //添加权限
        for (Permission permissions : role.getPermissions()) {
            simpleAuthorizationInfo.addStringPermission(permissions.getPname());
        }

        return simpleAuthorizationInfo;

    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        //获取用户信息
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;

        String username = token.getUsername();
        User user=new User();
        user.setUsername(username);
        user.setEmail(username);
        user = userService.getUserWithPass(user);
        
        if (user == null) {
            //返回后会报出对应异常
            return null;
        } else {
            SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(user, user.getPassword(), getName());
            return simpleAuthenticationInfo;
        }


    }

}
