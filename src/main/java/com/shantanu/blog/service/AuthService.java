package com.shantanu.blog.service;

import com.shantanu.blog.dto.AuthenticationResponse;
import com.shantanu.blog.dto.LoginRequest;
import com.shantanu.blog.dto.RegisterRequest;
import com.shantanu.blog.exceptions.SpringRedditException;
import com.shantanu.blog.model.NotificationEmail;
import com.shantanu.blog.model.User;
import com.shantanu.blog.model.VerificationToken;
import com.shantanu.blog.repository.UserRepository;
import com.shantanu.blog.repository.VerificationTokenRepository;
import com.shantanu.blog.security.JwtProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {

    private final PasswordEncoder encoder;

    private final UserRepository userRepository;

    private final MailService mailService;

    private final VerificationTokenRepository verificationTokenRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtProvider jwtProvider;

    @Transactional
    public void signUp(RegisterRequest registerRequest) {
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setUsername(registerRequest.getUserName());
        user.setPassword(encoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);

        userRepository.save(user);

        String token = generateVerificationToken(user);
        mailService.sendMail(new NotificationEmail("Please Activate your Account",
                user.getEmail(), "Thank you for signing up to Spring Reddit, " +
                "please click on the below url to activate your account : " +
                "http://localhost:8080/api/auth/accountVerification/" + token));
    }

    private String generateVerificationToken(User user) {

        String token = UUID.randomUUID().toString();
        VerificationToken v = new VerificationToken();
        v.setToken(token);
        v.setUser(user);

        verificationTokenRepository.save(v);

        return token;

    }

    public void verifyByToken(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(() -> new SpringRedditException("Invalid Token"));

        fetchUserAndEnable(verificationToken.get());
    }

    @Transactional
    public void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new SpringRedditException("User not found with name: " + username));
        user.setEnabled(true);
        userRepository.save(user);
    }

    public AuthenticationResponse login(LoginRequest request) {

        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUserName(),
                        request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);

        return new AuthenticationResponse(token, request.getUserName());
    }
}
