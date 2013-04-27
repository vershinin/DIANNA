package org.dianna.tests.rpc;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.contains;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dianna.rpc.JsonRpcHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;
import com.thetransactioncompany.jsonrpc2.server.Dispatcher;
import com.thetransactioncompany.jsonrpc2.server.MessageContext;

@RunWith(MockitoJUnitRunner.class)
public class JsonRpcHandlerTest {
	private static final String JSON_RPC_REQUEST = "{\"jsonrpc\": \"2.0\", \"method\": \"test\", \"params\": [\"testparams\"], \"id\": 2}";
	@InjectMocks
	private JsonRpcHandler jsonRpcHandler;

	@Mock
	private Dispatcher dispatcher;

	@Mock
	private HttpServletRequest req;
	
	@Mock(answer= Answers.RETURNS_DEEP_STUBS)
	private HttpServletResponse res;
	
	@Test
	public void shouldReturnErrorOnEmptyRequest() throws IOException, ServletException {
		given(req.getReader()).willReturn(new BufferedReader(new StringReader("")));
		
		//when
		jsonRpcHandler.handle(null, null, req, res);

		//then
		verify(res.getWriter(), atLeastOnce()).append(contains("error"));
	}
	
	@Test
	public void shouldProcessJsonRpcRequests() throws IOException, ServletException{
		given(req.getReader()).willReturn(new BufferedReader(new StringReader(JSON_RPC_REQUEST)));
		JSONRPC2Response response = new JSONRPC2Response("testResult", 1);
		given(dispatcher.process(any(JSONRPC2Request.class), any(MessageContext.class))).willReturn(response);

		//when
		jsonRpcHandler.handle(null, null, req, res);
		
		//then
		verify(res.getWriter(), times(1)).append(contains(response.toJSONString()));
	}
}
