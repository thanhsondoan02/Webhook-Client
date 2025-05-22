package com.peswoc.hookclient.security;

import com.peswoc.hookclient.service.IUserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtUtils jwtUtils;

  private final IUserService userService;

  public JwtAuthenticationFilter(JwtUtils jwtUtils, IUserService userService) {
    this.jwtUtils = jwtUtils;
    this.userService = userService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
    String header = request.getHeader("Authorization");

    if (header != null && header.startsWith("Bearer ")) {
      String token = header.substring(7);
      if (jwtUtils.validateJwtToken(token)) {
        String username = jwtUtils.getUsernameFromJwt(token);
        var user = userService.getUserByUsername(username);
        if (user != null) {
          var userDetails = new User(username, user.getPasswordHash(), new ArrayList<>());
          UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(user, null, userDetails.getAuthorities());
          authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authentication);
        }
      }
    }

    filterChain.doFilter(request, response);
  }
}

