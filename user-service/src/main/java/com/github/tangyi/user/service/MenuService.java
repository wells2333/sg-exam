package com.github.tangyi.user.service;

import com.github.tangyi.common.core.constant.CommonConstant;
import com.github.tangyi.common.core.service.CrudService;
import com.github.tangyi.common.core.utils.SysUtil;
import com.github.tangyi.common.security.utils.SecurityUtil;
import com.github.tangyi.user.api.module.Menu;
import com.github.tangyi.user.mapper.MenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 菜单service
 *
 * @author tangyi
 * @date 2018/8/26 22:45
 */
@Service
public class MenuService extends CrudService<MenuMapper, Menu> {

    @Autowired
    private MenuMapper menuMapper;

    /**
     * 根据角色查找菜单
     *
     * @param role 角色
     * @return List
     * @author tangyi
     * @date 2018/8/27 16:00
     */
    @Cacheable(value = "menu", key = "#role  + '_menu'")
    public List<Menu> findMenuByRole(String role) {
        return menuMapper.findByRole(role);
    }

    /**
     * 新增菜单
     *
     * @param menu menu
     * @return int
     * @author tangyi
     * @date 2018/10/28 0028 下午 3:56
     */
    @Transactional
    @Override
    public int insert(Menu menu) {
        // 初始化权限
        /*if (MenuConstant.MENU_TYPE_MENU.equals(menu.getType())) {
            List<Menu> menus = MenuUtil.initMenuPermission(menu);
            menus.forEach(super::insert);
        }*/
        return super.insert(menu);
    }

    /**
     * 更新菜单
     *
     * @param menu menu
     * @return int
     * @author tangyi
     * @date 2018/10/30 0030 下午 8:19
     */
    @Transactional
    @Override
    @CacheEvict(value = {"menu", "user"}, allEntries = true)
    public int update(Menu menu) {
        return super.update(menu);
    }

    /**
     * 删除菜单
     *
     * @param menu menu
     * @return int
     * @author tangyi
     * @date 2018/8/27 16:22
     */
    @Override
    @Transactional
    @CacheEvict(value = "menu", allEntries = true)
    public int delete(Menu menu) {
        // 删除当前菜单
        super.delete(menu);
        // 删除父节点为当前节点的菜单
        Menu parentMenu = new Menu();
        parentMenu.setParentId(menu.getId());
        parentMenu.setNewRecord(false);
        parentMenu.setCommonValue(SecurityUtil.getCurrentUsername(), SysUtil.getSysCode());
        parentMenu.setDelFlag(CommonConstant.DEL_FLAG_DEL);
        return super.update(parentMenu);
    }
}
