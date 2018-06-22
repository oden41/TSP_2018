package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import data.CityData;
import data.NeighborCitiesMaker;
import data.Param;
import localSearch.TwoOpt;

public class Main {

	static int[] cityNums = { 4663, 9847, 10639, 33708, 71009, 100000 };
	static String[] paths = { "\\tsp_data\\ca4663.tsp", "\\tsp_data\\ja9847.tsp", "\\tsp_data\\fi10639.tsp", "\\tsp_data\\bm33708.tsp", "\\tsp_data\\ch71009.tsp", "\\tsp_data\\mona-lisa100K.tsp" };
	static String[] neighborPaths = { "\\tsp_data\\neighbor_ca.csv", "\\tsp_data\\neighbor_ja.csv", "\\tsp_data\\neighbor_fi.csv", "\\tsp_data\\neighbor_bm.csv", "\\tsp_data\\neighbor_ch.csv", "\\tsp_data\\neighbor_mona.csv" };
	static String[] invNeighborPaths = { "\\tsp_data\\inv_neighbor_ca.csv", "\\tsp_data\\inv_neighbor_ja.csv", "\\tsp_data\\inv_neighbor_fi.csv", "\\tsp_data\\inv_neighbor_bm.csv", "\\tsp_data\\inv_neighbor_ch.csv", "\\tsp_data\\inv_neighbor_mona.csv" };

	public static void main(String[] args) {
		int id = 5;
		String dir = System.getProperty("user.dir");

		Param.rand = new Random();
		int k = 50;
		CityData data = new CityData(dir + paths[id]);
		NeighborCitiesMaker maker = new NeighborCitiesMaker(data);
		int[][] neighborList = maker.makeNeighborCityListFromPath(cityNums[id], k, dir + neighborPaths[id]);
		// HashMap<Integer, ArrayList<Integer>> invNL =
		// maker.makeInverseNeighborCityList(neighborList);
		HashMap<Integer, ArrayList<Integer>> invNL = maker.makeInverseNeighborCityListFromPath(dir + invNeighborPaths[id]);
		TwoOpt opt = new TwoOpt(cityNums[id], k, data, neighborList, invNL);
		opt.doOneTrial();

		ArrayList<Long> trialTimeList = new ArrayList<>();
		ArrayList<Integer> pathLengthList = new ArrayList<>();
		for (int i = 0; i < 11; i++) {
			long start = System.currentTimeMillis();
			opt.doOneTrial();
			long end = System.currentTimeMillis();
			trialTimeList.add(end - start);
			pathLengthList.add(opt.getLength());
		}

		double avePathLength = pathLengthList.stream().mapToInt(e -> e).average().getAsDouble();
		double stdPathLength = sd(pathLengthList, avePathLength);
		double aveTrialTime = trialTimeList.stream().mapToLong(e -> e).average().getAsDouble();
		double stdTrialTime = sd(trialTimeList, aveTrialTime);
		System.out.println("avePathLength: " + avePathLength + "(" + stdPathLength + ")");
		System.out.println("aveTime: " + aveTrialTime + "(" + stdTrialTime + ")");
	}

	static double sd(ArrayList<Integer> a, double ave) {
		int sum = 0;
		double mean = ave;

		for (Integer i : a)
			sum += Math.pow((i - mean), 2);
		return Math.sqrt(sum / (a.size() - 1)); // sample
	}

	static double sd(List<Long> a, double ave) {
		int sum = 0;
		double mean = ave;

		for (Long i : a)
			sum += Math.pow((i - mean), 2);
		return Math.sqrt(sum / (a.size() - 1)); // sample
	}

}
