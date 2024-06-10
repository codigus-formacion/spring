package es.codeurjc.board.controller;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.codeurjc.board.model.Post;
import es.codeurjc.board.service.PostService;

@Controller
public class PostController {

	@Autowired
	private PostService postService;

	@GetMapping("/")
	public String showPosts(Model model, @RequestParam(required = false) String usuario) {

		Collection<Post> posts = postService.findAll();

		if(usuario != null) posts = posts.stream().filter(p -> p.getUser().equals(usuario)).toList();

		model.addAttribute("posts", posts);
		return "index";
	}

	@PostMapping("/post/new")
	public String newPost(Model model, Post post) {

		postService.save(post);

		return "saved_post";
	}

	@GetMapping("/post/{id}")
	public String showPost(Model model, @PathVariable long id) {

		Post post = postService.findById(id);

		model.addAttribute("post", post);

		return "show_post";
	}
	
	@GetMapping("/post/{id}/delete")
	public String deletePost(Model model, @PathVariable long id) {

		postService.deleteById(id);

		return "deleted_post";
	}
}