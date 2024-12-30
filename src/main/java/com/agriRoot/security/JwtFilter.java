package com.agriRoot.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        log.info("Header {}",authorization);

        String username = null;
        String token = null;

        if(authorization != null && authorization.startsWith("Bearer")){
            token=authorization.substring(7);

            try {
                username= jwtHelper.getUserNameFromToken(token);
                log.info("username from token {}",username);
            }catch (IllegalArgumentException e) {
                e.printStackTrace();
                log.info("illegal argument while fetching the argument " + e.getMessage());
            }
            catch (ExpiredJwtException ex){
                log.info("Given JWT is Expired " + ex.getMessage());
            }
            catch (MalformedJwtException ex){
                log.info("Some changed has done in token "+ex.getMessage());
            }
            catch (Exception ex){
                ex.printStackTrace();
            }


        }else{
            logger.info("Invalid Header");
        }

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if(username.equals(userDetails.getUsername()) && !jwtHelper.isTokeExpired(token)){
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request,response);
    }
}
