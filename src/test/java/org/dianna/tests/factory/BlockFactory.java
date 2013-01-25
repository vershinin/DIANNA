package org.dianna.tests.factory;

import java.math.BigDecimal;
import java.util.Date;

import org.dianna.core.Protos;
import org.dianna.core.Protos.DiaBlock;
import org.dianna.core.Protos.DiaBlockHeader;
import org.dianna.core.Protos.DiaBlockHeader.DiaAuxData;
import org.dianna.core.Protos.DiaBlockHeader.Builder;
import org.dianna.core.message.payload.Block;
import org.joda.time.DateTime;

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

	public static DiaBlock buildCorrectDiaBlock() {
		DiaBlock.Builder blockBuilder = Protos.DiaBlock.newBuilder();
		blockBuilder.setHeader(buildCorrectDiaBlockHeader());
		return blockBuilder.build();
	}

	public static DiaBlockHeader buildCorrectDiaBlockHeader() {
		Builder headerBuilder = DiaBlockHeader.newBuilder();
		headerBuilder.setVersion(BLOCK_VERSION_CORRECT);
		headerBuilder.setTimestamp((int) new Date().getTime());// FIXME
		headerBuilder.setPrevBlockHash(ByteString.copyFrom(new Sha256Hash(PREV_BLOCK_HASH).getBytes()));
		headerBuilder.setNamespace(NAMESPACE);
		headerBuilder.setPrice(PRICE);
		headerBuilder.setMerkleRoot(ByteString.copyFrom(new Sha256Hash(MERKLE_ROOT).getBytes()));

		headerBuilder.setAuxData(buildCorrectDiaAuxData());
		return headerBuilder.build();
	}

	public static DiaAuxData buildCorrectDiaAuxData() {
		DiaAuxData.Builder auxBuilder = DiaAuxData.newBuilder();
		auxBuilder.setCoinbaseTxIndex(CONIBASE_INDEX);
		auxBuilder.addAuxBranch(ByteString.copyFromUtf8("Test aux branch"));

		auxBuilder.setParentHash(ByteString.copyFrom(new Sha256Hash(PARENT_HASH).getBytes()));
		return auxBuilder.build();
	}

	public static Block createCorrectBlock() {
		Block block = new Block();
		block.setVersion(BLOCK_VERSION_CORRECT);
		block.setNamespace(NAMESPACE);
		block.setPrevBlockHash(new Sha256Hash(PREV_BLOCK_HASH));
		block.setTimestamp(DateTime.now());
		block.setPrice(new BigDecimal(PRICE));
		block.setMerkleRootHash(new Sha256Hash(MERKLE_ROOT));
		block.setParentBlockHash(new Sha256Hash(PARENT_HASH));
		return block;
	}
}
