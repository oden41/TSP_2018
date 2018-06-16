package tour;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import data.CityData;
import data.Param;

public class TourTest {
	private static CityData data;
	private static Tour tour;

	@Before
	public void init() {
		String dir = System.getProperty("user.dir");
		// data = new CityData(dir + "\\tsp_data\\ca4663.tsp");
		data = new CityData(dir + "\\tsp_data\\test.tsp");
		tour = new Tour(data.getCityNum(), data);
		Param.rand = new Random();
	}

	// @Test
	public void testInitializeTour() {
		tour.initializeTour();
		for (int i = 0; i < Math.min(10, data.getCityNum()); i++) {
			System.out.println(tour.cityArray[i] + "," + tour.cityIndexArray[i]);
		}
	}

	@Test
	public void testNext() {
		tour.initializeTour();
		for (int i = 0; i < data.getCityNum(); i++) {
			if (i != data.getCityNum() - 1)
				assertEquals(tour.cityArray[i + 1], tour.next(tour.cityArray[i]));
			else
				assertEquals(tour.cityArray[0], tour.next(tour.cityArray[i]));
		}
	}

	@Test
	public void testPrev() {
		tour.initializeTour();
		for (int i = 0; i < data.getCityNum(); i++) {
			if (i != 0)
				assertEquals(tour.cityArray[i - 1], tour.prev(tour.cityArray[i]));
			else
				assertEquals(tour.cityArray[data.getCityNum() - 1], tour.prev(tour.cityArray[i]));
		}
	}

	@Test
	public void testFlipTour() {
		for (int i = 0; i < 10; i++) {
			tour.initializeTour();
			System.out.println(tour);

			int indexA = Param.rand.nextInt(tour.cityNum);
			int indexB = indexA + 1 >= tour.cityNum ? 0 : indexA + 1;
			int indexC = 9;
			int indexD = indexC + 1 >= tour.cityNum ? 0 : indexC + 1;
			System.out.println("A: " + indexA + ", B: " + indexB + ", C: " + indexC + ", D: " + indexD);
			tour.flipTour(tour.cityArray[indexA], tour.cityArray[indexB], tour.cityArray[indexC], tour.cityArray[indexD]);
			System.out.println(tour);
			System.out.println("---");
		}
	}

}
