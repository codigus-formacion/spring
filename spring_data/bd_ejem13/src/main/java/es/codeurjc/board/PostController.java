package es.codeurjc.board;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.annotation.PostConstruct;

@Controller
public class PostController {

	@Autowired
	private PostRepository posts;

	@PostConstruct
	public void init() {
		posts.save(new Post("Pepe", "Vendo moto", "Barata, barata"));
		posts.save(new Post("Juan", "Compro coche", "Pago bien"));
	}

	@GetMapping("/")
	public String showPosts(Model model, @RequestParam(required = false) String username) {
		if (username != null) {
			model.addAttribute("posts", posts.findByUsername(username));
		} else {
			model.addAttribute("posts", posts.findAll());
		}
		return "index";
	}

	@GetMapping("/post/{id}")
	public String showPost(Model model, @PathVariable long id) {

		Optional<Post> op = posts.findById(id);

		if (op.isPresent()) {
			Post post = op.get();
			model.addAttribute("post", post);
			return "show_post";	
		} else {
			return "post_not_found";
		}
	}

	@PostMapping("/post/new")
	public String newPost(Model model, Post post) {

		posts.save(post);

		return "saved_post";
	}

	@GetMapping("/editpost/{id}")
	public String editBook(Model model, @PathVariable long id) {

		Optional<Post> op = posts.findById(id);
		if (op.isPresent()) {
			Post post = op.get();
			model.addAttribute("post", post);
			return "edit_post_page";
		} else {
			return "post_not_found";
		}
	}

	@PostMapping("/editpost")
	public String editBookProcess(Model model, Post editedPost) {

		Optional<Post> op = posts.findById(editedPost.getId());
		if (op.isPresent()) {
			posts.save(editedPost);
			model.addAttribute("post", editedPost);
			return "edited_post";
		} else {
			return "post_not_found";
		}
	}

	@PostMapping("/post/{id}/delete")
	public String deletePost(Model model, @PathVariable long id) {

		Optional<Post> post = posts.findById(id);

		if (post.isPresent()) {
			posts.deleteById(id);
			return "deleted_post";
		} else {
			return "post_not_found";
		}
	}

}
