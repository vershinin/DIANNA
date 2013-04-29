package org.dianna.core.validators;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.dianna.bitcoinlite.BitcoinClient;
import org.dianna.core.entity.DiannaBlock;
import org.dianna.core.entity.DomainTransaction;
import org.dianna.core.exception.InvalidTransactionException;
import org.dianna.core.exception.ValidationException;
import org.dianna.core.utils.HashUtil;
import org.dianna.core.utils.MerkleTree;

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
		this.setBitcoinClient(bitcoinClient);
	}

	/**
	 * Validates block. If block is invalid throws exception
	 * 
	 * @param block
	 * @throws ValidationException
	 */
	public void validateBlock(DiannaBlock block) throws ValidationException {
		Sha256Hash merkleRoot = HashUtil.buildMerkleTree(block.getTransactions()).getRoot();

		if (!block.getMerkleRootHash().equals(merkleRoot)) {
			throw new ValidationException("Transactions merkle root is invalid!");
		}

		for (DomainTransaction tx : block.getTransactions()) {
			validateTransaction(tx);
		}

		Sha256Hash coinbase = bitcoinClient.getCoinbase(block.getParentBlockHash(), block.getCoinbaseTxIndex());
		if (coinbase == null) {
			throw new ValidationException("Cannot get coinbase for parent block");
		}
		List<Pair<Sha256Hash, Sha256Hash>> auxBranch = block.getAuxBranch();

		if (!MerkleTree.verifyPath(block.getHash(), coinbase, auxBranch)) {
			throw new ValidationException("Coinbase is invalid");
		}

	}

	private void validateTransaction(DomainTransaction tx) throws ValidationException {
		try {
			transactionValidator.validateTransaction(tx);
		} catch (InvalidTransactionException e) {
			throw new ValidationException(e);
		}
	}

	public void setTransactionValidator(TransactionValidator transactionValidator) {
		this.transactionValidator = transactionValidator;
	}

	public void setBitcoinClient(BitcoinClient bitcoinClient) {
		this.bitcoinClient = bitcoinClient;
	}
}
