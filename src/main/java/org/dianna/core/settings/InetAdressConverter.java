package org.dianna.core.settings;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.beust.jcommander.IStringConverter;

public class InetAdressConverter implements IStringConverter<InetAddress> {
	@Override
	public InetAddress convert(String value) {
		try {
			return InetAddress.getByName(value);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}
}