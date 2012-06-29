package org.openapplicant.dao.hibernate;

import org.apache.commons.lang.StringUtils;
import org.hibernate.cfg.ImprovedNamingStrategy;

/**
 * Custom ImprovedNamingStrategy that strips '_internal' from the end of 
 * column names allowing ease of use with columns mapped to methods like
 * <code>private Collection getFoosInternal()</code>
 */
public class NamingStrategy extends ImprovedNamingStrategy {

	@Override
	public String columnName(String columnName) {
		return chompInternal( super.columnName(columnName) );
	}
	
	@Override
	public String propertyToColumnName(String propertyName) {
		return chompInternal( super.propertyToColumnName(propertyName) );
	}

	@Override
	public String collectionTableName(String ownerEntity,
			String ownerEntityTable, String associatedEntity,
			String associatedEntityTable, String propertyName) {
		
		return chompInternal(
				super.collectionTableName(
						ownerEntity, 
						ownerEntityTable,
						associatedEntity, 
						associatedEntityTable, 
						propertyName
				)
		);
		
	}
	
	private String chompInternal(String value) {
		return StringUtils.removeEnd(value, "_internal");
	}
}
