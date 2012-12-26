package com.idk.databoss.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * this annotation will be used to help build sql statements
 * via reflection. It can only be placed on member/field variables.
 * If it is placed on an Object, that object should be reflected.
 *
 * @author shoemaki
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataBossColumn {

    public boolean select() default true;

    public boolean insert() default true;

    public boolean update() default true;

    public boolean delete() default true;

    public boolean id() default false;
    
    public boolean required() default false;
    
    public String join() default "defaultNo";

    public String resultSetMethod() default "getString";

    public String databaseColumnName() default "default";
}
