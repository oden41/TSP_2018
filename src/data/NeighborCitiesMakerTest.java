package data;

import org.junit.Test;

public class NeighborCitiesMakerTest {

	@Test
	public void test() {
		String dir = System.getProperty("user.dir");
		NeighborCitiesMaker maker = new NeighborCitiesMaker(new CityData(dir + "\\tsp_data\\mona-lisa100K.tsp"));
		int[][] result = maker.makeNeighborCityList(50);
		maker.writeToFile(result, "mona-lisa100K.csv");
	}

}
