package com.eltechs.axs.proto.input.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IntSign {
	int[] signedIndexes() default {};
	int[] unsignedIndexes() default {};
}
