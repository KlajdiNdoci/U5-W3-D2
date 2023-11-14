package KlajdiNdoci.U5W2D5Project.entities;

import KlajdiNdoci.U5W2D5Project.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String name;
    private String surname;
    private String email;
    private String avatar;
    private String password;
    private Role role;
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    @ToString.Exclude
    private List<Device> devices;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }
}