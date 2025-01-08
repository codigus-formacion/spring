package es.codeurjc.db;

import java.lang.foreign.Linker.Option;
import java.util.Optional;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import es.codeurjc.db.model.Player;
import es.codeurjc.db.model.Team;
import jakarta.annotation.PostConstruct;

@Controller
public class TeamController {

	@Autowired
	private TeamRepository teamRepository;
	
	@Autowired
	private PlayerRepository playerRepository;

	@PostConstruct
	public void init() {

		Player p1 = new Player("Torres", 10);
		Player p2 = new Player("Iniesta", 10);
		
		playerRepository.save(p1);
		playerRepository.save(p2);
		
		Team team = new Team("Selección", 1);
		
		team.getPlayers().add(p1);
		team.getPlayers().add(p2);

		teamRepository.save(team);
	}

	@GetMapping("/")
	public String getIndex(Model model) {
		model.addAttribute("teams", teamRepository.findAll());
		model.addAttribute("players", playerRepository.findAll());
		return "index";
	}

	@GetMapping("/teams/{id}")
	public String getTeam(Model model, @PathVariable long id) {
		Optional<Team> teamOptional = teamRepository.findById(id);
		if(teamOptional.isEmpty()) {
			return "team_not_found";
		}else{
			Team team = teamOptional.get();
			model.addAttribute("team", team);
			return "show_team";
		}
	}

	@GetMapping("/players/{id}")
	public String getPlayer(Model model, @PathVariable long id) {
		Optional<Player> playerOptional = playerRepository.findById(id);
		if(playerOptional.isEmpty()) {
			return "player_not_found";
		}else{
			Player player = playerOptional.get();
			model.addAttribute("player", player);
			return "show_player";
		}
	}
	
	// Deleting a team doesn't delete its associated players
	@PostMapping("/teams/{id}/delete")	
	public String deleteTeam(@PathVariable Long id) {
		Optional<Team> teamOptional = teamRepository.findById(id);
		if(teamOptional.isEmpty()) {
			return "team_not_found";
		}else{
			teamRepository.deleteById(id);
			return "deleted_team";
		}
	}
	
	// A player only can be deleted if it has no associated team
	@PostMapping("/players/{id}/delete")	
	public String deleteProject(@PathVariable Long id) {
		Optional<Player> playerOptional = playerRepository.findById(id);
		if(playerOptional.isEmpty()) {
			return "player_not_found";
		}else{
			playerRepository.deleteById(id);
			return "deleted_player";
		}
	}
}
