package com.cloud.common.intercepters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.cloud.common.annotations.DoAuth;
import com.cloud.common.annotations.NoAuth;
import com.cloud.common.constants.Magic;
import com.cloud.common.utils.JwtUtil;
import com.cloud.dto.User;
import com.cloud.service.UserService;
import javax.annotation.Resource;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class AuthenticationInterceptor implements HandlerInterceptor {

  @Resource
  private UserService userService;

  @Override
  public boolean preHandle(HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse, Object object) throws Exception {

    // Get token from header
    String token = httpServletRequest.getHeader(Magic.TOKEN);

    if (!(object instanceof HandlerMethod)) {
      return true;
    }
    HandlerMethod handlerMethod = (HandlerMethod) object;
    Method method = handlerMethod.getMethod();

    // check NoAuth annotations
    if (method.isAnnotationPresent(NoAuth.class)) {
      NoAuth noAuth = method.getAnnotation(NoAuth.class);
      if (noAuth.required()) {
        return true;
      }
    }

    // check DoAuth annotations
    if (method.isAnnotationPresent(DoAuth.class)) {
      DoAuth doAuth = method.getAnnotation(DoAuth.class);
      if (doAuth.required()) {
        if (token == null) {
          throw new RuntimeException("Authentication failed. Need a valid token");
        }

        String username;
        try {
          username = JwtUtil.getUsername(token);
        } catch (JWTDecodeException j) {
          throw new RuntimeException("Token decode failed.");
        }

        User user = userService.getUserByUsername(username);
        if (user == null) {
          throw new RuntimeException("Token is invalid. Unknown user");
        }

        // check token
        if (!JwtUtil.verify(token, username, user.getPassword())) {
          throw new RuntimeException("Invalid token");
        }
        return true;
      }
    }
    return true;
  }

  @Override
  public void postHandle(HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView)
      throws Exception {
    //ignore
  }

  @Override
  public void afterCompletion(HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    //ignore
  }
}