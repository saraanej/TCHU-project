package ch.epfl.tchu;

/**
 * The Preconditions class cannot be instanciated.
 *
 * @author Yasmin Benrahhal (329912)
 * @author Sara Anejjar (329905)
 */
public final class Preconditions {
	
	/**
	 * Private default constructor.
	 */
	private Preconditions() {}
	
	/**
	 * Checks preconditions are satisfied.
	 * 
	 * @param shouldBeTrue the precondition to satisfy
	 * @throws IllegalArgumentException 
	             if the preconditions are not satisfied
	 */
	public static void checkArgument (boolean shouldBeTrue) {
		if (!shouldBeTrue)
			throw new IllegalArgumentException();
	}
}
