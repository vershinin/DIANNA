package org.dianna.core.serialization.payload;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.dianna.core.Protos;
import org.dianna.core.Protos.DiaBlock;
import org.dianna.core.Protos.DiaBlock.Builder;
import org.dianna.core.Protos.DiaBlockHeader;
import org.dianna.core.Protos.DiaBlockHeader.DiaAuxData;
import org.dianna.core.exception.ValidationException;
import org.dianna.core.message.Block;
import org.dianna.core.message.Message;
import org.dianna.core.message.Message.MessageType;
import org.dianna.core.message.Payload;
import org.joda.time.DateTime;

import com.google.bitcoin.core.Sha256Hash;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

public class BlockSerializer implements PayloadSerializer {

	@Override
	public MessageType getType() {
		return MessageType.BLOCK;
	}

	@Override
	public byte[] serialize(Payload message) {
		Block block = (Block) message;

		Builder builder = Protos.DiaBlock.newBuilder();
		List<Transaction> transactions = block.getTransactions();
		if (CollectionUtils.isNotEmpty(transactions)) {
			for (Transaction transaction : transactions) {

			}
		}

		DiaBlockHeader.Builder headerBuilder = builder.getHeaderBuilder();
		serializeHeader(block, headerBuilder);

		DiaAuxData.Builder auxBuilder = headerBuilder.getAuxDataBuilder();
		serializeAuxData(block, auxBuilder);

		return builder.build().toByteArray();
	}

	private void serializeHeader(Block block, DiaBlockHeader.Builder headerBuilder) {
		headerBuilder.setVersion(block.getVersion());
		headerBuilder.setTimestamp(block.getTimestamp().getMillis());
		headerBuilder.setPrevBlockHash(ByteString.copyFrom(block.getPrevBlockHash().getBytes()));
		headerBuilder.setNamespace(block.getNamespace());
		headerBuilder.setPrice(block.getPrice().longValue());
		headerBuilder.setMerkleRoot(ByteString.copyFrom(block.getMerkleRootHash().getBytes()));
	}

	private void serializeAuxData(Block block, DiaAuxData.Builder auxBuilder) {
		auxBuilder.setCoinbaseTxIndex(block.getCoinbaseTxIndex());
		auxBuilder.setParentHash(ByteString.copyFrom(block.getParentBlockHash().getBytes()));

		List<byte[]> auxBranch = block.getAuxBranch();
		if (CollectionUtils.isNotEmpty(auxBranch)) {
			for (byte[] hash : auxBranch) {
				auxBuilder.addAuxBranch(ByteString.copyFrom(hash));
			}
		}
	}

	@Override
	public Payload deserialize(byte[] byteArray) {
		Block block = new Block();
		try {
			DiaBlock diaBlock = Protos.DiaBlock.parseFrom(byteArray);
			populateHeader(block, diaBlock.getHeader());
		} catch (InvalidProtocolBufferException e) {
			return null;
		}
		return block;
	}

	private void populateHeader(Block block, DiaBlockHeader header) {
		block.setVersion(header.getVersion());
		block.setPrevBlockHash(new Sha256Hash(header.getPrevBlockHash().toByteArray()));
		block.setTimestamp(new DateTime(header.getTimestamp()));
		block.setNamespace(header.getNamespace());
		block.setPrice(new BigDecimal(header.getPrice()));
		block.setMerkleRootHash(new Sha256Hash(header.getMerkleRoot().toByteArray()));
	}
}
