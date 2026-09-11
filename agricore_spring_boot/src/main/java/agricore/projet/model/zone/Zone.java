package agricore.projet.model.zone;

import agricore.projet.model.Plante;
import agricore.projet.model.Vehicule;
import agricore.projet.model.animal.Animal;
import agricore.projet.model.ressource.Ressource;
import agricore.projet.model.zone.position.CellGridPosition;
import agricore.projet.model.zone.position.Position;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name="zone")
public class Zone {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="zone_id")
	private Integer id;
	
	@Embedded
	private Position position;
	
	@Enumerated(EnumType.STRING) 
	@Column(nullable = false, columnDefinition = "varchar(50)")
	private NomZone nomZone;

	//---- Mapping avec les autres classes
	
	@OneToMany(mappedBy = "zone")
	@JsonIgnore
	private List<Animal> animals = new ArrayList<>();

	@OneToOne(mappedBy = "zone")
	private Plante plante;
	
	@OneToMany(mappedBy = "zone")
	@JsonIgnore
	private List<Vehicule> vehicules = new ArrayList<>();
	
	@OneToMany(mappedBy = "zone")
	@JsonIgnore
	private List<Ressource> ressources = new ArrayList<>();

	public Zone() {
	}

	public Zone(Position position, NomZone nomZone) {
		super();
		this.position = position;
		this.nomZone = nomZone;

	}
	public Zone(Integer id, Position position, NomZone nomZone) {
		super();
		this.id = id;
		this.position = position;
		this.nomZone = nomZone;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public NomZone getNomZone() {
		return nomZone;
	}

	public void setNomZone(NomZone nomZone) {
		this.nomZone = nomZone;
	}

	public List<Animal> getAnimals() {
		return animals;
	}

	public void setAnimals(List<Animal> animals) {
		this.animals = animals;
	}

	public Plante getPlante() {
		return plante;
	}

	public void setPlante(Plante plante) {
		this.plante = plante;
	}
	
	public List<Vehicule> getVehicules() {
		return vehicules;
	}

	public void setVehicules(List<Vehicule> vehicules) {
		this.vehicules = vehicules;
	}

	public List<Ressource> getRessources() {
		return ressources;
	}

	public void setRessources(List<Ressource> ressources) {
		this.ressources = ressources;
	}

	public Set<CellGridPosition> getCellGridPositions() {
		//Renvoie l'ensemble des Cellules sur la carte (ces cellules ne sont pas validé (pas verif overlap, pas verif limite de carte))
		return nomZone.getZoneShape()
				.getShape()
				.stream()
				.map(cellOffset-> cellOffset.convertToGridPosition(
						position.getAnchorX(),
						position.getAnchorY(),
						position.getRotation()))
				.collect(Collectors.toSet());
	}

	@Override
	public String toString() {
		return "Zone{" +
				"id=" + id +
				", position=" + position +
				", nomZone=" + nomZone +
				'}';
	}
}
