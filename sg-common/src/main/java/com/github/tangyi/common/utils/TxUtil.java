package com.github.tangyi.common.utils;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public final class TxUtil {

	private TxUtil() {
	}

	public static TransactionStatus startTransaction(PlatformTransactionManager txManager) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		return txManager.getTransaction(def);
	}
}
