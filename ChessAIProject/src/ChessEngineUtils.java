import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by nipun.ramk on 12/30/2016.
 */
public class ChessEngineUtils {
    public static int[][] whitePawnSquareTable = new int[8][8];
    public static int[][] blackPawnSquareTable = new int[8][8];
    public static int[][] whiteKnightSquareTable = new int[8][8];
    public static int[][] blackKnightSquareTable = new int[8][8];
    public static int[][] whiteBishopSquareTable = new int[8][8];
    public static int[][] blackBishopSquareTable = new int[8][8];
    public static int[][] whiteRookSquareTable = new int[8][8];
    public static int[][] blackRookSquareTable = new int[8][8];
    public static int[][] whiteQueenSquareTable = new int[8][8];
    public static int[][] blackQueenSquareTable = new int[8][8];
    public static int[][] whiteKingSquareTable = new int[8][8];
    public static int[][] blackKingSquareTable = new int[8][8];
    public static HashMap<String, int[][]> blackMap = new HashMap<String, int[][]>();
    public static HashMap<String, int[][]> whiteMap = new HashMap<String, int[][]>();
    public static ArrayList<Piece> pawns = new ArrayList<>();
    public static HashMap<String, Integer> pieceAttackWeight = new HashMap<>();
    public static HashMap<Integer, Integer> numberOfAttackersWeight = new HashMap<>();
    public static int count;



    public ChessEngineUtils() {}

    public static void initializePieceSquareArrays() {
        createPawnSquareTable();
        createKnightSquareTable();
        createBishopSquareTable();
        createRookSquareTable();
        createQueenSquareTable();
        createKingSquareTable();
        initializeMaps();
    }

    public static void move(Position position, String color) {



        Pair<Pair<String, String>, Integer> bestMove = alphaBetaMax(position, null, color, Integer.MIN_VALUE, Integer.MAX_VALUE, 3);
        Pair<String, String> move = bestMove.getKey();
        String initialSquare = move.getKey();
        String newSquare = move.getValue();
        ChessBoard.makeMove(initialSquare, newSquare);
        System.out.println(count);

    }


    public static Pair<Pair<String, String>, Integer> alphaBetaMax(Position position, Pair<String, String> move,
                                                                   String color, int alpha, int beta, int depthLeft) {
        if (depthLeft == 0) {
            return new Pair<Pair<String, String>, Integer>(move, position.evaluatePosition(position.gamePieces, color));
        }

        ArrayList<Pair<String, String>> allMoves = getAllPossibleMoves(position.gamePieces, color);
        Pair<String, String> bestMoveSoFar = move;
        count += 1;

        for (Pair<String, String> gameMove : allMoves) {
            count += 1;
            Position newPosition = testMove(gameMove, position);
            Pair<Pair<String, String>, Integer> possibility = alphaBetaMin(newPosition, gameMove,
                    ChessBoard.getOtherColor(color), alpha, beta, depthLeft - 1);
            int score = possibility.getValue();
            if (score >= beta) {
                return new Pair<Pair<String, String>, Integer>(bestMoveSoFar, beta);
            }

            if (score > alpha) {
                bestMoveSoFar = gameMove;
                alpha = score;
            }

        }

        return new Pair<Pair<String, String>, Integer>(bestMoveSoFar, alpha);

    }

    public static Pair<Pair<String, String>, Integer> alphaBetaMin(Position position, Pair<String, String> move,
                                                                   String color, int alpha, int beta, int depthLeft) {

        if (depthLeft == 0) {
            return new Pair<Pair<String, String>, Integer>(move, position.evaluatePosition(position.gamePieces, color));
        }

        count += 1;

        ArrayList<Pair<String, String>> allMoves = getAllPossibleMoves(position.gamePieces, color);
        Pair<String, String> bestMoveSoFar = move;
        for (Pair<String, String> gameMove : allMoves) {
            count += 1;
            Position newPosition = testMove(gameMove, position);
            Pair<Pair<String, String>, Integer> possibility = alphaBetaMax(newPosition, gameMove,
                    ChessBoard.getOtherColor(color), alpha, beta, depthLeft - 1);

            int score  = possibility.getValue();
            if (score <= alpha) {
                return new Pair<Pair<String, String>, Integer>(bestMoveSoFar, alpha);
            }

            if (score < beta) {
                bestMoveSoFar = gameMove;
                beta = score;
            }

        }

        return new Pair<Pair<String, String>, Integer>(bestMoveSoFar, beta);

    }

    public static Position testMove(Pair<String, String> move, Position position) {
        Position newPosition = new Position(position.board, position, position.gamePieces);
        ChessBoard.updatePositionWithMove(move.getKey(), move.getValue(), newPosition);
        return newPosition;



    }

    public static ArrayList<Pair<String, String>> getAllPossibleMoves(ArrayList<Piece> pieces, String color) {
        ArrayList<Pair<String, String>> allMoves = new ArrayList<>();
        for (Piece piece : pieces) {
            if (piece.color.equals(color)) {
                for (String square : piece.possibleDestinations) {
                    Pair<String, String> move = new Pair<>(piece.currentSquare, square);
                    allMoves.add(move);
                }
            }
        }

        return allMoves;

    }








    public static int[][] flipArrayHorizontally(int[][] previousArray) {
        int[][] newArray = new int[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                newArray[i][j] = previousArray[7 - i][j];
            }
        }

        return newArray;
    }

    public static void createPawnSquareTable() {
        whitePawnSquareTable = new int[][]{{0,  0,  0,  0,  0,  0,  0,  0},
                {50, 50, 50, 50, 50, 50, 50, 50},
                {10, 10, 20, 30, 30, 20, 10, 10},
                {5,  5, 10, 25, 25, 10,  5,  5},
                {0,  0,  0, 20, 20,  0,  0,  0},
                {5, -5,-10,  0,  0,-10, -5,  5},
                {5, 10, 10,-20,-20, 10, 10,  5},
                {0,  0,  0,  0,  0,  0,  0,  0}};

        blackPawnSquareTable = flipArrayHorizontally(whitePawnSquareTable);

        whiteMap.put("Pawn", whitePawnSquareTable);
        blackMap.put("Pawn", blackPawnSquareTable);
    }

    public static void createKnightSquareTable() {
        whiteKnightSquareTable = new int[][]{{-50,-40,-30,-30,-30,-30,-40,-50},
                                            {-40,-20,  0,  0,  0,  0,-20,-40},
                                            {-30,  0, 10, 15, 15, 10,  0,-30},
                                            {-30,  5, 15, 20, 20, 15,  5,-30},
                                            {-30,  0, 15, 20, 20, 15, 0, -30},
                                            {-30,  5, 10, 15, 15, 10,  5,-30},
                                            {-40,-20,  0,  5,  5,  0,-20,-40},
                                            {-50,-40,-30,-30,-30,-30,-40,-50}};

        blackKnightSquareTable = flipArrayHorizontally(whiteKnightSquareTable);
        whiteMap.put("Knight", whiteKnightSquareTable);
        blackMap.put("Knight", blackKnightSquareTable);


    }

    public static void createBishopSquareTable() {
        whiteBishopSquareTable = new int[][]{{-20,-10,-10,-10,-10,-10,-10,-20},
                                            {-10,  0,  0,  0,  0,  0,  0,-10},
                                            {-10,  0,  5, 10, 10,  5,  0,-10},
                                            {-10,  5,  5, 10, 10,  5,  5,-10},
                                            {-10,  0, 10, 10, 10, 10,  0,-10},
                                            {-10, 10, 10, 10, 10, 10, 10,-10},
                                            {-10,  5,  0,  0,  0,  0,  5,-10},
                                            {-20,-10,-10,-10,-10,-10,-10,-20}};
        blackBishopSquareTable = flipArrayHorizontally(whiteBishopSquareTable);

        whiteMap.put("Bishop", whiteBishopSquareTable);
        blackMap.put("Bishop", blackBishopSquareTable);
    }

    public static void createRookSquareTable() {
        whiteRookSquareTable = new int[][]{{0,  0,  0,  0,  0,  0,  0,  0},
                                            {5, 10, 10, 10, 10, 10, 10,  5},
                                            {-5,  0,  0,  0,  0,  0,  0, -5},
                                            {-5,  0,  0,  0,  0,  0,  0, -5},
                                            {-5,  0,  0,  0,  0,  0,  0, -5},
                                            {-5,  0,  0,  0,  0,  0,  0, -5},
                                            {-5,  0,  0,  0,  0,  0,  0, -5},
                                            {0,  0,  0,  5,  5,  0,  0,  0}};
        blackRookSquareTable = flipArrayHorizontally(whiteRookSquareTable);

        whiteMap.put("Rook", whiteRookSquareTable);
        blackMap.put("Rook", blackRookSquareTable);

    }

    public static void createQueenSquareTable() {
        whiteQueenSquareTable = new int[][]{{-20,-10,-10, -5, -5,-10,-10,-20},
                {-10,  0,  0,  0,  0,  0,  0,-10},
                {-10,  0,  5,  5,  5,  5,  0,-10},
                {-5,  0,  5,  5,  5,  5,  0, -5},
                { 0,  0,  5,  5,  5,  5,  0, -5},
                {-10,  5,  5,  5,  5,  5,  0,-10},
                {-10,  0,  5,  0,  0,  0,  0,-10},
                {-20,-10,-10, -5, -5,-10,-10,-20}};

        blackQueenSquareTable = flipArrayHorizontally(whiteQueenSquareTable);
        whiteMap.put("Queen", whiteQueenSquareTable);
        blackMap.put("Queen", blackQueenSquareTable);
    }

    public static void createKingSquareTable() {
        whiteKingSquareTable = new int[][]{{-30,-40,-40,-50,-50,-40,-40,-30},
                {-30,-40,-40,-50,-50,-40,-40,-30},
                {-30,-40,-40,-50,-50,-40,-40,-30},
                {-30,-40,-40,-50,-50,-40,-40,-30},
                {-20,-30,-30,-40,-40,-30,-30,-20},
                {-10,-20,-20,-20,-20,-20,-20,-10},
                {20, 20,  0,  0,  0,  0, 20, 20},
                {20, 30, 10,  0,  0, 10, 30, 20}};

        blackKingSquareTable = flipArrayHorizontally(whiteKingSquareTable);
        whiteMap.put("King", whiteKingSquareTable);
        blackMap.put("King", blackKingSquareTable);
    }

    public static void initializeMaps() {
        pieceAttackWeight.put("Knight", 20);
        pieceAttackWeight.put("Bishop", 20);
        pieceAttackWeight.put("Rook", 40);
        pieceAttackWeight.put("Queen", 80);

        numberOfAttackersWeight.put(0, 0);
        numberOfAttackersWeight.put(1, 0);
        numberOfAttackersWeight.put(2, 50);
        numberOfAttackersWeight.put(3, 75);
        numberOfAttackersWeight.put(4, 88);
        numberOfAttackersWeight.put(5, 94);
        numberOfAttackersWeight.put(6, 97);
        numberOfAttackersWeight.put(7, 99);



    }


    public static int getSquareEvalOfPiece(Piece piece) {
        int row = ChessBoard.getRow(piece.currentSquare);
        int col = ChessBoard.getCol(piece.currentSquare);

        if (piece.color.equals("White")) {
            return whiteMap.get(piece.name)[row][col];
        } else {
            return blackMap.get(piece.name)[row][col];
        }


    }



    public static void printTable(int[][] table) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(table[i][j] + " ");
            }
            System.out.println("");
        }
        System.out.println("");
    }

    public static boolean isOpenFile(int col) {
        for (Piece pawn : pawns) {
            int column = ChessBoard.getCol(pawn.currentSquare);
            if (column == col) {
                return false;
            }
        }

        return true;
    }

    // player of color has a semi open file at a col
    public static boolean isSemiOpenFile(int col, String color) {
        for (Piece pawn : pawns) {
            int column = ChessBoard.getCol(pawn.currentSquare);
            if (pawn.color.equals(color) && column == col) {
                return false;
            }
        }

        return true;
    }


//    public static void main(String[] args) {
//        createPawnSquareTable();
//        printTable(whitePawnSquareTable);
//        printTable(blackPawnSquareTable);
//
//    }
}
