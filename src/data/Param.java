package data;

import java.util.Random;

public class Param {
	public static Random rand;

	public Param(long seed) {
		rand = new Random(seed);
	}
}
