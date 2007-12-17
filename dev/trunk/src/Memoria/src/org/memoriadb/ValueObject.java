package org.memoriadb;

import java.lang.annotation.*;

/**
 * 
 * Objects of annotated classes are saved inline. They get no ObjectId what saves some space.
 * ValueObjects are not found by queries. 
 * 
 * @author msc
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ValueObject {

}
