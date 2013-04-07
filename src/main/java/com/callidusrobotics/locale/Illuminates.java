package com.callidusrobotics.locale;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation scanned by the FieldOfViewFactory to detect implementations of
 * FieldOfViewStrategy.
 *
 * @author Rusty
 * @since 0.0.1
 * @see FieldOfViewStrategy
 * @see FieldOfViewFactory
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface Illuminates {
  FieldOfViewType value();
}
