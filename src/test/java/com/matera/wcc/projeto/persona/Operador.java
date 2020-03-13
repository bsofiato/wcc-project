package com.matera.wcc.projeto.persona;

import org.springframework.core.annotation.AliasFor;
import org.springframework.security.test.context.support.WithMockUser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@WithMockUser(authorities = {"view_veiculos"})
public @interface Operador {

    @AliasFor(annotation = WithMockUser.class, attribute = "username")
    String username();
}
