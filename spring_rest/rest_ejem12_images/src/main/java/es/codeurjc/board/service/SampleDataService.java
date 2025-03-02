package es.codeurjc.board.service;

import java.io.IOException;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import es.codeurjc.board.domain.Post;
import es.codeurjc.board.repository.PostRepository;
import jakarta.annotation.PostConstruct;

@Service
public class SampleDataService {

    @Autowired
    private PostRepository postRepository;

    @PostConstruct
    public void init() throws IOException {
        
        Post post1 = new Post("Pepe", "Vendo moto", "Barata, barata");     
        setPostImage(post1, "/sampledata_images/post1_image.jpg");
        post1.setImage("http://127.0.0.1:8080/posts/1/image");

        Post post2 = new Post("Juan", "Compro coche", "Pago bien");

        postRepository.save(post1);
        postRepository.save(post2);
        
    }

    public void setPostImage(Post post, String classpathResource) throws IOException {
		Resource image = new ClassPathResource(classpathResource);
        post.setImageFile(BlobProxy.generateProxy(image.getInputStream(), image.contentLength()));
	}
}
