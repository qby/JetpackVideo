package com.baronqi.jetpackvideo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.baronqi.jetpackvideo.model.Destination;
import com.baronqi.jetpackvideo.model.Tabs;
import com.baronqi.jetpackvideo.util.AppConfig;
import com.baronqi.jetpackvideo.util.DimenUtil;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;

public class AppBottomBar extends BottomNavigationView {

    private static int[] sIcons = new int[] {
            R.mipmap.icon_tab_home,
            R.mipmap.icon_tab_sofa,
            R.mipmap.icon_tab_publish,
            R.mipmap.icon_tab_find,
            R.mipmap.icon_tab_mine
    };

    public AppBottomBar(@NonNull Context context) {
        this(context, null);
    }

    public AppBottomBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("RestrictedApi")
    public AppBottomBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        Tabs tabs = AppConfig.getTabs();

        int[][] states = new int[2][];
        states[0] = new int[android.R.attr.state_selected];
        states[1] = new int[]{};
        int[] colors = new int[]{Color.parseColor(tabs.getActiveColor()), Color.parseColor(tabs.getInActiveColor())};
        ColorStateList stateList = new ColorStateList(states, colors);
        setItemTextColor(stateList);
        setItemIconTintList(stateList);
        setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);

//        setSelectedItemId(tabs.getSelectTab());

        for (Tabs.TabBean tab : tabs.getTabs()) {
            if (!tab.isEnable()) continue;
            if (getId(tab.getPath()) < 0) continue;
            MenuItem item = getMenu().add(0, getId(tab.getPath()), tab.getIndex(), tab.getTitle());
            item.setIcon(sIcons[tab.getIndex()]);
        }

        int index = 0;
        for (Tabs.TabBean tab : tabs.getTabs()) {
            if (!tab.isEnable()) continue;
            int id;
            if ((id = getId(tab.getPath())) < 0) continue;

            int iconSize = DimenUtil.dp2px(tab.getSize());

            BottomNavigationMenuView menuView = (BottomNavigationMenuView) getChildAt(0);
            BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(index);
            itemView.setIconSize(iconSize);

            if (TextUtils.isEmpty(tab.getTitle())) {
                int tintColor = TextUtils.isEmpty(tab.getTintColor()) ? Color.parseColor("#ff678f") : Color.parseColor(tab.getTintColor());
                itemView.setIconTintList(ColorStateList.valueOf(tintColor));
                //禁止掉点按时 上下浮动的效果
                itemView.setShifting(false);

            }
            index++;
        }

        //底部导航栏默认选中项
        if (tabs.getSelectTab() != 0) {
            Tabs.TabBean selectTab = tabs.getTabs().get(tabs.getSelectTab());
            if (selectTab.isEnable()) {
                int itemId = getId(selectTab.getPath());
                post(() -> setSelectedItemId(itemId));
            }
        }
    }

    private int getId(String path) {
        Destination destination = AppConfig.getDestConfig().get(path);
        if (destination == null) {
            return -1;
        }
        return destination.getId();
    }


}
