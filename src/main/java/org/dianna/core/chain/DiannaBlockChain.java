package org.dianna.core.chain;

import java.math.BigDecimal;
import java.math.MathContext;

import org.dianna.core.entity.DiannaBlock;
import org.dianna.core.exception.ValidationException;
import org.dianna.core.store.BlockStore;
import org.dianna.core.validators.BlockValidator;
import org.joda.time.Days;
import org.joda.time.Minutes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.EventBus;

public class DiannaBlockChain {
	private static final int PRICE_PRECISION = 10;

	private static Logger logger = LoggerFactory.getLogger(DiannaBlockChain.class);

	private static final BigDecimal MAX_LIMIT = new BigDecimal(4);

	public static final int BLOCKS_CHECKPOINT_EXPIRE = 2;
	public static final Days DAYS_CHECKPOINT_EXPIRE = Days.days(14);

	private BlockValidator blockValidator;
	private BlockStore blockStore;
	private EventBus blockChainEventBus;

	public DiannaBlockChain(BlockStore blockStore, BlockValidator validator, EventBus blockChainEventBus) {
		this.blockStore = blockStore;
		this.blockValidator = validator;
		this.blockChainEventBus = blockChainEventBus;
	}

	public synchronized void handleIncomingBlock(DiannaBlock block) throws ValidationException {
		blockValidator.validateBlock(block);

		BigDecimal price = getPriceForBlock(block);

		if (!block.getPrice().equals(price)) {
			logger.error("Block price doesnt match. Price should be {}, but current is {}", price, block.getPrice());
			throw new ValidationException("Repricing procedure initiated. New price is " + price.toPlainString());
		}

		if (logger.isInfoEnabled()) {
			logger.info("Added block {} to blockchain {}");
		}

		blockStore.addBlock(block);
		blockChainEventBus.post(new IncomingBlockEvent(block));
	}

	/**
	 * Calculates price for given block based on block chain
	 * 
	 * @param block
	 * @return
	 */
	public synchronized BigDecimal getPriceForBlock(DiannaBlock block) {
		DiannaBlock lastCheckpoint = blockStore.getLatestCheckpointBlock();
		Days daysBetween = Days.daysBetween(lastCheckpoint.getTimestamp(), block.getTimestamp());
		boolean expiredByDate = daysBetween.isGreaterThan(DAYS_CHECKPOINT_EXPIRE);
		boolean expiredByHeight = block.getHeight() % BLOCKS_CHECKPOINT_EXPIRE == 0;

		if (expiredByDate || expiredByHeight) {
			DiannaBlock preLastCheckpoint = blockStore.getPreLastCheckpoint();
			float curFreq = getBlockFrequency(lastCheckpoint, block);
			float prevFreq = getBlockFrequency(preLastCheckpoint, lastCheckpoint);

			BigDecimal checkpointPrice = lastCheckpoint.getPrice();

			BigDecimal maxPrice = checkpointPrice.multiply(MAX_LIMIT, new MathContext(PRICE_PRECISION));
			BigDecimal newPrice = checkpointPrice.multiply(new BigDecimal(curFreq / prevFreq), new MathContext(
					PRICE_PRECISION));

			return newPrice.min(maxPrice);
		}
		return block.getPrice();
	}

	/**
	 * Method calculates dianna block generation frequency
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	private float getBlockFrequency(DiannaBlock start, DiannaBlock end) {
		Minutes timeInterval = Minutes.minutesBetween(start.getTimestamp(), end.getTimestamp());
		int blockCount = end.getHeight() - start.getHeight();
		return (float) blockCount / (float) timeInterval.getMinutes();
	}

	public Integer getHeight() {
		return null;
	}

	public DiannaBlock getLatestBlock() {
		// TODO Auto-generated method stub
		return null;
	}

}
