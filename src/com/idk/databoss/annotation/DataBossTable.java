package com.idk.databoss.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * this annotation will be used to help build sql statements
 * via reflection. It can only be placed on classes.
 * The annotation determines the databaseTableName and possibly other
 * options in the future.
 *
 * @author shoemaki
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataBossTable {

    /**
     * Default is used by the generic builders to use the class's simple name as
     * the table name
     *
     * @return the database table name
     */
    public String databaseTableName() default "default";

    /**
     * Shorthand of table name for queries.
     *
     * @return Shorthand String
     */
    public String tableShortHand();
}
