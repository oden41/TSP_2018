package tour;

import data.CityData;
import data.Param;

public class Tour {
	CityData data;
	int[] cityArray;
	int[] cityIndexArray;
	int cityNum;
	int tourLength;
	// ツアー距離が更新されているか
	boolean updated;

	public Tour(int cityNum, CityData data) {
		this.cityNum = cityNum;
		cityArray = new int[cityNum];
		cityIndexArray = new int[cityNum];
		tourLength = Integer.MAX_VALUE;
		this.data = data;
	}

	/**
	 * 経路を初期化し、経路長を計算する
	 *
	 */
	public void initializeTour() {
		for (int i = 0; i < cityNum; i++) {
			cityArray[i] = i;
			cityIndexArray[i] = i;
		}

		// shuffle
		for (int i = 0; i < cityNum; i++) {
			// i - cityNumまでの間とiをswapする
			int index = Param.rand.nextInt(cityNum - i) + i;
			swapIndex(cityArray[i], cityArray[index]);
			swapCityID(i, index);
		}

		// tourの距離を計算する
		tourLength = calcTour();
	}

	/**
	 * @param A
	 *                インデックス
	 * @param B
	 *                インデックス
	 */
	void swapCityID(int A, int B) {
		int temp = cityArray[A];
		cityArray[A] = cityArray[B];
		cityArray[B] = temp;
	}

	/**
	 * 都市IDインデックス配列をswap
	 *
	 * @param A
	 *                都市ID
	 * @param B
	 *                都市ID
	 */
	void swapIndex(int A, int B) {
		int tempIndex = cityIndexArray[A];
		cityIndexArray[A] = cityIndexArray[B];
		cityIndexArray[B] = tempIndex;
	}

	/**
	 * 経路長を計算する
	 *
	 * @return 経路長
	 */
	int calcTour() {
		int dist = 0;
		for (int i = 0; i < cityArray.length; i++) {
			double dx;
			double dy;
			if (i != cityNum - 1) {
				dx = data.getData(cityArray[i + 1]).getKey() - data.getData(cityArray[i]).getKey();
				dy = data.getData(cityArray[i + 1]).getValue() - data.getData(cityArray[i]).getValue();
			}
			else {
				dx = data.getData(cityArray[i]).getKey() - data.getData(cityArray[0]).getKey();
				dy = data.getData(cityArray[i]).getValue() - data.getData(cityArray[0]).getValue();
			}
			dist += (int) (Math.sqrt(dx * dx + dy * dy) + 0.5);
		}
		updated = true;
		return dist;
	}

	/**
	 * @param cityID
	 *                都市ID
	 * @return 次の都市ID
	 */
	public int next(int cityID) {
		int nextIndex = cityIndexArray[cityID] + 1;
		return nextIndex >= cityNum ? cityArray[0] : cityArray[nextIndex];
	}

	/**
	 * @param cityID
	 *                都市ID
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

	/**
	 * 都市A/B間と都市C/D間を入れ替える
	 *
	 * @param cityA
	 *                都市ID_A
	 * @param cityB
	 *                都市ID_B
	 * @param cityC
	 *                都市ID_C
	 * @param cityD
	 *                都市ID_D
	 */
	public void flipTour(int cityA, int cityB, int cityC, int cityD) {
		int head, tail;

		int indexA = cityIndexArray[cityA];
		int indexB = cityIndexArray[cityB];
		int indexC = cityIndexArray[cityC];
		int indexD = cityIndexArray[cityD];

		int ad = indexA - indexD < 0 ? indexA - indexD + cityNum : indexA - indexD;
		int bc = indexC - indexB < 0 ? indexC - indexB + cityNum : indexC - indexB;

		if (ad < bc) {
			head = indexD;
			tail = indexA;
		}
		else {
			head = indexB;
			tail = indexC;
		}

		int count = 0;
		int flipT = ((tail - head < 0 ? tail - head + cityNum : tail - head) + 1) / 2;
		while (count < flipT) {
			swapIndex(cityArray[head], cityArray[tail]);
			swapCityID(head, tail);

			head++;
			tail--;

			if (head >= cityNum)
				head = 0;
			if (tail < 0)
				tail = cityNum - 1;

			count++;
		}

		updated = false;
	}

	public void addLength(int diff) {
		tourLength += diff;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < cityNum; i++) {
			sb.append(cityArray[i] + " ");
		}
		return sb.toString();
	}

}
