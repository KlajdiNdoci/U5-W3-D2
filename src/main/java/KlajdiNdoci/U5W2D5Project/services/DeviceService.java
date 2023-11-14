package KlajdiNdoci.U5W2D5Project.services;

import KlajdiNdoci.U5W2D5Project.entities.Device;
import KlajdiNdoci.U5W2D5Project.entities.User;
import KlajdiNdoci.U5W2D5Project.enums.DeviceState;
import KlajdiNdoci.U5W2D5Project.enums.DeviceType;
import KlajdiNdoci.U5W2D5Project.exceptions.BadRequestException;
import KlajdiNdoci.U5W2D5Project.exceptions.NotFoundException;
import KlajdiNdoci.U5W2D5Project.payloads.NewDeviceDTO;
import KlajdiNdoci.U5W2D5Project.repositories.DeviceRepository;
import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;

@Service
public class DeviceService {
    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private Cloudinary cloudinary;


    public Page<Device> getDevices(int page, int size, String orderBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return deviceRepository.findAll(pageable);
    }

    public Device save(NewDeviceDTO body) throws IOException {

        Device newDevice = new Device();
        newDevice.setDeviceState(DeviceState.AVAILABLE);
        if (body.userId() !=null){
            User foundUser = userService.findById(body.userId());
            newDevice.setDeviceState(DeviceState.ASSIGNED);
            newDevice.setUser(foundUser);
        }
        if (body.deviceType().equalsIgnoreCase("smartphone")){
            newDevice.setDeviceType(DeviceType.SMARTPHONE);
        }else if (body.deviceType().equalsIgnoreCase("laptop")){
            newDevice.setDeviceType(DeviceType.LAPTOP);
        } else if (body.deviceType().equalsIgnoreCase("tablet")) {
            newDevice.setDeviceType(DeviceType.TABLET);
        }else {
            throw new BadRequestException("Invalid device type");
        }
        return deviceRepository.save(newDevice);
    }

    public Device findById(long id) throws NotFoundException {
        Device found = null;
        for (Device device : deviceRepository.findAll()) {
            if (device.getId() == id) {
                found = device;
            }
        }
        if (found == null) {
            throw new NotFoundException(id);
        } else {
            return found;
        }
    }

    public void findByIdAndDelete(int id) throws NotFoundException {
        Device found = this.findById(id);
        deviceRepository.delete(found);
    }

    public Device findByIdAndUpdate(int id, NewDeviceDTO body) throws NotFoundException {
        Device foundDevice = this.findById(id);
        if (body.userId()!= null){
            User foundUser = userService.findById(body.userId());
            foundDevice.setUser(foundUser);
        }

        if (body.deviceType().equalsIgnoreCase("smartphone")){
            foundDevice.setDeviceType(DeviceType.SMARTPHONE);
        }else if (body.toString().equalsIgnoreCase("laptop")){
            foundDevice.setDeviceType(DeviceType.LAPTOP);
        } else if (body.toString().equalsIgnoreCase("tablet")) {
            foundDevice.setDeviceType(DeviceType.TABLET);
        }else {
            throw new BadRequestException("Invalid device type");
        }
        return deviceRepository.save(foundDevice);
    }

    public Device findByIdAndAssign(int id, NewDeviceDTO body) throws NotFoundException{
        Device foundDevice = this.findById(id);
        if (body.userId()!= null){
            User foundUser = userService.findById(body.userId());
            foundDevice.setUser(foundUser);

            if (foundDevice.getDeviceState() == DeviceState.ASSIGNED) {
                throw new BadRequestException("The device is already assigned");
            } else if (foundDevice.getDeviceState() == DeviceState.DISMISSED) {
                throw new BadRequestException("The device is dismissed");
            }
            foundDevice.setDeviceState(DeviceState.ASSIGNED);
        }
        return deviceRepository.save(foundDevice);
    }

    public Device findByIdAndDismiss(int id) throws NotFoundException{
        Device foundDevice = this.findById(id);
        foundDevice.setDeviceState(DeviceState.DISMISSED);
        foundDevice.setUser(null);
        return deviceRepository.save(foundDevice);
    }
    public Device findByIdAndSendToMaintenance(int id) throws NotFoundException{
        Device foundDevice = this.findById(id);
        if (!foundDevice.getDeviceState().toString().equalsIgnoreCase("dismissed")){
            foundDevice.setDeviceState(DeviceState.MAINTENANCE);
        }else {
            throw new BadRequestException("The device is dismissed");
        }
        return deviceRepository.save(foundDevice);
    }
}
