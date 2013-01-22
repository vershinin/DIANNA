package org.dianna.tests;

import static org.junit.Assert.assertEquals;

import org.dianna.core.Protos.Block;
import org.dianna.core.message.DBlock;
import org.dianna.tests.factory.BlockFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.bitcoin.core.Sha256Hash;

@RunWith(MockitoJUnitRunner.class)
public class BlockTest {

	@Test
	public void shouldParseCorrectly() {
		Block networkBlock = BlockFactory.buildCorrectBlock();
		DBlock block = new DBlock(networkBlock.toByteArray());
		
		assertEquals(block.getMerkleRootHash(), new Sha256Hash(networkBlock.getHeader().getMerkleRoot().toByteArray()));
	}

}
