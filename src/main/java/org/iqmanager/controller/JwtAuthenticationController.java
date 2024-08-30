package org.iqmanager.controller;

import org.iqmanager.config.security.jwt.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

import static org.iqmanager.ApplicationC.URL_WEB;


@RestController
@Validated
@RequestMapping(value = "/api",produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = URL_WEB)
public class JwtAuthenticationController {
    
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil tokenUtil;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.tokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }

    /** Вход в аккаунт */
    @PostMapping(value = "/authenticate")
    public ResponseEntity<AuthResultData> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest, // Логин и пароль
                                                                   HttpServletResponse response, HttpServletRequest request) throws Exception {
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = tokenUtil.generateToken(userDetails);
        System.out.println("Token generated: " + token);
        final Cookie accessToken = new Cookie(HttpUtils.ACCESS_TOKEN_COOKIE_KEY, token);
        accessToken.setMaxAge(7 * 24 * 60 * 60);
        accessToken.setPath(request.getContextPath());
        response.addCookie(accessToken);
        System.out.println("Cookie set: " + accessToken);
        return ResponseEntity.ok(new AuthResultData(token));
    }
    @GetMapping(value = "/token")
    public ResponseEntity<Map<String,String>> getToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (HttpUtils.ACCESS_TOKEN_COOKIE_KEY.equals(cookie.getName())) {
                    String accessToken = cookie.getValue();
                    Map<String,String> token = new HashMap<>();
                    token.put("token",accessToken);
                    return ResponseEntity.ok(token);
                }
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }
    /** Выход из аккаунта */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        try {
            HttpUtils.removeAccessToken(response);
            return ResponseEntity.ok("logout success");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("logout error");
        }
    }

    /** Аутентификация */
    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

}
