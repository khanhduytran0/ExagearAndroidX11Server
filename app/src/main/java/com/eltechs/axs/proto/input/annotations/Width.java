package com.eltechs.axs.proto.input.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
// @Target({ElementType.PARAMETER})

@Retention(RetentionPolicy.RUNTIME)
public @interface Width {
	// int value();
	
	int[] indexes();
	int[] values();
}
