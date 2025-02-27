package es.codeurjc.board;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.Collection;
import java.util.NoSuchElementException;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
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

import jakarta.annotation.PostConstruct;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/posts")
public class PostController {

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private PostMapper mapper;

	@PostConstruct
	public void init() {
		postRepository.save(new Post("Pepe", "Vendo moto", "Barata, barata"));
		postRepository.save(new Post("Juan", "Compro coche", "Pago bien"));
	}

	@GetMapping("/")
	public Collection<PostDTO> getPosts() {
	
		return toDTOs(postRepository.findAll());
	}

	@GetMapping("/{id}")
	public PostDTO getPost(@PathVariable long id) {

		return toDTO(postRepository.findById(id).orElseThrow());
	}

	@PostMapping("/")
	public ResponseEntity<PostDTO> createPost(@RequestBody PostDTO postDTO) {

		Post post = toDomain(postDTO);

		postRepository.save(post);

		URI location = fromCurrentRequest().path("/{id}").buildAndExpand(post.getId()).toUri();

		return ResponseEntity.created(location).body(toDTO(post));
	}

	@PutMapping("/{id}")
	public PostDTO replacePost(@PathVariable long id, @RequestBody PostDTO newPostDTO) {

		if (postRepository.existsById(id)) {

			Post newPost = toDomain(newPostDTO);

			newPost.setId(id);
			postRepository.save(newPost);

			return toDTO(newPost);

		} else {
			throw new NoSuchElementException();
		}
	}

	@DeleteMapping("/{id}")
	public PostDTO deletePost(@PathVariable long id) {

		Post post = postRepository.findById(id).orElseThrow();

		postRepository.deleteById(id);

		return toDTO(post);
	}

	@PostMapping("/{id}/image")
	public ResponseEntity<Object> createPostImage(@PathVariable long id, @RequestParam MultipartFile imageFile)
			throws IOException {

		Post post = postRepository.findById(id).orElseThrow();

		URI location = fromCurrentRequest().build().toUri();

		post.setImage(location.toString());
		post.setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
		postRepository.save(post);

		return ResponseEntity.created(location).build();
	}

	@GetMapping("/{id}/image")
	public ResponseEntity<Object> getPostImage(@PathVariable long id) throws SQLException {

		Post post = postRepository.findById(id).orElseThrow();

		if (post.getImageFile() != null) {

			Resource file = new InputStreamResource(post.getImageFile().getBinaryStream());

			return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
					.contentLength(post.getImageFile().length()).body(file);

		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PutMapping("/{id}/image")
	public ResponseEntity<Object> replacePostImage(@PathVariable long id, @RequestParam MultipartFile imageFile)
			throws IOException {

		Post post = postRepository.findById(id).orElseThrow();

		URI location = fromCurrentRequest().build().toUri();

		post.setImage(location.toString());
		post.setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
		postRepository.save(post);

		return ResponseEntity.created(location).build();
	}

	@DeleteMapping("/{id}/image")
	public ResponseEntity<Object> deletePostImage(@PathVariable long id) throws IOException {

		Post post = postRepository.findById(id).orElseThrow();

		post.setImageFile(null);
		post.setImage(null);
		
		postRepository.save(post);

		return ResponseEntity.noContent().build();
	}

	private PostDTO toDTO(Post post){
		return mapper.toDTO(post);
	}

	private Post toDomain(PostDTO postDTO){
		return mapper.toDomain(postDTO);
	}

	private Collection<PostDTO> toDTOs(Collection<Post> posts){
		return mapper.toDTOs(posts);
	}
}
