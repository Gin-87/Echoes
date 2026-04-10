package echos.CharacterService.DTO;

import echos.CharacterService.Entity.Character;
import echos.CharacterService.Entity.CharacterConfig;
import echos.CharacterService.Service.StringToGender;
import echos.UserService.Service.StringToLanguage;
import echos.UserService.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CharacterMapper {




    // 实体转换为dto
    public static CharacterDTO toDTO(Character character) {
        CharacterDTO dto = new CharacterDTO();
        dto.setId(character.getId());
        dto.setName(character.getName());
        dto.setCode(character.getCode());
        dto.setDescription(character.getDescription());
        dto.setAvatar_url(character.getAvatar_url());
        dto.setStatus(character.getStatus());
        dto.setOwner(character.getOwner());
        dto.setIsFavorite(false);
        dto.setNum_of_favorites(0);
        return dto;
    }

    // DTO转换为实体
    public static Character toEntity(CharacterDTO dto) {
        Character character = new Character();
        character.setId(dto.getId());
        character.setName(dto.getName());
        character.setCode(dto.getCode());
        character.setDescription(dto.getDescription());
        character.setAvatar_url(dto.getAvatar_url());
        character.setStatus(dto.getStatus());
        character.setOwner(dto.getOwner());
        return character;
    }



    //CharacterCreateUpdateDTO转换为Character
    public static Character ComplexDtoToCharacter(CharacterCreateUpdateDTO characterCreateUpdateDTO) {

        Character character = new Character();
        character.setId(characterCreateUpdateDTO.getCharacterId());
        character.setName(characterCreateUpdateDTO.getName());
        character.setCode(characterCreateUpdateDTO.getCode());
        character.setDescription(characterCreateUpdateDTO.getDescription());
        character.setAvatar_url(characterCreateUpdateDTO.getAvatar_url());
        character.setStatus(characterCreateUpdateDTO.getStatus());
        character.setConfigId(characterCreateUpdateDTO.getConfigId());
        //转换语言为UserLanguage类
        character.setLanguage(StringToLanguage.stringToLanguage(characterCreateUpdateDTO.getLanguage()));

        //处理性别
        character.setGender(StringToGender.StringToGenderMethod(characterCreateUpdateDTO.getGender()));


        return character;
    }

    //CharacterCreateUpdateDTO转换为CharacterConfig
    public static CharacterConfig ComplexDtoToConfig(CharacterCreateUpdateDTO characterCreateUpdateDTO) {
        CharacterConfig characterConfig = new CharacterConfig();
        characterConfig.setId(characterCreateUpdateDTO.getConfigId());
        characterConfig.setBackgroundStory(characterCreateUpdateDTO.getBackgroundStory());
        characterConfig.setLanguageStyle(characterCreateUpdateDTO.getLanguageStyle());
        characterConfig.setUserAppellation(characterCreateUpdateDTO.getUserAppellation());
        characterConfig.setPersonalityTraits(characterCreateUpdateDTO.getPersonalityTraits());
        characterConfig.setFirstLetter(characterCreateUpdateDTO.getFirstLetter());
        return characterConfig;
    }


    //使用character和config拼接成一个大dto
    public static CharacterCreateUpdateDTO characterAndConfigToDto(Character character, CharacterConfig config) {
        CharacterCreateUpdateDTO characterCreateUpdateDTO = new CharacterCreateUpdateDTO();
        characterCreateUpdateDTO.setCharacterId(character.getId());
        characterCreateUpdateDTO.setName(character.getName());
        characterCreateUpdateDTO.setCode(character.getCode());
        characterCreateUpdateDTO.setDescription(character.getDescription());
        characterCreateUpdateDTO.setAvatar_url(character.getAvatar_url());
        characterCreateUpdateDTO.setStatus(character.getStatus());

        characterCreateUpdateDTO.setGender(character.getGender().toString());
        characterCreateUpdateDTO.setLanguage(character.getLanguage().toString());

        characterCreateUpdateDTO.setConfigId(config.getId());
        characterCreateUpdateDTO.setPersonalityTraits(config.getPersonalityTraits());
        characterCreateUpdateDTO.setUserAppellation(config.getUserAppellation());
        characterCreateUpdateDTO.setLanguageStyle(config.getLanguageStyle());
        characterCreateUpdateDTO.setBackgroundStory(config.getBackgroundStory());
        characterCreateUpdateDTO.setFirstLetter(config.getFirstLetter());
        return characterCreateUpdateDTO;

    }


    public static CharacterManageListDTO characterToManageListDTO(Character character) {
        CharacterManageListDTO characterManageListDTO = new CharacterManageListDTO();
        characterManageListDTO.setId(character.getId());
        characterManageListDTO.setName(character.getName());
        characterManageListDTO.setCode(character.getCode());
        characterManageListDTO.setCreationDateTime(character.getCreatedAt());
        characterManageListDTO.setStatus(character.getStatus());
        characterManageListDTO.setCreatorId(character.getOwner());
        return characterManageListDTO;
    }


    // 批量entity👉管理后台列表dto
    public static List<CharacterManageListDTO> toAdminDTOList(List<Character> characters) {
        return characters.stream()
                         .map(CharacterMapper::characterToManageListDTO)
                         .collect(Collectors.toList());

    }

    // 批量entity👉dto
    public static List<CharacterDTO> toDTOList(List<Character> characters) {
        return characters.stream().map(CharacterMapper::toDTO).collect(Collectors.toList());
    }

    //批量dto 到entity
    public static List<Character> toEntityList(List<CharacterDTO> dtos) {
        return dtos.stream().map(CharacterMapper::toEntity).collect(Collectors.toList());
    }



}
