/**
 *
 * @author Magyar Melinda Barbara
 */

public class Domineering {
    private static char[][] board; // game board
    
    private static final int N = 3; // the size of the board
    private static final char EMPTY = '_'; // empty cell
    
    private static final char PLAYER = 'P'; 
    private static final char OPPONENT = 'O';

    // the constructor
    public Domineering(char[][] board) {
        this.board = new char[N][N];
        
        for (int i = 0; i < N; i++) 
            for (int j = 0; j < N; j++) 
                this.board[i][j] = board[i][j];
    }

    // function to check who is the next player
    // true: PLAYER, false: OPPONENT
    public static boolean whoIsNext(char[][] board) {
        int opponent = 0;
        int player = 0;
        
        for (int i = 0; i < N; i++) 
            for (int j = 0; j < N; j++) {
                if (board[i][j] == PLAYER)
                    player++;
                else if (board[i][j] == OPPONENT)
                    opponent++;
            }
              
        // if the number of PLAYER and OPPONENT on
        // the board are the same, then the next one
        // is going to be the PLAYER - no matter what
        return (opponent / 2) >= (player / 2);
    }
    
    // function to check if there are any legal moves left
    public static boolean hasMovesLeft(char[][] board) {
        int row = 0, column = 0;
        
        if (whoIsNext(board))
            column = 1;
        else row = 1;
        
        for (int i = 0; i < N - column; i++) 
            for (int j = 0; j < N - column; j++) 
                if (board[i][j] == EMPTY && board[i + row][j + column] == EMPTY)
                    return true;
        
        return false;
    }
    
    // function to place the domino vertically
    // or horizontally on the gameboard
    // x1 = row, y1 = column
    // x2 = next row, y2 = next column
    private static boolean putDomino(char[][] board, int x1, int y1, int x2, int y2) {
        if (N <= (x1 + 1) || N <= (y1 + 1) || board[x1 + 1][y1] != '_' || board[x1][y1 + 1] != '_' || board[x1][y1] != '_' )
            return false;

        if (whoIsNext(board)){
            board[x1][y1] = PLAYER;
            board[x2][y2] = PLAYER;
        }   else {
            board[x1][y1] = OPPONENT;
            board[x2][y2] = OPPONENT;
        }
        
        return true;
    }
    
    // function to delete a domino from the board
    // x1 = row, y1 = column
    // x2 = next row, y2 = next column
    private static void deleteDomino(char[][] board, int x1, int y1, int x2, int y2) {
        if (x1 >= 0 && x1 < N - 1  && y1 >= 0 && y1 < N - 1) {
            board[x1][y1] = EMPTY;
            board[x2][y2] = EMPTY;
        }
    }
    
    // function to evaluate the board
    // if there is no more moves left and the next one is PLAYER: +10
    // if the OPPONENT is next: -10
    // if there is no solution: 0 
    private static int evaluateBoard (char[][] board) {
        if (whoIsNext(board) && !hasMovesLeft(board))
            return +10;
        else if (!whoIsNext(board) && !hasMovesLeft(board))
            return -10;
        
        return 0; 
    }
    
    private static int miniMax(char[][] board, boolean isMax) {
        int value = evaluateBoard(board);
        
        if (value == 10 || value == -10) 
            return value;
        
        // Max case
        if (isMax) {
            int bestValue = Integer.MIN_VALUE;
            
            for (int row = 0; row < N; row++) 
                for (int column = 0; column < N; column++) {
                    // check if there is any empty cell left
                    if (board[row][column] == EMPTY) {
                        // if found; try to replace it vertically or horizontally
                        // with the possible domino, check best value then remove the domino
                        if (putDomino(board, row, column, row + 1, column)) {
                            bestValue = Math.max(bestValue, miniMax(board, !isMax));
                            
                            deleteDomino(board, row, column, row + 1, column);
                        } else if (putDomino(board, row, column, row, column + 1)) {
                            bestValue = Math.max(bestValue, miniMax(board, !isMax));
                            
                            deleteDomino(board, row, column, row, column + 1);
                        }
                    }
                }
            
            return bestValue;
        // Min case
        } else {
            int bestValue = Integer.MAX_VALUE;
            
            for (int row = 0; row < N; row++) 
                for (int column = 0; column < N; column++) {
                    // check if there is any empty cell left
                    if (board[row][column] == EMPTY) {
                        // if found; try to replace it vertically or horizontally
                        // with the possible domino, check best value then remove the domino
                        if (putDomino(board, row, column, row + 1, column)) {
                            bestValue = Math.min(bestValue, miniMax(board, isMax));
                            
                            deleteDomino(board, row, column, row + 1, column);      
                        } else if (putDomino(board, row, column, row, column + 1)) {
                            bestValue = Math.min(bestValue, miniMax(board, isMax));
                            
                            deleteDomino(board, row, column, row, column + 1);
                        }
                    }
                }
            
            return bestValue;
        }
    }
    
    // function to find the next best optimal move
    public static void findBestMove(char[][] board) {
        int bestValue = Integer.MIN_VALUE;
        int row = 0, column = 0;
        
        for (int i = 0; i < N; i++) 
            for (int j = 0; j < N; j++) {
                // check if there is any empty cell left
                if (board[i][j] == EMPTY) {
                    // if found try to find the best move
                    if (putDomino(board, i, j, i + 1, j)) {
                        int moveValue = miniMax(board, false);
                        deleteDomino(board, i, j, i + 1, j);
                        
                        if (moveValue > bestValue) {
                            row = i;
                            column = j;
                            bestValue = moveValue;
                        }
                        
                    } else if (putDomino(board, i, j, i, j + 1)) {
                        int moveValue = miniMax(board, false);
                        deleteDomino(board, i, j, i, j + 1);
                        
                        if (moveValue > bestValue) {
                            row = i;
                            column = j;
                            bestValue = moveValue;
                        }
                    }
                }
            }
        
        System.out.println("DOMINO:[ROW,COLUMN]");
        System.out.println("[" + (row + 1) + "," + (column + 1) + "]");
    }
    
    // function to display the board
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) 
                sb.append(board[i][j]).append(" ");
            
            sb.append("\n");
        }
        
        return sb.toString();
    } 
}
