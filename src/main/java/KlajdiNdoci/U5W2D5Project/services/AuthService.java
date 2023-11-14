package KlajdiNdoci.U5W2D5Project.services;

import KlajdiNdoci.U5W2D5Project.entities.User;
import KlajdiNdoci.U5W2D5Project.exceptions.UnauthorizedException;
import KlajdiNdoci.U5W2D5Project.payloads.UserLoginDTO;
import KlajdiNdoci.U5W2D5Project.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserService userService;
    @Autowired
    private JWTTools jwtTools;

    public String authenticateUser(UserLoginDTO body){
        User user = userService.findByEmail(body.email());
        if(body.password().equals(user.getPassword())){
            return jwtTools.createToken(user);

        }else {
            throw new UnauthorizedException("Invalid credentials");
        }
    }
}
