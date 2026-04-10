package echos.CharacterService.Service;

import echos.CharacterService.Entity.CharacterConfig;
import echos.CharacterService.Repository.CharacterConfigRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CharacterConfigService {

    @Autowired
    private CharacterConfigRepository characterConfigRepository;

    @Transactional
    public CharacterConfig createCharacterConfig(CharacterConfig characterConfig) {
        return characterConfigRepository.save(characterConfig);
    }

    public CharacterConfig getCharacterConfigById(Long id) {
        return characterConfigRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CharacterConfig with id " + id + " not found"));
    }

    @Transactional
    public CharacterConfig updateCharacterConfig(Long id, CharacterConfig characterConfig) {
        if (!characterConfigRepository.existsById(id)) {
            throw new EntityNotFoundException("CharacterConfig with id " + id + " not found");
        }
        characterConfig.setId(id);
        return characterConfigRepository.save(characterConfig);  // 修正：原代码 return 放在 throw 之后，永远不可达
    }

    @Transactional
    public void deleteCharacterConfigById(Long id) {
        if (!characterConfigRepository.existsById(id)) {
            throw new EntityNotFoundException("CharacterConfig with id " + id + " not found");
        }
        characterConfigRepository.deleteById(id);
    }
}
