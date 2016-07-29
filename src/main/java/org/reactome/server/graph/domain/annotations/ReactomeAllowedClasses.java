package org.reactome.server.graph.domain.annotations;

import org.reactome.server.graph.domain.model.DatabaseObject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ReactomeAllowedClasses {
    Class<? extends  DatabaseObject>[] allowed() default DatabaseObject.class;
}
