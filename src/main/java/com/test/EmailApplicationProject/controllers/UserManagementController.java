package com.test.EmailApplicationProject.controllers;

import com.test.EmailApplicationProject.exceptions.LoginException;
import com.test.EmailApplicationProject.models.*;
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

import java.util.Optional;
import java.util.UUID;

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
        String domain = signup.getEmail().split("@")[1];
        if (!domain.equals("email.com")) {
            return new ResponseEntity<>("Please register your email with @email.com domain!", HttpStatus.BAD_REQUEST);
        }
        User user = userRepository.findByEmail(signup.getEmail());
        if(user != null) {
            return new ResponseEntity<>("Email is already taken! Please use a different e-mail id to sign-up.", HttpStatus.BAD_REQUEST);
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

    // Getting username (email) from jwt
    @GetMapping("/welcome")
    public String welcome(@RequestHeader(value="Authorization") String token) {
        String jwt = token.substring(7); //Bearer" "
        return "Welcome!, " + jwtUtil.extractUsername(jwt);
    }

    @PutMapping("/api/user")
    public String updateUser(@RequestHeader(value="Authorization") String token, @RequestBody UpdateRequest updateRequest) {
        String email = jwtUtil.extractUsername(token.substring(7));
        User user = userRepository.findByEmail(email);
        String hashedUpdatedPassword = null;
        if(!updateRequest.getPassword().equals(""))
        {
            hashedUpdatedPassword = bCryptPasswordEncoder.encode(updateRequest.getPassword());
        }

        String updatedFirstName = user.getFirstName().equals(updateRequest.getFirstName()) || updateRequest.getFirstName().equals("") ? user.getFirstName() : updateRequest.getFirstName();
        String updatedLastName = user.getLastName().equals(updateRequest.getLastName()) || updateRequest.getLastName().equals("") ? user.getLastName() : updateRequest.getLastName();
        String updatedPassword = hashedUpdatedPassword == null || user.getPassword().equals(hashedUpdatedPassword) ? user.getPassword() : hashedUpdatedPassword;

        userRepository.updateUser(user.getId(), updatedFirstName, updatedLastName, email, updatedPassword);
        return "Updated user with the email: " + email;
    }

    @DeleteMapping("/api/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") int id, @RequestHeader("Authorization") String token) {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()) {
            String email = user.get().getEmail();
            System.out.println(email);
            String jwtEmail = jwtUtil.extractUsername(token.substring(7));
            if(email.equals(jwtEmail)) {
                userRepository.deleteById(id);
                return new ResponseEntity<String>("User with ID: " + id + " and email: " + email + " was deleted.", HttpStatus.OK);
            } else {
                return new ResponseEntity<String>("You can only delete your email account!", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<String>("User with ID: " + id + " does not exist!", HttpStatus.NOT_FOUND);
        }
    }


}
