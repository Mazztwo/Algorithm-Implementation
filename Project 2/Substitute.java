// This is the substitute class to be used for Assignment 3

import java.util.Random;
import java.util.Arrays;

public class Substitute implements SymCipher
{
	// Holds out bit map
	private byte map[];

	// Random num generator
	private Random rand = new Random();


	// Constructor creates a random permutation of bytes 0-255 in and places
	// it in our byte array map
	public Substitute()
	{
		// To represent 256 possible ascii characters
		map = new byte[256];

		// Fill map with all 256 possibilities from 0-255
		for(int k = 0; k < 256; k = k + 1)
		{
			map[k] = (byte)k;
		}

		// To make a random permutation, we can generate a random index,
		// and swap that index with our current index
		int index1 = 0;
		byte temp = 0;
		
		for(int i = 0; i < 256; i = i + 1)
		{
			index1 = rand.nextInt(256);
			temp = map[i];
			map[i] = map[index1];
			map[index1] = temp;
		}
	}


	// Just initializes our map to be the argument of this function
	public Substitute(byte bytes[])
	{
		map = bytes;
	}


	// Return an array of bytes that represent the key for the cipher
	public byte [] getKey()
	{
		return map;
	}


	// Encode the string using the key and return the result as an array of
	// bytes.  Note that you will need to convert the String to an array of bytes
	// prior to encrypting it.
	public byte [] encode(String S)
	{
		// Encode works because of our map. Our map is a random permutation of the possible
		// ascii values of 0 to 255. Similar to the Ceaser Cypher, we will take our alphabet,
		// which in our case is all possible ascii characters, and make a random permutation
		// of those characters. This gives us a map to encode our messages. The act of encoding
		// is as follows: We take each character from our string and look it up in our map by 
		// turning it into an index. 
		//
		// So, for example, if our first character in our string S
		// is of byte value 97, then we go to map[97] and find the corresponding byte there, say 76. 
		// Because we've shuffled our alphabet, map[97] will be a random byte in the range 0-255. 
		// This is where our encoding comes in. We will take S[0], in our case 97, which represents
		// the character 'a' and map it to map[97], which represents the charcter 'L'. So, the first
		// character in our encoded message will be 'L'. We do this mapping to each character in our
		// string we want to encode and we're done! 
		//
		// NOTE: Because byte values can be negative( -128 to 127 ), we must handle negative byte values
		// a bit differently. For positive byte values in the range 0 to 128, we simply use the byte value
		// as our index for our map. For negative byte values, we simply add 256 to get our index. So, for
		// example, if S[5] has a byte value of -3, we will add 256 to this to get 253, which is the correct
		// index in our map array for the character.

		// Converts String paramater S into a byte array by mapping each char in S to the corresponding
		// char in our map.
		byte encryptedMessage[] = S.getBytes();
		int map_index;

		for(int i = 0; i < S.length(); i = i + 1)
		{	
			if(encryptedMessage[i] < 0) encryptedMessage[i] = map[(int)encryptedMessage[i] + 256];
			else encryptedMessage[i] = map[(int)encryptedMessage[i]];
		}

		return encryptedMessage;
	}


	// Decrypt the array of bytes and generate and return the corresponding String
	public String decode(byte [] bytes)
	{
		// Will hold our decoding map
		byte decode_map[] = new byte[map.length];
		
		// This for loop makes our decoding map. It will go into our map array, and use map[i] as the index
		// for our decode map. We then set the location in our decode map at that index equal to i. We're just
		// swapping the indicies and the values. If the index we use ends up being negative, we simply add 256.
		for(int i = 0; i < map.length; i = i + 1)
		{
			if( map[i] < 0 ) decode_map[ map[i] + 256 ] = (byte)i;
			else decode_map[map[i]] = (byte)i;
		}

		// This for loop will decode our cyper text into our message. We read a byte from our cyper text and use 
		// that byte as an index for our decode_map. We use the value at decode_map[that index] as our value. Again,
		// if our value is negative, we add 256.
		for(int j = 0; j < bytes.length; j = j + 1)
		{
			if(bytes[j] < 0) bytes[j] = decode_map[bytes[j] + 256];
			else bytes[j] = decode_map[bytes[j]];
		}

		// Converts the decrypted message to a string and returns it
		return new String(bytes);
	}
	 
}
