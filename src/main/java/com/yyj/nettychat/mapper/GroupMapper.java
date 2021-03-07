package com.yyj.nettychat.mapper;

import com.yyj.nettychat.model.Group;
import com.yyj.nettychat.model.GroupMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GroupMapper {

    int deleteGroup(Group group);

    int insertGroup(Group group);

    int insertMember(@Param("groupMember") GroupMember groupMember);

    int deleteMember(@Param("curGroup") Group group,@Param("memberid") String memberid);

    int deleteMembersByGroupid(String groupid);

    Group seleteGroup(String id);

    // 查找群组关系中存在的记录 包括待加入
    GroupMember seleteMember(GroupMember groupMember);

    List<GroupMember> seleteMembers(String id);

    List<Group> selectGroupsByuserid(String userid);

    int getOwnedGroupsNum(String masterid);

    int updateGroupName(Group group);

    int updateGroupMemberStatus(@Param("curGroup") Group group,@Param("memberid") String memberid);// 将status 改为1

    List<GroupMember> getGroupMemberZeroStatus(Group group);
}
