package com.github.tangyi.user.service;

import com.github.tangyi.common.core.service.CrudService;
import com.github.tangyi.common.core.utils.IdGen;
import com.github.tangyi.user.api.module.RoleMenu;
import com.github.tangyi.user.mapper.RoleMenuMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tangyi
 * @date 2018/8/26 22:47
 */
@Service
public class RoleMenuService extends CrudService<RoleMenuMapper, RoleMenu> {

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    /**
     * @param role  role
     * @param menus 菜单ID集合
     * @return int
     * @author tangyi
     * @date 2018/10/28 0028 下午 2:29
     */
    @Transactional
    @CacheEvict(value = "menu", allEntries = true)
    public int saveRoleMenus(String role, List<String> menus) {
        int update = -1;
        if (CollectionUtils.isNotEmpty(menus)) {
            // 删除旧的管理数据
            roleMenuMapper.deleteByRoleId(role);
            List<RoleMenu> roleMenus = new ArrayList<>();
            for (String menuId : menus) {
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setId(IdGen.uuid());
                roleMenu.setRoleId(role);
                roleMenu.setMenuId(menuId);
                roleMenus.add(roleMenu);
            }
            update = roleMenuMapper.insertBatch(roleMenus);
        }
        return update;
    }

    /**
     * 批量保存
     *
     * @param roleMenus roleMenus
     * @return int
     * @author tangyi
     * @date 2018/10/30 0030 下午 7:59
     */
    @Transactional
    public int insertBatch(List<RoleMenu> roleMenus) {
        return roleMenuMapper.insertBatch(roleMenus);
    }
}
