package org.dianna.tests.it;

import java.io.IOException;

import org.dianna.core.Dianna;
import org.junit.Before;

public class DiannaIT {
	private Dianna dianna;

	@Before
	public void init() throws IOException, InterruptedException {
		dianna.connect();
	}

}
