package io.github.hexagonnico.phantom.library;

public @interface Mod {

	String id();

	String version() default "";

	String name() default "";

	String description() default "";

	String[] authors() default {};

	String credits() default "";

	String homepage() default "";

	String sources() default "";

	String issues() default "";

	String license() default "All rights reserved";

	String logo() default "logo.png";

	String icon() default "icon.png";
}
