package org.memoriadb;

import java.lang.annotation.*;

/**
 * Use this annotation to mark a reference to an object as weak, meaning that saveAll and deleteAll stop
 * cascading at that point. The annotated ref itself is saved.  
 *  
 * @author msc
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface WeakRef {
  
}
