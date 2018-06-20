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
		Param.rand = new Random(5);
	}

	// @Test
	public void testInitializeTour() {
		tour.initializeTour();
		System.out.println(tour);
	}

	// @Test
	public void testcalcTour() {
		tour.initializeTour();
		tour.segmentOrt[1] = true;
		tour.segmentOrt[2] = true;
		System.out.println(tour);
		System.out.println(tour.calcTour());
	}

	// @Test
	public void testGetIndexFromStart() {
		tour.initializeTour();
		tour.segmentOrt[1] = true;
		tour.segmentOrt[0] = true;
		System.out.println(tour);
		for (int i = 0; i < tour.cityNum; i++) {
			System.out.println(i + " : " + tour.getIndexFromStart(i));
		}
	}

	// @Test
	public void testIsTerminal() {
		tour.initializeTour();
		tour.segmentOrt[1] = true;
		tour.segmentOrt[0] = true;
		System.out.println(tour);
		for (int i = 0; i < tour.cityNum; i++) {
			System.out.println(i + ", head : " + tour.isTerminal(i, true));
			System.out.println(i + ", tail : " + tour.isTerminal(i, false));
		}
	}

	// @Test
	public void testNext() {
		for (int i = 0; i < 5; i++) {
			tour.initializeTour();
			System.out.println(tour);
			for (int j = 0; j < 10; j++) {
				int cityID = Param.rand.nextInt(tour.cityNum);
				System.out.println(cityID + " 's next : " + tour.next(cityID));
			}
			System.out.println("--");
		}
	}

	/**
	 * ortが混ざっても正しく動作するかの確認
	 */
	// @Test
	public void testNext2() {
		for (int i = 0; i < 5; i++) {
			tour.initializeTour();
			tour.segmentOrt[1] = true;
			tour.segmentOrt[2] = true;
			System.out.println(tour);
			for (int j = 0; j < tour.cityNum; j++) {
				System.out.println(j + " 's next : " + tour.next(j));
			}
			System.out.println("--");
		}
	}

	// @Test
	public void testPrev() {
		for (int i = 0; i < 5; i++) {
			tour.initializeTour();
			System.out.println(tour);
			for (int j = 0; j < tour.cityNum; j++) {
				System.out.println(j + " 's prev : " + tour.prev(j));
			}
			System.out.println("--");
		}
	}

	/**
	 * ortが混ざっても正しく動作するかの確認
	 */
	// @Test
	public void testPrev2() {
		for (int i = 0; i < 5; i++) {
			tour.initializeTour();
			tour.segmentOrt[1] = true;
			tour.segmentOrt[2] = true;
			System.out.println(tour);
			for (int j = 0; j < tour.cityNum; j++) {
				System.out.println(j + " 's prev : " + tour.prev(j));
			}
			System.out.println("--");
		}
	}

	@Test
	public void testFilpSplit() {
		tour.initializeTour();
		tour.segmentOrt[1] = true;
		tour.segmentOrt[2] = true;
		System.out.println(tour);
		int cityA = Param.rand.nextInt(tour.cityNum);
		int cityC = Param.rand.nextInt(tour.cityNum);
		System.out.println("cityA: " + cityA + ", cityC :" + cityC);
		tour.flipTour(cityA, tour.next(cityA), cityC, tour.next(cityC));
	}
}
