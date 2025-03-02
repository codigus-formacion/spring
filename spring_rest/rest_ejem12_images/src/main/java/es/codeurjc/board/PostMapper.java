package es.codeurjc.board;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {

    PostDTO toDTO(Post post);

    List<PostDTO> toDTOs(Collection<Post> posts);

    @Mapping(target = "imageFile", ignore = true)
    Post toDomain(PostDTO postDTO);
}
