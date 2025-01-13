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
import es.codeurjc.db.repository.CommentRepository;
import es.codeurjc.db.repository.PostRepository;
import jakarta.annotation.PostConstruct;

@Controller
public class PostController {

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private CommentRepository commentRepository;

	@PostConstruct
	public void init() {

		Post post = new Post("Spring Post", "Spring is cool");
		post.addComment(new Comment("Pepe", "Cool!"));
		post.addComment(new Comment("Juan", "Very cool"));

		postRepository.save(post);
	}

	@GetMapping("/")
	public String getPosts(Model model) throws Exception {
		model.addAttribute("posts", postRepository.findAll());
		return "index";
	}

	@PostMapping("/posts/new")
	public String newPost(Model model, Post post) {
		postRepository.save(post);
		return "saved_post";
	}

	@GetMapping("/posts/{id}")
	public String getPost(Model model, @PathVariable long id) {
		Optional<Post> post = postRepository.findById(id);
		if (post.isPresent()) {
			model.addAttribute("post", post.get());
			return "show_post";
		} else {
			return "post_not_found";
		}
	}

	@GetMapping("/posts/{id}/edit")
	public String editPost(Model model, @PathVariable long id) {
		Optional<Post> post = postRepository.findById(id);
		if (post.isPresent()) {
			model.addAttribute("post", post.get());
			return "edit_post";
		} else {
			return "post_not_found";
		}
	}

	@PostMapping("/posts/{id}/update")
	public String updatePost(Model model, Post updatedPost, @PathVariable long id) {
		
		Post oldPost = postRepository.findById(id).orElseThrow();
		updatedPost.setId(id);

		// We assume that comments are not updated with PUT operation
		oldPost.getComments().forEach(c -> updatedPost.addComment(c));

		postRepository.save(updatedPost);

		return "redirect:/posts/" + id;
	}

	// Deleting a post delete its associated comments
	@PostMapping("/posts/{id}/delete")
	public String deletePost(@PathVariable long id) {
		Optional<Post> post = postRepository.findById(id);
		if (post.isPresent()) {
			postRepository.deleteById(id);
			return "redirect:/";
		} else {
			return "post_not_found";
		}
	}

	@PostMapping("/posts/{postId}/comments/new")
	public String newComment(@PathVariable long postId, Comment comment) {
		Post post = postRepository.findById(postId).orElseThrow();
		comment.setPost(post);
		commentRepository.save(comment);
		return "redirect:/posts/" + postId;
	}

	@PostMapping("/posts/{postId}/comments/{commentId}/delete")
	public String deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {
		Post post = postRepository.findById(postId).orElseThrow();
		Comment comment = commentRepository.findById(commentId).orElseThrow();
		post.getComments().remove(comment);
		commentRepository.deleteById(commentId);
		return "redirect:/posts/" + postId;
	}

}