// This is a De La Briandais implementation of a dictionary using the dictionary interface



public class DLBDictionary implements DictionaryInterface
{
	// Holds the reference to the dlb
	private Node root;
	
	// Initialize dictionary by making node;
	public DLBDictionary()
	{
		
		root = new Node();
	}

	// add method from the dictionary interface implemented here
	public boolean add(String s) 
	{
		// The words will all be stored in upper case. This is an arbitrary choice.
		s = s.toUpperCase();
		
		// A stringbuilder of the word is made for easier manipulation and access
		StringBuilder word = new StringBuilder(s);
		
		Node start = root.down;
		
		// If the dlb is empty and there are no words, this will add the first word
		if(start == null)
		{
			// makes a new node from the root
			root.setDownChild(new Node());
			
			start = root.down;
			
			start.setLetter(word.charAt(0));
			
			start.setIsWordFalse();
			
			Node curr = start;
			
			// Adds a new node for each character in the word to add.
			// Each node with a letter is also given a value, true or false,
			// based on if the letter is the last that makes a valid word.
			for(int i = 1; i < word.length(); i = i + 1)
			{
				Node toAdd = new Node();
			
				if(i == (word.length() - 1))
				{
					curr.setDownChild(toAdd);
					curr.getDownChild().setLetter(word.charAt(i));
					curr.getDownChild().setIsWordTrue();
				}
				else
				{
					curr.setDownChild(toAdd);
					curr.getDownChild().setLetter(word.charAt(i));
					curr.getDownChild().setIsWordFalse();
				}
				curr = curr.getDownChild();		
			}
		}
		else
		{
			// If the dlb already has words in it, this portion of code will go down the dlb
			// and add words in the appropriate fashion as per dlb structure.
			Node curr = start;
			int index_of_letter = 0;
			
			while( index_of_letter != (word.length() - 1) )
			{	
				while(curr.getLetter() != word.charAt(index_of_letter))
				{
					if(curr.hasRightChild())
					{
						curr = curr.getRightChild();
					}
					else
					{
						// If code reaches this point, it means there is no letter, so add the letter, and the whole
						// rest of the word after it
						curr.setRightChild(new Node());
						curr = curr.getRightChild();
						curr.setLetter(word.charAt(index_of_letter));
						index_of_letter = index_of_letter + 1;
						
						for(int i = index_of_letter; i < word.length(); i = i + 1)
						{
							Node toAdd = new Node();
						
							if(i == (word.length() - 1))
							{
								curr.setDownChild(toAdd);
								curr.getDownChild().setLetter(word.charAt(i));
								curr.getDownChild().setIsWordTrue();
							}
							else
							{
								curr.setDownChild(toAdd);
								curr.getDownChild().setLetter(word.charAt(i));
								curr.getDownChild().setIsWordFalse();
							}
							curr = curr.getDownChild();	
						}
						
						index_of_letter = word.length() - 1;
					}
				}
				
				// If code reaches this point, means that you found the letter in your word already
				// in the dlb. Now you must go down a level and search for the second
				// letter, and repeat the above until the word has been added.
				if(index_of_letter == word.length() - 1)
				{
					break; // If word already has been added, then exit the loop
				}
				else
				{
					index_of_letter = index_of_letter + 1;
					
					if(curr.hasDownChild())
					{
						curr = curr.getDownChild(); //theres a letter below this, so go down and repeat above to add word
					}
					else
					{
						// This means that there is no letters below this level, so just add the rest of your
						// word.
						for(int i = index_of_letter; i < word.length(); i = i + 1)
						{
							Node toAdd = new Node();
						
							if(i == (word.length() - 1))
							{
								curr.setDownChild(toAdd);
								curr.getDownChild().setLetter(word.charAt(i));
								curr.getDownChild().setIsWordTrue();
							}
							else
							{
								curr.setDownChild(toAdd);
								curr.getDownChild().setLetter(word.charAt(i));
								curr.getDownChild().setIsWordFalse();
							}
							curr = curr.getDownChild();	
						}
						
						index_of_letter = word.length() - 1;
					}
				}
			}	
				
				
		}		
		return true;
	}
	
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
	public int search(StringBuilder s) 
	{	
		String temp = s.toString();
		s = new StringBuilder(temp.toUpperCase());
		
		Node curr = root.getDownChild();
		
		if(curr == null)
		{
			return 0;
		}
		
		for(int i = 0; i < s.length() ; i = i + 1)
		{
			while(s.charAt(i) != curr.getLetter())
			{
				if( curr.hasRightChild() )
				{
					curr = curr.getRightChild();
				}
				else return 0;
			}
			
			// If code gets here, it means a letter was found, and we must move down a layer
			// and search for the next letter
			 
			if(i != s.length()-1)
			{
				if(curr.hasDownChild())
				{
					curr = curr.getDownChild();
				}
				else return 1;
			}
				
		}
		
		// If code gets here, it means that we've reached the last letter in our word to search for.
		// Now, we must see if the Node we are on isWord(). If this is true and there is a downChild, then
		// we return 3 because the word is a valid word in the dictionary and a prefix to other words.
		// If isWord() is true but there is no down child, this means that the word we searched for is 
		// a valid word in the dictionary but not a prefix to any other words. If isWord() returns false,
		// it means we are in the dlb somewhere, which means that the word is not a valid word in the dictionary
		// but it is a prefix to other words, whic means we return a 1.
		
		if( curr.isWord() && curr.hasDownChild() ) return 3;
		else if( curr.isWord() && !curr.hasDownChild() ) return 2;
		else if( !curr.isWord() && curr.hasDownChild() ) return 1;
		else return 0;
			
			
	}
	
	
	// Private inner node class to make the dlb
	private static class Node
	{	
		// If the string of nodes is a word, the last letter in the node will have a value of true for word, else it will be false
		private Boolean isWord;
		
		// Holds the letter of a word
		private char letter;
		
		// The node to the right of the current node
		private Node right;
		
		// The node below the current node
		private Node down;
		
		// To initialize node instance
		public Node()
		{
			isWord = false;
			right = null;
			down = null;
			
		}
			
		// The rest of the methods below are setter and getter methods
		// to access the various data associated with the Node
		public char getLetter()
		{
			return letter;
	
		}
		
		public void setLetter(char newLetter)
		{
			letter = newLetter;
		}
		
		public Node getDownChild()
		{
			return down;
		}
		
		public Node getRightChild()
		{
			return right;
		}

		public void setIsWordTrue()
		{
			isWord = true;
		}
		
		public void setIsWordFalse()
		{
			isWord = false;
		}
	
		public boolean isWord()
		{
			return isWord;
		}
		
		public void setDownChild(Node downChild)
		{
			down = downChild;
		}
		
		public void setRightChild(Node rightChild)
		{
			right = rightChild;
			
		}
	
		public boolean hasDownChild()
		{
			return down != null;
		}
	
		public boolean hasRightChild()
		{
			return right != null;
		}
	
	
	}


}


