 ///////////////////////////////////////////////////////////////////
// Boggle Class that displays the board and lets users guess words
// **Program assumes correct command line arguments **
// Alessio Mazzone
// ALM238

import java.io.*;
import java.util.*;

public class MyBoggle
{
    public static void main(String[] args)throws IOException
    {
        // This string will hold the storage implementation(either simple or dlb) determined by user
        String Storage_Implementation = "";
        
        // This file will hold the board.txt file that the user wants to load as their boggle board
        File Boggle_Board_File;
       
        // This file holds the cannonical dictionary words
        File dictionary_txt = new File("dictionary.txt");
        
        // This is the simple dictionary version of the cannonical dictionary. Will be used if user selects simple storage
        SimpleDictionary Simple_Cannonical_Dictionary = new SimpleDictionary();
        
        // This is the DLB version of the cannonical dictionary. Will be used if DLB was selected as storege impelmentation
        DLBDictionary DLB_Cannonical_Dictionary = new DLBDictionary();
        
        // This will hold all of the possible words on a given boggle board. Will be used if user selects simple storage
        SimpleDictionary All_Possible_Words_On_Boggle_Board_Simple = new SimpleDictionary();
        
        // This will hold all of the possible words on the boggle board, in DLB form.
        DLBDictionary All_Possible_Words_On_Boggle_Board_DLB = new DLBDictionary();
       
        // This will hold all of the words that are in the boggle board as well, but this will be used to print the words to the user
        ArrayList<String> words_for_print = new ArrayList<String>();
        
        // Grabs the storage implementation and boggle board file name from
        // the command line arguments. If no storage implementation is given,
        // the program will defult to SimpleDictionary storage.
        if(args[0].equals("-b") && args.length > 2)
        {
            Boggle_Board_File = new File(args[1]);
            Storage_Implementation = args[3];
        }
        else if(args[0].equals("-b") && args.length <= 2)
        {   
            Boggle_Board_File = new File(args[1]);
            Storage_Implementation = "simple";
        }
        else 
        {
            Boggle_Board_File = new File(args[3]);
            Storage_Implementation = args[1];
        }
        
        //Grab the letters from the boggle board file
        Scanner board = new Scanner(Boggle_Board_File);
        
        String Boggle_Board_Letters = board.nextLine();
        
        board.close();
        
        boolean board_has_wildcard = Boggle_Board_Letters.contains("*");
         
        //Determine if the words will be stored in simple dictionary or dlb.
        if(Storage_Implementation.equals("simple"))
        {
            Simple_Cannonical_Dictionary = createSimpleCannonicalDictionary(Boggle_Board_Letters,dictionary_txt,board_has_wildcard,Simple_Cannonical_Dictionary);
        
            All_Possible_Words_On_Boggle_Board_Simple = findPossibleWordsOnBoard(Boggle_Board_Letters, Simple_Cannonical_Dictionary,words_for_print);
        }
        else
        {
            DLB_Cannonical_Dictionary = createDLBCannonicalDictionary(Boggle_Board_Letters,dictionary_txt,board_has_wildcard,DLB_Cannonical_Dictionary);
            
            All_Possible_Words_On_Boggle_Board_DLB = findPossibleWordsOnBoard(Boggle_Board_Letters, DLB_Cannonical_Dictionary,words_for_print);
        }
        
        // calls method to display boggle board to user
        displayBoggleBoard(Boggle_Board_Letters);  
        
        ArrayList<String> User_Word_Guesses = new ArrayList<String>();
        Scanner keyboard = new Scanner(System.in);
        
        System.out.println("Welcome to Boggle! Please enter words you think are in the board.");
        System.out.println("Type a word followed by the enter key to guess.");
        System.out.println("To end the game and see how you did, enter a question mark(?) ");
    	System.out.println();
    	System.out.print("Word to guess: ");
    	
        StringBuilder guess = new StringBuilder();
        
        if( Storage_Implementation.equals("simple") )
        {
	        while( keyboard.hasNext() )
	        {
	        	guess = guess.append(keyboard.nextLine().toUpperCase());
	        	if(guess.toString().equals("?"))
	        	{
	        		System.out.println("You have chosen to end the game!");
	        		break;
	        	}
	        	else if( All_Possible_Words_On_Boggle_Board_Simple.search(guess) == 2 || All_Possible_Words_On_Boggle_Board_Simple.search(guess) == 3) 
	        	{
	        		User_Word_Guesses.add(guess.toString());
	        		System.out.println("Nice, you entered a valid word!");
	        		System.out.println();
	        	}
	        	else
	        	{
	        		System.out.println();
	        		System.out.println("Oh no! The word you entered is not a valid word!");
	        		System.out.println();
	        	}
	        	
	        	guess.replace(0, guess.length(), "");
	        	displayBoggleBoard(Boggle_Board_Letters);  
	        	System.out.println();
	        	System.out.print("Word to guess: ");
	        }
        }
        else
        {
        	 while( keyboard.hasNext() )
 	        {
 	        	guess = guess.append(keyboard.nextLine().toUpperCase());
 	        	if(guess.toString().equals("?"))
 	        	{
 	        		System.out.println("You have chosen to end the game!");
 	        		break;
 	        	}
 	        	else if( All_Possible_Words_On_Boggle_Board_DLB.search(guess) == 2 || All_Possible_Words_On_Boggle_Board_DLB.search(guess) == 3) 
 	        	{
 	        		User_Word_Guesses.add(guess.toString());
 	        		System.out.println("Nice, you entered a valid word!");
 	        		System.out.println();
 	        	}
 	        	else
 	        	{
 	        		System.out.println();
 	        		System.out.println("Oh no! The word you entered is not a valid word!");
 	        		System.out.println();
 	        	}
 	        	
 	        	guess.replace(0, guess.length(), "");
 	        	displayBoggleBoard(Boggle_Board_Letters);  
 	        	System.out.println();
 	        	System.out.print("Word to guess: ");
 	        }
        	
        	
        }
        
        // Removes duplicate words from the array list to print and sorts it
        HashSet<String> x = new HashSet<String>();      // Hashset is a collection that does now allow duplicates. By putting data in it
        x.addAll(words_for_print);      				// that has duplicates, it implicitly removes the duplicate data, but also scrambles
        words_for_print.clear();        				// the order of the data
        words_for_print.addAll(x);
        Collections.sort(words_for_print);
        
        
        System.out.println("Here is a list of all possible words on the board:");
        System.out.println(words_for_print);
        System.out.println("\nHere is a list of all the words you guessed correctly:");
        
        HashSet<String> y = new HashSet<String>();
        y.addAll(User_Word_Guesses);
        User_Word_Guesses.clear();   
        User_Word_Guesses.addAll(y);
        Collections.sort(User_Word_Guesses);
        
        System.out.println(User_Word_Guesses);
        System.out.println("\nYou guessed " + User_Word_Guesses.size() + " words correctly!");
        System.out.println("For this board, there was a total of " + words_for_print.size() + " possible words to guess.");
        
        double percent_correct = (User_Word_Guesses.size() * 1.0) / (words_for_print.size() * 1.0);
        
        System.out.printf("You have guessed %.3f percent of the words correctly!" , percent_correct);
       
    } 
       
       
       
       
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // This function creates the cannonical dictionary using the simple dictionary class. The arguments it takes are the follwing:
    //
    // 1. String Boggle_Board_Letters --> uses these letters to determine if any words can be pruned from the dictionary.
    //
    // 2. File dictionary_txt --> uses this file to import the dictionary words into the simple dictionary
    //
    // 3. Boolean board_has_wildcard --> used to determine if the board has a wild card. If it does, then all words from the cannonical
    //    dictionary get imported. If the board does not have a wild card character, then we can remove from our dictionary import any words
    //    that start with letters that are not on the boggle board. For example, if there is no wild card character(board_has_wildcard = false), 
    //    and there is no 'a' on the board, then when we go to import our words from the txt file, we can ignorre all words that begin with 'a' as
    //    they are not possible words on the boggle board.
    //
    // 4. SimpleDictionary Simple_Cannonical_Dictionary --> we pass an empty simple dictionary to this function. We import the possible words that can be made
    //    from the txt file, then we return the complete simple dictionary to the original function call. 
    public static SimpleDictionary createSimpleCannonicalDictionary(String Boggle_Board_Letters, File dictionary_txt, boolean board_has_wildcard, SimpleDictionary Simple_Cannonical_Dictionary ) throws IOException
    {
            Scanner Dictionary_File = new Scanner(dictionary_txt);
            
            while(Dictionary_File.hasNext())
            {
                String word = Dictionary_File.nextLine().toUpperCase();
                
                // If the board does not have a certain letter on it, we can ignore all words in the dictionary
                // that start with that letter.
                if(board_has_wildcard == false && Boggle_Board_Letters.indexOf(word.charAt(0)) != -1)
                {
                    Simple_Cannonical_Dictionary.add(word); 
                }
                else if(board_has_wildcard)
                {
                    Simple_Cannonical_Dictionary.add(word);
                }
            }
   
        return Simple_Cannonical_Dictionary;
    }
    
    // Same function as the simple dictionary, but instead used for DLB dictionary creation
    public static DLBDictionary createDLBCannonicalDictionary(String Boggle_Board_Letters,File dictionary_txt, boolean board_has_wildcard, DLBDictionary DLB_Cannonical_Dictionary) throws IOException
    {
    	Scanner Dictionary_File = new Scanner(dictionary_txt);
        
        while(Dictionary_File.hasNext())
        {
            String word = Dictionary_File.nextLine().toUpperCase();
            
            // If the board does not have a certain letter on it, we can ignore all words in the dictionary
            // that start with that letter.
            if(board_has_wildcard == false && Boggle_Board_Letters.indexOf(word.charAt(0)) != -1)
            {
                DLB_Cannonical_Dictionary.add(word); 
            }
            else if(board_has_wildcard)
            {
                DLB_Cannonical_Dictionary.add(word);
            }
        }

    return DLB_Cannonical_Dictionary;
    	
    	
    }
    
    // Calls recursive function to find all of the words on the board. 
    public static DLBDictionary findPossibleWordsOnBoard(String Boggle_Board_Letters, DLBDictionary DLB_Cannonical_Dictionary, ArrayList words_for_print)
    {
    	// This will be the dictionary that this method returns. It will hold all of the possible
        // words that a user could guess.
        DLBDictionary possible_words = new DLBDictionary();
        
        // This string will be passed to the recursive method. It will be mutated many times
        // and used for searches to the cannonical dictionary
        StringBuilder root_string = new StringBuilder("");
        
        //Tells algorithim if a letter has already been visited or not
        boolean visited[] = {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false};
        
        // Each row of this array represents an index on the board. Each sub array represents the possible indicies that a player can move from that letter.
        // So, if we are located in the top right corner of the board, the player can move to the right(index 1), diagonally(index 5), or down(index 4)
         int[][] possible_moves = { {1,5,4},
                                    {2,6,5,4,0},
                                    {3,7,6,5,1},
                                    {7,6,2},
                                    {5,9,8,0,1},
                                    {6,10,9,8,4,0,1,2},
                                    {7,11,10,9,5,1,2,3},
                                    {11,10,6,2,3},
                                    {9,13,12,4,5},
                                    {10,14,13,12,8,4,5,6},
                                    {11,15,14,13,9,5,6,7},
                                    {15,14,10,6,7},
                                    {13,8,9},
                                    {14,12,8,9,10},
                                    {15,13,9,10,11},
                                    {14,10,11} };
                             
        // This for loop will repeat once for each of the letter positions on the boggle board
        for(int i = 0; i < 16; i = i + 1)
        {
            root_string.append(Boggle_Board_Letters.charAt(i));
            visited[i] = true;
            FindWords(root_string,visited,possible_moves,i,Boggle_Board_Letters,DLB_Cannonical_Dictionary,possible_words,words_for_print);
            visited[i] = false;
        
        }
       
        return possible_words;
    	
    }
    
    // Same function as above, just for simple dictionary 
    public static SimpleDictionary findPossibleWordsOnBoard(String Boggle_Board_Letters, SimpleDictionary Simple_Cannonical_Dictionary, ArrayList words_for_print)
    {
        // This will be the dictionary that this method returns. It will hold all of the possible
        // words that a user could guess.
        SimpleDictionary possible_words = new SimpleDictionary();
        
        // This string will be passed to the recursive method. It will be mutated many times
        // and used for searches to the cannonical dictionary
        StringBuilder root_string = new StringBuilder("");
        
        //Tells algorithim if a letter has already been visited or not
        boolean visited[] = {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false};
        
         int[][] possible_moves = { {1,5,4},
                                    {2,6,5,4,0},
                                    {3,7,6,5,1},
                                    {7,6,2},
                                    {5,9,8,0,1},
                                    {6,10,9,8,4,0,1,2},
                                    {7,11,10,9,5,1,2,3},
                                    {11,10,6,2,3},
                                    {9,13,12,4,5},
                                    {10,14,13,12,8,4,5,6},
                                    {11,15,14,13,9,5,6,7},
                                    {15,14,10,6,7},
                                    {13,8,9},
                                    {14,12,8,9,10},
                                    {15,13,9,10,11},
                                    {14,10,11} };
                             
        // This for loop will repeat once for each of the letter positions on the boggle board
        for(int i = 0; i < 16; i = i + 1)
        {
            root_string.append(Boggle_Board_Letters.charAt(i));
            visited[i] = true;
            FindWords(root_string,visited,possible_moves,i,Boggle_Board_Letters,Simple_Cannonical_Dictionary,possible_words,words_for_print);
            visited[i] = false;
        
        }
       
        return possible_words;
    }
    
    // Recursive function that finds words for DLB dictionary
    public static void FindWords(StringBuilder root_string, boolean[] visited,int possible_moves[][],int index,String Boggle_Board_Letters,DLBDictionary DLB_Cannonical_Dictionary,DLBDictionary possible_words, ArrayList words_for_print )
    {
    	// Calculates the lenght of the string and acts appropirately if the string is only 1 character. We need at least 2 characters to check for prefixes
        int root_string_length = root_string.length();
        
        // if the length of the initial string is 1, then we must add a second letter before seeing if the letter is a prefix
        // after adding the second letter, we recall findWords recursively to start the next search
        if(root_string_length == 1)
        {
            char alphabet[] = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
            
            // if the board has a wild card character, we will replace the wild car with each of the characters of the alphabet and then
            // recursively start the next search
            if(root_string.indexOf("*") != -1)
            {
                int star_index = root_string.indexOf("*");
                
                for(char letter : alphabet)
                {
                	root_string.deleteCharAt(star_index);
                    root_string.append(letter);
                    FindWords(root_string,visited,possible_moves,index,Boggle_Board_Letters,DLB_Cannonical_Dictionary,possible_words,words_for_print);
                    root_string.append('*');
                }
                root_string.deleteCharAt(root_string_length - 1);
            }
            else
            {
           	
                int moves[] = possible_moves[index];
                for(int position : moves)
                {
                    
                    if(visited[position] == false)
                    {
                        root_string.append(Boggle_Board_Letters.charAt(position));
                        visited[position] = true;
                        FindWords(root_string,visited,possible_moves,position,Boggle_Board_Letters,DLB_Cannonical_Dictionary,possible_words,words_for_print);
                        visited[position]=false;
                    }
                }
                root_string.deleteCharAt(root_string_length - 1);
            }    
        }
        else
        {
        	// If we reach this part of the code, it means that our string to search for is at least 2 characters long.
        	// If we have a wildcard character present, we replace it once for each letter in the alphabet then we recursivly
        	// find words again
        	
            char alphabet[] = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
            
             
            if(root_string.indexOf("*") != -1)
            {             
                int star_index = root_string.indexOf("*");
                
                for(char letter : alphabet)
                {
                	root_string.deleteCharAt(star_index);
                    root_string.append(letter);
                    FindWords(root_string,visited,possible_moves,index,Boggle_Board_Letters,DLB_Cannonical_Dictionary,possible_words,words_for_print);                        
                    root_string.append('*');
                }
                root_string.deleteCharAt(root_string_length - 1);
            }
            else
            { 
            	// Since our string is at least two characters long, we can now search the cannonical dictionary for our string.
            	// The search will return 0,1,2,3. There is a switch statement to handle each of these cases.
            	// If the search returns a 0, then the word is not a prefix and not a valid word, so we remove that character and return backward from the recursion.
            	// if the search returns a 1, then the word is a prefix but not a word, so we add another letter from the board and recursively search for words again.
            	// If the search returns a 2, then the word is a valid word but not a prefix. We add this word to our dictionary of possible words on the board, then we remove that character and return backward from the recursion.
            	// If the search returns a 3, then the word is both a valid word and a prefix. We add the word to our dictionary of possible words and then we recursively search for words again.
            	
                int search_result = DLB_Cannonical_Dictionary.search(root_string);
                
                switch(search_result)
                {
                    case 0:
                    {
                        root_string.deleteCharAt(root_string_length - 1);
                        break;
                    }
                    case 1:
                    {
                        int moves[] = possible_moves[index];
                        for(int position : moves)
                        {
                            if(visited[position] == false)
                            {
                                root_string.append(Boggle_Board_Letters.charAt(position));
                                visited[position] = true;
                                FindWords(root_string,visited,possible_moves,position,Boggle_Board_Letters,DLB_Cannonical_Dictionary,possible_words,words_for_print);
                                visited[position]=false;
                            }
                        }
                        root_string.deleteCharAt(root_string_length - 1);
                        break;
                    }
                    case 2:
                    {
                        possible_words.add(root_string.toString());
                        words_for_print.add(root_string.toString());
                           
                        root_string.deleteCharAt(root_string_length - 1);
                        break;
                    }
                    case 3:
                    {
                        possible_words.add(root_string.toString());
                        words_for_print.add(root_string.toString());
                            
                        int moves[] = possible_moves[index];
                        for(int position : moves)
                        {
                            if(visited[position] == false)
                            {
                                root_string.append(Boggle_Board_Letters.charAt(position));
                                visited[position] = true;
                                FindWords(root_string,visited,possible_moves,position,Boggle_Board_Letters,DLB_Cannonical_Dictionary,possible_words,words_for_print);
                                visited[position]=false;
                            }
                        }
                        root_string.deleteCharAt(root_string_length - 1);
                        break;
                    }
                }
            }
        }
    	
    	
    }
    
    // Same as above, except for simple dictionary
    // same function as above, except this one is for simple dictionary
    public static void FindWords(StringBuilder root_string, boolean[] visited,int possible_moves[][],int index,String Boggle_Board_Letters,SimpleDictionary Simple_Cannonical_Dictionary,SimpleDictionary possible_words, ArrayList words_for_print )
    {
        // Calculates the lenght of the string and acts appropirately if the string is only 1 character. We need at least 2 characters to check for prefixes
        int root_string_length = root_string.length();
        
        if(root_string_length == 1)
        {
            char alphabet[] = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
            
            if(root_string.indexOf("*") != -1)
            {
                int star_index = root_string.indexOf("*");
                
                for(char letter : alphabet)
                {
                	root_string.deleteCharAt(star_index);
                    root_string.append(letter);
                    FindWords(root_string,visited,possible_moves,index,Boggle_Board_Letters,Simple_Cannonical_Dictionary,possible_words,words_for_print);
                    root_string.append('*');
                }
                root_string.deleteCharAt(root_string_length - 1);
            }
            else
            {
                int moves[] = possible_moves[index];
                for(int position : moves)
                {
                    
                    if(visited[position] == false)
                    {
                        root_string.append(Boggle_Board_Letters.charAt(position));
                        visited[position] = true;
                        FindWords(root_string,visited,possible_moves,position,Boggle_Board_Letters,Simple_Cannonical_Dictionary,possible_words,words_for_print);
                        visited[position]=false;
                    }
                }
                root_string.deleteCharAt(root_string_length - 1);
            }    
        }
        else
        {
            char alphabet[] = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
            //StringBuilder temp_root = new StringBuilder(root_string.toString());
             
            if(root_string.indexOf("*") != -1)
            {             
                int star_index = root_string.indexOf("*");
                
                for(char letter : alphabet)
                {
                	root_string.deleteCharAt(star_index);
                    root_string.append(letter);
                    FindWords(root_string,visited,possible_moves,index,Boggle_Board_Letters,Simple_Cannonical_Dictionary,possible_words,words_for_print);                        
                    root_string.append('*');
                }
                root_string.deleteCharAt(root_string_length - 1);
            }
            else
            { 
                int search_result = Simple_Cannonical_Dictionary.search(root_string);
                
                switch(search_result)
                {
                    case 0:
                    {
                        root_string.deleteCharAt(root_string_length - 1);
                        break;
                    }
                    case 1:
                    {
                        int moves[] = possible_moves[index];
                        for(int position : moves)
                        {
                            if(visited[position] == false)
                            {
                                root_string.append(Boggle_Board_Letters.charAt(position));
                                visited[position] = true;
                                FindWords(root_string,visited,possible_moves,position,Boggle_Board_Letters,Simple_Cannonical_Dictionary,possible_words,words_for_print);
                                visited[position]=false;
                            }
                        }
                        root_string.deleteCharAt(root_string_length - 1);
                        break;
                    }
                    case 2:
                    {
                        possible_words.add(root_string.toString());
                        words_for_print.add(root_string.toString());
                           
                        root_string.deleteCharAt(root_string_length - 1);
                        break;
                    }
                    case 3:
                    {
                        possible_words.add(root_string.toString());
                        words_for_print.add(root_string.toString());
                            
                        int moves[] = possible_moves[index];
                        for(int position : moves)
                        {
                            if(visited[position] == false)
                            {
                                root_string.append(Boggle_Board_Letters.charAt(position));
                                visited[position] = true;
                                FindWords(root_string,visited,possible_moves,position,Boggle_Board_Letters,Simple_Cannonical_Dictionary,possible_words,words_for_print);
                                visited[position]=false;
                            }
                        }
                        root_string.deleteCharAt(root_string_length - 1);
                        break;
                    }
                }
            }
        }
    }
     
    ////////////////////////////////////////////////////////////////////////////////////////////
    // This function is passed a string from a text file of the letters of the boggle board and
    // prints out the board to the user
    public static void displayBoggleBoard(String letters)
    {
        System.out.println("_____BOGGLE!_____");
        System.out.println("-----------------");
        System.out.println("| " + letters.charAt(0) + " " + "| " + letters.charAt(1) + " " + "| " + letters.charAt(2) + " " + "| " + letters.charAt(3) + " |");
        System.out.println("| " + letters.charAt(4) + " " + "| " + letters.charAt(5) + " " + "| " + letters.charAt(6) + " " + "| " + letters.charAt(7) + " |");
        System.out.println("| " + letters.charAt(8) + " " + "| " + letters.charAt(9) + " " + "| " + letters.charAt(10) + " " + "| " + letters.charAt(11) + " |");
        System.out.println("| " + letters.charAt(12) + " " + "| " + letters.charAt(13) + " " + "| " + letters.charAt(14) + " " + "| " + letters.charAt(15) + " |");
        System.out.println("-----------------");  
    }
    
    
    
    
    
    
}