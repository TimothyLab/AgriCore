package agricore.projet.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Employe extends Utilisateur {

	@ManyToOne
	@JoinColumn(name="fermier_id")
	private Fermier fermier;

	public Employe() {
	}

	public Employe(Integer id, String login, String password, String nom, String prenom) {
		super(id, login, password, nom, prenom);
	}

	@Override
	public String toString() {
		return "Employe [id=" + id + ", login=" + login + ", password=" + password + "]";
	}

}
