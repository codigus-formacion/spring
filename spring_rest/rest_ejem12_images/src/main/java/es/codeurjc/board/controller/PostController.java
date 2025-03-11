package es.codeurjc.board.controller;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

		return postService.getPost(id);
	}

	@PostMapping("/")
	public ResponseEntity<PostDTO> createPost(@RequestBody PostDTO postDTO) {

		postDTO = postService.createPost(postDTO);

		URI location = fromCurrentRequest().path("/{id}").buildAndExpand(postDTO.id()).toUri();

		return ResponseEntity.created(location).body(postDTO);
	}

	@PutMapping("/{id}")
	public PostDTO replacePost(@PathVariable long id, @RequestBody PostDTO updatedPostDTO) throws SQLException {

		return postService.replacePost(id, updatedPostDTO);
	}

	@DeleteMapping("/{id}")
	public PostDTO deletePost(@PathVariable long id) {

		return postService.deletePost(id);
	}

	@PostMapping("/{id}/image")
	public ResponseEntity<Object> createPostImage(@PathVariable long id, @RequestParam MultipartFile imageFile)
			throws IOException {

		URI location = fromCurrentRequest().build().toUri();

		postService.createPostImage(id, location, imageFile.getInputStream(), imageFile.getSize());

		return ResponseEntity.created(location).build();

	}

	@GetMapping("/{id}/image")
	public ResponseEntity<Object> getPostImage(@PathVariable long id) throws SQLException, IOException {

		Resource postImage = postService.getPostImage(id);

		return ResponseEntity
				.ok()
				.header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
				.body(postImage);

	}

	@PutMapping("/{id}/image")
	public ResponseEntity<Object> replacePostImage(@PathVariable long id, @RequestParam MultipartFile imageFile)
			throws IOException {

		postService.replacePostImage(id, imageFile.getInputStream(), imageFile.getSize());

		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}/image")
	public ResponseEntity<Object> deletePostImage(@PathVariable long id) throws IOException {

		postService.deletePostImage(id);

		return ResponseEntity.noContent().build();
	}
}
