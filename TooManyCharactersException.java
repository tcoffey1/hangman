/* Tadhg Coffey
 * Raymond Naval
 * Project #4: Hangman
 * December 17, 2013
 */

package hangman;

public class TooManyCharactersException extends Exception{

	public TooManyCharactersException(){
		super("Too many characters inputted.");
	}
}