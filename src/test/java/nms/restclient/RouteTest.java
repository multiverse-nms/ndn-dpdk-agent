package nms.restclient;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Base64;

import org.junit.jupiter.api.Test;

import io.vertx.core.json.JsonObject;

class RouteTest {

	@Test
	void test() {
		String expected = "/Emid/25042=P3/.../..../%1C%9F";	
		byte[] input1 = new byte[] {(byte)0x08,(byte)0x04,(byte)0x45,(byte)0x6D,(byte)0x69,(byte)0x64, (byte)0xFD,(byte)0x61,(byte)0xD2,(byte)0x02,(byte)0x50,(byte)0x33, (byte)0x08,(byte)0x00, (byte)0x08,(byte)0x01,(byte)0x2E, (byte)0x08,(byte)0x02,(byte)0x1C,(byte)0x9F };
		String encodedString = Base64.getEncoder().encodeToString(input1);
				
		Route route = new Route(new JsonObject()
				.put("prefix", encodedString)
				.put("faceId", 3000)
				.put("cost" , 0)
				.put("origin" , 1));
		
		assertEquals(expected, route.getPrefix());	
		
		 
	}
	
	
	@Test
	void test2() {

		Route route1 = new Route(new JsonObject()
				.put("prefix", "ab")
				.put("faceId", 3000)
				.put("cost" , 0)
				.put("origin" , 1));
		
		Route route2 = new Route(new JsonObject()
				.put("prefix", "ab")
				.put("faceId", 3000)
				.put("cost" , 0)
				.put("origin" , 1));
		
		assertEquals(true, route1.equals(route2));
	}

}
