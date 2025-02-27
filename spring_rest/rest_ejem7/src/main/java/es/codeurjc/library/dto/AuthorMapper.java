package es.codeurjc.library.dto;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;

import es.codeurjc.library.domain.Author;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    AuthorWithBooksDTO toDTO(Author author);

    List<AuthorWithoutBooksDTO> toDTOs(Collection<Author> authors);

    Author toDomain(AuthorWithoutBooksDTO authorDTO);
}