package KlajdiNdoci.U5W2D5Project.services;

import KlajdiNdoci.U5W2D5Project.entities.Device;
import KlajdiNdoci.U5W2D5Project.entities.User;
import KlajdiNdoci.U5W2D5Project.enums.DeviceState;
import KlajdiNdoci.U5W2D5Project.exceptions.BadRequestException;
import KlajdiNdoci.U5W2D5Project.exceptions.NotFoundException;
import KlajdiNdoci.U5W2D5Project.payloads.NewUserDTO;
import KlajdiNdoci.U5W2D5Project.repositories.UserRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Cloudinary cloudinary;


    public Page<User> getUsers(int page, int size, String orderBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return userRepository.findAll(pageable);
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
        newUser.setEmail(body.email());
        newUser.setPassword(body.password());
        return userRepository.save(newUser);
    }

    public User findById(long id) throws NotFoundException{
        User found = null;
        for (User user : userRepository.findAll()) {
            if (user.getId() == id) {
                found = user;
            }
        }
        if (found == null) {
            throw new NotFoundException(id);
        } else {
            return found;
        }
    }

    public void findByIdAndDelete(int id)throws NotFoundException {
        User found = this.findById(id);
        List<Device> devices = found.getDevices();
        for (Device device : devices) {
            device.setUser(null);
            device.setDeviceState(DeviceState.AVAILABLE);
        }
        userRepository.delete(found);
    }

    public User findByIdAndUpdate(int id, NewUserDTO body) throws NotFoundException{
        User found = this.findById(id);
        found.setUsername(body.username());
        found.setEmail(body.email());
        found.setSurname(body.surname());
        found.setName(body.name());
        found.setPassword(body.password());
        found.setAvatar("https://ui-avatars.com/api/?name=" + body.name() + "+" + body.surname());
        return userRepository.save(found);
    }
    public User uploadPicture(MultipartFile file, long id) throws IOException {
        User user = userRepository.findById(id).orElseThrow(()-> new NotFoundException(id));
        String url = (String) cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
        user.setAvatar(url);
        userRepository.save(user);
        return user;
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(()-> new NotFoundException("User with email " + email + "not found"));
    }
}
