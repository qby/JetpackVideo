package com.baronqi.jetpackvideo.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baronqi.jetpackvideo.model.Destination;
import com.baronqi.jetpackvideo.model.Tabs;

import java.util.HashMap;

public class AppConfig {

    private static HashMap<String, Destination> sDestination;

    public static HashMap<String, Destination> getDestConfig() {
        if (sDestination == null) {
            String s = FileUtil.parseAssetsFile("destination.json");

            sDestination = JSON.parseObject(s, new TypeReference<HashMap<String, Destination>>() {
            });
        }

        return sDestination;
    }

    private static Tabs sTab;

    public static Tabs getTabs() {
        if (sTab == null) {
            String s = FileUtil.parseAssetsFile("main_tabs_config.json");
            sTab = JSON.parseObject(s, Tabs.class);
        }

        return sTab;
    }

}
