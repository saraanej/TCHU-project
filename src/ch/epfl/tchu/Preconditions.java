package ch.epfl.tchu;

/**
 * class Preconditions - cannot be instanciated
 * 
 * 
 * @author Yasmin Benrahhal
 * @author Sara Anejjar
 *
 */
public final class Preconditions {
	
	/**
	 * private default constructor
	 * 
	 */
	private Preconditions() {}
	
	/**
	 * checks preconditions are satisfied 
	 * 
	 * @param shouldBeTrue (boolean) the precondition to satisfy
	 * 
	 * @throws IllegalArgumentException if the preconditions are not satisfied
	 */
	public static void checkArgument (boolean shouldBeTrue) {
		
		if (!shouldBeTrue) {
			throw new IllegalArgumentException();
		}
		
	}

}
