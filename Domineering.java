/**
 *
 * @author Magyar Melinda Barbara
 */

public class Domineering {
    private static char[][] board; // Represents the game board

    private static final int N = 3; /// The size of the board (3x3)
    private static final char EMPTY = '_'; // Represents an empty cell

    private static final char PLAYER = 'P';
    private static final char OPPONENT = 'O';

    // Constructor
    public Domineering(char[][] board) {
        this.board = new char[N][N];

        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                this.board[i][j] = board[i][j];
    }

    // Function to check the next player
    // Returns true for PLAYER, false for OPPONENT
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

        // If the number of PLAYER and OPPONENT on the board is equal,
        // the next player will always be PLAYER.
        return (opponent / 2) >= (player / 2);
    }

    // Function to check if any legal moves are left
    public static boolean hasMovesLeft(char[][] board) {
        int row = 0, column = 0;

        if (whoIsNext(board))
            column = 1;
        else
            row = 1;

        for (int i = 0; i < N - column; i++)
            for (int j = 0; j < N - column; j++)
                if (board[i][j] == EMPTY && board[i + row][j + column] == EMPTY)
                    return true;

        return false;
    }

    // Function to place a domino vertically or horizontally on the game board
    // Parameters:
    // x1 = starting row, y1 = starting column
    // x2 = next row, y2 = next column
    private static boolean putDomino(char[][] board, int x1, int y1, int x2, int y2) {
        if (N <= (x1 + 1) || N <= (y1 + 1) || board[x1 + 1][y1] != '_' || board[x1][y1 + 1] != '_'
                || board[x1][y1] != '_')
            return false;

        if (whoIsNext(board)) {
            board[x1][y1] = PLAYER;
            board[x2][y2] = PLAYER;
        } else {
            board[x1][y1] = OPPONENT;
            board[x2][y2] = OPPONENT;
        }

        return true;
    }

    // Function to delete a domino from the board
    // Parameters:
    // x1 = row, y1 = column of the domino to delete
    // x2 = next row, y2 = next column of the domino
    private static void deleteDomino(char[][] board, int x1, int y1, int x2, int y2) {
        if (x1 >= 0 && x1 < N - 1 && y1 >= 0 && y1 < N - 1) {
            board[x1][y1] = EMPTY;
            board[x2][y2] = EMPTY;
        }
    }

    // Function to evaluate the board
    // Returns +10 if no moves are left and the next player is PLAYER
    // Returns -10 if the next player is OPPONENT
    // Returns 0 if there is no solution
    private static int evaluateBoard(char[][] board) {
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

        // Maximum case scenario
        if (isMax) {
            int bestValue = Integer.MIN_VALUE;

            for (int row = 0; row < N; row++)
                for (int column = 0; column < N; column++) {
                    // Check for any remaining empty cells
                    if (board[row][column] == EMPTY) {
                        // If an empty cell is found, attempt to place the domino vertically or horizontally.
                        // Check the best value, then remove the domino.
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
            // Minimum case scenario
        } else {
            int bestValue = Integer.MAX_VALUE;

            for (int row = 0; row < N; row++)
                for (int column = 0; column < N; column++) {
                    // Check for any remaining empty cells
                    if (board[row][column] == EMPTY) {
                        // If an empty cell is found, attempt to place the domino vertically or horizontally.
                        // Check the best value, then remove the domino.
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

    // Function to find the next optimal move
    public static void findBestMove(char[][] board) {
        int bestValue = Integer.MIN_VALUE;
        int row = 0, column = 0;

        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++) {
                // Check for any remaining empty cells
                if (board[i][j] == EMPTY) {
                    // If found, attempt to identify the best move
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

    // Function to display the game board
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
