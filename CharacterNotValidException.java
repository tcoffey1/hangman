/* Tadhg Coffey
 * Raymond Naval
 * Project #4: Hangman
 * December 17, 2013
 */

package hangman;

public class CharacterNotValidException extends Exception{
	
	public CharacterNotValidException(){
		super("Not a valid character.");
	}
}