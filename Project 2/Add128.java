// This is the add 128 class to be used for Assignment 3

import java.util.Random;

public class Add128 implements SymCipher
{
	// Dont forget: bytes have a range -128 to 127
	// This will hold our key
	private byte key[];

	// Random num generator
	private Random rand = new Random();
	

	// Constructor 1 to be used by the SecureChatClient
	// This constructor generates a 128 random bytes and 
	// assigns each to an index in our byte array key.
	public Add128()
	{	
		// Initialize byte key
		key = new byte[128];
		rand.nextBytes(key);
	}


	// Constructor 2 to be used by the SecureChatServer
	// This constructor receives a byte array as a parameter
	// and sets the key equal to that parameter.
	public Add128( byte bytes[] )
	{	
		// Makes our key equal to the argument key received from the constructor
		key = bytes;
	}


	// Getter method to retrieve the key
	public byte [] getKey()
	{
		return key;
	}


	// Encrypts message S  and returns an encrypted byte array version of S
	public byte [] encode(String S)
	{
		// Converts String paramater S into a byte array
		byte encryptedMessage[] = S.getBytes();

		// We now encrypt our String S. We do this by adding each byte in our
		// key to the corresponding byte in our String byte array. 
		//
		// If the message S is less than the length of our key, then we stop 
		// adding our key bytes once we reach the end of the String byte array. 
		//
		// If the message S is greater than the length of our key, then we recycle
		// through the key as many times as needed to encrypt the entire message.
		
		// Keeps track of our key index
		int key_index = 0; 

		for( int i = 0; i < encryptedMessage.length; i = i + 1 )
		{
			// If we've reached the end of our key an still have to encrypt, then
			// we wrap around and reset our key_index.
			if( key_index > ( key.length - 1 ) )
			{
				key_index = 0;
			}

			encryptedMessage[i] = (byte)(encryptedMessage[i] + key[key_index]);
			key_index = key_index + 1;
		}	

		return encryptedMessage;
	}


	// Decrypts the byte[] array and returns a string
	public String decode(byte [] bytes)
	{	
		// We decrypt our message in bytes[]. We do this by subtracting each byte in our
		// key to the corresponding byte in our String byte array. 
		//
		// If the message bytes[] is less than the length of our key, then we stop 
		// adding our key bytes once we reach the end of byte[]. 
		//
		// If the message bytes is greater than the length of our key, then we recycle
		// through the key as many times as needed to encrypt the entire message.
		
		// Keeps track of our key index
		int key_index = 0; 

		for( int i = 0; i < bytes.length; i = i + 1 )
		{
			// If we've reached the end of our key an still have to decrypt, then
			// we wrap around and reset our key_index.
			if( key_index > ( key.length - 1 ) )
			{
				key_index = 0;
			}

			bytes[i] = (byte)(bytes[i] - key[key_index]);
			key_index = key_index + 1;
		}	

		// Converts the decrypted message to a string and returns it
		return new String(bytes);
	}

}