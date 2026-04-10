package echos.SystemService.Service;

import echos.SystemService.Entity.Menu;
import echos.SystemService.Repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;

    public MenuService(MenuRepository menuRepository) {this.menuRepository = menuRepository;}

    // 通过ID查找菜单
    public Optional<Menu> getMenuById(Long id) {return menuRepository.findById(id);}

    // 创建菜单
    public Menu createMenu(Menu menu) {return menuRepository.save(menu);}

    // 更新菜单
    public Menu updateMenu(Menu menu) {return menuRepository.save(menu);}

    // 删除菜单
    public void deleteMenu(Long id) {menuRepository.deleteById(id);}

}
