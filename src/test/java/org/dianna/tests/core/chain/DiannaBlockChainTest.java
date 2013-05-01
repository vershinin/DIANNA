package org.dianna.tests.core.chain;

import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;

import org.dianna.core.chain.DiannaBlockChain;
import org.dianna.core.chain.IncomingBlockEvent;
import org.dianna.core.entity.DiannaBlock;
import org.dianna.core.exception.ValidationException;
import org.dianna.core.store.BlockStore;
import org.dianna.core.validators.BlockValidator;
import org.dianna.tests.builders.TestDataFactory;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Minutes;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.eventbus.EventBus;

@RunWith(MockitoJUnitRunner.class)
public class DiannaBlockChainTest {

	@InjectMocks
	private DiannaBlockChain chain;

	@Mock
	private BlockStore blockStore;

	@Mock
	private BlockValidator blockValidator;

	@Mock
	private EventBus eventBus;

	@Before
	public void setUp() throws ValidationException {
		// by default all blocks structure are valid
		doNothing().when(blockValidator).validateBlock(any(DiannaBlock.class));

		DiannaBlock preLastCheckpoint = TestDataFactory.buildCorrectBlock();
		preLastCheckpoint.setHeight(0);
		preLastCheckpoint.setTimestamp(DateTime.now().minus(Days.days(30)));
		given(blockStore.getPreLastCheckpoint()).willReturn(preLastCheckpoint);

		DiannaBlock checkpointBlock = TestDataFactory.buildCorrectBlock();
		checkpointBlock.setHeight(100);
		checkpointBlock.setTimestamp(DateTime.now().minus(Days.days(15)));
		given(blockStore.getLatestCheckpointBlock()).willReturn(checkpointBlock);

		DiannaBlock lastBlock = TestDataFactory.buildCorrectBlock();
		lastBlock.setHeight(199);
		lastBlock.setTimestamp(DateTime.now().minus(Minutes.minutes(10)));
		given(blockStore.getLatestBlock()).willReturn(lastBlock);
	}

	@Test
	public void shouldRepriceAfterExpiryByTime() throws ValidationException {
		// given
		DiannaBlock incomingBlock = TestDataFactory.buildCorrectBlock();
		incomingBlock.setHeight(400);
		incomingBlock.setTimestamp(DateTime.now());
		incomingBlock.setPrice(incomingBlock.getPrice().multiply(new BigDecimal(3)));

		// when
		chain.handleIncomingBlock(incomingBlock);

		// then
		verify(eventBus, times(1)).post(any(IncomingBlockEvent.class));
		verify(blockStore, times(1)).addBlock(eq(incomingBlock));
	}
	
	

	@Test(expected = ValidationException.class)
	public void shouldThrowValidationExceptionIfPriceIsInvalid() throws ValidationException {
		// given
		DiannaBlock incomingBlock = TestDataFactory.buildCorrectBlock();
		incomingBlock.setHeight(400);
		incomingBlock.setTimestamp(DateTime.now());

		// when
		chain.handleIncomingBlock(incomingBlock);

		// then
		fail("Invalid price, exception should be thrown!");
	}
	
	
}
