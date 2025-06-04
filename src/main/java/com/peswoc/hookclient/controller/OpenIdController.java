package com.peswoc.hookclient.controller;

import com.peswoc.hookclient.constant.ConnectionAction;
import com.peswoc.hookclient.constant.ConnectionStatus;
import com.peswoc.hookclient.constant.MessageConst;
import com.peswoc.hookclient.constant.SyncScope;
import com.peswoc.hookclient.dto.request.group.GroupUserRequestDto;
import com.peswoc.hookclient.dto.request.openid.auth.OpenIdLoginRequestDto;
import com.peswoc.hookclient.dto.request.openid.connect.ConnectRequestFromClientDto;
import com.peswoc.hookclient.dto.request.openid.connect.ConnectRequestToServerDto;
import com.peswoc.hookclient.dto.request.openid.connect.UpdateConnectionRequestDto;
import com.peswoc.hookclient.dto.request.openid.sync.SyncRequestDto;
import com.peswoc.hookclient.dto.request.openid.webhook.RegisterWebhookRequestDto;
import com.peswoc.hookclient.dto.request.openid.webhook.WebhookDto;
import com.peswoc.hookclient.dto.response.group.GroupResponseDto;
import com.peswoc.hookclient.dto.response.post.PostResponseDto;
import com.peswoc.hookclient.dto.response.user.UserResponseDto;
import com.peswoc.hookclient.model.openid.Connection;
import com.peswoc.hookclient.model.openid.Server;
import com.peswoc.hookclient.service.*;
import com.peswoc.hookclient.util.ResponseBuilder;
import com.peswoc.hookclient.util.ValidationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@RestController
@RequestMapping("/api")
public class OpenIdController {

  private final IOpenIdService openIdService;
  private final IApiService apiService;
  private final IUserService userService;
  private final IPostService postService;
  private final IGroupService groupService;

  public OpenIdController(
    IOpenIdService openIdService,
    IApiService apiService,
    IUserService userService,
    IPostService postService,
    IGroupService groupService) {
    this.openIdService = openIdService;
    this.apiService = apiService;
    this.userService = userService;
    this.postService = postService;
    this.groupService = groupService;
  }

  @PostMapping("/connections")
  public ResponseEntity<?> requestConnect(@RequestBody ConnectRequestFromClientDto request) {
    if (!ValidationUtils.isValidServerName(request.getName())
      || !ValidationUtils.isValidDomain(request.getDomain())) {
      return ResponseBuilder.error(HttpStatus.BAD_REQUEST.value(), MessageConst.BAD_REQUEST);
    }

    // Call to server B to register new connection
    var apiRequest = new ConnectRequestToServerDto();
    var connectionId = UUID.randomUUID().toString().replace("-", "");
    var ownerDomain = openIdService.getOwnerDomain();
    apiRequest.setName(request.getName());
    apiRequest.setId(connectionId);
    apiRequest.setDomain(ownerDomain);
    apiRequest.setCallbackUrl(ownerDomain + "/api/connections/" + connectionId);

    var response = apiService.registerConnection(request.getDomain() + "/api/connections", apiRequest);
    if (!response.isSuccess()) {
      return ResponseBuilder.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getMessage());
    }

    // Save the new connection to the database
    var server = new Server(request.getDomain());
    var newConnection = new Connection();
    newConnection.setId(connectionId);
    newConnection.setName(request.getName());
    newConnection.setCallbackUrl(apiRequest.getCallbackUrl());
    newConnection.setStatus(ConnectionStatus.PENDING);

    var connectionDto = openIdService.addPendingConnection(server, newConnection);

    return ResponseBuilder.success(connectionDto);
  }

  @GetMapping("/connections")
  public ResponseEntity<?> getConnections(@RequestParam(required = false) String status) {
    if (status == null) {
      return ResponseBuilder.success(openIdService.getAllConnections());
    } else {
      try {
        return ResponseBuilder.success(openIdService.getFilteredConnections(ConnectionStatus.fromString(status)));
      } catch (IllegalArgumentException e) {
        return ResponseBuilder.error(HttpStatus.BAD_REQUEST.value(), MessageConst.BAD_REQUEST);
      }
    }
  }

  @PostMapping("/connections/{id}")
  public ResponseEntity<?> updateConnection(@PathVariable("id") String id, @RequestBody UpdateConnectionRequestDto request) {
    ConnectionAction action;
    try {
      action = ConnectionAction.fromString(request.getAction());
    } catch (IllegalArgumentException e) {
      return ResponseBuilder.error(HttpStatus.BAD_REQUEST.value(), MessageConst.BAD_REQUEST);
    }

    if (action == ConnectionAction.ACCEPT && (request.getClientId() == null || request.getClientSecret() == null)) {
      return ResponseBuilder.error(HttpStatus.BAD_REQUEST.value(), MessageConst.BAD_REQUEST);
    }

    if (!openIdService.isConnectionExistAndPending(id)) {
      return ResponseBuilder.error(HttpStatus.NOT_FOUND.value(), MessageConst.CONNECTION_NOT_FOUND);
    }

    if (action == ConnectionAction.REJECT) {
      openIdService.rejectConnection(id);
      return ResponseBuilder.success();
    } else {
      return ResponseBuilder.success(openIdService.acceptConnection(id, request.getClientId(), request.getClientSecret()));
    }
  }

  @PostMapping("/sync")
  public ResponseEntity<?> syncData(@RequestBody SyncRequestDto request) {
    if (request.getConnectionId() == null || request.getScopes() == null || request.getScopes().isEmpty()) {
      return ResponseBuilder.error(HttpStatus.BAD_REQUEST.value(), MessageConst.MISSING_REQUIRED_FIELD);
    }

    List<SyncScope> scopes = new ArrayList<>();
    for (var v : request.getScopes()) {
      try {
        scopes.add(SyncScope.fromString(v));
      } catch (IllegalArgumentException e) {
        return ResponseBuilder.error(HttpStatus.BAD_REQUEST.value(), MessageConst.BAD_REQUEST);
      }
    }

    var connection = openIdService.getAcceptedConnection(request.getConnectionId());
    if (connection == null) {
      return ResponseBuilder.error(HttpStatus.NOT_FOUND.value(), MessageConst.CONNECTION_NOT_FOUND);
    }

    var domainB = connection.getTargetServer().getDomain();
    var accessToken = apiService.getAccessToken(
      domainB + "/api/auth/login-openid",
      new OpenIdLoginRequestDto(connection.getClientId(), connection.getClientSecret())
    ).getData().getToken();

    for (var scope : scopes) {
      switch (scope) {
        case USERS -> {
          var res = apiService.getUsers(domainB + "/api/users", accessToken);
          userService.syncUsers(res.getData());
        }
        case POSTS -> {
          var res = apiService.getPosts(domainB + "/api/posts", accessToken);
          postService.syncPosts(res.getData());
        }
        case GROUPS -> {
          var res = apiService.getGroups(domainB + "/api/groups", accessToken);
          groupService.syncGroups(res.getData());
        }
      }
    }
    return ResponseBuilder.success();
  }

  @GetMapping("connections/{id}/events")
  public ResponseEntity<?> getEvents(@PathVariable("id") String connectionId) {
    var connection = openIdService.getAcceptedConnection(connectionId);
    if (connection == null) {
      return ResponseBuilder.error(HttpStatus.NOT_FOUND.value(), MessageConst.CONNECTION_NOT_FOUND);
    }

    var url = connection.getTargetServer().getDomain() + "/api/events";
    var token = getToken(connection);
    return ResponseBuilder.success(apiService.getScopeEvents(url, token));
  }

  @PostMapping("connections/{id}/webhooks")
  public ResponseEntity<?> registerWebhook(@PathVariable("id") String connectionId, @RequestBody RegisterWebhookRequestDto body) {
    var connection = openIdService.getAcceptedConnection(connectionId);
    if (connection == null) {
      return ResponseBuilder.error(HttpStatus.NOT_FOUND.value(), MessageConst.CONNECTION_NOT_FOUND);
    }

    var url = connection.getTargetServer().getDomain() + "/api/webhooks";
    var token = getToken(connection);
    return ResponseBuilder.success(apiService.registerWebhook(url, token, body));
  }

  @GetMapping("connections/{id}/webhooks")
  public ResponseEntity<?> getWebhooks(@PathVariable("id") String connectionId) {
    var connection = openIdService.getAcceptedConnection(connectionId);
    if (connection == null) {
      return ResponseBuilder.error(HttpStatus.NOT_FOUND.value(), MessageConst.CONNECTION_NOT_FOUND);
    }

    var url = connection.getTargetServer().getDomain() + "/api/webhooks";
    var token = getToken(connection);
    return ResponseBuilder.success(apiService.getWebhooks(url, token));
  }

  @DeleteMapping("connections/{connectionId}/webhooks/{webhookId}")
  public ResponseEntity<?> deleteWebhook(
    @PathVariable("connectionId") String connectionId,
    @PathVariable("webhookId") String webhookId
  ) {
    var connection = openIdService.getAcceptedConnection(connectionId);
    if (connection == null) {
      return ResponseBuilder.error(HttpStatus.NOT_FOUND.value(), MessageConst.CONNECTION_NOT_FOUND);
    }

    var url = connection.getTargetServer().getDomain() + "/api/webhooks/" + webhookId;
    var token = getToken(connection);
    apiService.deleteWebhook(url, token);
    return ResponseBuilder.success();
  }

  @PostMapping("/webhooks/users/create")
  public ResponseEntity<?> createUser(@RequestBody() WebhookDto<UserResponseDto> body) {
    return handleHookEvent(body, v -> {
      userService.addUser(v);
      return true;
    });
  }

  @PostMapping("/webhooks/users/update")
  public ResponseEntity<?> updateUser(@RequestBody() WebhookDto<UserResponseDto> body) {
    return handleHookEvent(body, v -> {
      userService.updateUser(v);
      return true;
    });
  }

  @PostMapping("/webhooks/users/delete")
  public ResponseEntity<?> deleteUser(@RequestBody() WebhookDto<UserResponseDto> body) {
    return handleHookEvent(body, v -> {
      userService.deleteUser(v.getId());
      return true;
    });
  }

  @PostMapping("/webhooks/posts/create")
  public ResponseEntity<?> createPost(@RequestBody() WebhookDto<PostResponseDto> body) {
    return handleHookEvent(body, v -> {
      postService.addPost(v);
      return true;
    });
  }

  @PostMapping("/webhooks/posts/update")
  public ResponseEntity<?> updatePost(@RequestBody() WebhookDto<PostResponseDto> body) {
    return handleHookEvent(body, v -> {
      postService.updatePost(v.getId(), v.getTitle(), v.getContent());
      return true;
    });
  }

  @PostMapping("/webhooks/posts/delete")
  public ResponseEntity<?> deletePost(@RequestBody() WebhookDto<PostResponseDto> body) {
    return handleHookEvent(body, v -> {
      postService.deletePost(v.getId());
      return true;
    });
  }

  @PostMapping("/webhooks/groups/create")
  public ResponseEntity<?> createGroup(@RequestBody() WebhookDto<GroupResponseDto> body) {
    return handleHookEvent(body, v -> {
      groupService.addGroup(v);
      return true;
    });
  }

  @PostMapping("/webhooks/groups/update")
  public ResponseEntity<?> updateGroup(@RequestBody() WebhookDto<GroupResponseDto> body) {
    return handleHookEvent(body, v -> {
      groupService.updateGroup(v);
      return true;
    });
  }

  @PostMapping("/webhooks/groups/delete")
  public ResponseEntity<?> deleteGroup(@RequestBody() WebhookDto<GroupResponseDto> body) {
    return handleHookEvent(body, v -> {
      groupService.deleteGroup(v.getId());
      return true;
    });
  }

  @PostMapping("/webhooks/group_members/create")
  public ResponseEntity<?> addGroupMember(@RequestBody() WebhookDto<GroupResponseDto> body) {
    return handleHookEvent(body, v -> {
      for (var user : v.getUsers()) {
        var request = new GroupUserRequestDto(user.getUserId(), user.getRole().toString());
        groupService.addUserToGroup(v.getId(), request);
      }
      return true;
    });
  }

  @PostMapping("/webhooks/group_members/delete")
  public ResponseEntity<?> removeGroupMember(@RequestBody() WebhookDto<GroupResponseDto> body) {
    return handleHookEvent(body, v -> {
      var users = v.getUsers();
      for (var user : users) {
        groupService.removeUserFromGroup(v.getId(), user.getUserId());
      }
      return true;
    });
  }

  private <T, R> ResponseEntity<?> handleHookEvent(WebhookDto<T> body, Function<T, R> serviceMethod) {
    var connection = openIdService.getAcceptedConnection(body.getConnectionId());
    if (connection == null) {
      return ResponseBuilder.error(HttpStatus.NOT_FOUND.value(), MessageConst.CONNECTION_NOT_FOUND);
    }

    serviceMethod.apply(body.getData());

    return ResponseBuilder.success();
  }

  private String getToken(Connection connection) {
    return apiService.getAccessToken(
      connection.getTargetServer().getDomain() + "/api/auth/login-openid",
      new OpenIdLoginRequestDto(connection.getClientId(), connection.getClientSecret())
    ).getData().getToken();
  }
}
