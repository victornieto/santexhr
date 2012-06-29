package org.openapplicant.domain;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclareError;

@Aspect
public class DomainObjectPolicyAspect {
	
	@DeclareError(
			"call(* org.openapplicant.domain.DomainObject.beforeSave()) && " +
			"!withincode(* org.openapplicant.dao.hibernate.DomainObjectSaveOrUpdateListener.onSaveOrUpdate(..))"
	)
	public static final String errorOnSaveMessage =
		"beforeSave() should only be called within DomainObjectSaveOrUpdateListener.onSaveOrUpdate()";

    @DeclareError(
            "call(* org.openapplicant.domain.DomainObject.beforeDelete()) && " +
                    "!withincode(* org.openapplicant.dao.hibernate.DomainObjectDeleteListener.onDelete(..))"
    )
    public static final String errorOnDeleteMessage =
            "beforeDelete() should only be called within DomainObjectDeleteListener.onDelete()";

}
