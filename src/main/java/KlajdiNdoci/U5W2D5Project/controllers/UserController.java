package KlajdiNdoci.U5W2D5Project.controllers;

import KlajdiNdoci.U5W2D5Project.entities.User;
import KlajdiNdoci.U5W2D5Project.exceptions.BadRequestException;
import KlajdiNdoci.U5W2D5Project.payloads.NewUserDTO;
import KlajdiNdoci.U5W2D5Project.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;


    @GetMapping("")
    public Page<User> getUser(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size,
                                @RequestParam(defaultValue = "id") String orderBy){
        return userService.getUsers(page, size, orderBy);
    }


    @GetMapping("/{id}")
    public User findById(@PathVariable long id) {
        return userService.findById(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable long id) {
        userService.findByIdAndDelete(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User findByIdAndUpdate(@PathVariable long id, @RequestBody @Validated NewUserDTO body,BindingResult validation) {
        if (validation.hasErrors()){
            throw new BadRequestException(validation.getAllErrors());
        }else {
        return userService.findByIdAndUpdate(id, body);
        }
    }

    @PostMapping("/{id}/upload")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User upload(@RequestParam("avatar") MultipartFile body, @PathVariable long id) throws IOException {
        if (!Objects.equals(body.getContentType(), "multipart/form-data")) {
            throw new BadRequestException("Insert a supported file");
        }
        return userService.uploadPicture(body, id);
    }

    @GetMapping("/me")
    public UserDetails getProfile(@AuthenticationPrincipal UserDetails currentUser){
        return currentUser;
    }

    @PutMapping("/me")
    public UserDetails updateProfile(@AuthenticationPrincipal User currentUser, @RequestBody NewUserDTO body){
        return userService.findByIdAndUpdate(currentUser.getId(), body);
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProfile(@AuthenticationPrincipal User currentUser){
        userService.findByIdAndDelete(currentUser.getId());
    }

    @PostMapping("/me/upload")
    public UserDetails uploadOnProfile(@AuthenticationPrincipal User currentUser,  @RequestParam("avatar") MultipartFile body) throws IOException {
        if (!Objects.equals(body.getContentType(), "multipart/form-data")) {
            throw new BadRequestException("Insert a supported file");
        }
        return userService.uploadPicture(body, currentUser.getId());
    }

}

