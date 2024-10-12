/**
 *
 * @author Magyar Melinda Barbara
 */

public class Main {

    public static void main(String[] args) {

        // Starting grid that can be manually set for testing.
        char[][] board = new char[][] {
                { '_', '_', '_' },
                { 'O', '_', '_' },
                { 'O', '_', '_' },
        };

        Domineering domineering = new Domineering(board);
        System.out.println(domineering.toString());

        if (Domineering.hasMovesLeft(board)) {
            if (Domineering.whoIsNext(board))
                System.out.println("PLAYER is next.\nHorizontal move.");
            else
                System.out.println("OPPONENT is next.\nVertical move.");

            Domineering.findBestMove(board);
        } else {
            System.out.println("There are no more moves!");
            System.out.println(Domineering.whoIsNext(board) ? "The OPPONENT has won." : "The PLAYER has won.");
        }
    }

}
