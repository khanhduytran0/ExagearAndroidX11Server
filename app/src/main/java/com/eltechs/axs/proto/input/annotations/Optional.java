package com.eltechs.axs.proto.input.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Optional {
    String bit();
    String mask() default "mask";
}
*/

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Optional {
	int[] indexes();
    String[] bits();
    // String[] masks();
}

