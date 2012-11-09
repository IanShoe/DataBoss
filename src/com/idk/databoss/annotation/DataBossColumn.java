package com.idk.databoss.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * this annotation will be used to help build sql statements
 * and mappers via reflection. It can only be placed on member/field
 * variables - if it is placed on an Object - that object will be reflected.
 *
 * @author nibertj shoemaki
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataBossColumn {

    public boolean select() default true;

    public boolean insert() default true;

    public boolean update() default true;

    public boolean delete() default true;

    public boolean mapper() default true;

    public String resultSetMethod() default "getString";

    public String databaseColumnName() default "default";
}
