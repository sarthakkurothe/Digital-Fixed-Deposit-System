package com.fdsystem.backend.util.jwt;


import com.fdsystem.backend.exception.TokenExpiredException;
import com.fdsystem.backend.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter {
  private JWTUtils jwtUtil;

  private HandlerExceptionResolver exceptionResolver;
  private UserDetailsServiceImpl userDetailsService;

  public AuthTokenFilter(JWTUtils jwtUtil,
                         HandlerExceptionResolver exceptionResolver,
                         UserDetailsServiceImpl userDetailsService) {
    this.jwtUtil = jwtUtil;
    this.exceptionResolver = exceptionResolver;
    this.userDetailsService = userDetailsService;
  }

  @Override
  public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    try{

      String jwt = this.jwtUtil.getJwtFromHeader(request);

      log.info("jwt token {}",jwt);


      if(jwt != null && jwtUtil.isValidJwtToken(jwt) ){

        String username = this.jwtUtil.getUsernameFromJwtToken(jwt);

        UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));


        SecurityContextHolder.getContext().setAuthentication(authentication);
      }

    }catch(Exception e) {
      log.error("Error in the finding the bearer {}", e.getMessage());
      exceptionResolver.resolveException(request, response, null, e);
      return;
    }
    filterChain.doFilter(request,response);
  }
}
