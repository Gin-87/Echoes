package echos.SystemService.Entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 菜单实体类
 */
@Data
@Entity
@Table(name = "menu")
public class Menu {

    /** 主键ID，唯一 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /** 名称 */
    @Column(name = "name", length = 100, nullable = false, unique = true)
    private String name;

    /** 订单 */
    @Column(name = "order", nullable = false, unique = true)
    private int order;
}
