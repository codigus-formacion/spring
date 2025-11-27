package es.codeurjc.board.service;

import java.io.IOException;
import java.sql.SQLException;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

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
    public void init() throws Exception {
        
        Post post1 = new Post("Pepe", "Vendo moto", "Barata, barata");     
        Post post2 = new Post("Juan", "Compro coche", "Pago bien");

        postRepository.save(post1);
        postRepository.save(post2);

        setPostImage(post1, "/sampledata_images/moto.jpg");
    }

    public void setPostImage(Post post, String classpathResource) throws IOException, SerialException, SQLException {
		Resource image = new ClassPathResource(classpathResource);
        post.setImageFile(new SerialBlob(image.getInputStream().readAllBytes()));
        post.setImage("http://127.0.0.1:8080/posts/"+post.getId()+"/image");
        postRepository.save(post);
	}
}
