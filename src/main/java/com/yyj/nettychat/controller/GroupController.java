package com.yyj.nettychat.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.yyj.nettychat.entity.ResponseResult;
import com.yyj.nettychat.model.Group;
import com.yyj.nettychat.model.GroupMember;
import com.yyj.nettychat.model.User;
import com.yyj.nettychat.service.GroupService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GroupController {

    @Autowired
    private GroupService groupService;

    /**
     * 处理 群组创建
     * 
     * @param group
     * @param httpSession
     * @return
     */
    @PostMapping("/group")
    public ResponseResult<Group> createGroup(HttpSession httpSession, Group group) {
        String userid = (String) httpSession.getAttribute("userid");
        ResponseResult<Group> responseResult = new ResponseResult<>();
        group.setMasterid(userid);
        group.setCreatetime(new Date());
        group = groupService.createGroup(group);
        responseResult.setData(group);
        if (group == null) {
            responseResult.setStatus(10400);
        } else
            responseResult.setStatus(10200);
        return responseResult;
    }

    /**
     * 处理 申请加入群组
     * 
     * @param member
     * @param httpSession
     * @return
     */
    @PostMapping("/groupmember/{id:\\d+}")
    public ResponseResult<GroupMember> addGroupRequire(@PathVariable String id, HttpSession httpSession) {
        GroupMember member = new GroupMember();
        member.setStatus(0);
        String userid = (String) httpSession.getAttribute("userid");
        User user = new User();
        member.setGroupid(id);
        user.setUserid(userid);
        ResponseResult<GroupMember> responseResult = new ResponseResult<>();
        member.setMember(user);
        member = groupService.addInGroup(member);
        responseResult.setData(member);
        if (member == null) {
            responseResult.setStatus(10400);
        } else
            responseResult.setStatus(10200);
        return responseResult;
    }

    /**
     * 处理 删除群组
     * 
     * @param id
     * @param httpSession
     * @return
     */
    @DeleteMapping("/group/{id:\\d+}")
    public ResponseResult<Group> deleteGroup(@PathVariable String id, HttpSession httpSession) {
        String userid = (String) httpSession.getAttribute("userid");
        Group group = new Group();
        group.setId(id);
        group.setMasterid(userid);
        ResponseResult<Group> responseResult = new ResponseResult<>();
        Boolean boolean1 = groupService.delteGroup(group);
        if (boolean1 == false) {
            responseResult.setStatus(10400);
        } else
            responseResult.setStatus(10200);
        return responseResult;
    }

    /**
     * 处理 退出/踢出群组
     * 
     * @param id
     * @param httpSession
     * @return
     */
    @DeleteMapping("/groupmember/{groupid:\\d+}")
    public ResponseResult<GroupMember> withdrawGroup(@PathVariable String groupid, String memberid,
            HttpSession httpSession) {
        String userid = (String) httpSession.getAttribute("userid");
        ResponseResult<GroupMember> responseResult = new ResponseResult<>();
        GroupMember member = new GroupMember();
        member.setGroupid(groupid);
        User user = new User();
        Boolean boolean1 = false;
        if (memberid.equals(userid)) {
            user.setUserid(userid);
            member.setMember(user);
            boolean1 = groupService.withdrawGroup(member);
        } else {
            Group group = new Group();
            group.setId(groupid);
            group.setMasterid(userid);
            user.setUserid(memberid);
            member.setMember(user);
            group.addMember(member);
            boolean1 = groupService.deleteMember(group);
        }
        responseResult.setData(member);
        if (boolean1 == false) {
            responseResult.setStatus(10400);
        } else
            responseResult.setStatus(10200);
        return responseResult;
    }

    /**
     * 处理 获取所有群列表
     * 
     * @param httpSession
     * @return
     */
    @GetMapping("/groups")
    public ResponseResult<List<Group>> getGroupsList(HttpSession httpSession) {
        String userid = (String) httpSession.getAttribute("userid");
        ResponseResult<List<Group>> responseResult = new ResponseResult<>();
        List<Group> list = groupService.getGroups(userid);
        responseResult.setData(list);
        responseResult.setStatus(10200);
        return responseResult;
    }

    /**
     * 处理 更新群名
     * 
     * @param id
     * @param group
     * @return
     */
    @PatchMapping("/group/{id:\\d+}")
    public ResponseResult<Group> updateGroupName(@PathVariable String id, String name, HttpSession httpSession) {
        Group group = new Group();
        String userid = (String) httpSession.getAttribute("userid");
        group.setMasterid(id);
        group.setName(name);
        group.setMasterid(userid);
        group.setId(id);
        ResponseResult<Group> responseResult = new ResponseResult<>();
        Boolean boolean1 = groupService.updateGroupName(group);
        if (boolean1 == false) {
            responseResult.setStatus(10400);
        } else
            responseResult.setStatus(10200);
        return responseResult;
    }

    /**
     * 处理 同意群组加入请求
     * 
     * @param groupMember
     * @return
     */
    @PatchMapping("/groupmember")
    public ResponseResult<GroupMember> acceptInGroup(HttpSession httpSession, GroupMember groupMember) {
        String userid = (String) httpSession.getAttribute("userid");
        Group group = new Group();
        group.setId(groupMember.getGroupid());
        group.setMasterid(userid);
        group.addMember(groupMember);
        Boolean boolean1 = groupService.acceptGroupMember(group);
        ResponseResult<GroupMember> responseResult = new ResponseResult<>();
        if (boolean1 == false) {
            responseResult.setStatus(10400);
        } else
            responseResult.setStatus(10200);
        return responseResult;
    }

    /**
     * 处理 拒绝群组加入请求
     * 
     * @param groupMember
     * @return
     */
    @DeleteMapping("/groupmember")
    public ResponseResult<GroupMember> IgnoreInGroup(HttpSession httpSession, GroupMember groupMember) {
        String userid = (String) httpSession.getAttribute("userid");
        Group group = new Group();
        group.setId(groupMember.getGroupid());
        group.setMasterid(userid);
        group.addMember(groupMember);
        Boolean boolean1 = groupService.IgnoreGroupMember(group);
        ResponseResult<GroupMember> responseResult = new ResponseResult<>();
        if (boolean1 == false) {
            responseResult.setStatus(10400);
        } else
            responseResult.setStatus(10200);
        return responseResult;
    }

    /**
     * 处理 查询群组
     * 
     * @param id
     * @param httpSession
     * @return
     */
    @GetMapping("/group/{id:\\d+}")
    public ResponseResult<Group> getGroupMembers(@PathVariable String id, HttpSession httpSession) {
        String userid = (String) httpSession.getAttribute("userid");
        Group group = new Group();
        group.setId(id);
        group = groupService.getGroup(group, userid);
        ResponseResult<Group> responseResult = new ResponseResult<>();
        responseResult.setData(group);
        if (group == null) {
            responseResult.setStatus(10400);
        } else {
            responseResult.setStatus(10200);
        }
        return responseResult;
    }

    /**
     * 处理 获取所有入群请求
     * 
     * @param httpSession
     * @return
     */
    @GetMapping("/groupmembers")
    public ResponseResult<List<GroupMember>> getGroupRequire(HttpSession httpSession) {
        String userid = (String) httpSession.getAttribute("userid");
        Group group = new Group();
        group.setMasterid(userid);
        List<GroupMember> list = groupService.getGroupRequire(group);
        ResponseResult<List<GroupMember>> responseResult = new ResponseResult<>();
        responseResult.setData(list);
        responseResult.setStatus(10200);
        return responseResult;
    }
}