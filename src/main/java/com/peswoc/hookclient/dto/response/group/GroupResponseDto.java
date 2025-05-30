package com.peswoc.hookclient.dto.response.group;

import com.peswoc.hookclient.model.group.Group;
import com.peswoc.hookclient.model.group.GroupUser;
import com.peswoc.hookclient.model.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GroupResponseDto {
  private String id;
  private String name;
  private String thumbnail;
  private List<GroupUserResponseDto> users;

  public GroupResponseDto(Group group) {
    this.id = group.getId();
    this.name = group.getName();
    this.thumbnail = group.getThumbnail();
    this.users = null;
  }

  public GroupResponseDto(String id, String name, String thumbnail, List<GroupUser> users) {
    this.id = id;
    this.name = name;
    this.thumbnail = thumbnail;
    this.users = users.stream()
      .map(GroupUserResponseDto::new)
      .toList();
  }

  public Group toGroup() {
    var group = new Group(name, thumbnail);
    group.setId(id);

    if (users != null) {
      var groupUsers = new ArrayList<GroupUser>();
      for (var v : users) {
        var user = new User();
        user.setId(v.getUserId());
        var groupUser = new GroupUser(group, user, v.getRole());
        groupUser.setId(v.getId());
        groupUsers.add(groupUser);
      }
      group.setGroupUsers(groupUsers);
    }

    return group;
  }
}
