package KlajdiNdoci.U5W2D5Project.entities;

import KlajdiNdoci.U5W2D5Project.enums.DeviceState;
import KlajdiNdoci.U5W2D5Project.enums.DeviceType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@ToString
@NoArgsConstructor
@Table(name = "devices")
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Enumerated(EnumType.STRING)
    private DeviceType deviceType;
    @Enumerated(EnumType.STRING)
    private DeviceState deviceState;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Device(DeviceType deviceType, DeviceState deviceState, User user) {
        this.deviceType = deviceType;
        this.deviceState = DeviceState.AVAILABLE;
        this.user = user;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public void setDeviceState(DeviceState deviceState) {
        this.deviceState = deviceState;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

