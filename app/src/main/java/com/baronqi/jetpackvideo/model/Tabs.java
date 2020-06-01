package com.baronqi.jetpackvideo.model;

import java.util.List;

public class Tabs {

    /**
     * activeColor : #333333
     * inActiveColor : #666666
     * selectTab : 0
     * tabs : [{"size":24,"enable":true,"index":0,"page":"main/tabs/home","title":"首页"},{"size":24,"enable":true,"index":1,"page":"main/tabs/sofa","title":"沙发"},{"size":40,"enable":true,"index":2,"tintColor":"#ff678f","path":"main/tabs/publish","title":""},{"size":24,"enable":true,"index":3,"path":"main/tabs/find","title":"发现"},{"size":24,"enable":true,"index":4,"path":"main/tabs/my","title":"我的"}]
     */

    private String activeColor;
    private String inActiveColor;
    private int selectTab;
    private List<TabBean> tabs;

    public String getActiveColor() {
        return activeColor;
    }

    public void setActiveColor(String activeColor) {
        this.activeColor = activeColor;
    }

    public String getInActiveColor() {
        return inActiveColor;
    }

    public void setInActiveColor(String inActiveColor) {
        this.inActiveColor = inActiveColor;
    }

    public int getSelectTab() {
        return selectTab;
    }

    public void setSelectTab(int selectTab) {
        this.selectTab = selectTab;
    }

    public List<TabBean> getTabs() {
        return tabs;
    }

    public void setTabs(List<TabBean> tabs) {
        this.tabs = tabs;
    }

    public static class TabBean {
        /**
         * size : 24
         * enable : true
         * index : 0
         * page : main/tabs/home
         * title : 首页
         * tintColor : #ff678f
         * path : main/tabs/publish
         */

        private int size;
        private boolean enable;
        private int index;
        private String title;
        private String tintColor;
        private String path;

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTintColor() {
            return tintColor;
        }

        public void setTintColor(String tintColor) {
            this.tintColor = tintColor;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }
}
