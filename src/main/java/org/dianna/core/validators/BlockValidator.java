package org.dianna.core.validators;

import java.util.List;

import org.dianna.bitcoinlite.BitcoinClient;
import org.dianna.core.crypto.HashUtil;
import org.dianna.core.entity.DiannaBlock;
import org.dianna.core.entity.DomainTransaction;
import org.dianna.core.exception.ValidationException;

import com.google.bitcoin.core.Sha256Hash;

/**
 * This class validates dianna blocks by making all necessary requests
 * 
 * @author ivan
 * 
 */
public class BlockValidator {
	private BitcoinClient bitcoinClient;
	private TransactionValidator transactionValidator;

	public BlockValidator(BitcoinClient bitcoinClient) {
		this.bitcoinClient = bitcoinClient;
	}

	/**
	 * Validates block. If block is invalid throws exception
	 * 
	 * @param block
	 * @throws ValidationException
	 */
	public void validateBlock(DiannaBlock block) throws ValidationException {
		List<Sha256Hash> merkleTree = HashUtil.buildMerkleTree(block.getTransactions());
		block.getMerkleRootHash(); // TODO should be equal
		// TODO implement block validation
		for (DomainTransaction tx : block.getTransactions()) {
			transactionValidator.validateTransaction(tx);
		}
		Sha256Hash coinbase = bitcoinClient.getCoinbase(block.getParentBlockHash(), block.getCoinbaseTxIndex());
		if (coinbase == null) {
			throw new ValidationException("Cannot get coinbase for parent block");
		}
		if (!block.getHash().equals(coinbase)) {
			throw new ValidationException("Coinbase is invalid");
		}
	}
}
