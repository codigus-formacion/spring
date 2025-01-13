package es.codeurjc.db.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.codeurjc.db.model.Post;
import es.codeurjc.db.repository.PostRepository;

@Service
public class PostService {
	
	@Autowired
	private PostRepository posts;

	public void save(Post post) {
		posts.save(post);		
	}

	public List<Post> findAll() {
		return posts.findAll();
	}

	public Optional<Post> findById(long id) {
		return posts.findById(id);
	}

	public void deleteById(long id) {
		
		posts.deleteById(id);		
	}
}
