package org.openapplicant.dao.hibernate;

import org.hibernate.event.DeleteEvent;
import org.hibernate.event.def.DefaultDeleteEventListener;
import org.openapplicant.domain.DomainObject;


public class DomainObjectDeleteListener 
	extends DefaultDeleteEventListener {
    
    @Override
    public void onDelete(DeleteEvent event) {
        if(event.getObject() instanceof DomainObject) {
            ((DomainObject)event.getObject()).beforeDelete();
        }
        super.onDelete(event);
    }
}
