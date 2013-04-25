package org.dianna.rpc;

import org.dianna.core.settings.DiannaSettings;
import org.eclipse.jetty.server.Server;

public class JsonRpcServer {
	
	public JsonRpcServer(DiannaSettings settings) throws Exception {
		Server server = new Server(settings.getJsonRpcPort());
		server.start();
		server.join();
		
	}
}
