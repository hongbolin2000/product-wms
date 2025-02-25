/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
package com.hongyou.abner.menu;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import com.hongyou.baron.logging.Log;
import com.hongyou.baron.logging.LogFactory;
import com.hongyou.baron.util.XmlUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜单
 *
 * @author Berlin
 */
@RestController
@RequestMapping("/menu")
public class MenuPage {

    /**
     * logger
     */
    private static final Log logger = LogFactory.getLog(MenuPage.class);

    /**
     * 缓存
     */
    private final Cache<String, List<MenuOption>> menuCaches = CacheUtil.newFIFOCache(1);

    /**
     * 缓存的key
     */
    private static final String MENU_KEY = "menu";

    /**
     * 加载菜单
     */
    @GetMapping("/load")
    public List<MenuOption> load() {

        try {
            // 从缓存中读取
            if (menuCaches.containsKey(MENU_KEY)) {
                return menuCaches.get(MENU_KEY);
            }

            // 加载XML文件
            URL resource = ResourceUtil.getResource("static/menus.xml");
            Document document = XmlUtil.readXML(resource.getPath());

            // 解析根节点
            Element root = XmlUtil.getRootElement(document);
            List<Element> menus = XmlUtil.getChildElements(root, MENU_KEY);

            // 解析菜单
            List<MenuOption> menuOptions = this.loadMenu(menus, "");
            menuCaches.put(MENU_KEY, menuOptions);
            return menuOptions;
        } catch (Exception e) {
            logger.error("菜单加载失败", e);
            return new ArrayList<>();
        }
    }

    /**
     * 解析菜单
     */
    private List<MenuOption> loadMenu(final List<Element> menus, final String parentIcon) {
        List<MenuOption> menuOptions = new ArrayList<>();
        for (Element menu: menus) {
            MenuOption.MenuOptionBuilder menuOption = MenuOption.builder();

            // 解析菜单属性
            String path = XmlUtil.getAttribute(menu, "path");
            String label = XmlUtil.getAttribute(menu, "label");
            String icon = XmlUtil.getAttribute(menu, "icon");

            // 递归检查是否有子菜单
            if (menu.hasChildNodes()) {
                String parentIcons = StrUtil.isBlank(icon) ? parentIcon : icon;
                List<Element> childMenus = XmlUtil.getChildElements(menu, MENU_KEY);
                menuOption.children(this.loadMenu(childMenus, parentIcons));
            }

            boolean fixed = XmlUtil.getBooleanAttribute(menu, "fixed");
            menuOption.key(path).
                    label(label).
                    icons(icon).
                    parentIcon(parentIcon).
                    fixed(fixed);
            menuOptions.add(menuOption.build());
        }
        return menuOptions;
    }
}
