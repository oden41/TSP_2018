package tour;

import data.CityData;
import data.Param;

public class Tour {
	CityData data;
	int[] cityArray;
	int[] cityIndexArray;
	int cityNum;
	int tourLength;
	//ツアー距離が更新されているか
	boolean updated;

	public Tour(int cityNum, CityData data) {
		this.cityNum = cityNum;
		cityArray = new int[cityNum];
		cityIndexArray = new int[cityNum];
		tourLength = Integer.MAX_VALUE;
		this.data = data;
	}

	public void initializeTour() {
		for (int i = 0; i < cityNum; i++) {
			cityArray[i] = i;
			cityIndexArray[i] = i;
		}

		//shuffle
		for (int i = 0; i < cityNum; i++) {
			// i - cityNumまでの間とiをswapする
			int index = Param.rand.nextInt(cityNum - i) + i;

			int temp = cityArray[i];
			int temp2 = cityArray[index];
			cityArray[i] = temp2;
			cityArray[index] = temp;

			int tempIndex = cityIndexArray[temp];
			cityIndexArray[temp] = cityIndexArray[temp2];
			cityIndexArray[temp2] = tempIndex;
		}

		// tourの距離を計算する
		tourLength = calcTour();
	}

	int calcTour() {
		int dist = 0;
		for (int i = 0; i < cityArray.length; i++) {
			double dx;
			double dy;
			if (i != cityNum - 1) {
				dx = data.getData(cityArray[i + 1]).getKey() - data.getData(cityArray[i]).getKey();
				dy = data.getData(cityArray[i + 1]).getValue() - data.getData(cityArray[i]).getValue();
			} else {
				dx = data.getData(cityArray[i]).getKey() - data.getData(cityArray[0]).getKey();
				dy = data.getData(cityArray[i]).getValue() - data.getData(cityArray[0]).getValue();
			}
			dist += (int) (Math.sqrt(dx * dx + dy * dy) + 0.5);
		}
		updated = true;
		return dist;
	}

	/**
	 * @param cityID 都市ID
	 * @return 次の都市ID
	 */
	public int next(int cityID) {
		int nextIndex = cityIndexArray[cityID] + 1;
		return nextIndex >= cityNum ? cityArray[0] : cityArray[nextIndex];
	}

	/**
	 * @param cityID 都市ID
	 * @return 前の都市ID
	 */
	public int prev(int cityID) {
		int prevIndex = cityIndexArray[cityID] - 1;
		return prevIndex < 0 ? cityArray[cityNum - 1] : cityArray[prevIndex];
	}

	public int getTourLength() {
		if (updated)
			return tourLength;
		else {
			tourLength = calcTour();
			return tourLength;
		}
	}

	public void flipTour(int cityA, int cityB, int cityC, int cityD) {

		updated = false;
	}
}
