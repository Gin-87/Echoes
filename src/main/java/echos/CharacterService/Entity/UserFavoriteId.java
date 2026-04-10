package echos.CharacterService.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
public class UserFavoriteId implements Serializable {

    private Long userId;
    private Long characterId;


    public UserFavoriteId(Long userId, Long characterId) {
        this.userId = userId;
        this.characterId = characterId;
    }

    // 重写 equals 和 hashCode 方法
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserFavoriteId that = (UserFavoriteId) o;
        return Objects.equals(userId, that.userId) &&
               Objects.equals(characterId, that.characterId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, characterId);
    }
}
