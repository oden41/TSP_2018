package data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

import org.junit.Test;

public class NeighborCitiesMakerTest {

	// @Test
	public void test() {
		String dir = System.getProperty("user.dir");
		NeighborCitiesMaker maker = new NeighborCitiesMaker(new CityData(dir + "\\tsp_data\\mona-lisa100K.tsp"));
		int[][] result = maker.makeNeighborCityList(50);
		maker.writeToFile(result, "mona-lisa100K.csv");
	}

	@Test
	public void testMakeInvNeighbor() {
		String dir = System.getProperty("user.dir");
		NeighborCitiesMaker maker = new NeighborCitiesMaker(new CityData(dir + "\\tsp_data\\ch71009.tsp"));
		int[][] result = maker.makeNeighborCityList(50);
		HashMap<Integer, ArrayList<Integer>> invNL = maker.makeInverseNeighborCityList(result);
		invNL.entrySet().forEach(e -> {
			try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(new File(dir + "\\tsp_data\\inv_neighbor_ch71009.csv"), true)))) {
				pw.println(e.getValue().stream().map(String::valueOf).collect(Collectors.joining(",")));
			}
			catch (IOException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
			}
		});
	}

}
