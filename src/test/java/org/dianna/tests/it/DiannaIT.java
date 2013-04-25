package org.dianna.tests.it;

import java.io.IOException;

import org.dianna.core.Dianna;
import org.dianna.core.settings.DiannaSettings;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.defaultprovider.PropertyFileDefaultProvider;

@RunWith(MockitoJUnitRunner.class)
public class DiannaIT {
	private Dianna dianna;
	private Dianna dianna2;

	@Before
	public void init() throws IOException, InterruptedException {
		DiannaSettings settings = new DiannaSettings();
		DiannaSettings settings2 = new DiannaSettings();
		JCommander jc = new JCommander(settings);
		jc.setDefaultProvider(new PropertyFileDefaultProvider("testSettings.properties"));
		jc.parse();
		jc = new JCommander(settings2);
		jc.setDefaultProvider(new PropertyFileDefaultProvider("testSettings.properties"));
		jc.parse();
		dianna = new Dianna(settings);
		settings2.setTcpPort(7701);
		settings2.setUdpPort(7701);
		dianna2 = new Dianna(settings2);
	}

	@Test
	public void shouldBootstrapOnNetwork() throws IOException, InterruptedException {
		dianna.connect();
		dianna2.connect();
		dianna2.bootstrap();
		Thread.sleep(100000);
	}

}
