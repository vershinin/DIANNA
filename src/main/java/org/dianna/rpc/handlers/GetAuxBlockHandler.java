package org.dianna.rpc.handlers;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dianna.core.DiannaAuxBlockHandler;
import org.dianna.core.entity.AuxBlock;
import org.dianna.core.entity.AuxData;
import org.dianna.core.utils.BitcoinDataReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;
import com.google.bitcoin.core.ProtocolException;
import com.google.bitcoin.core.Sha256Hash;
import com.google.bitcoin.core.Transaction;
import com.google.bitcoin.core.Utils;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Error;
import com.thetransactioncompany.jsonrpc2.JSONRPC2ParamsType;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;
import com.thetransactioncompany.jsonrpc2.server.MessageContext;
import com.thetransactioncompany.jsonrpc2.server.RequestHandler;

public class GetAuxBlockHandler implements RequestHandler {
	private static final String MERGED_MINING_MAGIC = "fabe6d6d";

	private static Logger log = LoggerFactory.getLogger(GetAuxBlockHandler.class);

	private static final String METHOD = "getauxblock";
	private DiannaAuxBlockHandler dianna;

	public GetAuxBlockHandler(DiannaAuxBlockHandler handler) {
		this.dianna = handler;
	}

	@Override
	public String[] handledRequests() {
		return new String[] { METHOD };
	}

	@Override
	public JSONRPC2Response process(JSONRPC2Request request, MessageContext requestCtx) {
		if (request.getParamsType() == JSONRPC2ParamsType.NO_PARAMS || request.getPositionalParams().size() == 0) {
			AuxBlock auxBlock = dianna.getAuxBlock();
			Map<String, Object> auxData = Maps.newHashMap();
			auxData.put("hash", auxBlock.getHash());
			auxData.put("chainid", auxBlock.getChainId());
			auxData.put("target", auxBlock.getTarget());
			JSONRPC2Response response = new JSONRPC2Response(auxData, request.getID());
			return response;
		}

		List<Object> positionalParams = request.getPositionalParams();
		String hash = (String) positionalParams.get(0);
		String data = (String) positionalParams.get(1);
		log.info("Got merged mining info for block with hash {}", hash);

		AuxData auxData = parseAuxData(data);
		if (auxData == null) {
			log.error("Cannot parse aux data");
			return new JSONRPC2Response(new JSONRPC2Error(-32605, "Cannot parse aux data"), request.getID());
		}

		dianna.postAuxData(new Sha256Hash(hash), auxData);

		return new JSONRPC2Response(false, request.getID());
	}

	private Transaction readTransaction(byte[] bytes) {
		try {
			Transaction tx = new Transaction(null, bytes);
			return tx;
		} catch (ProtocolException e) {
			log.error("Cannot parse transaction from getauxblock request");
			return null;
		}

	}

	/**
	 * 
	 * @param data
	 * @return
	 */
	private AuxData parseAuxData(String data) {
		AuxData auxData = new AuxData();
		byte[] bytes = Utils.parseAsHexOrBase58(data);

		BitcoinDataReader reader = new BitcoinDataReader(bytes);
		Transaction tx = readTransaction(bytes);
		int messageSize = tx.getMessageSize();
		reader.setCursor(messageSize);

		Sha256Hash parentBlockHash = reader.readHash();
		List<Sha256Hash> txMerkleLink = parseMerkleTree(reader);
		long txMerkleIndex = reader.readUint32();

		List<Sha256Hash> mergeMiningMerkleLink = parseMerkleTree(reader);
		long merkleIndex2 = reader.readUint32();

		parseBlock(reader);

		auxData.setParentBlockHash(parentBlockHash);
		auxData.setAuxMerkleBranch(mergeMiningMerkleLink);
		auxData.setCoinbaseTxIndex(0);
		return auxData;
	}

	@SuppressWarnings("unused")
	private void parseBlock(BitcoinDataReader reader) {
		long blockversion = reader.readUint32();
		Sha256Hash prevBlockHash = reader.readHash();
		Sha256Hash merkleRoot = reader.readHash();

		// TODO implement time and difficulty parsing
		long time = reader.readUint32();
		long difficultyTarget = reader.readUint32();
		long nonce = reader.readUint32();
	}

	private List<Sha256Hash> parseMerkleTree(BitcoinDataReader reader) {
		int merkleLength = (int) reader.readVarInt();
		List<Sha256Hash> merkleHashes = Lists.newArrayList(merkleLength);
		for (int i = 0; i < merkleLength; i++) {
			Sha256Hash merkleHash = reader.readHash();
			merkleHashes.add(merkleHash);
		}
		return merkleHashes;
	}

	private Sha256Hash getMekleRootFromCoinbase(byte[] coinbase) {
		String bytesToHexString = Utils.bytesToHexString(coinbase);
		String mergedMineInfo = StringUtils.substringAfterLast(bytesToHexString, MERGED_MINING_MAGIC);

		BitcoinDataReader reader = new BitcoinDataReader(Utils.parseAsHexOrBase58(mergedMineInfo));
		Sha256Hash hash = reader.readHash();
		hash = new Sha256Hash(Utils.reverseBytes(hash.getBytes()));
		long merkleSize = reader.readUint32();
		long merkleNonce = reader.readUint32();
		return hash;
	}

}
