package es.codeurjc.library.dto;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;

import es.codeurjc.library.domain.Book;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookWithAuthorsDTO toDTO(Book book);

    List<BookWithoutAuthorsDTO> toDTOs(Collection<Book> books);
    
    Book toDomain(BookWithoutAuthorsDTO bookDTO);
}