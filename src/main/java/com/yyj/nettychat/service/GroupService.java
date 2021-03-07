package com.yyj.nettychat.service;

import java.util.List;

import com.yyj.nettychat.model.Group;
import com.yyj.nettychat.model.GroupMember;

public interface GroupService {
    /**
     * 创建群组
     * 
     * @param group
     * @return
     */
    Group createGroup(Group group);

    /**
     * 删除群组
     * 
     * @param group
     * @return
     */
    boolean delteGroup(Group group);

    /**
     * 加入群组 发送请求
     * 
     * @param member
     * @return
     */
    GroupMember addInGroup(GroupMember member);

    /**
     * 退出群组
     * 
     * @param member
     * @return
     */
    boolean withdrawGroup(GroupMember member);

    /**
     * 获取群组列表
     * 
     * @param member
     * @return
     */
    List<Group> getGroups(String userid);

    /**
     * 检查是否是群成员
     * 
     * @param member
     * @return
     */
    boolean isGroupMember(GroupMember member);

    /**
     * 修改群组名字
     * 
     * @param group
     * @return
     */
    boolean updateGroupName(Group group);

    /**
     * 允许加入群组
     * 
     * @param group
     * @return
     */
    boolean acceptGroupMember(Group group);

    /**
     * 拒绝加入群组
     * 
     * @param group
     * @return
     */
    boolean IgnoreGroupMember(Group group);

    /**
     * 获取入群请求列表
     * 
     * @param group
     * @return
     */
    List<GroupMember> getGroupRequire(Group group);

    /**
     * 查询群组信息
     * 
     * @param group
     * @param userid
     * @return
     */
    Group getGroup(Group group, String userid);

    /**
     * 踢出群组
     * 
     * @param group
     * @return
     */
    boolean deleteMember(Group group);

    /**
     * 判断是否是群组成员
     * @param groupid
     * @param userid
     * @return
     */
    boolean isGroupMember(String groupid,String userid);

}
