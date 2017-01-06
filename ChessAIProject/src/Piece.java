import java.util.HashSet;

/**
 * Created by nipun.ramk on 12/22/2016.
 */

public class Piece implements Cloneable {
    public String currentSquare;
    public String color;
    public String name;
    public HashSet<String> possibleDestinations;
    public HashSet<String> attackingSquares;
    public boolean enPassant = false;
    public String enPassantSquare = ""; // possible attack square as a result of En Passant rule
    public String enPassantOpponentPawnSquare = "";
    public String kingSideCastleSquare = "";
    public String queenSideCastleSquare = "";
    public boolean kingMoved = false;
    public boolean rookMoved = false;
    public String originalSquare;
    public boolean kingSideCastled = false;
    public boolean queenSideCastled = false;

    public Piece(String square, String side, String type) {
        currentSquare = square;
        color = side;
        name = type;
        possibleDestinations = new HashSet<>();
        attackingSquares = new HashSet<>();
        originalSquare = square;
    }

    public void updatePossibleMoves() {}

    public void updateUpperLeftDiagonal(int row, int col) {
        int newRow = row - 1;
        int newCol = col - 1;
        // upper left diagonal
        while (ChessBoard.isValidRowCol(newRow, newCol)) {
            if (color == "White" && ChessBoard.whitePieceExistsAtSquare(newRow, newCol, ChessBoard.gameBoard)) {
                String square = ChessBoard.convertRowColToSquare(newRow, newCol);
                attackingSquares.add(square);
                break;
            } else if (color == "White" && ChessBoard.blackPieceExistsAtSquare(newRow, newCol, ChessBoard.gameBoard)) {
                String square = ChessBoard.convertRowColToSquare(newRow, newCol);
                possibleDestinations.add(square);
                attackingSquares.add(square);
                break;
            } else if (color == "Black" && ChessBoard.blackPieceExistsAtSquare(newRow, newCol, ChessBoard.gameBoard)) {
                String square = ChessBoard.convertRowColToSquare(newRow, newCol);
                attackingSquares.add(square);
                break;
            } else if (color == "Black" && ChessBoard.whitePieceExistsAtSquare(newRow, newCol, ChessBoard.gameBoard)) {
                String square = ChessBoard.convertRowColToSquare(newRow, newCol);
                possibleDestinations.add(square);
                attackingSquares.add(square);
                break;
            } else {
                String square = ChessBoard.convertRowColToSquare(newRow, newCol);
                possibleDestinations.add(square);
                attackingSquares.add(square);
                newRow -= 1;
                newCol -= 1;
            }
        }
    }

    public void updateUpperRightDiagonal(int row, int col) {
        int newRow = row - 1;
        int newCol = col + 1;
        // upper left diagonal
        while (ChessBoard.isValidRowCol(newRow, newCol)) {
            if (color == "White" && ChessBoard.whitePieceExistsAtSquare(newRow, newCol, ChessBoard.gameBoard)) {
                String square = ChessBoard.convertRowColToSquare(newRow, newCol);
                attackingSquares.add(square);
                break;
            } else if (color == "White" && ChessBoard.blackPieceExistsAtSquare(newRow, newCol, ChessBoard.gameBoard)) {
                String square = ChessBoard.convertRowColToSquare(newRow, newCol);
                possibleDestinations.add(square);
                attackingSquares.add(square);
                break;
            } else if (color == "Black" && ChessBoard.blackPieceExistsAtSquare(newRow, newCol, ChessBoard.gameBoard)) {
                String square = ChessBoard.convertRowColToSquare(newRow, newCol);
                attackingSquares.add(square);
                break;
            } else if (color == "Black" && ChessBoard.whitePieceExistsAtSquare(newRow, newCol, ChessBoard.gameBoard)) {
                String square = ChessBoard.convertRowColToSquare(newRow, newCol);
                possibleDestinations.add(square);
                attackingSquares.add(square);
                break;
            } else {
                String square = ChessBoard.convertRowColToSquare(newRow, newCol);
                possibleDestinations.add(square);
                attackingSquares.add(square);
                newRow -= 1;
                newCol += 1;
            }
        }
    }

    public void updateLowerLeftDiagonal(int row, int col) {
        int newRow = row + 1;
        int newCol = col - 1;
        // upper left diagonal
        while (ChessBoard.isValidRowCol(newRow, newCol)) {
            if (color == "White" && ChessBoard.whitePieceExistsAtSquare(newRow, newCol, ChessBoard.gameBoard)) {
                String square = ChessBoard.convertRowColToSquare(newRow, newCol);
                attackingSquares.add(square);
                break;
            } else if (color == "White" && ChessBoard.blackPieceExistsAtSquare(newRow, newCol, ChessBoard.gameBoard)) {
                String square = ChessBoard.convertRowColToSquare(newRow, newCol);
                possibleDestinations.add(square);
                attackingSquares.add(square);
                break;
            } else if (color == "Black" && ChessBoard.blackPieceExistsAtSquare(newRow, newCol, ChessBoard.gameBoard)) {
                String square = ChessBoard.convertRowColToSquare(newRow, newCol);
                attackingSquares.add(square);
                break;
            } else if (color == "Black" && ChessBoard.whitePieceExistsAtSquare(newRow, newCol, ChessBoard.gameBoard)) {
                String square = ChessBoard.convertRowColToSquare(newRow, newCol);
                possibleDestinations.add(square);
                attackingSquares.add(square);
                break;
            } else {
                String square = ChessBoard.convertRowColToSquare(newRow, newCol);
                possibleDestinations.add(square);
                attackingSquares.add(square);
                newRow += 1;
                newCol -= 1;
            }
        }
    }

    public void updateLowerRightDiagonal(int row, int col) {
        int newRow = row + 1;
        int newCol = col + 1;
        // upper left diagonal
        while (ChessBoard.isValidRowCol(newRow, newCol)) {
            if (color == "White" && ChessBoard.whitePieceExistsAtSquare(newRow, newCol, ChessBoard.gameBoard)) {
                String square = ChessBoard.convertRowColToSquare(newRow, newCol);
                attackingSquares.add(square);
                break;
            } else if (color == "White" && ChessBoard.blackPieceExistsAtSquare(newRow, newCol, ChessBoard.gameBoard)) {
                String square = ChessBoard.convertRowColToSquare(newRow, newCol);
                possibleDestinations.add(square);
                attackingSquares.add(square);
                break;
            } else if (color == "Black" && ChessBoard.blackPieceExistsAtSquare(newRow, newCol, ChessBoard.gameBoard)) {
                String square = ChessBoard.convertRowColToSquare(newRow, newCol);
                attackingSquares.add(square);
                break;
            } else if (color == "Black" && ChessBoard.whitePieceExistsAtSquare(newRow, newCol, ChessBoard.gameBoard)) {
                String square = ChessBoard.convertRowColToSquare(newRow, newCol);
                possibleDestinations.add(square);
                attackingSquares.add(square);
                break;
            } else {
                String square = ChessBoard.convertRowColToSquare(newRow, newCol);
                possibleDestinations.add(square);
                attackingSquares.add(square);
                newRow += 1;
                newCol += 1;
            }
        }
    }

    public void updateColumn(int row, int col) {

        int newRow = row + 1;
        int newCol = col;
        // upper left diagonal
        while (ChessBoard.isValidRowCol(newRow, newCol)) {
            if (color == "White" && ChessBoard.whitePieceExistsAtSquare(newRow, newCol, ChessBoard.gameBoard)) {
                String square = ChessBoard.convertRowColToSquare(newRow, newCol);
                attackingSquares.add(square);
                break;
            } else if (color == "White" && ChessBoard.blackPieceExistsAtSquare(newRow, newCol, ChessBoard.gameBoard)) {
                String square = ChessBoard.convertRowColToSquare(newRow, newCol);
                possibleDestinations.add(square);
                attackingSquares.add(square);
                break;
            } else if (color == "Black" && ChessBoard.blackPieceExistsAtSquare(newRow, newCol, ChessBoard.gameBoard)) {
                String square = ChessBoard.convertRowColToSquare(newRow, newCol);
                attackingSquares.add(square);
                break;
            } else if (color == "Black" && ChessBoard.whitePieceExistsAtSquare(newRow, newCol, ChessBoard.gameBoard)) {
                String square = ChessBoard.convertRowColToSquare(newRow, newCol);
                possibleDestinations.add(square);
                attackingSquares.add(square);
                break;
            } else {
                String square = ChessBoard.convertRowColToSquare(newRow, newCol);
                possibleDestinations.add(square);
                attackingSquares.add(square);
                newRow += 1;
            }
        }

        newRow = row - 1;

        while (ChessBoard.isValidRowCol(newRow, newCol)) {
            if (color == "White" && ChessBoard.whitePieceExistsAtSquare(newRow, newCol, ChessBoard.gameBoard)) {
                String square = ChessBoard.convertRowColToSquare(newRow, newCol);
                attackingSquares.add(square);
                break;
            } else if (color == "White" && ChessBoard.blackPieceExistsAtSquare(newRow, newCol, ChessBoard.gameBoard)) {
                String square = ChessBoard.convertRowColToSquare(newRow, newCol);
                possibleDestinations.add(square);
                attackingSquares.add(square);
                break;
            } else if (color == "Black" && ChessBoard.blackPieceExistsAtSquare(newRow, newCol, ChessBoard.gameBoard)) {
                String square = ChessBoard.convertRowColToSquare(newRow, newCol);
                attackingSquares.add(square);
                break;
            } else if (color == "Black" && ChessBoard.whitePieceExistsAtSquare(newRow, newCol, ChessBoard.gameBoard)) {
                String square = ChessBoard.convertRowColToSquare(newRow, newCol);
                possibleDestinations.add(square);
                attackingSquares.add(square);
                break;
            } else {
                String square = ChessBoard.convertRowColToSquare(newRow, newCol);
                possibleDestinations.add(square);
                attackingSquares.add(square);
                newRow -= 1;
            }
        }

    }

    public void updateRow(int row, int col) {
        int newRow = row;
        int newCol = col + 1;
        // upper left diagonal
        while (ChessBoard.isValidRowCol(newRow, newCol)) {
            if (color == "White" && ChessBoard.whitePieceExistsAtSquare(newRow, newCol, ChessBoard.gameBoard)) {
                String square = ChessBoard.convertRowColToSquare(newRow, newCol);
                attackingSquares.add(square);
                break;
            } else if (color == "White" && ChessBoard.blackPieceExistsAtSquare(newRow, newCol, ChessBoard.gameBoard)) {
                String square = ChessBoard.convertRowColToSquare(newRow, newCol);
                possibleDestinations.add(square);
                attackingSquares.add(square);
                break;
            } else if (color == "Black" && ChessBoard.blackPieceExistsAtSquare(newRow, newCol, ChessBoard.gameBoard)) {
                String square = ChessBoard.convertRowColToSquare(newRow, newCol);
                attackingSquares.add(square);
                break;
            } else if (color == "Black" && ChessBoard.whitePieceExistsAtSquare(newRow, newCol, ChessBoard.gameBoard)) {
                String square = ChessBoard.convertRowColToSquare(newRow, newCol);
                possibleDestinations.add(square);
                attackingSquares.add(square);
                break;
            } else {
                String square = ChessBoard.convertRowColToSquare(newRow, newCol);
                possibleDestinations.add(square);
                attackingSquares.add(square);
                newCol += 1;
            }
        }

        newCol = col - 1;

        while (ChessBoard.isValidRowCol(newRow, newCol)) {
            if (color == "White" && ChessBoard.whitePieceExistsAtSquare(newRow, newCol, ChessBoard.gameBoard)) {
                String square = ChessBoard.convertRowColToSquare(newRow, newCol);
                attackingSquares.add(square);
                break;
            } else if (color == "White" && ChessBoard.blackPieceExistsAtSquare(newRow, newCol, ChessBoard.gameBoard)) {
                String square = ChessBoard.convertRowColToSquare(newRow, newCol);
                possibleDestinations.add(square);
                attackingSquares.add(square);
                break;
            } else if (color == "Black" && ChessBoard.blackPieceExistsAtSquare(newRow, newCol, ChessBoard.gameBoard)) {
                String square = ChessBoard.convertRowColToSquare(newRow, newCol);
                attackingSquares.add(square);
                break;
            } else if (color == "Black" && ChessBoard.whitePieceExistsAtSquare(newRow, newCol, ChessBoard.gameBoard)) {
                String square = ChessBoard.convertRowColToSquare(newRow, newCol);
                possibleDestinations.add(square);
                attackingSquares.add(square);
                break;
            } else {
                String square = ChessBoard.convertRowColToSquare(newRow, newCol);
                possibleDestinations.add(square);
                attackingSquares.add(square);
                newCol -= 1;
            }
        }
    }



    @Override
    public String toString() {
        return name + "{" +
                "currentSquare='" + currentSquare + '\'' +
                ", color='" + color + '\'' + " " + enPassantOpponentPawnSquare +
                '}' + possibleDestinations.toString();
    }

    public Piece clone() {
        Piece piece = new Piece(currentSquare, color, name);
        piece.possibleDestinations = (HashSet<String>) possibleDestinations.clone();
        piece.attackingSquares = (HashSet<String>) possibleDestinations.clone();
        piece.enPassant = enPassant;
        piece.enPassantOpponentPawnSquare = enPassantOpponentPawnSquare;
        piece.enPassantSquare = enPassantSquare;
        piece.kingMoved = kingMoved;
        piece.kingSideCastleSquare = kingSideCastleSquare;
        piece.queenSideCastleSquare = queenSideCastleSquare;
        piece.rookMoved = rookMoved;
        piece.originalSquare = originalSquare;
        piece.kingSideCastled = kingSideCastled;
        piece.queenSideCastled = queenSideCastled;
        return piece;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Piece piece = (Piece) o;

        if (enPassant != piece.enPassant) return false;
        if (kingMoved != piece.kingMoved) return false;
        if (rookMoved != piece.rookMoved) return false;
        if (!currentSquare.equals(piece.currentSquare)) return false;
        if (!color.equals(piece.color)) return false;
        return name.equals(piece.name);

    }

    @Override
    public int hashCode() {
        int result = currentSquare.hashCode();
        result = 31 * result + color.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + (enPassant ? 1 : 0);
        result = 31 * result + (kingMoved ? 1 : 0);
        result = 31 * result + (rookMoved ? 1 : 0);
        return result;
    }
}
