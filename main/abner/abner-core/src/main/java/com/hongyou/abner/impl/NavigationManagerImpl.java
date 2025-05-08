/*
 * Copyright 2024, Hongyou Software Development Studio.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hongyou.abner.impl;

import com.hongyou.baron.Application;
import com.hongyou.baron.cache.CacheUtil;
import com.hongyou.baron.cache.FIFOCache;
import com.hongyou.baron.util.ResourceUtil;
import com.hongyou.baron.util.StringUtil;
import com.hongyou.baron.util.XmlUtil;
import com.hongyou.baron.web.navigation.Family;
import com.hongyou.baron.web.navigation.Navigate;
import com.hongyou.baron.web.navigation.NavigationManager;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 导航菜单
 *
 * @author Berlin
 */
@Service
public class NavigationManagerImpl implements NavigationManager {

    /**
     * 缓存菜单(key: 导航族ID, value: 导航族菜单)
     */
    private final FIFOCache<String, Family> menuCaches = CacheUtil.newFIFOCache(5);

    /**
     * 项目配置参数
     */
    private final Application application;

    /**
     * 注入依赖
     */
    public NavigationManagerImpl(final Application application) {
        this.application = application;
    }

    /**
     * 加载菜单
     *
     * @param family 导航祖
     */
    public List<Navigate> load(final String family) {
        // 从缓存中读取
        if (!application.isDebug() && this.menuCaches.containsKey(family)) {
            return this.menuCaches.get(family).getNavigates();
        }

        // 加载XML文件
        InputStream stream = ResourceUtil.getStream("static/menus.xml");
        Document document = XmlUtil.readXML(stream);
        Element root = XmlUtil.getRootElement(document);

        // 解析导航族
        List<Element> familyNodes = XmlUtil.getChildElements(root, "family");
        for (Element familyNode : familyNodes) {
            String familyName = XmlUtil.getAttribute(familyNode, "name");
            String familyLabel = XmlUtil.getAttribute(familyNode, "label");

            // 解析菜单
            List<Element> menus = XmlUtil.getChildElements(familyNode, "menu");
            List<Navigate> menuOptions = this.loadMenu(menus, "", "");

            Family build = Family.builder().
                    familyName(familyName).
                    familyLabel(familyLabel).
                    navigates(menuOptions).
                    build();
            menuCaches.put(familyName, build);
        }
        if (!menuCaches.containsKey(family)) {
            return null;
        }
        return menuCaches.get(family).getNavigates();
    }

    /**
     * 从xml中解析菜单
     */
    private List<Navigate> loadMenu(final List<Element> menus, final String parentIcon, final String parentUrl) {
        List<Navigate> menuOptions = new ArrayList<>();
        for (Element menu: menus) {
            Navigate.NavigateBuilder menuOption = Navigate.builder();

            // 解析菜单属性
            String id = XmlUtil.getAttribute(menu, "id");
            String url = XmlUtil.getAttribute(menu, "url");
            String label = XmlUtil.getAttribute(menu, "label");
            String icon = XmlUtil.getAttribute(menu, "icon");
            String permission = XmlUtil.getAttribute(menu, "permission");
            String action = XmlUtil.getAttribute(menu, "action");

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
                    fixed(fixed).
                    permission(permission).
                    action(action);
            menuOptions.add(menuOption.build());
        }
        return menuOptions;
    }

    /**
     * 获取所有导航族
     */
    @Override
    public List<Family> getFamilies() {
        if (this.menuCaches.size() <= 0) {
            this.load("main");
        }
        List<Family> families = new ArrayList<>();
        this.menuCaches.forEach(families::add);
        return families;
    }
}
