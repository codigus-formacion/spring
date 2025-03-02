package es.codeurjc.board.service;

import java.util.Collection;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
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

	public PostDTO replacePost(PostDTO updatedPostDTO) {

		if (postRepository.existsById(updatedPostDTO.id())) {

			Post updatedPost = toDomain(updatedPostDTO);

			postRepository.save(updatedPost);

			return toDTO(updatedPost);

		} else {
			throw new NoSuchElementException();
		}
	}

	public PostDTO deletePost(long id) {

		Post post = postRepository.findById(id).orElseThrow();

		postRepository.deleteById(id);

		return toDTO(post);
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
