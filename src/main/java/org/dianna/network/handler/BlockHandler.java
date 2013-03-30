package org.dianna.network.handler;

import net.tomp2p.peers.PeerAddress;

import org.dianna.core.message.BlockMessage;
import org.dianna.core.message.Message;
import org.dianna.core.message.Message.MessageType;
import org.dianna.core.message.payload.Block;
import org.dianna.core.validators.BlockValidator;
import org.dianna.core.validators.ValidationException;
import org.dianna.network.DiannaClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class handles broadcasted blocks
 * 
 * @author ivan
 * 
 */
public class BlockHandler implements Handler {
	private static Logger log = LoggerFactory.getLogger(BlockHandler.class);

	private DiannaClient client;
	private BlockValidator blockValidator;

	public BlockHandler() {

	}

	@Override
	public Message handle(PeerAddress peer, Message message) {
		BlockMessage block = (BlockMessage) message;
		if (log.isDebugEnabled()) {
			log.debug("New block recieved", block.toString());
		}
		Block b = null;
		try {
			blockValidator.validateBlock(b);
		} catch (ValidationException e) {
			log.warn("Recieved block is invalid", e);
		}
		return null;
	}

	@Override
	public MessageType getType() {
		return MessageType.BLOCK;
	}

}
