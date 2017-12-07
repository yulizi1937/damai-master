package com.yztc.core.literouter.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Intent extras key annotation
 *
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 16/10/21 13:44]
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Key {
    String value();
}
