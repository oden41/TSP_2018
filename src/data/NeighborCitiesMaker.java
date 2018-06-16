package data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javafx.util.Pair;

public class NeighborCitiesMaker {
	CityData data;

	public NeighborCitiesMaker(String filePath) {
		// TODO 自動生成されたコンストラクター・スタブ
		data = new CityData(filePath);
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
		List<String> list = Arrays.stream(result).map(line -> Arrays.stream(line).mapToObj(String::valueOf).collect(Collectors.joining(","))).collect(Collectors.toList());
		try {
			Files.write(Paths.get(System.getProperty("user.dir"), "\\" + fileName), list, StandardOpenOption.CREATE);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
