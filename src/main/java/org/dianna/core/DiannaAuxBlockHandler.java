package org.dianna.core;

import org.dianna.core.entity.AuxBlock;
import org.dianna.core.entity.AuxData;

import com.google.bitcoin.core.Sha256Hash;

public interface DiannaAuxBlockHandler {
	public AuxBlock getAuxBlock();

	public void postAuxData(Sha256Hash diannaBlockHash, AuxData auxData);
}
