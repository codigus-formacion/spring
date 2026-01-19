package es.codeurjc.board.dto;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;

import es.codeurjc.board.domain.Image;

@Mapper(componentModel = "spring")
public interface ImageMapper {

	ImageDTO toDTO(Image image);

	List<ImageDTO> toDTOs(Collection<Image> images);
}
