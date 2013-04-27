package org.dianna.rpc.handlers;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dianna.core.DiannaAuxBlockHandler;
import org.dianna.core.entity.AuxBlock;
import org.dianna.core.entity.AuxData;
import org.spongycastle.util.encoders.Hex;

import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;
import com.google.bitcoin.core.NetworkParameters;
import com.google.bitcoin.core.ProtocolException;
import com.google.bitcoin.core.Sha256Hash;
import com.google.bitcoin.core.Transaction;
import com.google.bitcoin.core.Utils;
import com.google.bitcoin.core.VarInt;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Error;
import com.thetransactioncompany.jsonrpc2.JSONRPC2ParamsType;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;
import com.thetransactioncompany.jsonrpc2.server.MessageContext;
import com.thetransactioncompany.jsonrpc2.server.RequestHandler;

public class GetAuxBlockHandler implements RequestHandler {

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
		dianna.postAuxData(parseAuxData(data));

		return new JSONRPC2Response(JSONRPC2Error.METHOD_NOT_FOUND, request.getID());
	}

	private AuxData parseAuxData(String data) {
		AuxData auxData = new AuxData();
		byte[] bytes = Hex.decode(data);
		AuxDataReader reader = new AuxDataReader(bytes);
		long version = reader.readUint32();
		long inputLenght = reader.readVarInt();
		Transaction tx;
		try {
			tx = new Transaction(NetworkParameters.testNet3(), bytes);
			System.out.println(getMekleRootFromCoinbase((tx.getInput(0).getScriptBytes())));
			int messageSize = tx.getMessageSize();
			reader.cursor = messageSize;

			Sha256Hash parentBlockHash = reader.readHash();

			// merkle tree
			long merkleLength = reader.readUint32();
			List<Sha256Hash> merkleHashes = Lists.newArrayList();
			for (int i = 0; i < merkleLength; i++) {
				Sha256Hash merkleHash = reader.readHash();
				merkleHashes.add(merkleHash);
				System.out.println("hash: " + merkleHash);
			}
			long merkleIndex = reader.readUint32();
			System.out.println("idx: " + merkleIndex);

			long Blockversion = reader.readUint32();
			Sha256Hash prevBlockHash = reader.readHash();
			System.out.println();
			Sha256Hash merkleRoot = reader.readHash();
			long time = reader.readUint32();
			long difficultyTarget = reader.readUint32();
			long nonce = reader.readUint32();

		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return auxData;
		// auxData.set
	}

	private Sha256Hash getMekleRootFromCoinbase(byte[] coinbase) {
		String bytesToHexString = Utils.bytesToHexString(coinbase);
		String mergedMineInfo = StringUtils.substringAfterLast(bytesToHexString, "fabe6d6d");

		AuxDataReader reader = new AuxDataReader(Utils.parseAsHexOrBase58(mergedMineInfo));
		Sha256Hash hash = reader.readHash();
		hash = new Sha256Hash(Utils.reverseBytes(hash.getBytes()));
		long merkleSize = reader.readUint32();
		System.out.println("size " + merkleSize);
		long merkleNonce = reader.readUint32();
		System.out.println("nonce " + merkleNonce);
		return hash;
	}

	private class AuxDataReader {
		private int cursor = 0;
		private byte[] bytes;

		public AuxDataReader(byte[] bytes) {
			this.bytes = bytes;
		}

		public long readUint32() {
			long u = Utils.readUint32(bytes, cursor);
			cursor += 4;
			return u;
		}

		public Sha256Hash readHash() {
			byte[] hash = new byte[32];
			System.arraycopy(bytes, cursor, hash, 0, 32);
			// We have to flip it around, as it's been read off the wire in
			// little endian.
			// Not the most efficient way to do this but the clearest.
			hash = Utils.reverseBytes(hash);
			cursor += 32;
			return new Sha256Hash(hash);
		}

		public long readInt64() {
			long u = Utils.readInt64(bytes, cursor);
			cursor += 8;
			return u;
		}

		public BigInteger readUint64() {
			// Java does not have an unsigned 64 bit type. So scrape it off the
			// wire then flip.
			byte[] valbytes = new byte[8];
			System.arraycopy(bytes, cursor, valbytes, 0, 8);
			valbytes = Utils.reverseBytes(valbytes);
			cursor += valbytes.length;
			return new BigInteger(valbytes);
		}

		public long readVarInt() {
			return readVarInt(0);
		}

		public long readVarInt(int offset) {
			VarInt varint = new VarInt(bytes, cursor + offset);
			cursor += offset + varint.getOriginalSizeInBytes();
			return varint.value;
		}

		public byte[] readBytes(int length) {
			byte[] b = new byte[length];
			System.arraycopy(bytes, cursor, b, 0, length);
			cursor += length;
			return b;
		}

		public byte[] readByteArray() {
			long len = readVarInt();
			return readBytes((int) len);
		}

		public String readStr() {
			VarInt varInt = new VarInt(bytes, cursor);
			if (varInt.value == 0) {
				cursor += 1;
				return "";
			}
			cursor += varInt.getOriginalSizeInBytes();
			byte[] characters = new byte[(int) varInt.value];
			System.arraycopy(bytes, cursor, characters, 0, characters.length);
			cursor += characters.length;
			try {
				return new String(characters, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e); // Cannot happen, UTF-8 is always
												// supported.
			}
		}
	}

}
