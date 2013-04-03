package org.dianna.core.validators;

import java.util.List;

import org.dianna.bitcoinlite.BitcoinClient;
import org.dianna.core.crypto.HashUtil;
import org.dianna.core.entity.Block;
import org.dianna.core.exception.ValidationException;

import com.google.bitcoin.core.Sha256Hash;

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
	public void validateBlock(Block block) throws ValidationException {
		Sha256Hash coinbase = bitcoinClient.getCoinbase(block.getParentBlockHash(), block.getCoinbaseTxIndex());
		List<Sha256Hash> merkleTree = HashUtil.buildMerkleTree(block.getTransactions());
		block.getMerkleRootHash(); // TODO should be equal
		// TODO implement block validation
	}
}
