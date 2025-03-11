package es.codeurjc.board.controller;

import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import es.codeurjc.board.dto.PostDTO;
import es.codeurjc.board.service.PostService;

@Controller
public class PostWebController {

	@Autowired
	private PostService postService;

	@GetMapping("/")
	public String showPosts(Model model) {

		model.addAttribute("posts", postService.getPosts());

		return "index";
	}

	@GetMapping("/post/{id}")
	public String showPost(Model model, @PathVariable long id) {

		try {
			PostDTO post = postService.getPost(id);
			model.addAttribute("post", post);
			return "show_post";	

		} catch (NoSuchElementException e){
			return "post_not_found";
		}
	}

	@PostMapping("/post/new")
	public String newPost(Model model, PostDTO postDTO) {

		postService.createPost(postDTO);

		return "saved_post";
	}

	@GetMapping("/editpost/{id}")
	public String editPost(Model model, @PathVariable long id) {

		try {
			PostDTO post = postService.getPost(id);
			model.addAttribute("post", post);
			return "edit_post_page";	

		} catch (NoSuchElementException e){
			return "post_not_found";
		}
	}

	@PostMapping("/editpost")
	public String editPostProcess(Model model, PostDTO updatedPostDTO) {

		try {
		
			postService.replacePost(updatedPostDTO.id(), updatedPostDTO);
			model.addAttribute("post", updatedPostDTO);
			return "edited_post";

		} catch (NoSuchElementException e){
			return "post_not_found";
		}
	}

	@PostMapping("/post/{id}/delete")
	public String deletePost(Model model, @PathVariable long id) {

		try {
		
			postService.deletePost(id);
			return "deleted_post";

		} catch (NoSuchElementException e){
			return "post_not_found";
		}
	}

}
