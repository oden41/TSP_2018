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
			} else
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
	 * @param i セグメントID
	 * @param j セグメントID
	 */
	void swapSegmentIndex(int i, int j) {
		int tempIndex = segmentIndexFromID[i];
		segmentIndexFromID[i] = segmentIndexFromID[j];
		segmentIndexFromID[j] = tempIndex;
	}

	/**
	 * segmentIDをスワップする
	 *
	 * @param i インデックス
	 * @param index インデックス
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
				int prevSegID = segmentIDFromIndex[segmentIndexFromID[currentSegID] == 0 ? segmentTour.length - 1
						: segmentIndexFromID[currentSegID] - 1];
				return segmentTour[prevSegID][segmentOrt[prevSegID] ? 0 : segmentTour[prevSegID].length - 1];
			} else {
				return segmentTour[currentSegID][positionInSegmentFromCityID[cityID] + 1];
			}
		} else {
			// 順方向
			if (positionInSegmentFromCityID[cityID] == 0) {
				int prevSegID = segmentIDFromIndex[segmentIndexFromID[currentSegID] == 0 ? segmentTour.length - 1
						: segmentIndexFromID[currentSegID] - 1];
				return segmentTour[prevSegID][segmentOrt[prevSegID] ? 0 : segmentTour[prevSegID].length - 1];
			} else {
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
				int nextSegID = segmentIDFromIndex[segmentIndexFromID[currentSegID] == segmentTour.length - 1 ? 0
						: segmentIndexFromID[currentSegID] + 1];
				return segmentTour[nextSegID][segmentOrt[nextSegID] ? segmentTour[nextSegID].length - 1 : 0];
			} else {
				return segmentTour[currentSegID][positionInSegmentFromCityID[cityID] - 1];
			}
		} else {
			// 順方向
			if (positionInSegmentFromCityID[cityID] == segmentTour[currentSegID].length - 1) {
				int nextSegID = segmentIDFromIndex[segmentIndexFromID[currentSegID] == segmentTour.length - 1 ? 0
						: segmentIndexFromID[currentSegID] + 1];
				return segmentTour[nextSegID][segmentOrt[nextSegID] ? segmentTour[nextSegID].length - 1 : 0];
			} else {
				return segmentTour[currentSegID][positionInSegmentFromCityID[cityID] + 1];
			}
		}
	}

	@Override
	int calcTour() {
		int dist = 0;
		int prevCity = 0;
		int currentCity = segmentTour[segmentIDFromIndex[0]][segmentOrt[segmentIDFromIndex[0]]
				? segmentTour[segmentIDFromIndex[0]].length - 1
				: 0];
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
		int indexA = getIndexFromStart(cityA);
		int indexB = getIndexFromStart(cityB);
		int indexC = getIndexFromStart(cityC);
		int indexD = getIndexFromStart(cityD);

		int ad = indexA - indexD < 0 ? indexA - indexD + cityNum : indexA - indexD;
		int bc = indexC - indexB < 0 ? indexC - indexB + cityNum : indexC - indexB;

		if (ad < bc) {
			head = cityD;
			tail = cityA;
		} else {
			head = cityB;
			tail = cityC;
		}

		// すべてortはFと仮定
		int[] marginHead = new int[0];
		int[] marginTail = new int[0];
		int segLength;
		int segID;
		// 分割
		if (!isTerminal(head, true)) {
			segID = segmentIDFromCityID[head];
			segLength = segmentTour[segID].length;
			int[] newArray = new int[segmentOrt[segID] ? positionInSegmentFromCityID[head] + 1
					: segLength - positionInSegmentFromCityID[head]];
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
			//newArrayでのposition更新
			for (int i = 0; i < newArray.length; i++) {
				positionInSegmentFromCityID[newArray[i]] = i;
			}
		}
		if (!isTerminal(tail, false)) {
			segID = segmentIDFromCityID[tail];
			segLength = segmentTour[segID].length;
			int[] newArray = new int[segmentOrt[segID] ? segLength - positionInSegmentFromCityID[tail]
					: positionInSegmentFromCityID[tail] + 1];
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
			//newArrayでのposition更新
			for (int i = 0; i < newArray.length; i++) {
				positionInSegmentFromCityID[newArray[i]] = i;
			}
		}

		// 交換
		int headSeg = segmentIDFromCityID[head];
		int tailSeg = segmentIDFromCityID[tail];
		int headSegIndex = segmentIndexFromID[headSeg];
		int tailSegIndex = segmentIndexFromID[tailSeg];
		//結合の際に参考とするインデックス headはinitHeadSegIndexかinitHeadSegIndex-1のセグメントにつける
		int initHeadSegIndex = headSegIndex;
		//tailはtailHeadSegIndexかtailHeadSegIndex+1のセグメントにつける
		int initTailSegIndex = tailSegIndex;
		int count = 0;
		int flipT = ((tailSegIndex - headSegIndex < 0 ? tailSegIndex - headSegIndex + segNum
				: tailSegIndex - headSegIndex) + 1) / 2;
		while (count < flipT) {
			headSeg = segmentIDFromIndex[headSegIndex];
			tailSeg = segmentIDFromIndex[tailSegIndex];
			segmentOrt[headSeg] = !segmentOrt[headSeg];
			segmentOrt[tailSeg] = !segmentOrt[tailSeg];
			swapSegmentIndex(headSeg, tailSeg);
			swapSegmentID(headSegIndex, tailSegIndex);

			headSegIndex++;
			tailSegIndex--;

			if (headSegIndex >= segNum)
				headSegIndex = 0;
			if (tailSegIndex < 0)
				tailSegIndex = segNum - 1;

			count++;
		}
		if (headSegIndex == tailSegIndex) {
			segmentOrt[segmentIDFromIndex[headSegIndex]] = !segmentOrt[segmentIDFromIndex[headSegIndex]];
		}

		// 結合
		if (marginHead.length != 0) {
			int beforeIndex = initHeadSegIndex == 0 ? segNum - 1 : initHeadSegIndex - 1;
			if (segmentTour[segmentIDFromIndex[beforeIndex]].length < segmentTour[segmentIDFromIndex[initHeadSegIndex]].length) {
				combineSegment(segmentIDFromIndex[beforeIndex], marginHead, false);
			} else {
				combineSegment(segmentIDFromIndex[initHeadSegIndex], marginHead, true);
			}
		}
		if (marginTail.length != 0) {
			int afterIndex = initTailSegIndex == segNum - 1 ? 0 : initTailSegIndex + 1;
			if (segmentTour[segmentIDFromIndex[afterIndex]].length < segmentTour[segmentIDFromIndex[initTailSegIndex]].length) {
				combineSegment(segmentIDFromIndex[afterIndex], marginTail, true);
			} else {
				combineSegment(segmentIDFromIndex[initTailSegIndex], marginTail, false);
			}
		}
		updated = false;
	}

	/**
	 * marginをセグメントidに結合する
	 * @param id セグメントID
	 * @param margin 結合する配列
	 * @param b true:前結合,false:後結合
	 */
	private void combineSegment(int id, int[] margin, boolean b) {
		int[] newArray = new int[segmentTour[id].length + margin.length];
		if (b) {
			//marginが前結合
			if (segmentOrt[id]) {
				for (int i = 0; i < newArray.length; i++) {
					if (i < segmentTour[id].length)
						newArray[i] = segmentTour[id][i];
					else
						newArray[i] = margin[margin.length - (i - segmentTour[id].length) - 1];
					segmentIDFromCityID[newArray[i]] = id;
					positionInSegmentFromCityID[newArray[i]] = i;
				}
			} else {
				for (int i = 0; i < newArray.length; i++) {
					if (i < margin.length)
						newArray[i] = margin[i];
					else
						newArray[i] = segmentTour[id][i - margin.length];
					segmentIDFromCityID[newArray[i]] = id;
					positionInSegmentFromCityID[newArray[i]] = i;
				}
			}
		} else {
			//marginが後結合
			if (segmentOrt[id]) {
				for (int i = 0; i < newArray.length; i++) {
					if (i < margin.length)
						newArray[i] = margin[margin.length - i - 1];
					else
						newArray[i] = segmentTour[id][i - margin.length];
					segmentIDFromCityID[newArray[i]] = id;
					positionInSegmentFromCityID[newArray[i]] = i;
				}
			} else {
				for (int i = 0; i < newArray.length; i++) {
					if (i < segmentTour[id].length)
						newArray[i] = segmentTour[id][i];
					else
						newArray[i] = margin[i - segmentTour[id].length];
					segmentIDFromCityID[newArray[i]] = id;
					positionInSegmentFromCityID[newArray[i]] = i;
				}
			}
		}
		segmentTour[id] = newArray;
	}

	/**
	 * cityIDが端点かどうかを返すメソッド
	 *
	 * @param cityID
	 * @return
	 */
	boolean isTerminal(int cityID, boolean isHead) {
		if (isHead && segmentOrt[segmentIDFromCityID[cityID]]
				&& positionInSegmentFromCityID[cityID] == segmentTour[segmentIDFromCityID[cityID]].length - 1)
			return true;
		if (isHead && !segmentOrt[segmentIDFromCityID[cityID]] && positionInSegmentFromCityID[cityID] == 0)
			return true;
		if (!isHead && segmentOrt[segmentIDFromCityID[cityID]] && positionInSegmentFromCityID[cityID] == 0)
			return true;
		if (!isHead && !segmentOrt[segmentIDFromCityID[cityID]]
				&& positionInSegmentFromCityID[cityID] == segmentTour[segmentIDFromCityID[cityID]].length - 1)
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
				sb.append(segmentTour[segmentIDFromIndex[i]][segmentOrt[segmentIDFromIndex[i]]
						? segmentTour[segmentIDFromIndex[i]].length - j - 1
						: j] + " ");
			}
		}
		return sb.toString();
	}
}
