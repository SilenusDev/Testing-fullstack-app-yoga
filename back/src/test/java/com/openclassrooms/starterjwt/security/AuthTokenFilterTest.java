package com.openclassrooms.starterjwt.security;

import com.openclassrooms.starterjwt.security.jwt.AuthTokenFilter;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AuthTokenFilterTest {

    @InjectMocks
    private AuthTokenFilter authTokenFilter;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private UserDetails userDetails;

    private Method doFilterInternal;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext();

        // Utilisation de la réflexion pour rendre doFilterInternal accessible
        doFilterInternal = AuthTokenFilter.class.getDeclaredMethod(
            "doFilterInternal", HttpServletRequest.class, HttpServletResponse.class, FilterChain.class
        );
        doFilterInternal.setAccessible(true);
    }

    @Test
    void shouldAuthenticateUser_WhenJwtIsValid() throws Exception {
        String validJwt = "sample-valid-jwt";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + validJwt);
        when(jwtUtils.validateJwtToken(validJwt)).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken(validJwt)).thenReturn("alphaUser");
        when(userDetailsService.loadUserByUsername("alphaUser")).thenReturn(userDetails);

        doFilterInternal.invoke(authTokenFilter, request, response, filterChain);

        verify(jwtUtils, times(1)).validateJwtToken(validJwt);
        verify(userDetailsService, times(1)).loadUserByUsername("alphaUser");
        verify(filterChain, times(1)).doFilter(request, response);

        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder
                .getContext().getAuthentication();
        assertNotNull(authentication, "L'authentification ne doit pas être nulle pour un JWT valide");
        assertEquals(userDetails, authentication.getPrincipal(),
                "Le principal dans l'authentification doit correspondre aux détails utilisateur chargés");
    }

    @Test
    void shouldNotAuthenticateUser_WhenJwtIsInvalid() throws Exception {
        String invalidJwt = "invalid-jwt-sample";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + invalidJwt);
        when(jwtUtils.validateJwtToken(invalidJwt)).thenReturn(false);

        doFilterInternal.invoke(authTokenFilter, request, response, filterChain);

        verify(jwtUtils, times(1)).validateJwtToken(invalidJwt);
        verify(userDetailsService, never()).loadUserByUsername(anyString());
        verify(filterChain, times(1)).doFilter(request, response);

        assertNull(SecurityContextHolder.getContext().getAuthentication(),
                "L'authentification doit rester nulle pour un JWT invalide");
    }

    @Test
    void shouldSkipAuthentication_WhenNoJwtIsProvided() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        doFilterInternal.invoke(authTokenFilter, request, response, filterChain);

        verify(jwtUtils, never()).validateJwtToken(anyString());
        verify(userDetailsService, never()).loadUserByUsername(anyString());
        verify(filterChain, times(1)).doFilter(request, response);

        assertNull(SecurityContextHolder.getContext().getAuthentication(),
                "L'authentification doit rester nulle lorsqu'aucun JWT n'est fourni");
    }

    @Test
    void shouldHandleMalformedJwtToken() throws Exception {
        String malformedJwt = "malformed-jwt-sample";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + malformedJwt);
        when(jwtUtils.validateJwtToken(malformedJwt)).thenThrow(new IllegalArgumentException("Malformed token"));

        doFilterInternal.invoke(authTokenFilter, request, response, filterChain);

        verify(jwtUtils, times(1)).validateJwtToken(malformedJwt);
        verify(userDetailsService, never()).loadUserByUsername(anyString());
        verify(filterChain, times(1)).doFilter(request, response);

        assertNull(SecurityContextHolder.getContext().getAuthentication(),
                "L'authentification doit rester nulle pour un JWT malformé");
    }
}
