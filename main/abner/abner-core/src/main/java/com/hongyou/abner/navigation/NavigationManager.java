/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.navigation;

import com.hongyou.baron.cache.CacheUtil;
import com.hongyou.baron.cache.FIFOCache;
import com.hongyou.baron.util.ObjectUtil;
import com.hongyou.baron.util.ResourceUtil;
import com.hongyou.baron.util.StringUtil;
import com.hongyou.baron.util.XmlUtil;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 导航菜单管理器
 *
 * @author Hong Bo Lin
 */
@Service
public class NavigationManager {

    /**
     * 缓存
     */
    private final FIFOCache<String, NavigationFamily> menuCaches = CacheUtil.newFIFOCache(5);

    /**
     * 加载菜单
     *
     * @param familyName 导航祖
     */
    public List<MenuOption> load(final String familyName) {

        // 从缓存中读取
        if (this.menuCaches.containsKey(familyName)) {
            return this.menuCaches.get(familyName).getMenus();
        }

        // 加载XML文件
        InputStream stream = ResourceUtil.getStream("static/menus.xml");
        Document document = XmlUtil.readXML(stream);

        // 解析根节点
        Element root = XmlUtil.getRootElement(document);
        Element family = null;

        // 解析当前平台组
        List<Element> familyNodes = XmlUtil.getChildElements(root, "family");
        for (Element familyNode : familyNodes) {
            String attribute = XmlUtil.getAttribute(familyNode, "name");
            if (familyName.equals(attribute)) {
                family = familyNode;
                break;
            }
        }
        if (ObjectUtil.isNull(family)) {
            return null;
        }
        String name = XmlUtil.getAttribute(family, "name");
        String label = XmlUtil.getAttribute(family, "label");

        // 解析菜单
        List<Element> menus = XmlUtil.getChildElements(family, "menu");
        List<MenuOption> menuOptions = this.loadMenu(menus, "", "");

        NavigationFamily build = NavigationFamily.builder().
                value(name).
                label(label).
                menus(menuOptions).
                build();
        menuCaches.put(familyName, build);
        return menuOptions;
    }

    /**
     * 从xml中解析菜单
     */
    private List<MenuOption> loadMenu(final List<Element> menus, final String parentIcon, final String parentUrl) {
        List<MenuOption> menuOptions = new ArrayList<>();
        for (Element menu: menus) {
            MenuOption.MenuOptionBuilder menuOption = MenuOption.builder();

            // 解析菜单属性
            String id = XmlUtil.getAttribute(menu, "id");
            String url = XmlUtil.getAttribute(menu, "url");
            String label = XmlUtil.getAttribute(menu, "label");
            String icon = XmlUtil.getAttribute(menu, "icon");

            // 递归检查是否有子菜单
            if (menu.hasChildNodes()) {
                String parentPaths = parentUrl + url;
                String parentIcons = StringUtil.isBlank(icon) ? parentIcon : icon;
                List<Element> childMenus = XmlUtil.getChildElements(menu, "menu");
                menuOption.children(this.loadMenu(childMenus, parentIcons, parentPaths));
            }

            boolean fixed = XmlUtil.getAttributeAsBool(menu, "fixed", false);
            menuOption.id(id).
                    url(url).
                    fullUrl(parentUrl + url).
                    label(label).
                    icons(icon).
                    parentIcon(parentIcon).
                    fixed(fixed);
            menuOptions.add(menuOption.build());
        }
        return menuOptions;
    }

    /**
     * 获取所有导航族
     */
    public List<NavigationFamily> getFamilies() {
        List<NavigationFamily> families = new ArrayList<>();
        this.menuCaches.forEach(families::add);
        return families;
    }
}
