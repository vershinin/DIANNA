package org.dianna.rpc;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Error;
import com.thetransactioncompany.jsonrpc2.JSONRPC2ParseException;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;
import com.thetransactioncompany.jsonrpc2.server.Dispatcher;
import com.thetransactioncompany.jsonrpc2.server.MessageContext;

public class JsonRpcHandler extends AbstractHandler {

	private Dispatcher dispatcher;

	public JsonRpcHandler(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {
		res.setContentType("application/json");

		String jsonRequest = IOUtils.toString(req.getReader());
		PrintWriter writer = res.getWriter();

		try {
			JSONRPC2Request request = JSONRPC2Request.parse(jsonRequest);
			JSONRPC2Response response = dispatcher.process(request, new MessageContext(req));
			String jsonResponse = response.toJSONString();

			res.setContentLength(jsonResponse.length());
			writer.append(jsonResponse);
			writer.close();

		} catch (JSONRPC2ParseException e) {
			JSONRPC2Response errorResponse = new JSONRPC2Response(new JSONRPC2Error(-32700, e.getMessage()), null);
			String errorString = errorResponse.toJSONString();
			res.setContentLength(errorString.length());
			writer.append(errorString);
			writer.close();
		}
	}

}
