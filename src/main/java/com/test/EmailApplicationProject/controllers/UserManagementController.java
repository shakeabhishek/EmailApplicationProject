package com.test.EmailApplicationProject.controllers;

import com.test.EmailApplicationProject.exceptions.LoginException;
import com.test.EmailApplicationProject.models.LoginRequest;
import com.test.EmailApplicationProject.models.LoginResponse;
import com.test.EmailApplicationProject.models.SignUpRequest;
import com.test.EmailApplicationProject.models.User;
import com.test.EmailApplicationProject.repositories.UserRepository;
import com.test.EmailApplicationProject.services.MyUserDetailsService;
import com.test.EmailApplicationProject.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserManagementController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @PostMapping("/api/signup")
    public ResponseEntity <?> signupUser(@RequestBody SignUpRequest signup) {
        System.out.println(signup);
        User user = userRepository.findByEmail(signup.getEmail());
        if(user != null) {
            return new ResponseEntity<>("Email is taken! Please use a different e-mail id to sign-up.", HttpStatus.BAD_REQUEST);
        } else {
            userRepository.save(new User(signup.getFirstName(), signup.getLastName(), signup.getEmail(), bCryptPasswordEncoder.encode(signup.getPassword())));
            return new ResponseEntity<>("Email: " + signup.getEmail() + " successfully registered!", HttpStatus.CREATED);
        }
    }

    @PostMapping("/api/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) throws LoginException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new LoginException("Incorrect email or password!");
        }

        final UserDetails userDetails = myUserDetailsService.loadUserByUsername(loginRequest.getEmail());

        // Map<String, Object> userInfoClaim = new HashMap<String, Object>();

        final String jwt = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new LoginResponse(jwt));
    }

    // testing only!!
    @GetMapping("/open")
    public String open() {
        return "Hello, this endpoint is open for all!";
    }

    // testing only!!
    @GetMapping("/secure")
    public String secure() {
        return "Hello, this endpoint is secured!";
    }

    @GetMapping("/welcome")
    public String welcome(@RequestHeader(value="Authorization") String token) {
        String jwt = token.substring(7); //Bearer" "
        return "Welcome!, " + jwtUtil.extractUsername(jwt);
    }
}
