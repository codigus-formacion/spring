package es.codeurjc.library.dto;

import java.util.List;

public record AuthorWithBooksDTO(Long id, String name, String nationality, List<BookWithoutAuthorsDTO> books) {
}