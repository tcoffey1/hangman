/* Tadhg Coffey
 * Raymond Naval
 * Project #4: Hangman
 * December 17, 2013
 */

package hangman;

import java.util.*;
import java.io.*;
import java.text.NumberFormat;

public class Hangman {

    public static void main(String[] args) {
        // All variables
        char guess = ' ';
        char[] letters;
        char[] userCorrectGuesses;
        int numGuesses = 6;
        int numWordsInFile;
        int wins = 0;
        int losses = 0;
        double percent = 0.0;
        String line;
        String fileName;
        String wordToGuess;
        File dictionaryTextFile;
        
        String PlayAgainYOrN;

        Scanner fileInput;
        Scanner readDictionaryTextFile;
        Scanner guessCharUserInput;

        ArrayList<String> words;
        ArrayList<Character> allLetters;
        ArrayList<Character> allGuessesList;
        ArrayList<Character> allIncorrectGuesses;
        Random randomWordSelect;

        boolean validGuess;
        boolean promptToEnterFile = true;
        boolean playAnotherGame = true;
        boolean youWin = false;

        while (promptToEnterFile) {
            try {

                // Prompt user to enter file, have fileName String store the
                // user's input of the file name (words.txt). Create file object 
                //called dictionaryTextFile that stores the fileName String
                //(remember that it stores the words.txt file that the user 
                //inputted).
                System.out.println("Enter file:");
                fileInput = new Scanner(System.in);
                fileName = fileInput.nextLine(); // fileName is a string
                dictionaryTextFile = new File(fileName); // make a file object

                /* Create another Scanner object that takes in the file object.
                 * So instead of reading user keyboard input (like most 
                 * Scanners), it's reading from the words.txt file. */
                readDictionaryTextFile = new Scanner(dictionaryTextFile);

                //This while loop is reading each line in the 
                //readDictionaryTextFile Scanner object (see above). Each line 
                //of text is stored in a String variable called line. Then each 
                //line of text is added to a String ArrayList called words.
                words = new ArrayList<String>();
                while (readDictionaryTextFile.hasNextLine()) {
                    line = readDictionaryTextFile.nextLine();
                    words.add(line);
                }

                //If the user wants to play again, the game restarts here.
                while (playAnotherGame) {
                    // Create random object. Create int the size of the
                    // ArrayList of words.txt. wordToGuess String stores the 
                    //randomly selected word.
                    randomWordSelect = new Random();
                    numWordsInFile = randomWordSelect.nextInt(words.size());
                    wordToGuess = words.get(numWordsInFile);

                    // change the string of the random word to uppercase
                    wordToGuess = wordToGuess.toUpperCase();

                    // Create char array the length of the String wordToGuess
                    // and store each letter of the String.
                    letters = new char[wordToGuess.length()];
                    for (int i = 0; i < wordToGuess.length(); i++) {
                        letters[i] = wordToGuess.charAt(i);
                    }

                    // This Scanner object will be used for the user's guesses.
                    guessCharUserInput = new Scanner(System.in);

                    // create empty array (to hold the user's input) the same length
                    // as the randomly selected word. Fill this array with dashes by 
                    //calling the appropriate method
                    userCorrectGuesses = new char[wordToGuess.length()];
                    createDashes(userCorrectGuesses);

                    allGuessesList = new ArrayList<Character>();
                    allIncorrectGuesses = new ArrayList<Character>();
                    allLetters = new ArrayList<Character>();

                    while (numGuesses > 0 && !youWin) {
                        winLossPercentage(wins, losses, percent);
                        validGuess = false;

                        //Keep making the user guess a char until it's
                        //alphanumeric, not >1, and not a blank entry.
                        while (!validGuess) {
                            System.out.println("Guesses remaining: " + numGuesses);
                            System.out.print("Guess a letter: ");
                            String userGuess = guessCharUserInput.nextLine();

                            //debugUserGuess looks for excess chars or non-
                            //alphanumeric chars before assigning it to guess.
                            if (debugUserGuess(userGuess)) {
                                validGuess = true;
                                guess = userGuess.charAt(0);

                                //Make guess uppercase.
                                guess = Character.toUpperCase(guess);
                            }
                        }

                        //Print all user guesses.
                        System.out.println(allGuessesList = allGuesses(guess, allLetters));

                        //numGuesses reduced if letter is guessed incorrectly, 
                        //but only once.
                        if (compareGuessToWord(guess, letters, userCorrectGuesses, allGuessesList) == false
                                && allIncorrectGuesses(guess, letters, allIncorrectGuesses, allGuessesList) == false) {

                            numGuesses--;
                        }

                        /* Prints out correct guesses and unguessed
                         * letters as dashes */
                        correctGuessesAndDashesDisplay(userCorrectGuesses);

                        //If the user's correct guess array matches the actual
                        //word array, the user wins.
                        String userGuessCorrectWord = "";
                        String theWordToGuess = "";

                        for (int i = 0; i < userCorrectGuesses.length; i++) {
                            userGuessCorrectWord += userCorrectGuesses[i];
                        }
                        for (int i = 0; i < letters.length; i++) {
                            theWordToGuess += letters[i];
                        }

                        if (userGuessCorrectWord.equalsIgnoreCase(theWordToGuess)) {
                            youWin = true;
                        }
                    }
                    if (youWin) {
                        wins++;
                        System.out.println("You win. Would you like to play again? Enter y or n:");          
                    } else {
                        losses++;
                        System.out.println("Sorry, you lost.\nWould you like to play again? Enter y or n:");
                    }
                    
                    // if user enters y to play again, set booleans accordingly
                    Scanner playAgainScanner = new Scanner(System.in);
                    char playAgain = playAgainScanner.nextLine().charAt(0);

                    if (playAgain == 'y' || playAgain == 'Y') {
                        numGuesses = 6;  //reset number of guesses to original
                        youWin = false;
                        allGuessesList.clear();
                    } else if (playAgain == 'n' || playAgain == 'N') {
                        System.out.println("Goodbye");
                        playAnotherGame = false;
                        promptToEnterFile = false; // exit game
                    } else {
                        System.out.println("Would you like to play again?");
                    }
                }
            } catch (FileNotFoundException ex) {
                System.out.println("File does not exist. Please re-enter.");
            }
        }
    }

    //Calculate and display win loss percentage.
    public static void winLossPercentage(int w, int l, double p) {
        NumberFormat format = NumberFormat.getPercentInstance();
        p = (1.0) * w / (w + l);
        if (l == 0 && w == 0) {
            System.out.println("Wins: " + w + "  Losses: " + l + "  Winning percentage: 0%");
        } else {
            System.out.println("Wins: " + w + "  Losses: " + l + "  Winning percentage: " + format.format(p));
        }
    }

    //Check to see if user entered just 1 alphanumeric char.
    public static boolean debugUserGuess(String guessTotal) {
        char guess;
        int countGuessTotal = 0;
        boolean guessIsValid = false;

        //try block that checks if user entered more than one char, if user 
        //entered a non-alphanumeric char, and if the user didn't enter anything
        try {
            //Count letters in guess.
            for (int i = 0; i < guessTotal.length(); i++) {
                countGuessTotal++;
            }
            //Assign first letter in String to char.
            guess = guessTotal.charAt(0);

            if ((countGuessTotal == 1) && ((guess >= 'a' && guess <= 'z') || (guess >= 'A' && guess <= 'Z'))) {
                guessIsValid = true;
            }
            if (countGuessTotal > 1) {
                throw new TooManyCharactersException();
            }
            if (!((guess >= 'a' && guess <= 'z') || (guess >= 'A' && guess <= 'Z'))) {
                throw new CharacterNotValidException();
            }
       
        
		} 
        catch (CharacterNotValidException ex) {
			System.out.println(ex.getMessage());
		} 
		catch (TooManyCharactersException ex) {
			System.out.println(ex.getMessage());
		}
		catch (StringIndexOutOfBoundsException ex) {
			System.out.println(ex.getMessage());
		}    
        
        
   /*  
     	}    
        catch (TooManyCharactersException | CharacterNotValidException | StringIndexOutOfBoundsException ex) {
            System.out.println(ex.getMessage());
        }
*/
        return guessIsValid;
    }

    /* Display all correct guesses and unguessed letters as underscores, with
     * a space in between each.
     */
    public static void correctGuessesAndDashesDisplay(char[] usersCorrectGuesses) {
        String correctGuessesAndDashes = "";
        for (int i = 0; i < usersCorrectGuesses.length; i++) {
            correctGuessesAndDashes += usersCorrectGuesses[i] + " ";
        }
        System.out.println(correctGuessesAndDashes);
    }

    /* All incorrect guesses are stored in an array. All repeated incorrect guesses
     * are not stored in array.
     */
    public static boolean allIncorrectGuesses(char userGuess, char[] allLettersInWord, ArrayList<Character> incorrectUserGuesses, ArrayList<Character> allGuesses) {
        boolean userAlreadyGuessed = false;
        boolean guessedLetterCorrect = true;

        //If userGuess is incorrect(i.e. not a letter in the word), boolean is 
        //set to false (boolean is initially set to true).
        for (int i = 0; i < allLettersInWord.length; i++) {
            if (userGuess != allLettersInWord[i]) {
                guessedLetterCorrect = false;
            }
        }

        //If incorrect, determine if incorrect guess was already guessed
        //previously. If it was, set userAlreadyGuessed boolean to true.
        if (!guessedLetterCorrect) {
            for (int i = 0; i < incorrectUserGuesses.size(); i++) {
                if (userGuess == incorrectUserGuesses.get(i)) {
                    userAlreadyGuessed = true;

                }
            }

            //If incorrect guess wasn't guessed previously, it is added to 
            //ArrayList of incorrect guesses.
            if (!userAlreadyGuessed) {
                incorrectUserGuesses.add(userGuess);
            }
        }

        //if userAlreadyGuessed is set to false (which adds it to ArrayList of
        //incorrect guesses, this will decrement numGuesses by 1.
        return userAlreadyGuessed;

    }

    //Determine if guessed letter is correct.
    public static boolean compareGuessToWord(char userGuess, char[] allLettersInWord, char[] correctUserGuesses, ArrayList<Character> allGuessesList) {
        boolean match = false;
        // If guessed correctly, match is set to true and char array of correct
        //guesses is updated.
        for (int i = 0; i < allLettersInWord.length; i++) {
            if (userGuess == allLettersInWord[i]) {
                correctUserGuesses[i] = allLettersInWord[i];
                match = true;
            }
        }
        //If false, numGuesses decrements by 1
        return match;
    }

    // Take user's guess, turn into Character object, store in Character
    // ArrayList.
    public static ArrayList<Character> allGuesses(char c, ArrayList<Character> a) {
        // Converting the char to Character object.
        Character userGuess = new Character(c);

        // Boolean used to help figure out if the user's guess matches a char in
        // the ArrayList.
        boolean matchingChars = false;

        // If the guess matches a char already on the list, set matchingChars to
        // true. Done by looping through the ArrayList and comparing the guess
        // to each char. If they don't match, add the guess to the ArrayList.
        for (int i = 0; i < a.size(); i++) {
            if (userGuess.equals(a.get(i))) {
                matchingChars = true;
            }
        }
        // if letter has not been entered before, add it to the array
        if (!matchingChars) {
            a.add(userGuess);
        }
        return a;
    }

    // Print dashes to screen to match the number of letters in the random word.
    public static void createDashes(char[] dashes) {
        String dashesAndSpaces = "";

        // Each dash matches each letter (s.length()).
        for (int i = 0; i < dashes.length; i++) {
            dashes[i] = '_';
            dashesAndSpaces += "_ ";
        }
        System.out.println(dashesAndSpaces);
    }
}