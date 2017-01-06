import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by nipun.ramk on 12/22/2016.
 */
public class King extends Piece {
    public King(String square, String side) {
        super(square, side, "King");
    }




    public void updatePossibleMoves() {
        super.possibleDestinations = new HashSet<>();
        super.attackingSquares = new HashSet<>();
        int row = ChessBoard.getRow(currentSquare);
        int col = ChessBoard.getCol(currentSquare);
        ArrayList<Pair<Integer, Integer>> allPossibleMoves = enumerateAllKingMoves(row, col);
        int newRow, newCol;
        if (color == "White") {
            for (Pair<Integer, Integer> possibleMove : allPossibleMoves) {
                newRow = possibleMove.getKey();
                newCol = possibleMove.getValue();
                if (ChessBoard.isValidRowCol(newRow, newCol) && !ChessBoard.whitePieceExistsAtSquare(newRow, newCol, ChessBoard.gameBoard)
                        && !ChessBoard.pieceAttackingSquare(newRow, newCol, "Black")) {
                    String newSquare = ChessBoard.convertRowColToSquare(newRow, newCol);
                    super.possibleDestinations.add(newSquare);
                }

                if (ChessBoard.isValidRowCol(newRow, newCol)) {
                    String newSquare = ChessBoard.convertRowColToSquare(newRow, newCol);
                    super.attackingSquares.add(newSquare);
                }
            }
        } else {
            for (Pair<Integer, Integer> possibleMove : allPossibleMoves) {
                newRow = possibleMove.getKey();
                newCol = possibleMove.getValue();
                if (ChessBoard.isValidRowCol(newRow, newCol) && !ChessBoard.blackPieceExistsAtSquare(newRow, newCol, ChessBoard.gameBoard)
                        && !ChessBoard.pieceAttackingSquare(newRow, newCol, "White")) {
                    String newSquare = ChessBoard.convertRowColToSquare(newRow, newCol);
                    super.possibleDestinations.add(newSquare);
                }

                if (ChessBoard.isValidRowCol(newRow, newCol)) {
                    String newSquare = ChessBoard.convertRowColToSquare(newRow, newCol);
                    super.attackingSquares.add(newSquare);
                }
            }
        }

        if (ChessBoard.canKingSideCastle(color)) {
            newRow = row;
            newCol = col + 2;
            String newSquare = ChessBoard.convertRowColToSquare(newRow, newCol);
            super.kingSideCastleSquare = newSquare;
            super.possibleDestinations.add(newSquare);
        }

        if (ChessBoard.canQueenSideCastle(color)) {
            newRow = row;
            newCol = col - 2;
            String newSquare = ChessBoard.convertRowColToSquare(newRow, newCol);
            super.queenSideCastleSquare = newSquare;
            super.possibleDestinations.add(newSquare);
        }
    }

    public ArrayList<Pair<Integer,Integer>> enumerateAllKingMoves(int row, int col) {
        ArrayList<Pair<Integer, Integer>> allPossibleMoves = new ArrayList();
        int newRow = row - 1;
        int newCol = col;
        Pair<Integer, Integer> rowCol = new Pair<>(newRow, newCol);
        allPossibleMoves.add(rowCol);
        newCol = col - 1;
        rowCol = new Pair<>(newRow, newCol);
        allPossibleMoves.add(rowCol);
        newCol = col + 1;
        rowCol = new Pair<>(newRow, newCol);
        allPossibleMoves.add(rowCol);
        newRow = row;
        rowCol = new Pair<>(newRow, newCol);
        allPossibleMoves.add(rowCol);
        newRow = row + 1;
        rowCol = new Pair<>(newRow, newCol);
        allPossibleMoves.add(rowCol);
        newCol = col;
        rowCol = new Pair<>(newRow, newCol);
        allPossibleMoves.add(rowCol);
        newCol = col - 1;
        rowCol = new Pair<>(newRow, newCol);
        allPossibleMoves.add(rowCol);
        newRow = row;
        rowCol = new Pair<>(newRow, newCol);
        allPossibleMoves.add(rowCol);
        return allPossibleMoves;
    }
}
