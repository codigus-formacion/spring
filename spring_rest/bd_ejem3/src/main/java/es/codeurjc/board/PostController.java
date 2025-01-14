package es.codeurjc.board;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import java.net.URI;
import java.util.Collection;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
public class PostController {

	@Autowired
	private PostRepository postRepository;

	@PostConstruct
	public void init() {
		postRepository.save(new Post("Pepe", "Vendo moto", "Barata, barata"));
		postRepository.save(new Post("Juan", "Compro coche", "Pago bien"));
	}
	
	@GetMapping("/")
	public Collection<Post> getPostRepository() {
		return postRepository.findAll();
	}

	@GetMapping("/{id}")
	public Post getPost(@PathVariable long id) {

		return postRepository.findById(id).orElseThrow();
	}

	@PostMapping("/")
	public ResponseEntity<Post> createPost(@RequestBody Post post) {

		postRepository.save(post);

		URI location = fromCurrentRequest().path("/{id}").buildAndExpand(post.getId()).toUri();

		return ResponseEntity.created(location).body(post);
	}

	@PutMapping("/{id}")
	public Post replacePost(@PathVariable long id, @RequestBody Post newPost) {

		postRepository.findById(id).orElseThrow();

		newPost.setId(id);
		postRepository.save(newPost);
			
		return newPost;
	}

	@DeleteMapping("/{id}")
	public Post deletePost(@PathVariable long id) {

		Post post = postRepository.findById(id).orElseThrow();

		postRepository.deleteById(id);
		
		return post;
	}
}
