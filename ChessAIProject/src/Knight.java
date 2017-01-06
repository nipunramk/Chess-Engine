import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by nipun.ramk on 12/22/2016.
 */
public class Knight extends Piece {
    public Knight(String square, String color) {
        super(square, color, "Knight");

    }

    @Override
    public void updatePossibleMoves() {
        super.possibleDestinations = new HashSet<>();
        super.attackingSquares = new HashSet<>();
        int row = ChessBoard.getRow(currentSquare);
        int col = ChessBoard.getCol(currentSquare);
        ArrayList<Pair<Integer, Integer>> allPossibleMoves = enumerateAllKnightMoves(row, col);
        int newRow, newCol;
        if (color == "White") {
            for (Pair<Integer, Integer> possibleMove : allPossibleMoves) {
                newRow = possibleMove.getKey();
                newCol = possibleMove.getValue();
                if (ChessBoard.isValidRowCol(newRow, newCol) && !ChessBoard.whitePieceExistsAtSquare(newRow, newCol, ChessBoard.gameBoard)) {
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
                if (ChessBoard.isValidRowCol(newRow, newCol) && !ChessBoard.blackPieceExistsAtSquare(newRow, newCol, ChessBoard.gameBoard)) {
                    String newSquare = ChessBoard.convertRowColToSquare(newRow, newCol);
                    super.possibleDestinations.add(newSquare);
                }

                if (ChessBoard.isValidRowCol(newRow, newCol)) {
                    String newSquare = ChessBoard.convertRowColToSquare(newRow, newCol);
                    super.attackingSquares.add(newSquare);
                }

            }
        }

//        System.out.println("Printing Knight possible moves ... ");
//        System.out.println(currentSquare);
//        System.out.println(super.possibleDestinations);


    }
    // Returns an ArrayList of Pairs that constain row, column coordinates of 8 possible knight moves
    public ArrayList<Pair<Integer, Integer>> enumerateAllKnightMoves(int row, int col) {
        ArrayList<Pair<Integer, Integer>> allPossibleMoves = new ArrayList();
        int newRow = row - 2;
        int newCol = col + 1;
        Pair<Integer, Integer> rowCol = new Pair<>(newRow, newCol);
        allPossibleMoves.add(rowCol);
        newCol = col - 1;
        rowCol = new Pair<>(newRow, newCol);
        allPossibleMoves.add(rowCol);
        newRow = row + 2;
        rowCol = new Pair<>(newRow, newCol);
        allPossibleMoves.add(rowCol);
        newCol = col + 1;
        rowCol = new Pair<>(newRow, newCol);
        allPossibleMoves.add(rowCol);
        newRow = row + 1;
        newCol = col + 2;
        rowCol = new Pair<>(newRow, newCol);
        allPossibleMoves.add(rowCol);
        newRow = row - 1;
        rowCol = new Pair<>(newRow, newCol);
        allPossibleMoves.add(rowCol);
        newCol = col - 2;
        rowCol = new Pair<>(newRow, newCol);
        allPossibleMoves.add(rowCol);
        newRow = row + 1;
        rowCol = new Pair<>(newRow, newCol);
        allPossibleMoves.add(rowCol);
        return allPossibleMoves;



    }


}
