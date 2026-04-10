package echos.AuthorizationService.DTO;


import jakarta.persistence.Column;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
public class MenuDto {
    private long id;
    private String menuName;
    private Long parentId;
    private Integer orderNum;
    private String icon;
    private String component;
    private String resource;



    // 构造方法
    public MenuDto(long id, String menuName) {
        this.id = id;
        this.menuName = menuName;
    }
}
