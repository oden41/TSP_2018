package tour;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import data.CityData;
import data.Param;

public class TourTest {
	private static CityData data;
	private static Tour tour;

	@BeforeAll
	public static void init() {
		String dir = System.getProperty("user.dir");
		//data = new CityData(dir + "\\tsp_data\\ca4663.tsp");
		data = new CityData(dir + "\\tsp_data\\test.tsp");
		tour = new Tour(data.getCityNum(), data);
		Param.rand = new Random();
	}

	@Test
	void testInitializeTour() {
		tour.initializeTour();
		for (int i = 0; i < Math.min(10, data.getCityNum()); i++) {
			System.out.println(tour.cityArray[i] + "," + tour.cityIndexArray[i]);
		}
	}

	@Test
	void testNext() {
		tour.initializeTour();
		for (int i = 0; i < data.getCityNum(); i++) {
			if (i != data.getCityNum() - 1)
				assertEquals(tour.cityArray[i + 1], tour.next(tour.cityArray[i]));
			else
				assertEquals(tour.cityArray[0], tour.next(tour.cityArray[i]));
		}
	}

	@Test
	void testPrev() {
		tour.initializeTour();
		for (int i = 0; i < data.getCityNum(); i++) {
			if (i != 0)
				assertEquals(tour.cityArray[i - 1], tour.prev(tour.cityArray[i]));
			else
				assertEquals(tour.cityArray[data.getCityNum() - 1], tour.prev(tour.cityArray[i]));
		}
	}

	@Test
	void testFlipTour() {
		fail("まだ実装されていません");
	}

}
