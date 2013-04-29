package org.dianna.core.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

import com.google.bitcoin.core.Sha256Hash;
import com.google.bitcoin.core.Utils;
import com.google.bitcoin.core.VarInt;

public class BitcoinDataReader {
	int cursor = 0;
	private byte[] bytes;

	public BitcoinDataReader(byte[] bytes) {
		this.bytes = bytes;
	}
	
	public void setCursor(int cursor){
		this.cursor = cursor;
		
	}
	public int getCursor(){
		return this.cursor;
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

	public byte[] getBytes() {
		return bytes;
	}
}