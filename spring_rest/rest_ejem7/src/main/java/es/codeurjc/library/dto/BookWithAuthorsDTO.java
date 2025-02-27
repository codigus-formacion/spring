package es.codeurjc.library.dto;

import java.util.List;

public record BookWithAuthorsDTO(Long id, String title, int price, List<AuthorWithoutBooksDTO> authors) {
}