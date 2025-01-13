package es.codeurjc.db.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import es.codeurjc.db.model.Comment;
import es.codeurjc.db.model.Post;
import es.codeurjc.db.service.CommentService;
import es.codeurjc.db.service.PostService;

@Controller
public class PostController {

	@Autowired
	private PostService postService;

	@Autowired
	private CommentService commentService;

	@GetMapping("/")
	public String getPosts(Model model) throws Exception {
		model.addAttribute("posts", postService.findAll());
		return "index";
	}

	@PostMapping("/posts/new")
	public String newPost(Model model, Post post) {
		postService.save(post);
		return "saved_post";
	}

	@GetMapping("/posts/{id}")
	public String getPost(Model model, @PathVariable long id) {
		Optional<Post> post = postService.findById(id);
		if (post.isPresent()) {
			model.addAttribute("post", post.get());
			return "show_post";
		} else {
			return "post_not_found";
		}
	}

	// Deleting a post delete its associated comments
	@PostMapping("/posts/{id}/delete")
	public String deletePost(@PathVariable long id) {
		Optional<Post> post = postService.findById(id);
		if (post.isPresent()) {
			postService.deleteById(id);
			return "redirect:/";
		} else {
			return "post_not_found";
		}
	}

	@PostMapping("/posts/{postId}/comments/new")
	public String newComment(@PathVariable long postId, Comment comment) {
		Post post = postService.findById(postId).get();
		post.getComments().add(comment);
		commentService.save(comment);
		return "redirect:/posts/" + postId;
	}

	@PostMapping("/posts/{postId}/comments/{commentId}/delete")
	public String deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {
		Comment comment = commentService.findById(commentId).get();
		commentService.delete(comment, postId);
		return "redirect:/posts/" + postId;
	}

}
