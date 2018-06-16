package localSearch;

import java.util.ArrayList;
import java.util.HashMap;

import data.CandidateList;
import data.CityData;
import tour.Tour;

public class TwoOpt {
	int cityNum;
	int k;
	Tour tour;
	CandidateList candidateList;
	CityData data;
	int[][] neighborList;
	HashMap<Integer, ArrayList<Integer>> inverseNL;

	public TwoOpt(int cityNum, int k, CityData data, int[][] neighborList, HashMap<Integer, ArrayList<Integer>> invNL) {
		this.cityNum = cityNum;
		this.k = k;
		this.data = data;
		tour = new Tour(cityNum, data);
		this.neighborList = neighborList;
		this.inverseNL = invNL;
	}

	public void doOneTrial() {
		candidateList = new CandidateList(cityNum);
		tour.initializeTour();

		while (candidateList.getSize() != 0) {
			ITR:
			{
				int cityA = candidateList.get();
				int cityB;
				for (int ort = 0; ort < 1; ort++) {
					if (ort == 0)
						cityB = tour.next(cityA);
					else
						cityB = tour.prev(cityA);

					for (int i = 0; i < k; i++) {
						int cityC = neighborList[cityA][i];
						int cityD;
						if (ort == 0)
							cityD = tour.next(cityC);
						else
							cityD = tour.prev(cityC);

						if (data.calcDistance(cityA, cityB) <= data.calcDistance(cityA, cityC))
							break;

						int diff = (data.calcDistance(cityA, cityC) + data.calcDistance(cityB, cityD)) - (data.calcDistance(cityA, cityB) + data.calcDistance(cityC, cityD));
						if (diff < 0) {
							if (ort == 0)
								tour.flipTour(cityA, cityB, cityC, cityD);
							else
								tour.flipTour(cityB, cityA, cityD, cityC);

							candidateList.add(cityB);
							candidateList.add(cityC);
							candidateList.add(cityD);

							tour.addLength(diff);

							for (Integer city : inverseNL.get(cityA)) {
								candidateList.add(city);
							}
							for (Integer city : inverseNL.get(cityB)) {
								candidateList.add(city);
							}
							for (Integer city : inverseNL.get(cityC)) {
								candidateList.add(city);
							}
							for (Integer city : inverseNL.get(cityD)) {
								candidateList.add(city);
							}

							break ITR;

						}
					}
				}
				candidateList.remove(cityA);
			}
		}
	}

	public int getLength() {
		return tour.getTourLength();
	}
}
