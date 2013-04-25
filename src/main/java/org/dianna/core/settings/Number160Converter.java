package org.dianna.core.settings;

import net.tomp2p.peers.Number160;

import com.beust.jcommander.IStringConverter;

public class Number160Converter implements IStringConverter<Number160> {
	@Override
	public Number160 convert(String value) {
		return new Number160(value);
	}
}
