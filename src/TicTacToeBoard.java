import java.util.ArrayList;
/**
 * Class which is used to create a game board and do certain game operations
 */
public class TicTacToeBoard {
    private int rows;
    private int columns;
    private char[][] board;
    private char computer = 'o';
    private char player = 'x';
    private int places_filled = 0;
    private String gameEndResults = "DCP";

    /**
     * Constructor which allows basic initialization
     */
    TicTacToeBoard(){
        rows = 3;
        columns = 3;
        board = new char[rows][columns];
        for (int i = 0; i < board.length; i++)
            board[i] = "   ".toCharArray();
    }

    /**
     * Constructor to create an object with an existing board
     * @param newBoard existing board
     */
    TicTacToeBoard (TicTacToeBoard newBoard){
        this.board =  newBoard.board;
        computer = newBoard.computer;
        player = newBoard.player;
        places_filled = newBoard.places_filled;
        rows = newBoard.rows;
        columns = newBoard.columns;
    }
    
    /**
     * Method which prints the board
     */
    public void printBoard() {
        System.out.println("Current Board: ");
        System.out.println("R/C  1   2   3  ");
        System.out.println("   +---+---+---+");
        for (int i = 0; i < board.length; i++) {
            System.out.print(" " + (i+1) + " ");
            for (int j = 0; j < board[i].length; j++) {
                System.out.print("|");
                System.out.print(" " + board[i][j] + " ");
                if (j+1 == board[i].length)
                    System.out.println("|");
            }
            System.out.println("   +---+---+---+");
        }
        System.out.println();
    }

    /**
     * Method which checks if the spot is empty
     * @param row row in which spot is to be checked
     * @param column column in which spot is to be checked
     * @return boolean if spot is empty
     */
    public boolean checkAvailability(int row, int column){
        return board[row][column] == ' ';
    }
    /**
     * Method to get all possible moves
     * @return all possible moves 
     */
    public ArrayList<ArrayList<Integer>> getPossibleMoves() {
        ArrayList<ArrayList<Integer>> moves = new ArrayList<>();
        int[][] allMoves = {{1,1}, {0,1}, {1,0}, {1,2}, {2,1}, {0,0}, {0,2}, {2,0}, {2,2}};
        for (int i = 0; i < allMoves.length; i++)
            if (checkAvailability(allMoves[i][0], allMoves[i][1])) {
                ArrayList<Integer> toInsert = new ArrayList<>();
                toInsert.add(allMoves[i][0]);
                toInsert.add(allMoves[i][1]);
                moves.add(toInsert);
            }
        return moves;
    }

    /**
     * Method to add data to a coordinate
     * @param row Row in coordinate
     * @param column Column in coordinate
     * @param value Value to be added. (x or o)
     * @return boolean whether data was added or not
     */
    public boolean addData(int row, int column, char value) {
        if (checkAvailability(row, column)) {
            places_filled++;
            board[row][column] = value;
            return true;
        }
        return false;
    }

    /**
     * Method to remove data in method
     * @param row
     * @param column
     */
    public void removeData(int row, int column) {
        board[row][column] = ' ';
        places_filled--;
    }

    /**
     * Get the data in the space
     * @param coordinate coordinate to get data from
     * @return char data at the given coordinate
     */
    private char queryData (int[] coordinate){
        return board[coordinate[0]][coordinate[1]];
    }

    /**
     * Method to check if game is won or draw or none
     * @return char whether a game is won or draw or no result
     */
    private char gameResult(){
        // character return when game is draw
        char draw = 'D';
        // character returned when game is won by computer
        char computerWon = 'C';
        // character returned when game is won by player
        char playerWon = 'P';
        // character returned when computer played a move with no draw or win as a result
        char noResult = 'N';

        int[][][] places = {{{0,0}, {0,1}, {0,2}}, {{1,0}, {1,1}, {1,2}}, {{2,0}, {2,1}, {2,2}}, // Check for 1, 2, 3
                {{0,0}, {1,0}, {2,0}}, {{0,1}, {1,1}, {2,1}}, {{0,2}, {1,2}, {2,2}}, // Check for 4, 5, 6
                {{0,0}, {1,1}, {2,2}}, {{0,2}, {1,1}, {2,0}}}; // Check for 7, 8
        for (int i = 0; i < places.length; i++){
            char first = queryData(places[i][0]);
            int j;
            if (first != ' ') {
                for (j = 1; j < places[i].length; j++) {
                    if (queryData(places[i][j]) != first)
                        break;
                }
                if (j == 3)
                    return (first == computer) ? computerWon : playerWon;
            }
        }
        if (places_filled == 9)
            return draw;

        return noResult;
    }

    /**
     * Play move for player
     * @param row row in which player wants to be placed in
     * @param column column in which player wants to be placed in
     * @return char game status
     */
    public char playPlayer(int row, int column){
        if (!addData(row, column, player))
            return 'X';
        char result = gameResult();
        if (gameEndResults.indexOf(result) != -1){
            return result;
        }
        return result;
    }

    /**
     * Method for computer to play
     * @return String if game is won or draw
     */
    public char playComputer() {

        char result = gameResult();
        if (gameEndResults.indexOf(result) != -1){
            return result;
        }
        int currentBestRow = -1;
        int currentBestColumn = -1;
        int currentBestScore = Integer.MIN_VALUE;
        ArrayList<ArrayList<Integer>> legalMoves = getPossibleMoves();
        for (int i = 0; i < legalMoves.size(); i++) {
            TicTacToeBoard newboard = new TicTacToeBoard(this);
            int row = legalMoves.get(i).get(0);
            int column = legalMoves.get(i).get(1);
            char value = computer;
            newboard.addData(row, column, value);
            int score = getFutureMoveScore(newboard, player);
            if (score > currentBestScore) {
                currentBestRow = row;
                currentBestColumn = column;
                currentBestScore = score;
            }
            newboard.removeData(row, column);
        }
        addData(currentBestRow, currentBestColumn, computer);
        result = gameResult();
        return result;
    }

    private static int getFutureMoveScore(TicTacToeBoard currentBoard, char player) {
        ArrayList<ArrayList<Integer>> legalMoves = currentBoard.getPossibleMoves();
        char result = currentBoard.gameResult();
        if (currentBoard.gameEndResults.indexOf(result) != -1){
            if (result == 'C') {
                return 1;
            }
            else if (result == 'P') {
                return -1;
            }
            else {
                return 0;
            }
        }
        ArrayList<ArrayList<Integer>> moves = new ArrayList<>();
        ArrayList<Integer> moveScores = new ArrayList<>();
        for (int i = 0; i < legalMoves.size(); i++){
            TicTacToeBoard newboard = new TicTacToeBoard(currentBoard);
            int row = legalMoves.get(i).get(0);
            int column = legalMoves.get(i).get(1);
            newboard.addData(row, column, player);

            if (player == currentBoard.computer){
                int resultMethodCall = getFutureMoveScore(newboard, currentBoard.player);
                moveScores.add(i, resultMethodCall);
            }
            else{
                int resultMethodCall = getFutureMoveScore(newboard, currentBoard.computer);
                moveScores.add(i, resultMethodCall);
            }
            newboard.removeData(row, column);
            moves.add(legalMoves.get(i));
        }

        int bestScore;
        if (player == currentBoard.computer){
            bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < moveScores.size(); i++){
                if (moveScores.get(i) > bestScore){
                    bestScore = moveScores.get(i);
                }
            }
        }
        else{
            bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < moveScores.size(); i++){
                if (moveScores.get(i) < bestScore){
                    bestScore = moveScores.get(i);
                }
            }
        }
        return bestScore;
    }
}