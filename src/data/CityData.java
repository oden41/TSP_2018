package data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import javafx.util.Pair;

public class CityData {
	HashMap<Integer, Pair<Double, Double>> data;
	int cityNum;

	public CityData(String filePath) {
		data = new HashMap<>();

		try (BufferedReader br = new BufferedReader(new FileReader(filePath));) {
			String str = br.readLine();
			cityNum = Integer.parseInt(str);
			while ((str = br.readLine()) != null) {
				String[] split = str.split(" ");
				//1-originのため -1して0-originに
				data.put(Integer.parseInt(split[0]) - 1,
						new Pair<>(Double.parseDouble(split[1]), Double.parseDouble(split[2])));
			}
		} catch (FileNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	public int getCityNum() {
		return cityNum;
	}

	public Pair<Double, Double> getData(int cityID) {
		return data.get(cityID);
	}
}
