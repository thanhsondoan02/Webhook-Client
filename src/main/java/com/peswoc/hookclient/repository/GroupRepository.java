package com.peswoc.hookclient.repository;

import com.peswoc.hookclient.constant.State;
import com.peswoc.hookclient.model.group.Group;
import com.peswoc.hookclient.model.group.GroupUser;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, String> {
  @Query("SELECT COUNT(g) > 0 FROM Group g WHERE g.id = :id AND g.state = 1")
  boolean existsAndActiveById(@Param("id") String id);

  @Query("SELECT COUNT(gu) > 0 FROM GroupUser gu WHERE gu.user.id = :userId AND gu.group.id = :groupId AND gu.role = 1 AND gu.state = 1")
  boolean isGroupAdmin(@Param("userId") String userId, @Param("groupId") String groupId);

  @Query("SELECT COUNT(gu) > 0 FROM GroupUser gu WHERE gu.user.id = :userId AND gu.group.id = :groupId AND gu.state = 1")
  boolean isUserActiveInGroup(@Param("userId") String userId, @Param("groupId") String groupId);

  @Query("SELECT COUNT(gu) > 0 FROM GroupUser gu WHERE gu.user.id = :userId AND gu.group.id = :groupId AND gu.state = 0")
  boolean isUserInactiveInGroup(@Param("userId") String userId, @Param("groupId") String groupId);

  @Query("SELECT g FROM Group g WHERE g.id = :id AND g.state = 1")
  Optional<Group> findActiveById(@Param("id") String id);

  @Query("SELECT gu FROM GroupUser gu WHERE gu.group.id = :groupId AND gu.user.id = :userId")
  Optional<GroupUser> findUserInGroupById(@Param("groupId") String groupId, @Param("userId") String userId);

  @Query("""
    SELECT gu FROM Group g JOIN g.groupUsers gu
    WHERE g.id = :groupId AND g.state = 1 AND gu.state = 1
    """)
  List<GroupUser> getActiveUsersInGroup(@Param("groupId") String groupId);

  @Query("SELECT g FROM Group g JOIN g.groupUsers gu WHERE gu.user.id = :userId AND gu.state = 1 AND g.state = 1")
  List<Group> findActiveGroupsOfUser(@Param("userId") String userId);

  @Transactional
  @Modifying
  @Query("UPDATE GroupUser gu SET gu.state = :state WHERE gu.group.id = :groupId AND gu.user.id = :userId")
  void updateGroupUserState(@Param("groupId") String groupId, @Param("userId") String userId, @Param("state") State state);
}
