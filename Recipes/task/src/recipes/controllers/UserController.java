package recipes.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import recipes.entities.User;
import recipes.services.UserDetailServiceImpl;
import recipes.repo.UserRepository;

import javax.validation.Valid;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserDetailServiceImpl detailService;
    @Autowired
    PasswordEncoder encoder;

    @PostMapping("/api/register")
    public User register(@Valid @RequestBody User user) {
        User newUser = new User(
                user.getId(), user.getEmail(), user.getPassword()
        );

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        newUser.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(newUser);

        return newUser;
    }

}
