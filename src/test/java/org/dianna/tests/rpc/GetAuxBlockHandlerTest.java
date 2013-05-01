package org.dianna.tests.rpc;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.dianna.core.DiannaAuxBlockHandler;
import org.dianna.core.entity.AuxBlock;
import org.dianna.core.entity.AuxData;
import org.dianna.rpc.handlers.GetAuxBlockHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.bitcoin.core.Sha256Hash;
import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;

@RunWith(MockitoJUnitRunner.class)
public class GetAuxBlockHandlerTest {

	private static final String ZERO_HASH = Sha256Hash.ZERO_HASH.toString();
	private static final int CHAIN_ID = 1;

	private static final String AUX_POW = "010000000100000000000000000000000000000000000000"
			+ "00000000000000000000000000ffffffff3702ea232cfabe6d6d0d406ebce426a972f34abd5f40"
			+ "94298c8eb0d4feebf9d3ef5d573331ac173f7b0200000000000000062f503253482fffffffff03"
			+ "40787d01000000001976a914b29175a47c791580f29a5296c049e2609db0b0ac88acc079882801"
			+ "000000434104ffd03de44a6e11b9917f3a29f9443283d9871c9d743ef30d5eddcd37094b64d1b3"
			+ "d8090496b53256786bf5c82932ec23c3b74d9f05a6f95a8b5529352656664bac00000000000000"
			+ "0025243271a79e85bf1849e48cf73d96d0c1cfcf5549010de71189f4aa9a80d37c137a00000000"
			+ "00000000443391e4867dc14bf041fe18e72d3b97191d78025ff6adefb36fb1f7d8590000000000"
			+ "000001db05e624c994c7d5956d6be74433fac8efb618491233523f843f3dc627b6fa8e00000000"
			+ "0200000073f435472aba1eb2eca4cf63ab278e643df7ff62b798e0238629c02ddf710000c16304"
			+ "2ea51470adab4c97ad6644da3de80ce65f9d7a8d6341efc6c44170b5f173d27f51ffff031f0000209e";

	private static final Sha256Hash BLOCK_HASH = new Sha256Hash(
			"5b4ad06057a22806ac1a7e61044abdab7a30ffef63a63032efa7de6a4dbead7e");

	private static final Sha256Hash PARENT_HASH = new Sha256Hash(
			"000059d8f7b16fb3efadf65f02781d19973b2de718fe41f04bc17d86e4913344");

	private static final List<Sha256Hash> merkleBranch = Lists.newArrayList(new Sha256Hash(
			"8efab627c63d3f843f5233124918b6efc8fa3344e76b6d95d5c794c924e605db"));

	@InjectMocks
	private GetAuxBlockHandler getAuxBlockHandler;

	@Mock
	private DiannaAuxBlockHandler handler;

	@Test
	public void shouldReturnAuxBlockDataOnEmptyParams() {
		// given
		JSONRPC2Request request = new JSONRPC2Request("getauxblock", 1);
		AuxBlock auxBlock = new AuxBlock();
		auxBlock.setChainId(1);
		auxBlock.setHash(ZERO_HASH);
		auxBlock.setTarget(ZERO_HASH);
		given(handler.getAuxBlock()).willReturn(auxBlock);

		// when
		JSONRPC2Response response = getAuxBlockHandler.process(request, null);

		// then
		JsonObject json = convertResultToJsonObject(response).getAsJsonObject();
		assertEquals(CHAIN_ID, json.get("chainid").getAsInt());
		assertEquals(ZERO_HASH, json.get("hash").getAsString());
		assertEquals(ZERO_HASH, json.get("target").getAsString());
	}

	@Test
	public void shouldHandleAuxBlockData() {
		// given
		List<Object> list = Lists.newArrayList();
		list.add(BLOCK_HASH.toString());
		list.add(AUX_POW);
		JSONRPC2Request request = new JSONRPC2Request("getauxblock", list, 1);
		// when

		JSONRPC2Response response = getAuxBlockHandler.process(request, null);
		// then
		boolean result = convertResultToJsonObject(response).getAsBoolean();
		assertEquals(true, result);
		verify(handler, times(1)).postAuxData(eq(BLOCK_HASH), auxDataContains(PARENT_HASH, merkleBranch, 0));

	}

	private AuxData auxDataContains(final Sha256Hash parentHash, final List<Sha256Hash> merkleBranch,
			final int coinBaseIndex) {
		return argThat(new ArgumentMatcher<AuxData>() {
			@Override
			public boolean matches(Object argument) {
				AuxData auxData = (AuxData) argument;
				if (auxData.getCoinbaseTxIndex() != coinBaseIndex) {
					return false;
				}

				if (!auxData.getParentBlockHash().equals(parentHash)) {
					return false;
				}

				if (!auxData.getAuxMerkleBranch().equals(merkleBranch)) {
					return false;
				}
				return true;
			}

		});
	}

	private JsonElement convertResultToJsonObject(JSONRPC2Response response) {
		String jsonString = response.toJSONString();
		JsonObject json = new JsonParser().parse(jsonString).getAsJsonObject();
		return json.get("result");
	}
}
