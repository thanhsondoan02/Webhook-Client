package com.peswoc.hookclient.controller;

import com.peswoc.hookclient.constant.GroupRole;
import com.peswoc.hookclient.constant.MessageConst;
import com.peswoc.hookclient.dto.request.group.CreateGroupRequestDto;
import com.peswoc.hookclient.dto.request.group.GroupUserRequestDto;
import com.peswoc.hookclient.dto.request.group.UpdateGroupRequestDto;
import com.peswoc.hookclient.service.IAuthService;
import com.peswoc.hookclient.service.IGroupService;
import com.peswoc.hookclient.service.IUserService;
import com.peswoc.hookclient.util.ResponseBuilder;
import com.peswoc.hookclient.util.ValidationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

  private final IGroupService groupService;
  private final IAuthService authService;
  private final IUserService userService;

  public GroupController(IGroupService groupService, IAuthService authService, IUserService userService) {
    this.groupService = groupService;
    this.authService = authService;
    this.userService = userService;
  }

  @PostMapping
  public ResponseEntity<?> createGroup(@RequestBody CreateGroupRequestDto request) {
    if (!ValidationUtils.isGroupNameValid(request.getName())) {
      return ResponseBuilder.error(HttpStatus.BAD_REQUEST.value(), MessageConst.INVALID_INPUT_FORMAT);
    }

    List<GroupUserRequestDto> users = new ArrayList<>();
    // Add the authenticated user as an admin
    users.add(new GroupUserRequestDto(authService.getAuthUser().getId(), GroupRole.ADMIN.toString()));
    // Add other users from the request
    if (request.getUsers() != null) {
      users.addAll(request.getUsers());
    }

    // Check if users have any duplicate user IDs
    if (users.size() > 1) {
      var userIds = users.stream().map(GroupUserRequestDto::getUserId).toList();
      if (userIds.size() != userIds.stream().distinct().count()) {
        return ResponseBuilder.error(HttpStatus.BAD_REQUEST.value(), MessageConst.DUPLICATE_USER_ID);
      }
    }

    // Check if users' role is valid
    for (var userRole : users) {
      if (userRole.getRole() == null) {
        return ResponseBuilder.error(HttpStatus.UNPROCESSABLE_ENTITY.value(), MessageConst.INVALID_ROLE);
      }
    }

    // Check if users exist
    for (var userRole : users) {
      if (!userService.isUserIdExist(userRole.getUserId())) {
        return ResponseBuilder.error(HttpStatus.NOT_FOUND.value(), MessageConst.USER_NOT_FOUND);
      }
    }

    request.setUsers(users);
    return ResponseBuilder.success(groupService.createGroup(request));
  }

  @PutMapping("/{groupId}")
  public ResponseEntity<?> updateGroupInfo(@PathVariable("groupId") String groupId, @RequestBody UpdateGroupRequestDto request) {
    var name = request.getName();
    if (name != null && !ValidationUtils.isGroupNameValid(name)) {
      return ResponseBuilder.error(HttpStatus.BAD_REQUEST.value(), MessageConst.INVALID_INPUT_FORMAT);
    }
    if (!groupService.isGroupExistAndActive(groupId)) {
      return ResponseBuilder.error(HttpStatus.NOT_FOUND.value(), MessageConst.GROUP_NOT_FOUND);
    }
    if (!groupService.isInGroup(authService.getAuthUser().getId(), groupId)) {
      return ResponseBuilder.error(HttpStatus.FORBIDDEN.value(), MessageConst.ACCESS_DENIED);
    }

    groupService.updateGroup(groupId, request);
    return ResponseBuilder.success(null);
  }

  @DeleteMapping("/{groupId}")
  public ResponseEntity<?> deleteGroup(@PathVariable("groupId") String groupId) {
    if (!groupService.isGroupExistAndActive(groupId)) {
      return ResponseBuilder.error(HttpStatus.NOT_FOUND.value(), MessageConst.GROUP_NOT_FOUND);
    }
    if (!groupService.isGroupAdmin(authService.getAuthUser().getId(), groupId)) {
      return ResponseBuilder.error(HttpStatus.FORBIDDEN.value(), MessageConst.ACCESS_DENIED);
    }

    groupService.deleteGroup(groupId);
    return ResponseBuilder.success();
  }

  @PostMapping("/{groupId}/members")
  public ResponseEntity<?> addUserToGroup(@PathVariable("groupId") String groupId, @RequestBody GroupUserRequestDto request) {
    if (request.getRole() == null) {
      return ResponseBuilder.error(HttpStatus.UNPROCESSABLE_ENTITY.value(), MessageConst.INVALID_ROLE);
    }
    if (!groupService.isGroupExistAndActive(groupId)) {
      return ResponseBuilder.error(HttpStatus.NOT_FOUND.value(), MessageConst.GROUP_NOT_FOUND);
    }
    if (!groupService.isGroupAdmin(authService.getAuthUser().getId(), groupId)) {
      return ResponseBuilder.error(HttpStatus.FORBIDDEN.value(), MessageConst.ACCESS_DENIED);
    }
    if (!userService.isUserIdExistAndActive(request.getUserId())) {
      return ResponseBuilder.error(HttpStatus.NOT_FOUND.value(), MessageConst.USER_NOT_FOUND);
    }
    if (groupService.isInGroup(request.getUserId(), groupId)) {
      return ResponseBuilder.error(HttpStatus.CONFLICT.value(), MessageConst.USER_ALREADY_IN_GROUP);
    }

    groupService.addUserToGroup(groupId, request);
    return ResponseBuilder.success();
  }

  @DeleteMapping("/{groupId}/members/{userId}")
  public ResponseEntity<?> deleteUserFromGroup(@PathVariable("groupId") String groupId, @PathVariable("userId") String userId) {
    if (!groupService.isGroupExistAndActive(groupId)) {
      return ResponseBuilder.error(HttpStatus.NOT_FOUND.value(), MessageConst.GROUP_NOT_FOUND);
    }
    if (!groupService.isGroupAdmin(authService.getAuthUser().getId(), groupId)) {
      return ResponseBuilder.error(HttpStatus.FORBIDDEN.value(), MessageConst.ACCESS_DENIED);
    }
    if (!groupService.isInGroup(userId, groupId)) {
      return ResponseBuilder.error(HttpStatus.CONFLICT.value(), MessageConst.USER_NOT_FOUND);
    }
    groupService.removeUserFromGroup(groupId, userId);
    return ResponseBuilder.success();
  }

  @GetMapping()
  public ResponseEntity<?> getGroupList() {
    var userId = authService.getAuthUser().getId();
    var groups = groupService.getGroupList(userId);
    return ResponseBuilder.success(groups);
  }

  @GetMapping("/{groupId}")
  public ResponseEntity<?> getGroupInfo(@PathVariable("groupId") String groupId) {
    var userId = authService.getAuthUser().getId();
    if (!groupService.isGroupExistAndActive(groupId)) {
      return ResponseBuilder.error(HttpStatus.NOT_FOUND.value(), MessageConst.GROUP_NOT_FOUND);
    }
    if (!groupService.isInGroup(userId, groupId)) {
      return ResponseBuilder.error(HttpStatus.FORBIDDEN.value(), MessageConst.ACCESS_DENIED);
    }
    return ResponseBuilder.success(groupService.getGroupInfo(groupId));
  }
}
