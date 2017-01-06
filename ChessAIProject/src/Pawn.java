import java.util.HashSet;

/**
 * Created by nipun.ramk on 12/22/2016.
 */
public class Pawn extends Piece {
    public Pawn(String square, String color) {
        super(square, color, "Pawn");


    }

    // TODO Pawn Promotion
    @Override
    public void updatePossibleMoves() {
        super.possibleDestinations = new HashSet<>();
        super.attackingSquares = new HashSet<>();
        int row = ChessBoard.getRow(currentSquare);
        int col = ChessBoard.getCol(currentSquare);
        if (color == "White") {
            int newRow = row - 1;
            // handles pawn move up one square
            if (ChessBoard.isValidRowCol(newRow, col) &&
                    !ChessBoard.pieceExistsAtSquare(newRow, col)) {
                String newSquare = ChessBoard.convertRowColToSquare(newRow, col);
                super.possibleDestinations.add(newSquare);
            }

            // handles pawn move up two squares
            if (row == 6 && !ChessBoard.pieceExistsAtSquare(newRow, col) &&
                    !ChessBoard.pieceExistsAtSquare(newRow - 1, col)) {
                String newSquare = ChessBoard.convertRowColToSquare(newRow - 1, col);
                super.possibleDestinations.add(newSquare);
            }
            // handles pawn captures
            if (ChessBoard.isValidRowCol(newRow, col - 1) &&
                    ChessBoard.blackPieceExistsAtSquare(newRow, col - 1, ChessBoard.gameBoard)) {
                String newSquare = ChessBoard.convertRowColToSquare(newRow, col - 1);
                super.possibleDestinations.add(newSquare);
            }

            if (ChessBoard.isValidRowCol(newRow, col + 1) &&
                    ChessBoard.blackPieceExistsAtSquare(newRow, col + 1, ChessBoard.gameBoard)) {
                String newSquare = ChessBoard.convertRowColToSquare(newRow, col + 1);
                super.possibleDestinations.add(newSquare);
            }

            if (ChessBoard.isValidRowCol(newRow, col + 1)) {
                String newSquare = ChessBoard.convertRowColToSquare(newRow, col + 1);
                super.attackingSquares.add(newSquare);
            }

            if (ChessBoard.isValidRowCol(newRow, col - 1)) {
                String newSquare = ChessBoard.convertRowColToSquare(newRow, col - 1);
                super.attackingSquares.add(newSquare);
            }


        } else {

            int newRow = row + 1;
            // handles pawn move up one square
            if (ChessBoard.isValidRowCol(newRow, col) &&
                    !ChessBoard.pieceExistsAtSquare(newRow, col)) {
                String newSquare = ChessBoard.convertRowColToSquare(newRow, col);
                super.possibleDestinations.add(newSquare);
            }

            // handles pawn move up two squares
            if (row == 1 && !ChessBoard.pieceExistsAtSquare(newRow, col) &&
                    !ChessBoard.pieceExistsAtSquare(newRow + 1, col)) {
                String newSquare = ChessBoard.convertRowColToSquare(newRow + 1, col);
                super.possibleDestinations.add(newSquare);
            }
            // handles pawn captures
            if (ChessBoard.isValidRowCol(newRow, col - 1) &&
                    ChessBoard.whitePieceExistsAtSquare(newRow, col - 1, ChessBoard.gameBoard)) {
                String newSquare = ChessBoard.convertRowColToSquare(newRow, col - 1);
                super.possibleDestinations.add(newSquare);
            }

            if (ChessBoard.isValidRowCol(newRow, col + 1) &&
                    ChessBoard.whitePieceExistsAtSquare(newRow, col + 1, ChessBoard.gameBoard)) {
                String newSquare = ChessBoard.convertRowColToSquare(newRow, col + 1);
                super.possibleDestinations.add(newSquare);
            }

            if (ChessBoard.isValidRowCol(newRow, col + 1)) {
                String newSquare = ChessBoard.convertRowColToSquare(newRow, col + 1);
                super.attackingSquares.add(newSquare);
            }

            if (ChessBoard.isValidRowCol(newRow, col - 1)) {
                String newSquare = ChessBoard.convertRowColToSquare(newRow, col - 1);
                super.attackingSquares.add(newSquare);
            }

        }

        if (super.enPassant) {
            super.possibleDestinations.add(super.enPassantSquare);
        }
    }
}
