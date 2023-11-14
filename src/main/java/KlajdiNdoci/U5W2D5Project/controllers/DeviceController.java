package KlajdiNdoci.U5W2D5Project.controllers;

import KlajdiNdoci.U5W2D5Project.entities.Device;
import KlajdiNdoci.U5W2D5Project.exceptions.BadRequestException;
import KlajdiNdoci.U5W2D5Project.payloads.NewDeviceDTO;
import KlajdiNdoci.U5W2D5Project.services.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/devices")
public class DeviceController {
    @Autowired
    private DeviceService deviceService;

    @GetMapping("")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<Device> getDevices(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 @RequestParam(defaultValue = "id") String orderBy){
        return deviceService.getDevices(page, size, orderBy);
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Device saveDevice(@RequestBody @Validated NewDeviceDTO body, BindingResult validation) {
        if (validation.hasErrors()){
            throw new BadRequestException(validation.getAllErrors());
        }else {
            try {
                return deviceService.save(body);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Device findById(@PathVariable long id) {
        return deviceService.findById(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable int id) {
        deviceService.findByIdAndDelete(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Device findByIdAndUpdate(@PathVariable int id, @RequestBody NewDeviceDTO body) {
        return deviceService.findByIdAndUpdate(id, body);
    }

    @PatchMapping ("/{id}/assign")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Device findByIdAndAssign(@PathVariable int id, @RequestBody NewDeviceDTO body){
        return deviceService.findByIdAndAssign(id, body);
    }

    @PatchMapping ("/{id}/dismiss")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Device findByIdAndDismiss(@PathVariable int id){
        return deviceService.findByIdAndDismiss(id);
    }

    @PatchMapping ("/{id}/service")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Device findByIdAndSendToMaintenance(@PathVariable int id){
        return deviceService.findByIdAndSendToMaintenance(id);
    }
}
