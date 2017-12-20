import java.util.Scanner;

/**
 * Main tic tac toe class which handles the actual game
 */
public class TicTacToe {
    /**
     * Handling
     * @param value Data to check if game ended
     * @return if the game ended along with results
     */
    static Scanner sc = new Scanner(System.in);
    public static boolean checkGameResult(char value){
        if (value == 'C') {
            System.out.println("Computer Won. Better luck next time...");
            return false;
        }
        else if (value == 'P'){
            System.out.println("Player Won. Good going!");
            return false;
        }
        else if (value == 'D'){
            System.out.println("Draw. Better luck next time...");
            return false;
        }
        return true;
    }

    /**
     * Method to get valid input within a certain range
     * @param lowerBound Beginning of range
     * @param upperBound End of range
     * @param message Message to print each time to prompt message
     * @return int valid integer received from user
     */
    public static int getInput(int lowerBound, int upperBound, String message){

        int player = -1;
        do {
            System.out.print(message);
            while(!sc.hasNextInt())
            {
                System.out.print(message);
                sc.next();
            }
            player = sc.nextInt();
        } while (!(lowerBound <= player && player <= upperBound));
        return player;
    }

    /**
     * Main method
     * @param args data from console
     */
    public static void main(String[] args) {
        System.out.print("Do you want to play first or second? ");
        int player = getInput(1,2, "If first type 1, if second type 2: ");
        TicTacToeBoard board = new TicTacToeBoard();

        if (player == 2) {
            board.playComputer();
            System.out.println("Computer Played");
        }

        board.printBoard();

        while (true){
            int row = getInput(1, 3, "Enter a valid row between 1 and 3: ");
            int column = getInput(1, 3, "Enter a valid column between 1 and 3: ");
            char playerPlayed = board.playPlayer(--row, --column);
            while (playerPlayed == 'X') {
                System.out.println("Spot is already occupied. Please try again.");
                row = getInput(1, 3, "Enter a valid row between 1 and 3: ");
                column = getInput(1, 3, "Enter a valid column between 1 and 3: ");
                playerPlayed = board.playPlayer(--row, --column);
            }

            board.printBoard();
            if (!checkGameResult(playerPlayed))
                break;

            char value = board.playComputer();
            System.out.println("Computer Played");
            board.printBoard();
            if (!checkGameResult(value))
                break;
        }
    }
}
