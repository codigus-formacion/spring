package es.codeurjc.board.controller;

import java.net.URI;
import java.util.Collection;
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

import es.codeurjc.board.dto.PostDTO;
import es.codeurjc.board.service.PostService;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/posts")
public class PostController {

	@Autowired
	private PostService postService;

	@GetMapping("/")
	public Collection<PostDTO> getPosts() {
	
		return postService.getPosts();
	}

	@GetMapping("/{id}")
	public PostDTO getPost(@PathVariable long id) {

		return getPost(id);
	}

	@PostMapping("/")
	public ResponseEntity<PostDTO> createPost(@RequestBody PostDTO postDTO) {

		postService.createPost(postDTO);

		URI location = fromCurrentRequest().path("/{id}").buildAndExpand(postDTO.id()).toUri();

		return ResponseEntity.created(location).body(postDTO);
	}

	@PutMapping("/{id}")
	public PostDTO replacePost(@PathVariable long id, @RequestBody PostDTO updatedPost) {

		return postService.replacePost(updatedPost);
	}

	@DeleteMapping("/{id}")
	public PostDTO deletePost(@PathVariable long id) {

		return postService.deletePost(id);
	}
}
