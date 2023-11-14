package KlajdiNdoci.U5W2D5Project.services;

import KlajdiNdoci.U5W2D5Project.entities.User;
import KlajdiNdoci.U5W2D5Project.enums.Role;
import KlajdiNdoci.U5W2D5Project.exceptions.BadRequestException;
import KlajdiNdoci.U5W2D5Project.exceptions.UnauthorizedException;
import KlajdiNdoci.U5W2D5Project.payloads.NewUserDTO;
import KlajdiNdoci.U5W2D5Project.payloads.UserLoginDTO;
import KlajdiNdoci.U5W2D5Project.repositories.UserRepository;
import KlajdiNdoci.U5W2D5Project.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AuthService {
    @Autowired
    PasswordEncoder bcrypt;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private JWTTools jwtTools;

    public String authenticateUser(UserLoginDTO body){
        User user = userService.findByEmail(body.email());
        if(bcrypt.matches(body.password(), user.getPassword())){
            return jwtTools.createToken(user);

        }else {
            throw new UnauthorizedException("Invalid credentials");
        }
    }

    public User save(NewUserDTO body) throws IOException {

        userRepository.findByEmail(body.email()).ifPresent( user -> {
            throw new BadRequestException("The email " + user.getEmail() + " has already been used!");
        });

        User newUser = new User();
        newUser.setAvatar("https://ui-avatars.com/api/?name=" + body.name() + "+" + body.surname());
        newUser.setUsername(body.username());
        newUser.setName(body.name());
        newUser.setSurname(body.surname());
        newUser.setRole(Role.USER);
        newUser.setEmail(body.email());
        newUser.setPassword(bcrypt.encode(body.password()));
        return userRepository.save(newUser);
    }
}
