package org.dianna.tests.factory;

import java.util.Date;

import org.dianna.core.Protos;
import org.dianna.core.Protos.Block;
import org.dianna.core.Protos.BlockHeader;
import org.dianna.core.Protos.BlockHeader.AuxData;
import org.dianna.core.Protos.BlockHeader.Builder;

import com.google.bitcoin.core.Sha256Hash;
import com.google.protobuf.ByteString;

public class BlockFactory {
	private static final int CONIBASE_INDEX = 1;
	private static final int NAMESPACE = 1;
	private static final String PREV_BLOCK_HASH = "155c572331fe3de390ff0bf82f1dc340d4fd621fe15e0fa4b8b84b046bc3608f";
	public static final int BLOCK_VERSION_CORRECT = 1;
	private static final long PRICE = 100000000;
	private static final String MERKLE_ROOT = "155c572331fe3de390ff0bf82f1dc340d4fd621fe15e0fa4b8b84b046bc3608f";
	private static final String PARENT_HASH = "155c572331fe3de390ff0bf82f1dc340d4fd621fe15e0fa4b8b84b046bc3608f";

	public static Block buildCorrectBlock() {
		Block.Builder blockBuilder = Protos.Block.newBuilder();
		blockBuilder.setHeader(buildCorrectBlockHeader());
		return blockBuilder.build();
	}

	public static BlockHeader buildCorrectBlockHeader() {
		Builder headerBuilder = BlockHeader.newBuilder();
		headerBuilder.setVersion(BLOCK_VERSION_CORRECT);
		headerBuilder.setTimestamp((int) new Date().getTime());// FIXME
		headerBuilder.setPrevBlockHash(ByteString.copyFrom(new Sha256Hash(PREV_BLOCK_HASH).getBytes()));
		headerBuilder.setNamespace(NAMESPACE);
		headerBuilder.setPrice(PRICE);
		headerBuilder.setMerkleRoot(ByteString.copyFrom(new Sha256Hash(MERKLE_ROOT).getBytes()));

		headerBuilder.setAuxData(buildCorrectAuxData());
		return headerBuilder.build();
	}

	public static AuxData buildCorrectAuxData() {
		AuxData.Builder auxBuilder = AuxData.newBuilder();
		auxBuilder.setCoinbaseTxIndex(CONIBASE_INDEX);
		auxBuilder.addAuxBranch(ByteString.copyFromUtf8("Test aux branch"));

		auxBuilder.setParentHash(ByteString.copyFrom(new Sha256Hash(PARENT_HASH).getBytes()));
		return auxBuilder.build();
	}
}
