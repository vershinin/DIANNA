package org.dianna.tests.core.factory;

import org.dianna.core.entity.DiannaBlock;
import org.dianna.core.factory.BlockFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BlockFactoryTest {

	@InjectMocks
	private BlockFactory blockFactory;

	@Test
	public void shouldBuildNewBlock() {
		
		DiannaBlock block = blockFactory.build();
	}
}
