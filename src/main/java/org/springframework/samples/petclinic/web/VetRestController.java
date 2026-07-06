package org.springframework.samples.petclinic.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.model.Vets;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class VetRestController {

    private final ClinicService clinicService;

    @Autowired
    public VetRestController(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    @GetMapping("/vets")
    public ResponseEntity<Vets> getVets() {
        Vets vets = new Vets();
        vets.getVetList().addAll(clinicService.findVets());
        return ResponseEntity.ok(vets);
    }
}
