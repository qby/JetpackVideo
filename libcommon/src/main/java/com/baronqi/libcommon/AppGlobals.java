package com.baronqi.libcommon;

import android.app.Application;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AppGlobals {

    private static Application sApplication;
    public static Application getApplication() {
        if (sApplication == null) {

            try {
                Class aClass = Class.forName("android.app.ActivityThread");
                Method currentApplication = aClass.getDeclaredMethod("currentApplication");
                sApplication = (Application) currentApplication.invoke(null, null);
            } catch (IllegalAccessException
                    | InvocationTargetException
                    | NoSuchMethodException
                    | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return sApplication;
    }
}
