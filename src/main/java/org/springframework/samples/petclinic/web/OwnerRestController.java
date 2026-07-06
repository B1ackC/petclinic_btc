package org.springframework.samples.petclinic.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class OwnerRestController {

    private final ClinicService clinicService;

    @Autowired
    public OwnerRestController(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    @GetMapping("/owners")
    public ResponseEntity<Collection<Owner>> findOwners(
            @RequestParam(value = "lastName", required = false, defaultValue = "") String lastName) {
        return ResponseEntity.ok(clinicService.findOwnerByLastName(lastName));
    }

    @GetMapping("/owners/{ownerId}")
    public ResponseEntity<Owner> getOwner(@PathVariable int ownerId) {
        Owner owner = clinicService.findOwnerById(ownerId);
        if (owner == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(owner);
    }

    @PostMapping("/owners")
    public ResponseEntity<?> createOwner(@Valid @RequestBody Owner owner, BindingResult result) {
        if (result.hasErrors()) return ResponseEntity.badRequest().body(fieldErrors(result));
        clinicService.saveOwner(owner);
        return ResponseEntity.status(HttpStatus.CREATED).body(owner);
    }

    @PutMapping("/owners/{ownerId}")
    public ResponseEntity<?> updateOwner(@PathVariable int ownerId,
                                         @Valid @RequestBody Owner owner, BindingResult result) {
        if (result.hasErrors()) return ResponseEntity.badRequest().body(fieldErrors(result));
        owner.setId(ownerId);
        clinicService.saveOwner(owner);
        return ResponseEntity.ok(owner);
    }

    @GetMapping("/pet-types")
    public ResponseEntity<Collection<PetType>> getPetTypes() {
        return ResponseEntity.ok(clinicService.findPetTypes());
    }

    @PostMapping("/owners/{ownerId}/pets")
    public ResponseEntity<?> addPet(@PathVariable int ownerId,
                                    @RequestBody Map<String, Object> body) {
        Owner owner = clinicService.findOwnerById(ownerId);
        if (owner == null) return ResponseEntity.notFound().build();

        Pet pet = new Pet();
        pet.setName((String) body.get("name"));
        String bd = (String) body.get("birthDate");
        if (bd != null && !bd.isEmpty()) pet.setBirthDate(LocalDate.parse(bd));
        resolveAndSetType(pet, body);
        owner.addPet(pet);
        clinicService.savePet(pet);
        return ResponseEntity.status(HttpStatus.CREATED).body(pet);
    }

    @PutMapping("/owners/{ownerId}/pets/{petId}")
    public ResponseEntity<?> updatePet(@PathVariable int ownerId, @PathVariable int petId,
                                       @RequestBody Map<String, Object> body) {
        Owner owner = clinicService.findOwnerById(ownerId);
        if (owner == null) return ResponseEntity.notFound().build();
        Pet pet = clinicService.findPetById(petId);
        if (pet == null) return ResponseEntity.notFound().build();

        if (body.containsKey("name")) pet.setName((String) body.get("name"));
        if (body.containsKey("birthDate")) {
            String bd = (String) body.get("birthDate");
            if (bd != null && !bd.isEmpty()) pet.setBirthDate(LocalDate.parse(bd));
        }
        if (body.containsKey("typeId")) resolveAndSetType(pet, body);
        clinicService.savePet(pet);
        return ResponseEntity.ok(pet);
    }

    @PostMapping("/owners/{ownerId}/pets/{petId}/visits")
    public ResponseEntity<?> addVisit(@PathVariable int ownerId, @PathVariable int petId,
                                      @RequestBody Map<String, Object> body) {
        Pet pet = clinicService.findPetById(petId);
        if (pet == null) return ResponseEntity.notFound().build();

        Visit visit = new Visit();
        String date = (String) body.get("date");
        if (date != null && !date.isEmpty()) visit.setDate(LocalDate.parse(date));
        visit.setDescription((String) body.get("description"));
        pet.addVisit(visit);
        clinicService.saveVisit(visit);
        return ResponseEntity.status(HttpStatus.CREATED).body(visit);
    }

    private void resolveAndSetType(Pet pet, Map<String, Object> body) {
        Object typeIdObj = body.get("typeId");
        if (typeIdObj == null) return;
        int typeId = ((Number) typeIdObj).intValue();
        for (PetType pt : clinicService.findPetTypes()) {
            if (pt.getId() == typeId) { pet.setType(pt); return; }
        }
    }

    private Map<String, String> fieldErrors(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));
        return errors;
    }
}
