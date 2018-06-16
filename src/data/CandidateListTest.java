package data;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

public class CandidateListTest {

	@Before
	public void init() {
		Param.rand = new Random();
	}

	@Test
	public void testCandidateList() {
		CandidateList list = new CandidateList(10);
		System.out.println(list);
	}

	@Test
	public void testGet() {
		CandidateList list = new CandidateList(10);
		// ランダムにadd,removeして正しくリストがなっているかを確認
		for (int i = 0; i < 1000; i++) {
			int num = Param.rand.nextInt(list.cityNum);
			if (Param.rand.nextInt() % 2 == 0)
				list.remove(num);
			else
				list.add(num);
		}

		System.out.println(list);
		for (int i = 0; i < 10; i++) {
			System.out.println(list.get());
		}
	}

	@Test
	public void testAdd() {
		CandidateList list = new CandidateList(10);
		for (int i = 0; i < list.cityNum; i++) {
			list.remove(i);
		}
		list.add(0);
		list.add(5);
		System.out.println(list);
	}

	@Test
	public void testRemove() {
		CandidateList list = new CandidateList(10);
		list.remove(0);
		list.remove(3);
		list.remove(5);
		System.out.println(list);
	}

}
