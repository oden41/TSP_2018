package tour;

import data.CityData;
import data.Param;

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
		// ランダムに並んだ配列が欲しいため
		super.initializeTour();

		int segIndex = 0;
		int segPos = 0;
		for (int i = 0; i < cityNum; i++) {
			segmentTour[segIndex][segPos] = cityArray[i];
			positionInSegmentFromCityID[cityArray[i]] = segPos;
			segmentIDFromCityID[cityArray[i]] = segIndex;
			if (segPos == segmentTour[segIndex].length - 1) {
				segmentIDFromIndex[segIndex] = segIndex;
				segmentIndexFromID[segIndex] = segIndex;
				segmentOrt[segIndex] = false;
				segIndex++;
				segPos = 0;
			}
			else
				segPos++;
		}

		for (int i = 0; i < segNum; i++) {
			int index = Param.rand.nextInt(segNum - i) + i;
			swapSegmentIndex(segmentIDFromIndex[i], segmentIDFromIndex[index]);
			swapSegmentID(i, index);
		}

		tourLength = calcTour();
	}

	/**
	 * segmentIndexをスワップする
	 *
	 * @param i
	 * @param index
	 */
	void swapSegmentIndex(int i, int j) {
		int tempIndex = segmentIndexFromID[i];
		segmentIndexFromID[i] = segmentIndexFromID[j];
		segmentIndexFromID[j] = tempIndex;
	}

	/**
	 * segmentIDをスワップする
	 *
	 * @param i
	 * @param index
	 */
	void swapSegmentID(int i, int index) {
		int tempID = segmentIDFromIndex[i];
		segmentIDFromIndex[i] = segmentIDFromIndex[index];
		segmentIDFromIndex[index] = tempID;
	}

	@Override
	public int prev(int cityID) {
		int currentSegID = segmentIDFromCityID[cityID];
		if (segmentOrt[currentSegID]) {
			// 逆方向
			if (positionInSegmentFromCityID[cityID] == segmentTour[currentSegID].length - 1) {
				int prevSegID = segmentIDFromIndex[segmentIndexFromID[currentSegID] == 0 ? segmentTour.length - 1 : segmentIndexFromID[currentSegID] - 1];
				return segmentTour[prevSegID][segmentOrt[prevSegID] ? 0 : segmentTour[prevSegID].length - 1];
			}
			else {
				return segmentTour[currentSegID][positionInSegmentFromCityID[cityID] + 1];
			}
		}
		else {
			// 順方向
			if (positionInSegmentFromCityID[cityID] == 0) {
				int prevSegID = segmentIDFromIndex[segmentIndexFromID[currentSegID] == 0 ? segmentTour.length - 1 : segmentIndexFromID[currentSegID] - 1];
				return segmentTour[prevSegID][segmentOrt[prevSegID] ? 0 : segmentTour[prevSegID].length - 1];
			}
			else {
				return segmentTour[currentSegID][positionInSegmentFromCityID[cityID] - 1];
			}
		}
	}

	@Override
	public int next(int cityID) {
		int currentSegID = segmentIDFromCityID[cityID];
		if (segmentOrt[currentSegID]) {
			// 逆方向
			if (positionInSegmentFromCityID[cityID] == 0) {
				int nextSegID = segmentIDFromIndex[segmentIndexFromID[currentSegID] == segmentTour.length - 1 ? 0 : segmentIndexFromID[currentSegID] + 1];
				return segmentTour[nextSegID][segmentOrt[nextSegID] ? segmentTour[nextSegID].length - 1 : 0];
			}
			else {
				return segmentTour[currentSegID][positionInSegmentFromCityID[cityID] - 1];
			}
		}
		else {
			// 順方向
			if (positionInSegmentFromCityID[cityID] == segmentTour[currentSegID].length - 1) {
				int nextSegID = segmentIDFromIndex[segmentIndexFromID[currentSegID] == segmentTour.length - 1 ? 0 : segmentIndexFromID[currentSegID] + 1];
				return segmentTour[nextSegID][segmentOrt[nextSegID] ? segmentTour[nextSegID].length - 1 : 0];
			}
			else {
				return segmentTour[currentSegID][positionInSegmentFromCityID[cityID] + 1];
			}
		}
	}

	@Override
	int calcTour() {
		int dist = 0;
		int prevCity = 0;
		int currentCity = segmentTour[segmentIDFromIndex[0]][segmentOrt[segmentIDFromIndex[0]] ? segmentTour[segmentIDFromIndex[0]].length - 1 : 0];
		for (int i = 0; i < cityNum; i++) {
			prevCity = currentCity;
			currentCity = next(prevCity);
			dist += calcDist(prevCity, currentCity);
		}
		return dist;
	}

	@Override
	public void flipTour(int cityA, int cityB, int cityC, int cityD) {
		// head,tailはcityID
		int head, tail;
		int ad = Math.abs(getIndexFromStart(cityA) - getIndexFromStart(cityD)) - 1;
		int bc = Math.abs(getIndexFromStart(cityB) - getIndexFromStart(cityC)) - 1;

		if (ad < bc) {
			head = cityD;
			tail = cityA;
		}
		else {
			head = cityB;
			tail = cityC;
		}

		// すべてortはFと仮定
		int[] marginHead;
		int[] marginTail;
		int segLength;
		int segID;
		// 分割
		if (!isTerminal(head, true)) {
			segID = segmentIDFromCityID[head];
			segLength = segmentTour[segID].length;
			int[] newArray = new int[segmentOrt[segID] ? positionInSegmentFromCityID[head] + 1 : segLength - positionInSegmentFromCityID[head]];
			marginHead = new int[segLength - newArray.length];
			for (int i = 0; i < newArray.length; i++) {
				if (segmentOrt[segID])
					newArray[i] = segmentTour[segID][i];
				else
					newArray[i] = segmentTour[segID][marginHead.length + i];
			}
			for (int i = 0; i < marginHead.length; i++) {
				if (segmentOrt[segID])
					marginHead[i] = segmentTour[segID][segLength - i - 1];
				else
					marginHead[i] = segmentTour[segID][i];
			}
			segmentTour[segID] = newArray;
		}
		if (!isTerminal(tail, false)) {
			segID = segmentIDFromCityID[tail];
			segLength = segmentTour[segID].length;
			int[] newArray = new int[segmentOrt[segID] ? segLength - positionInSegmentFromCityID[tail] : positionInSegmentFromCityID[tail] + 1];
			marginTail = new int[segLength - newArray.length];
			for (int i = 0; i < newArray.length; i++) {
				if (segmentOrt[segID])
					newArray[i] = segmentTour[segID][marginTail.length + i];
				else
					newArray[i] = segmentTour[segID][i];
			}
			for (int i = 0; i < marginTail.length; i++) {
				if (segmentOrt[segID])
					marginTail[i] = segmentTour[segID][segLength - newArray.length - i - 1];
				else
					marginTail[i] = segmentTour[segID][newArray.length + i];
			}
			segmentTour[segID] = newArray;
		}

		// 交換

		// 結合

		updated = false;
	}

	/**
	 * cityIDが端点かどうかを返すメソッド
	 *
	 * @param cityID
	 * @return
	 */
	boolean isTerminal(int cityID, boolean isHead) {
		if (isHead && segmentOrt[segmentIDFromCityID[cityID]] && positionInSegmentFromCityID[cityID] == segmentTour[segmentIDFromCityID[cityID]].length - 1)
			return true;
		if (isHead && !segmentOrt[segmentIDFromCityID[cityID]] && positionInSegmentFromCityID[cityID] == 0)
			return true;
		if (!isHead && segmentOrt[segmentIDFromCityID[cityID]] && positionInSegmentFromCityID[cityID] == 0)
			return true;
		if (!isHead && !segmentOrt[segmentIDFromCityID[cityID]] && positionInSegmentFromCityID[cityID] == segmentTour[segmentIDFromCityID[cityID]].length - 1)
			return true;
		return false;
	}

	int getIndexFromStart(int city) {
		int segID = segmentIDFromCityID[city];
		int segIndex = segmentIndexFromID[segID];
		int position = positionInSegmentFromCityID[city];
		int segLength = segmentTour[segID].length;
		int count = 0;
		for (int i = 0; i < segIndex; i++) {
			count += segmentTour[segmentIDFromIndex[i]].length;
		}
		return count + (segmentOrt[segID] ? segLength - position : position + 1);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < segmentTour.length; i++) {
			for (int j = 0; j < segmentTour[segmentIDFromIndex[i]].length; j++) {
				sb.append(segmentTour[segmentIDFromIndex[i]][segmentOrt[segmentIDFromIndex[i]] ? segmentTour[segmentIDFromIndex[i]].length - j - 1 : j] + " ");
			}
		}
		return sb.toString();
	}
}
