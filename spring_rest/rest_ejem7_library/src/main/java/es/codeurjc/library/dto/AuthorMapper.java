package es.codeurjc.library.dto;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;

import es.codeurjc.library.domain.Author;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    AuthorDTO toDTO(Author author);

    List<AuthorBasicDTO> toDTOs(Collection<Author> authors);

    Author toDomain(AuthorBasicDTO authorDTO);
}