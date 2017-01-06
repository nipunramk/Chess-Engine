import java.util.HashSet;

/**
 * Created by nipun.ramk on 12/22/2016.
 */
public class Queen extends Piece {
    public Queen(String square, String side) {
        super(square, side, "Queen");
    }

    @Override
    public void updatePossibleMoves() {
        super.possibleDestinations = new HashSet<>();
        super.attackingSquares = new HashSet<>();
        int row = ChessBoard.getRow(currentSquare);
        int col = ChessBoard.getCol(currentSquare);
        super.updateColumn(row, col);
        super.updateRow(row, col);
        super.updateUpperLeftDiagonal(row, col);
        super.updateUpperRightDiagonal(row, col);
        super.updateLowerLeftDiagonal(row, col);
        super.updateLowerRightDiagonal(row, col);

    }
}
