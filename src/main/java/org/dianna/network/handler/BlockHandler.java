package org.dianna.network.handler;

import net.tomp2p.peers.PeerAddress;

import org.dianna.core.entity.DiannaBlock;
import org.dianna.core.exception.ValidationException;
import org.dianna.core.message.BlockMessage;
import org.dianna.core.message.Message;
import org.dianna.core.message.Message.MessageType;
import org.dianna.core.store.BlockStore;
import org.dianna.network.server.DiannaPeer;
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

	private DiannaPeer peer;
	private BlockStore blockStore;

	public BlockHandler(BlockStore blockStore) {
		this.blockStore = blockStore;
	}

	@Override
	public Message handle(PeerAddress peer, Message message) {
		BlockMessage block = (BlockMessage) message;
		if (log.isDebugEnabled()) {
			log.debug("New block recieved from {} {}", peer.getInetAddress(), block.toString());
		}
		DiannaBlock b = block.getBlock();
		try {
			blockStore.addBlock(b);
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
