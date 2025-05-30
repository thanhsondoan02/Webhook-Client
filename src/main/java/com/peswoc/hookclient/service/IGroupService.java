package com.peswoc.hookclient.service;

import com.peswoc.hookclient.dto.request.group.CreateGroupRequestDto;
import com.peswoc.hookclient.dto.request.group.GroupUserRequestDto;
import com.peswoc.hookclient.dto.request.group.UpdateGroupRequestDto;
import com.peswoc.hookclient.dto.response.group.GroupListResponseDto;
import com.peswoc.hookclient.dto.response.group.GroupResponseDto;

public interface IGroupService {
  GroupResponseDto createGroup(CreateGroupRequestDto request);

  void syncGroups(GroupListResponseDto data);

  boolean isGroupExistAndActive(String id);

  boolean isInGroup(String userId, String groupId);

  boolean isGroupAdmin(String userId, String groupId);

  void updateGroup(String groupId, UpdateGroupRequestDto request);

  void deleteGroup(String groupId);

  void addUserToGroup(String groupId, GroupUserRequestDto request);

  void removeUserFromGroup(String groupId, String userId);

  GroupListResponseDto getGroupList(String userId);

  GroupResponseDto getGroupInfo(String groupId);

  void addGroup(GroupResponseDto data);

  void updateGroup(GroupResponseDto data);
}
