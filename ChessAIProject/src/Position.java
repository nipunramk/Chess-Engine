import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by nipun.ramk on 12/28/2016.
 */
public class Position {
    public Piece[][] board = new Piece[8][8]; // pass in copy of gameBoard
    public Position previousPosition;
    public ArrayList<Piece> gamePieces;



    public Position(Piece[][] gameBoard, Position previous, ArrayList<Piece> pieces) {
        board = ChessBoard.copyBoard(gameBoard);
        previousPosition= previous;
        gamePieces = ChessBoard.copyPiecesArrayList(pieces);
    }

    public int evaluatePosition(ArrayList<Piece> pieces, String color) {
        int material = evaluateMaterial(pieces, color);
        int pieceSquares = evaluatePieceSquare(pieces, color);
        int mobility = evaluateMobility(pieces, color);
        int kingSafety = evaluateKingSafety(pieces, color);
        return material + pieceSquares + mobility + kingSafety;
    }

    public int evaluateMaterial(ArrayList<Piece> pieces, String color) {
        int eval = 0;
        for (Piece piece : pieces) {
            if (piece.color.equals(color)) {
                if (piece.name.equals("King")) {
                    eval = eval + 20000;
                } else if (piece.name.equals("Queen")) {
                    eval = eval + 900;
                } else if (piece.name.equals("Rook")) {
                    eval = eval +  500;
                } else if (piece.name.equals("Bishop")) {
                    eval = eval + 300;
                } else if (piece.name.equals("Knight")) {
                    eval = eval + 300;
                } else {
                    eval = eval + 100;
                }
            } else {
                if (piece.name.equals("King")) {
                    eval = eval - 20000;
                } else if (piece.name.equals("Queen")) {
                    eval = eval - 900;
                } else if (piece.name.equals("Rook")) {
                    eval = eval - 500;
                } else if (piece.name.equals("Bishop") || piece.name.equals("Knight")) {
                    eval = eval - 300;
                } else {
                    eval = eval - 100;
                }
            }
        }

        return eval;
    }

    public int evaluatePieceSquare(ArrayList<Piece> pieces, String color) {
        int eval = 0;
        for (Piece piece : pieces) {
            if (piece.color.equals(color)) {
                eval += ChessEngineUtils.getSquareEvalOfPiece(piece);
            } else {
                eval -= ChessEngineUtils.getSquareEvalOfPiece(piece);
            }
        }

        return eval;
    }

    public int evaluateMobility(ArrayList<Piece> pieces, String color) {
        int eval = 0;
        for (Piece piece : pieces) {
            if (piece.color.equals(color)) {
                eval += piece.possibleDestinations.size();
            } else {
                eval -= piece.possibleDestinations.size();
            }
        }

        return 10 * eval;
    }

    public int evaluateKingSafety(ArrayList<Piece> pieces, String color) {
        String otherColor = ChessBoard.getOtherColor(color);
        King king = (King) ChessBoard.getKing(color);
        King otherKing = (King) ChessBoard.getKing(otherColor);
        int kingRow = ChessBoard.getRow(king.currentSquare);
        int kingCol = ChessBoard.getCol(king.currentSquare);
        int castlingScore = castlingScore(king) - castlingScore(otherKing);
        int pawnShieldScore = pawnShieldScore(king, color) - pawnShieldScore(otherKing, otherColor);
        int openFileSemiFileScore = openAndSemiOpenFileAroundKingScore(king, otherColor)
                - openAndSemiOpenFileAroundKingScore(otherKing, otherColor);
        int kingAttackScore = scoreKingAttack(king, color, pieces) - scoreKingAttack(otherKing, otherColor, pieces);


        return castlingScore + pawnShieldScore + openFileSemiFileScore + kingAttackScore;



    }

    public int castlingScore(King king) {
        if (!king.queenSideCastled && !king.kingSideCastled) {
            return -20;
        } else {
            return 20;
        }
    }
    public int pawnShieldScore(King king, String color) {
        int count = 0;
        int kingRow = ChessBoard.getRow(king.currentSquare);
        int kingCol = ChessBoard.getCol(king.currentSquare);
        ArrayList<Pair<Integer, Integer>> moves = king.enumerateAllKingMoves(kingRow, kingCol);
        for (Pair<Integer, Integer> move : moves) {
            int row = move.getKey();
            int col = move.getKey();
            if (color.equals("White") && row < kingRow) {
                if (ChessBoard.pieceOfNameAndColorExistsAtSquare(row, col, color, "Pawn")
                        || ChessBoard.pieceOfNameAndColorExistsAtSquare(row - 1, col, color, "Pawn")) {
                    count += 1;
                }

                if (ChessBoard.pieceOfNameAndColorExistsAtSquare(row, col, "Black", "Pawn")
                        || ChessBoard.pieceOfNameAndColorExistsAtSquare(row - 1, col, "Black", "Pawn")
                        || ChessBoard.pieceOfNameAndColorExistsAtSquare(row - 2, col, "Black", "Pawn")) {
                    count -= 1;
                }
            } else if (color.equals("Black") && row > kingRow) {
                if (ChessBoard.pieceOfNameAndColorExistsAtSquare(row, col, color, "Pawn")
                        || ChessBoard.pieceOfNameAndColorExistsAtSquare(row + 1, col, color, "Pawn")) {
                    count += 1;
                }

                if (ChessBoard.pieceOfNameAndColorExistsAtSquare(row, col, "White", "Pawn")
                        || ChessBoard.pieceOfNameAndColorExistsAtSquare(row + 1, col, "White", "Pawn")
                        || ChessBoard.pieceOfNameAndColorExistsAtSquare(row + 2, col, "White", "Pawn")) {
                    count -= 1;
                }
            }
        }

        return count * 10;

    }

    public int openAndSemiOpenFileAroundKingScore(King king, String color) {
        int eval = 0;
        int kingCol = ChessBoard.getCol(king.currentSquare);
        int kingRow = ChessBoard.getRow(king.currentSquare);

        if (ChessBoard.isValidRowCol(kingRow, kingCol) && ChessEngineUtils.isOpenFile(kingCol - 1)) {
            eval -= 50;
        } else if (ChessBoard.isValidRowCol(kingRow, kingCol) && ChessEngineUtils.isSemiOpenFile(kingCol - 1, color)) {
            eval -= 25;
        }

        if (ChessBoard.isValidRowCol(kingRow, kingCol) && ChessEngineUtils.isOpenFile(kingCol)) {
            eval -= 50;
        } else if (ChessBoard.isValidRowCol(kingRow, kingCol) && ChessEngineUtils.isSemiOpenFile(kingCol, color)) {
            eval -= 25;
        }

        if (ChessBoard.isValidRowCol(kingRow, kingCol) && ChessEngineUtils.isOpenFile(kingCol + 1)) {
            eval -= 50;
        } else if (ChessBoard.isValidRowCol(kingRow, kingCol) && ChessEngineUtils.isSemiOpenFile(kingCol + 1, color)) {
            eval -= 25;
        }

        return eval;



    }

    // scores the color's King on a negative scale of how much attack from the opponent it is under
    // smaller scores (more negative) means that the opponent has a larger attack
    public int scoreKingAttack(King king, String color, ArrayList<Piece> pieces) {
        ArrayList<Pair<Integer, Integer>> kingAttackZone = createKingAttackingZone(king, color);
        String otherColor = ChessBoard.getOtherColor(color);
        boolean isPieceAttackingZone = false;
        int valueOfAttacks = 0;
        int numberOfAttackers = 0;
        for (Piece piece : pieces) {
            if (piece.color.equals(color) || piece.name.equals("Pawn") || piece.name.equals("King")) {
                continue;
            }

            for (String square : piece.attackingSquares) {
                int row = ChessBoard.getRow(square);
                int col = ChessBoard.getCol(square);
                Pair<Integer, Integer> p = new Pair<Integer, Integer>(row, col);
                if (kingAttackZone.contains(p)) {
                    valueOfAttacks += ChessEngineUtils.pieceAttackWeight.get(piece.name);
                    isPieceAttackingZone = true;
                }

            }

            if (isPieceAttackingZone) {
                numberOfAttackers += 1;
                isPieceAttackingZone = false;
            }
        }

        int totalWeight = valueOfAttacks * ChessEngineUtils.numberOfAttackersWeight.get(numberOfAttackers);
        return -(totalWeight / 100);
    }

    public ArrayList<Pair<Integer, Integer>> createKingAttackingZone(King king, String color) {
        int kingCol = ChessBoard.getCol(king.currentSquare);
        int kingRow = ChessBoard.getRow(king.currentSquare);
        ArrayList<Pair<Integer, Integer>> attackZone = new ArrayList<>();
        Pair<Integer, Integer> pair;
        if (color.equals("White")) {
            for (int i = kingRow; i > kingRow - 4; i--) {
                if (ChessBoard.isValidRowCol(i, kingCol - 1)) {
                    pair = new Pair<>(i, kingCol - 1);
                    attackZone.add(pair);
                }

                if (ChessBoard.isValidRowCol(i, kingCol)) {
                    pair = new Pair<>(i, kingCol);
                    attackZone.add(pair);
                }

                if (ChessBoard.isValidRowCol(i, kingCol + 1)) {
                    pair = new Pair<>(i, kingCol + 1);
                    attackZone.add(pair);
                }


            }
        } else {
            for (int i = kingRow; i < kingRow + 4; i++) {
                if (ChessBoard.isValidRowCol(i, kingCol - 1)) {
                    pair = new Pair<>(i, kingCol - 1);
                    attackZone.add(pair);
                }

                if (ChessBoard.isValidRowCol(i, kingCol)) {
                    pair = new Pair<>(i, kingCol);
                    attackZone.add(pair);
                }

                if (ChessBoard.isValidRowCol(i, kingCol + 1)) {
                    pair = new Pair<>(i, kingCol + 1);
                    attackZone.add(pair);
                }


            }
        }

        return attackZone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        return Arrays.deepEquals(board, position.board);

    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }
}
