// CS/COE 1501
// Use this interface with Assignment 1

public interface DictionaryInterface {
	/* Add a new String to the end of the DictInterface
	 */
	public boolean add(String s);
	
	/* The search method could be defined with various parameters.
	 * However, in our program, we will only use the version with
	 * the StringBuilder argument shown below.  This is so that we
	 * don't have the overhead of converting back and forth between
	 * StringBuilder and String each time we add a new character
	 */
	/* Returns 0 if s is not a word or prefix within the DictInterface
	 * Returns 1 if s is a prefix within the DictInterface but not a 
	 *         valid word
	 * Returns 2 if s is a word within the DictInterface but not a
	 *         prefix to other words
	 * Returns 3 if s is both a word within the DictInterface and a
	 *         prefix to other words
	 */
	public int search(StringBuilder s);
}