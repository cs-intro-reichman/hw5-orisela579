public class Wordle {

    // Reads all words from dictionary filename into a String array.
    public static String[] readDictionary(String filename) {
        In in = new In(filename);
        String[] words = in.readAllStrings();
        in.close();
        return words;
    }
    // Choose a random secret word from the dictionary. 
    // Hint: Pick a random index between 0 and dict.length (not including) using Math.random()
    public static String chooseSecretWord(String[] dict) {
        String word = "";
        int i = (int) (Math.random() * (dict.length-1));
        word = dict[i];
		return word;
    }

    // Simple helper: check if letter c appears anywhere in secret (true), otherwise
    // return false.
    public static boolean containsChar(String secret, char c) {
		for(int i=0; i<secret.length(); i++){
            char chAt = c;
            if(chAt>96 && chAt<123){
                chAt = (char) (chAt - 32);
            }
                
            if(secret.charAt(i)==chAt){
                return true;
            }
        }
        return false;
    }

    // Compute feedback for a single guess into resultRow.
    // G for exact match, Y if letter appears anywhere else, _ otherwise.
    public static void computeFeedback(String secret, String guess, char[] resultRow) {
        for(int i=0; i<5; i++){
            char c = guess.charAt(i);
            if(c>96 && c<123){
                c = (char) (c-32);
            }
            if (c==secret.charAt(i)) {
                   resultRow[i] = 'G';
                }
            else if(containsChar(secret, c)){
                resultRow[i] = 'Y';
            }
            else{
                resultRow[i] = '_';
            }
        }
    }

    // Store guess string (chars) into the given row of guesses 2D array.
    // For example, of guess is HELLO, and row is 2, then after this function 
    // guesses should look like:
    // guesses[2][0] // 'H'
	// guesses[2][1] // 'E'
	// guesses[2][2] // 'L'
	// guesses[2][3] // 'L'
	// guesses[2][4] // 'O'
    public static void storeGuess(String guess, char[][] guesses, int row) {
		for(int i=0; i<5; i++){
            guesses[row][i] = guess.charAt(i);
        }
    }

    // Prints the game board up to currentRow (inclusive).
    public static void printBoard(char[][] guesses, char[][] results, int currentRow) {
        System.out.println("Current board:");
        for (int row = 0; row <= currentRow; row++) {
            System.out.print("Guess " + (row + 1) + ": ");
            for (int col = 0; col < guesses[row].length; col++) {
                System.out.print(guesses[row][col]);
            }
            System.out.print("   Result: ");
            for (int col = 0; col < results[row].length; col++) {
                System.out.print(results[row][col]);
            }
            System.out.println();
        }
        System.out.println();
    }

    // Returns true if all entries in resultRow are 'G'.
    public static boolean isAllGreen(char[] resultRow) {
		for(int i=0; i<resultRow.length; i++){
            if(resultRow[i]!= 'G'){
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {

        int WORD_LENGTH = 5;
        int MAX_ATTEMPTS = 6;
        
        // Read dictionary
        String[] dict = readDictionary("dictionary.txt");

        // Choose secret word
        String secret = chooseSecretWord(dict);

        // Prepare 2D arrays for guesses and results
        char[][] guesses = new char[6][5];
        char[][] results = new char[6][5];

        // Prepare to read from the standart input 
        In inp = new In();
        

        int attempt = 0;
        boolean won = false;

        while (attempt < MAX_ATTEMPTS && !won) {

            String guess = "";
            boolean valid = false;
            while (!valid) {
                System.out.print("Enter your guess (5-letter word): ");
                guess=inp.readString();
                if (guess.length()!=5) {
                    System.out.println("Invalid word. Please try again.");
                } else {
                    valid = true;
                }
            }
            computeFeedback(secret, guess, results[attempt]);
            storeGuess(guess, guesses, attempt);
            // Store guess and compute feedback
            // ... use storeGuess and computeFeedback

            // Print board
            printBoard(guesses, results, attempt);

            // Check win
            if (isAllGreen(results[attempt])) {
                System.out.println("Congratulations! You guessed the word in " + (attempt + 1) + " attempts.");
                won = true;
            }

            attempt++;
        }

        if (!won) {
            System.out.println("Sorry, you did not guess the word.");
            System.out.println("The secret word was: " + secret);
        }

        inp.close();
    }
}
