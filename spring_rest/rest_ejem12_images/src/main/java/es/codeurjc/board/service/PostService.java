package es.codeurjc.board.service;

import java.io.InputStream;
import java.net.URI;
import java.sql.SQLException;
import java.util.Collection;
import java.util.NoSuchElementException;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import es.codeurjc.board.domain.Post;
import es.codeurjc.board.dto.PostDTO;
import es.codeurjc.board.dto.PostMapper;
import es.codeurjc.board.repository.PostRepository;

@Service
public class PostService {

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private PostMapper mapper;

	public Collection<PostDTO> getPosts() {

		return toDTOs(postRepository.findAll());
	}

	public PostDTO getPost(long id) {

		return toDTO(postRepository.findById(id).orElseThrow());
	}

	public PostDTO createPost(PostDTO postDTO) {

		Post post = toDomain(postDTO);

		postRepository.save(post);

		return toDTO(post);
	}

	public PostDTO replacePost(long id, PostDTO updatedPostDTO) throws SQLException {

		Post oldPost = postRepository.findById(id).orElseThrow();
		Post updatedPost = toDomain(updatedPostDTO);
		updatedPost.setId(id);

		if (oldPost.getImage() != null) {

			//Set the image in the updated post
			updatedPost.setImageFile(BlobProxy.generateProxy(oldPost.getImageFile().getBinaryStream(),
					oldPost.getImageFile().length()));
			updatedPost.setImage(oldPost.getImage());
		}

		postRepository.save(updatedPost);

		return toDTO(updatedPost);
	}

	public PostDTO deletePost(long id) {

		Post post = postRepository.findById(id).orElseThrow();

		postRepository.deleteById(id);

		return toDTO(post);
	}

	public void createPostImage(long id, URI location, InputStream inputStream, long size) {

		Post post = postRepository.findById(id).orElseThrow();

		post.setImage(location.toString());
		post.setImageFile(BlobProxy.generateProxy(inputStream, size));
		postRepository.save(post);
	}

	public Resource getPostImage(long id) throws SQLException {

		Post post = postRepository.findById(id).orElseThrow();

		if (post.getImageFile() != null) {
			return new InputStreamResource(post.getImageFile().getBinaryStream());
		} else {
			throw new NoSuchElementException();
		}
	}

	public void replacePostImage(long id, InputStream inputStream, long size) {

		Post post = postRepository.findById(id).orElseThrow();

		post.setImageFile(BlobProxy.generateProxy(inputStream, size));

		postRepository.save(post);
	}

	public void deletePostImage(long id) {

		Post post = postRepository.findById(id).orElseThrow();

		if(post.getImage() == null){
			throw new NoSuchElementException();
		}

		post.setImageFile(null);
		post.setImage(null);

		postRepository.save(post);
	}

	private PostDTO toDTO(Post post) {
		return mapper.toDTO(post);
	}

	private Post toDomain(PostDTO postDTO) {
		return mapper.toDomain(postDTO);
	}

	private Collection<PostDTO> toDTOs(Collection<Post> posts) {
		return mapper.toDTOs(posts);
	}
}
