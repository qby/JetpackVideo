package com.baronqi.jetpackvideo;

import android.content.ComponentName;

import androidx.fragment.app.FragmentActivity;
import androidx.navigation.ActivityNavigator;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavGraphNavigator;
import androidx.navigation.NavigatorProvider;
import androidx.navigation.fragment.FragmentNavigator;

import com.baronqi.jetpackvideo.model.Destination;
import com.baronqi.jetpackvideo.navigator.FixFragmentNavigator;
import com.baronqi.jetpackvideo.util.AppConfig;
import com.baronqi.libcommon.AppGlobals;

import java.util.HashMap;

public class NavGraphBuilder {

    public static void build(NavController controller, FragmentActivity activity, int containerId) {

        NavigatorProvider navigatorProvider = controller.getNavigatorProvider();
//        FragmentNavigator fragmentNavigator = navigatorProvider.getNavigator(FragmentNavigator.class);

        FixFragmentNavigator fixFragmentNavigator = new FixFragmentNavigator(activity, activity.getSupportFragmentManager(), containerId);

        navigatorProvider.addNavigator(fixFragmentNavigator);

        ActivityNavigator activityNavigator = navigatorProvider.getNavigator(ActivityNavigator.class);

        HashMap<String, Destination> destConfig = AppConfig.getDestConfig();

        NavGraph navGraph = new NavGraph(new NavGraphNavigator(navigatorProvider));

        for (Destination value : destConfig.values()) {

            if (value.isActivity()) {
                ActivityNavigator.Destination destination = activityNavigator.createDestination();
                destination.setId(value.getId());
                destination.addDeepLink(value.getPath());
                destination.setComponentName(new ComponentName(AppGlobals.getApplication().getPackageName(), value.getClazzName()));
                navGraph.addDestination(destination);
            } else {
                FragmentNavigator.Destination destination = fixFragmentNavigator.createDestination();
                destination.setClassName(value.getClazzName());
                destination.setId(value.getId());
                destination.addDeepLink(value.getPath());
                navGraph.addDestination(destination);
            }

            if (value.isDefault()) {
                navGraph.setStartDestination(value.getId());
            }
        }

        controller.setGraph(navGraph);
    }
}
