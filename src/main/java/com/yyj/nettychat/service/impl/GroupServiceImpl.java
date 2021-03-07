package com.yyj.nettychat.service.impl;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import com.yyj.nettychat.config.push.AsyncCenter;
import com.yyj.nettychat.entity.GroupPushEnum;
import com.yyj.nettychat.mapper.GroupMapper;
import com.yyj.nettychat.mapper.UserMapper;
import com.yyj.nettychat.model.Group;
import com.yyj.nettychat.model.GroupMember;
import com.yyj.nettychat.model.User;
import com.yyj.nettychat.service.GroupService;
import com.yyj.nettychat.util.IdWorker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupMapper groupMapper;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AsyncCenter asyncCenter;

    @Override
    public Group createGroup(Group group) {
        try {
            // 限制只能创建10个群组
            if (groupMapper.getOwnedGroupsNum(group.getMasterid()) > 10)
                return null;
            group.setId(idWorker.nextId());
            groupMapper.insertGroup(group); // 创建群聊
            GroupMember groupMember = new GroupMember();
            groupMember.setCreatetime(new Date());
            groupMember.setGroupid(group.getId());
            User user = userMapper.selectById(group.getMasterid());// 查找群主信息
            groupMember.setMember(user);
            groupMember.setId(idWorker.nextId());
            groupMember.setStatus(1);
            groupMapper.insertMember(groupMember); // 插入群主到群聊
            return groupMapper.seleteGroup(group.getId());
        } catch (Exception e) {
            throw new RuntimeException("创建群组失败");
        }

    }

    @Override
    public boolean delteGroup(Group group) {
        try {
            group = getGroup(group, null);
            if (groupMapper.deleteGroup(group) <= 0)
                return false;
            if (groupMapper.deleteMembersByGroupid(group.getId()) <= 0)
                return false;
            asyncCenter.sendPush(GroupPushEnum.DELETE, group); // 发送删除群聊到消息中心
            return true;
        } catch (Exception e) {
            throw new RuntimeException("删除群组失败");
        }
    }

    @Override
    public GroupMember addInGroup(GroupMember member) {
        try {
            Group group = new Group();
            group.setId(member.getGroupid());
            member.setCreatetime(new Date());
            member.setId(idWorker.nextId());

            int success = groupMapper.insertMember(member);
            if (success == 0)
                return null;
            group = groupMapper.seleteGroup(group.getId());
            group.setMembers(new LinkedList<>());
            group.addMember(member);
            asyncCenter.sendPush(GroupPushEnum.REQUIRE, group); // 发送入群请求消息
            return member;
        } catch (Exception e) {
            throw new RuntimeException("加入群组失败");
        }
    }

    @Override
    public boolean withdrawGroup(GroupMember member) {
        try {
            Group group = new Group();
            group.setId(member.getGroupid());
            member = groupMapper.seleteMember(member);
            if (member == null || groupMapper.deleteMember(group, member.getMember().getUserid()) <= 0)
                return false;
            group.setMasterid(member.getMember().getUserid());
            group.addMember(member);
            asyncCenter.sendPush(GroupPushEnum.WITHDRAW, group); // 发送退群消息
            return true;
        } catch (Exception e) {
            throw new RuntimeException("退出群组失败");
        }
    }

    @Override
    public boolean deleteMember(Group group) {
        try {
            if (groupMapper.deleteMember(group, group.getMembers().get(0).getMember().getUserid()) <= 0)
                return false;
            group.setMasterid(group.getMembers().get(0).getMember().getUserid());
            asyncCenter.sendPush(GroupPushEnum.DELETE, group);// 发送 踢出群员消息
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("踢出群组失败");
        }
    }

    @Override
    public List<Group> getGroups(String userid) {
        try {
            return groupMapper.selectGroupsByuserid(userid);
        } catch (Exception e) {
            throw new RuntimeException("查找群组列表失败");
        }
    }

    @Override
    public boolean isGroupMember(GroupMember member) {
        try {
            member = groupMapper.seleteMember(member);
            return member != null && member.getStatus() == 1;
        } catch (Exception e) {
            throw new RuntimeException("查找群组失败");
        }
    }

    @Override
    public boolean updateGroupName(Group group) {
        try {
            return groupMapper.updateGroupName(group) > 0;
        } catch (Exception e) {
            throw new RuntimeException("更新群组名字失败");
        }
    }

    @Override
    public boolean acceptGroupMember(Group group) {
        try {
            String userid = group.getMembers().get(0).getMember().getUserid();
            if (groupMapper.updateGroupMemberStatus(group, userid) <= 0)
                return false;
            group.getMembers().get(0).setStatus(1);
            System.out.println(group);
            group.setMasterid(userid);
            asyncCenter.sendPush(GroupPushEnum.ACCEPT, group); // 发送同意入群消息
            return true;
        } catch (Exception e) {
            throw new RuntimeException("接受入群请求失败");
        }
    }

    @Override
    public boolean IgnoreGroupMember(Group group) {
        try {
            String userid = group.getMembers().get(0).getMember().getUserid();
            if (groupMapper.deleteMember(group, userid) <= 0)
                return false;
            group.getMembers().get(0).setStatus(0);
            group.setMasterid(userid);
            asyncCenter.sendPush(GroupPushEnum.IGNORE, group); // 发送拒绝入群消息
            return true;
        } catch (Exception e) {
            throw new RuntimeException("拒绝入群请求操作失败");
        }
    }

    @Override
    public List<GroupMember> getGroupRequire(Group group) {
        try {
            return groupMapper.getGroupMemberZeroStatus(group);
        } catch (Exception e) {
            throw new RuntimeException("获取入群请求失败");
        }
    }

    @Override
    public Group getGroup(Group group, String userid) {
        try {
            group = groupMapper.seleteGroup(group.getId());
            if (group == null)
                return group;
            if (!isGroupMember(group.getId(), userid))
                group.setMembers(null);
            return group;
        } catch (Exception e) {
            throw new RuntimeException("获取群组信息失败");
        }
    }

    @Override
    public boolean isGroupMember(String groupid, String userid) {
        try {
            if (groupid == null || userid == null)
                return false;
            GroupMember member = new GroupMember();
            member.setGroupid(groupid);
            User user = new User();
            user.setUserid(userid);
            member.setMember(user);
            member = groupMapper.seleteMember(member);
            if (member == null || member.getStatus() == 0)
                return false;
            return true;
        } catch (Exception e) {
            throw new RuntimeException("判断群组信息失败失败");
        }
    }
}
