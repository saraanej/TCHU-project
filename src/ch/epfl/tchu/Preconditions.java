package ch.epfl.tchu;

public final class Preconditions {
	
	private Preconditions() {}
	
	public static void checkArgument (boolean shouldBeTrue) {
		//!shouldBeTrue ? throw new IllegalArgumentException;
		
		if (!shouldBeTrue) {
			throw new IllegalArgumentException;
		}
		
	}

}
