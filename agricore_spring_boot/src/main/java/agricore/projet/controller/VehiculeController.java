package agricore.projet.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import agricore.projet.dto.vehicule.request.VehiculeRequestDTO;
import agricore.projet.dto.vehicule.response.TypeVehiculeDTO;
import agricore.projet.dto.vehicule.response.VehiculeResponseDTO;
import agricore.projet.model.Plante;
import agricore.projet.model.TypeVehicule;
import agricore.projet.model.Vehicule;
import agricore.projet.model.animal.Animal;
import agricore.projet.model.ressource.NomRessource;
import agricore.projet.model.ressource.Ressource;
import agricore.projet.repository.IDAOAnimal;
import agricore.projet.repository.IDAOPlante;
import agricore.projet.repository.IDAORessource;
import agricore.projet.repository.IDAOVehicule;
import agricore.projet.repository.IDAOZone;
import agricore.projet.services.VehiculeService;
import jakarta.validation.Valid;



@RestController
@RequestMapping("/api/vehicule")
public class VehiculeController {

  
    private final VehiculeService vehiculeService;
    public final IDAOVehicule daoVehicule;
    public final IDAORessource daoRessource;
    public final IDAOAnimal daoAnimal;  
    public final IDAOZone daoZone;  
    public final IDAOPlante daoPlante;

    VehiculeController(IDAOVehicule vehiculeRepository, IDAORessource ressourceRepository, VehiculeService vehiculeService, IDAOAnimal animalRepository, IDAOZone zoneRepository, IDAOPlante planteRepository) {
        this.vehiculeService = vehiculeService;
        this.daoVehicule = vehiculeRepository;
        this.daoRessource = ressourceRepository;
        this.daoAnimal = animalRepository;
        this.daoZone = zoneRepository;
        this.daoPlante = planteRepository;
    }



    @GetMapping("/types")
    public List<TypeVehiculeDTO> getTypesVehicule() {
        return Arrays.stream(TypeVehicule.values())
                .map(tv -> new TypeVehiculeDTO(tv.name(), tv.getCapaciteReservoir(), tv.getConsoParKm(), tv.getPathSprite()))
                .toList();
    }

// Consommer du carburant pour acheter animal ou récolter ressource : on vérifie que le véhicule a assez de carburant pour faire le trajet, si oui on consomme le carburant nécessaire, sinon on lève une exception
    @PostMapping("/{animalId}/acheterAnimal")
    public void acheterAnimal(@PathVariable Integer animalId, @RequestParam Integer vehiculeId) {
        
        Vehicule vehicule = daoVehicule.findById(vehiculeId).orElseThrow(() -> new RuntimeException("Véhicule non trouvé"));
        Animal animal = daoAnimal.findById(animalId).orElseThrow(() -> new RuntimeException("Animal non trouvé"));
        
        vehiculeService.acheterAnimal(animal,vehicule);
    }

    @PostMapping("/{planteId}/recolterPlante")
    public void recolterPlante(@PathVariable Integer planteId, @RequestParam Integer vehiculeId) {
        
        Vehicule vehicule = daoVehicule.findById(vehiculeId).orElseThrow(() -> new RuntimeException("Véhicule non trouvé"));
        Plante plante = daoPlante.findById(planteId).orElseThrow(() -> new RuntimeException("Plante non trouvée"));
        
        vehiculeService.recolterPlante(plante,vehicule);
    }



// FAIRE PLEIN : on consomme la ressource de carburant et on met à jour le niveau de carburant du véhicule.
    @PostMapping("/{id}/fairePlein")
    public void fairePlein(@PathVariable Integer id) {
        Vehicule vehicule = daoVehicule.findById(id).orElseThrow(() -> new RuntimeException("Véhicule non trouvé"));

        Ressource carburant = daoRessource.findByNom(NomRessource.ESSENCE).orElseThrow(() -> new RuntimeException("Carburant non trouvé") );
        
        vehiculeService.fairePlein(vehicule, carburant);
    }

//CRUD de base pour les véhicules

    @GetMapping
    public List<VehiculeResponseDTO> getAll() {
        return vehiculeService.findAllDTO();
    }


    @GetMapping("/{id}")
    public VehiculeResponseDTO getVehiculeById(@PathVariable int id) {
        return vehiculeService.findByIdDTO(id);
    }

    @PutMapping("/{id}")
    public VehiculeResponseDTO modifier( @PathVariable Integer id, @Valid @RequestBody VehiculeRequestDTO vehiculeRequestDTO) {
        return vehiculeService.update(id, vehiculeRequestDTO);
    }
    

    @PostMapping
    public VehiculeResponseDTO ajouter(@Valid @RequestBody VehiculeRequestDTO vehiculeRequestDTO) {
        return vehiculeService.create(vehiculeRequestDTO);
    }

   

    @DeleteMapping("/{id}")
    public void deleteVehicule(@PathVariable Integer id) { 
        vehiculeService.delete(id);

    }
    
    

}
