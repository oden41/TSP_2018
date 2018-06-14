package data;

import static org.junit.Assert.*;

import org.junit.jupiter.api.Test;

class CityDataTest {

	@Test
	void testCityData() {
		String dir = System.getProperty("user.dir");
		CityData data = new CityData(dir + "\\tsp_data\\ca4663.tsp");

		assertEquals(4663, data.getCityNum());
		assertEquals(41800.0000, data.getData(0).getKey(), 1e-5);
		assertEquals(82650.0000, data.getData(0).getValue(), 1e-5);
	}

}
