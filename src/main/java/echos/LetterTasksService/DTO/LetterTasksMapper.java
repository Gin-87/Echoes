package echos.LetterTasksService.DTO;

import echos.LetterTasksService.Entity.LetterTasks;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class LetterTasksMapper {

    public static LetterTasksDTO toDTO(LetterTasks letterTasks) {
        LetterTasksDTO dto = new LetterTasksDTO();
        dto.setId(letterTasks.getId());
        dto.setCharacterId(letterTasks.getCharacterId());
        dto.setUserId(letterTasks.getUserId());
        dto.setStatus(letterTasks.getStatus());
        return dto;
    }

    public static LetterTasks toEntity(LetterTasksDTO dto) {
        LetterTasks letterTasks = new LetterTasks();
        letterTasks.setId(dto.getId());
        letterTasks.setCharacterId(dto.getCharacterId());
        letterTasks.setUserId(dto.getUserId());
        letterTasks.setStatus(dto.getStatus());
        return letterTasks;
    }

    // 批量转换DTO
    public static List<LetterTasksDTO> toDTO(List<LetterTasks> letterTasks) {
        return letterTasks.stream().map(LetterTasksMapper::toDTO).collect(Collectors.toList());
    }
}
