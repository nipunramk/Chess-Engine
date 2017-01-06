import java.util.HashSet;

/**
 * Created by nipun.ramk on 12/22/2016.
 */
public class Rook extends Piece {
    public Rook(String square, String side) {
        super(square, side, "Rook");
    }

    @Override
    public void updatePossibleMoves() {
        super.possibleDestinations = new HashSet<>();
        super.attackingSquares = new HashSet<>();
        int row = ChessBoard.getRow(currentSquare);
        int col = ChessBoard.getCol(currentSquare);
        super.updateColumn(row, col);
        super.updateRow(row, col);

    }
}
