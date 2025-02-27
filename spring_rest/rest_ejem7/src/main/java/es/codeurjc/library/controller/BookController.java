package es.codeurjc.library.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.codeurjc.library.dto.BookWithoutAuthorsDTO;
import es.codeurjc.library.dto.BookMapper;
import es.codeurjc.library.dto.BookWithAuthorsDTO;
import es.codeurjc.library.repository.BookRepository;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookMapper mapper;

    @GetMapping("/")
    public List<BookWithoutAuthorsDTO> getAllBooks() {
        return mapper.toDTOs(bookRepository.findAll());
    }

    @GetMapping("/{id}")
    public BookWithAuthorsDTO getBookById(@PathVariable Long id) {
        return mapper.toDTO(bookRepository.findById(id).orElseThrow());
    }
}