package tour;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import data.CityData;
import data.Param;

public class TwoLevelTourTest {
	private static CityData data;
	private static TwoLevelTour tour;

	@Before
	public void init() {
		String dir = System.getProperty("user.dir");
		// data = new CityData(dir + "\\tsp_data\\ca4663.tsp");
		data = new CityData(dir + "\\tsp_data\\test.tsp");
		tour = new TwoLevelTour(data.getCityNum(), data);
		Param.rand = new Random();
	}

	@Test
	public void testInitializeTour() {
		tour.initializeTour();
		System.out.println(tour);
	}
}
