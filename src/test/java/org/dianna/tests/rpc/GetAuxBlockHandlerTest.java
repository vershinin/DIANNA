package org.dianna.tests.rpc;

import java.util.List;

import org.dianna.rpc.handlers.GetAuxBlockHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.bitcoin.core.Sha256Hash;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;

@RunWith(MockitoJUnitRunner.class)
public class GetAuxBlockHandlerTest {
	
	private static final String AUX_POW = "01000000010000000000000000000000000000000000000000000000000000000000000000ffffffff3702a7232cfabe6d6dcc21f0ded985809a04174f4d5af7975068ee3ede9e1aa627b8199c3d798ef0ed0100000000000000062f503253482fffffffff0340787d01000000001976a914b29175a47c791580f29a5296c049e2609db0b0ac88acc079882801000000434104ffd03de44a6e11b9917f3a29f9443283d9871c9d743ef30d5eddcd37094b64d1b3d8090496b53256786bf5c82932ec23c3b74d9f05a6f95a8b5529352656664bac00000000000000002524059b9686a04f66daa73a5ea1ce43b138871e6a373b795f695fe93978c3cd4d1b000000000000000069e3e72baad309ebd9b945466f129683247c7f7a34e4e72da9d80c23a31c000000000000000000000000020000002bd5e4eaf1d632c45d3ae3eba2c2c765bde9d435cf16b7dc4f8af1d22c9500006aa05edb84f965e468bd8b8b02d67624adf645c91fcae0ea6f953878d0ca4cf0bba27b51ffff031f00002c17";
	@InjectMocks
	private GetAuxBlockHandler getAuxBlockHandler;

	@Test
	public void shouldReturnAuxBlockDataOnEmptyParams() {
		// given
		JSONRPC2Request request = new JSONRPC2Request("getauxblock", 1);

		// when
		JSONRPC2Response response = getAuxBlockHandler.process(request, null);

		// then
		JsonObject json = convertJsonResponseToJsonObject(response);
		// assertTrue();
	}

	@Test
	public void shouldHandleAuxBlockData() {
		// given
		List<Object> list = Lists.newArrayList();
		list.add(Sha256Hash.ZERO_HASH.toString());
		list.add(AUX_POW);
		JSONRPC2Request request = new JSONRPC2Request("getauxblock", list, 1);
		// when
		
		JSONRPC2Response response = getAuxBlockHandler.process(request, null);
		
		// then
		
	}
	private JsonObject convertJsonResponseToJsonObject(JSONRPC2Response response){
		String jsonString = response.toJSONString();
		JsonObject json = new JsonParser().parse(jsonString).getAsJsonObject();
		return json;
	}
}
