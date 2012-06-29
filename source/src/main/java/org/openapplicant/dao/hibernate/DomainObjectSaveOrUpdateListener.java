package org.openapplicant.dao.hibernate;

import org.hibernate.event.SaveOrUpdateEvent;
import org.hibernate.event.def.DefaultSaveOrUpdateEventListener;
import org.openapplicant.domain.DomainObject;


public class DomainObjectSaveOrUpdateListener 
	extends DefaultSaveOrUpdateEventListener {

	@Override
	public void onSaveOrUpdate(SaveOrUpdateEvent event) {
		if(event.getObject() instanceof DomainObject) {
			((DomainObject)event.getObject()).beforeSave();
		}
		super.onSaveOrUpdate(event);
	}
}
