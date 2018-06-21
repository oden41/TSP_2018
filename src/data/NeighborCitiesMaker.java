package data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.util.Pair;

public class NeighborCitiesMaker {
	CityData data;

	public NeighborCitiesMaker(CityData data) {
		// TODO 自動生成されたコンストラクター・スタブ
		this.data = data;
	}

	public int[][] makeNeighborCityListFromPath(int cityNum, int k, String path) {
		int[][] result = new int[cityNum][k];

		try (Stream<String> stream = Files.lines(Paths.get(path))) {
			// read each line
			result = stream.map(line -> line.split(","))
					.map(line -> Arrays.stream(line).mapToInt(Integer::parseInt).toArray()).toArray(int[][]::new);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

	public int[][] makeNeighborCityList(int k) {
		int[][] result = new int[data.getCityNum()][k];

		for (int i = 0; i < data.getCityNum(); i++) {
			ArrayList<Pair<Integer, Integer>> list = new ArrayList<>();
			for (int j = 0; j < data.getCityNum(); j++) {
				if (i == j)
					continue;
				list.add(new Pair<Integer, Integer>(j, calcDistance(i, j)));
			}
			list.sort((a, b) -> a.getValue() - b.getValue());
			for (int j = 0; j < k; j++) {
				result[i][j] = list.get(j).getKey();
			}
		}

		return result;
	}

	private int calcDistance(int idA, int idB) {
		Pair<Double, Double> cityA = data.getData(idA);
		Pair<Double, Double> cityB = data.getData(idB);

		double dx = cityA.getKey() - cityB.getKey();
		double dy = cityA.getValue() - cityB.getValue();

		return (int) (Math.sqrt(dx * dx + dy * dy) + 0.5);
	}

	/**
	 * 生成した近傍リストをファイルに書き込む
	 *
	 * @param result
	 * @param string
	 */
	public void writeToFile(int[][] result, String fileName) {
		List<String> list = Arrays.stream(result)
				.map(line -> Arrays.stream(line).mapToObj(String::valueOf).collect(Collectors.joining(",")))
				.collect(Collectors.toList());
		try {
			Files.write(Paths.get(System.getProperty("user.dir"), "\\" + fileName), list, StandardOpenOption.CREATE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public HashMap<Integer, ArrayList<Integer>> makeInverseNeighborCityList(int[][] neighborList) {
		HashMap<Integer, ArrayList<Integer>> result = new HashMap<>();
		int cityNum = neighborList.length;
		int k = neighborList[0].length;

		for (int i = 0; i < cityNum; i++) {
			ArrayList<Integer> list = new ArrayList<>();
			for (int j = 0; j < cityNum; j++) {
				if (i == j)
					continue;
				for (int j2 = 0; j2 < k; j2++) {
					if (neighborList[j][j2] == i)
						list.add(j);
				}

			}
			result.put(i, list);
		}

		return result;
	}
}
