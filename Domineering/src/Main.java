/**
 *
 * @author Magyar Melinda Barbara
 */

public class Main {
    
    public static void main(String[] args) {
        
        // starting grid which can be manually set for testing
        char[][] board = new char[][] {            
            {'_', '_', '_'},
            {'O', '_', '_'},
            {'O', '_', '_'},
        };
        
        Domineering domineering = new Domineering(board);
        System.out.println(domineering.toString());
        
        if (Domineering.hasMovesLeft(board)) {
            if (Domineering.whoIsNext(board))
                System.out.println("PLAYER is the next one.\nHorizontal move.");
            else System.out.println("OPPONENT is the next one.\nVertical move.");
            
            Domineering.findBestMove(board);
        } else {
            System.out.println("No more moves!");
            System.out.println(Domineering.whoIsNext(board) ? "OPPONENT won." : "PLAYER won.");
        }
    }
    
}
