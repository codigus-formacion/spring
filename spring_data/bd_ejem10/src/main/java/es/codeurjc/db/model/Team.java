package es.codeurjc.db.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

@Entity
public class Team {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	long id;

	private String name;
	
	private int ranking;

	@ManyToMany(mappedBy="teams")
	private List<Player> players = new ArrayList<>();

	public Team() {}

	public Team(String name, int ranking) {
		this.name = name;
		this.ranking = ranking;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String title) {
		this.name = title;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> comments) {
		this.players = comments;
	}

	@Override
	public String toString() {
		return "Team [id=" + id + ", name=" + name + ", ranking=" + ranking + "]";
	}

}