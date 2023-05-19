package io.github._4drian3d.chatregulator.api.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * If a method contains this annotation,
 * it means that it must be invoked before any future execution.
 *
 * <p>If used in builder context,
 * it is required to invoke this method,
 * otherwise, when trying to generate a new instance from the builder,
 * it will throw an exception.</p>
 */
@Target(ElementType.METHOD)
@Documented
public @interface Required {
}
