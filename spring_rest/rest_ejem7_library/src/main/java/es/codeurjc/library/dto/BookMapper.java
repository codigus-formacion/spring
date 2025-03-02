package es.codeurjc.library.dto;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;

import es.codeurjc.library.domain.Book;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookDTO toDTO(Book book);

    List<BookBasicDTO> toDTOs(Collection<Book> books);
    
    Book toDomain(BookBasicDTO bookDTO);
}