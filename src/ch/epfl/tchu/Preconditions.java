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
	 * @throws an IllegalArgumentException if the preconditions are not satisfied
	 * 
	 * @param shouldBeTrue (boolean) the precondition to satisfy
	 * 
	 */
	public static void checkArgument (boolean shouldBeTrue) {
		
		if (!shouldBeTrue) {
			throw new IllegalArgumentException();
		}
		
	}

}
