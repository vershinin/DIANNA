package org.dianna.core;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

public class DiannaSettings {
	private Args args;

	public DiannaSettings(String[] args) {
		this.args = new Args();
		JCommander jc = new JCommander(this.args, args);
		// jc.setDefaultProvider(new PropertyFileDefaultProvider(fileName));
	}

	public InetAddress getBootstrapAddress() {
		return null;
	}

	public Long getSendMessageTimeout() {
		return 1000L;
	}

	private class Args {
		@Parameter(names = "-bootstrap", converter = InetAdressConverter.class)
		private InetAddress bootstrapAddress;

		@Parameter(names = "-timeout")
		private Integer timeout = 10000;
	}

	private class InetAdressConverter implements IStringConverter<InetAddress> {
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

	public Integer getCoinbaseTxIndex() {
		return 0;
	}

	public int getNamespace() {
		return 0;
	}

}
