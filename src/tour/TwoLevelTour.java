package tour;

import data.CityData;

public class TwoLevelTour extends Tour {
	int segNum;
	int[][] segmentTour;
	// []:segmentID, F:0から
	boolean[] segmentOrt;
	int[] segmentIDFromCityID;
	int[] positionInSegmentFromCityID;
	int[] segmentIDFromIndex;
	int[] segmentIndexFromID;

	public TwoLevelTour(int cityNum, CityData data) {
		super(cityNum, data);
		segNum = (int) Math.sqrt(cityNum);
		segmentTour = new int[segNum][];
		segmentTour[0] = new int[cityNum - segNum * (segNum - 1)];
		for (int i = 1; i < segNum; i++) {
			segmentTour[i] = new int[segNum];
		}
		segmentOrt = new boolean[segNum];
		segmentIDFromCityID = new int[cityNum];
		positionInSegmentFromCityID = new int[cityNum];
		segmentIDFromIndex = new int[segNum];
		segmentIndexFromID = new int[segNum];
	}

	@Override
	public void initializeTour() {
		// TODO 自動生成されたメソッド・スタブ
		super.initializeTour();
	}

	@Override
	public int prev(int cityID) {
		// TODO 自動生成されたメソッド・スタブ
		return super.prev(cityID);
	}

	@Override
	public int next(int cityID) {
		// TODO 自動生成されたメソッド・スタブ
		return super.next(cityID);
	}

	@Override
	int calcTour() {
		// TODO 自動生成されたメソッド・スタブ
		return super.calcTour();
	}

	@Override
	public void flipTour(int cityA, int cityB, int cityC, int cityD) {
		// TODO 自動生成されたメソッド・スタブ
		super.flipTour(cityA, cityB, cityC, cityD);
	}
}
