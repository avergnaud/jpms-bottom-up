package zoo.animal.feeding.api;

import zoo.animal.feeding.internal.*;

public class Feeding {
	public static String getFood() {
		return "some food " + FeedingLogic.get();
	}
	
	public static void main(String... args) {
		System.out.println("main " + getFood());
	}
}