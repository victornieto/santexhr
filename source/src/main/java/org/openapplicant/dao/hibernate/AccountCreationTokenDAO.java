package org.openapplicant.dao.hibernate;

import org.openapplicant.dao.IAccountCreationTokenDAO;
import org.openapplicant.domain.AccountCreationToken;


public class AccountCreationTokenDAO extends DomainObjectDAO<AccountCreationToken> 
	implements IAccountCreationTokenDAO {

	public AccountCreationTokenDAO(Class<AccountCreationToken> type) {
		super(type);
	}

	public AccountCreationTokenDAO() {
		super(AccountCreationToken.class);
	}
	
}
