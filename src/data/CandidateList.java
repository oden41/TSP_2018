package data;

public class CandidateList {
	int cityNum;
	int size;
	int[] candArray;
	int[] candIndexArray;
	// []:都市IDがリストにあるかどうかのフラグ
	boolean[] candExistArray;

	public CandidateList(int cityNum) {
		// TODO 自動生成されたコンストラクター・スタブ
		this.cityNum = cityNum;
		candArray = new int[cityNum];
		candIndexArray = new int[cityNum];
		candExistArray = new boolean[cityNum];
		// 最初はすべて追加
		size = cityNum;
		for (int i = 0; i < cityNum; i++) {
			candArray[i] = i;
			candIndexArray[i] = i;
			candExistArray[i] = true;
		}
	}

	/**
	 * ランダムに都市を選択
	 *
	 * @return 都市ID
	 */
	public int get() {
		return candArray[Param.rand.nextInt(size)];
	}

	public void add(int cityID) {
		if (candExistArray[cityID])
			return;
		candArray[size] = cityID;
		candIndexArray[cityID] = size;
		candExistArray[cityID] = true;
		size++;
	}

	public void remove(int cityID) {
		if (!candExistArray[cityID])
			return;
		candArray[candIndexArray[cityID]] = candArray[size - 1];
		candIndexArray[candArray[size - 1]] = candIndexArray[cityID];
		candExistArray[cityID] = false;
		candArray[size - 1] = -1;
		candIndexArray[cityID] = -1;
		size--;
	}

	public int getSize() {
		return size;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < cityNum; i++) {
			if (candExistArray[i])
				sb.append(i + " ");
		}
		return sb.toString();
	}
}
