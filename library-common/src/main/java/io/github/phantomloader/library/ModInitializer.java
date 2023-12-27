package io.github.phantomloader.library;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * <p>
 *     Annotation used by Phantom Loader to generate loader specific code.
 * </p>
 *
 * @author Nico
 */
@Target(ElementType.METHOD)
public @interface ModInitializer {

	Loader modLoader() default Loader.BOTH;

	String type() default "main";

	enum Loader {
		BOTH,
		FORGE,
		FABRIC
	}
}
