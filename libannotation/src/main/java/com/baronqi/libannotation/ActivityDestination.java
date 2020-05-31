package com.baronqi.libannotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface ActivityDestination {

    String path();

    boolean login() default false;

    boolean isDefault() default false;

}
