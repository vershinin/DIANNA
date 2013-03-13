package org.dianna.tests;

import static org.junit.Assert.assertEquals;

import org.dianna.core.Protos.DiaBlock;
import org.dianna.core.message.payload.Block;
import org.dianna.tests.factory.TestBlockFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.bitcoin.core.Sha256Hash;

@RunWith(MockitoJUnitRunner.class)
public class BlockTest {

	@Test
	public void shouldParseCorrectly() {
		DiaBlock networkBlock = TestBlockFactory.buildCorrectDiaBlock();
		Block block = new Block();

		assertEquals(block.getMerkleRootHash(), new Sha256Hash(networkBlock.getHeader().getMerkleRoot().toByteArray()));
	}

}
