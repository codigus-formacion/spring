package es.codeurjc.db;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import es.codeurjc.db.model.Blog;
import es.codeurjc.db.model.Comment;
import jakarta.annotation.PostConstruct;

@Controller
public class BlogController {

	@Autowired
	private BlogRepository blogRepository;

	@Autowired
	private CommentRepository commentRepository;

	@PostConstruct
	public void init() {

		Blog blog = new Blog("Spring Blog", "Spring is cool");
		blog.getComments().add(new Comment("Pepe", "Cool!"));
		blog.getComments().add(new Comment("Juan", "Very cool"));

		blogRepository.save(blog);
	}

	@GetMapping("/")
	public String getBlogs(Model model) throws Exception {
		model.addAttribute("blogs", blogRepository.findAll());
		return "index";
	}

	@PostMapping("/blogs/new")
	public String newBlog(Model model, Blog blog) {
		blogRepository.save(blog);
		return "saved_blog";
	}

	@GetMapping("/blogs/{id}")
	public String getBlog(Model model, @PathVariable long id) {
		Optional<Blog> blog = blogRepository.findById(id);
		if (blog.isPresent()) {
			model.addAttribute("blog", blog.get());
			return "show_blog";
		} else {
			return "blog_not_found";
		}
	}

	// Deleting a blog delete its associated comments
	@PostMapping("/blogs/{id}/delete")
	public String deleteBlog(@PathVariable long id) {
		Optional<Blog> blog = blogRepository.findById(id);
		if (blog.isPresent()) {
			blogRepository.deleteById(id);
			return "redirect:/";
		} else {
			return "blog_not_found";
		}
	}

	@PostMapping("/blogs/{blogId}/comments/new")
	public String newComment(@PathVariable long blogId, Comment comment) {
		Blog blog = blogRepository.findById(blogId).orElseThrow();
		blog.getComments().add(comment);
		commentRepository.save(comment);
		return "redirect:/blogs/" + blogId;
	}

	@PostMapping("/blogs/{blogId}/comments/{commentId}/delete")
	public String deleteComment(@PathVariable Long blogId, @PathVariable Long commentId) {
		Blog blog = blogRepository.findById(blogId).orElseThrow();
		Comment comment = commentRepository.findById(commentId).orElseThrow();
		blog.getComments().remove(comment);
		commentRepository.deleteById(commentId);
		return "redirect:/blogs/" + blogId;
	}

}
