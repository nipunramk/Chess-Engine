/**
 * Created by nipun.ramk on 12/21/2016.
 */
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.border.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import javax.imageio.ImageIO;

public class ChessBoard {

    private static final JPanel gui = new JPanel(new BorderLayout(3, 3));
    private static JButton[][] chessBoardSquares = new JButton[8][8];
    public static Piece[][] gameBoard = new Piece[8][8];
    private static Image[][] chessPieceImages = new Image[2][6];
    private static JPanel chessBoard;
    private static final JLabel message = new JLabel(
            "ChessAI is ready to play!");
    private static final String COLS = "ABCDEFGH";
    public static final int QUEEN = 0, KING = 1,
            ROOK = 2, KNIGHT = 3, BISHOP = 4, PAWN = 5;
    public static final int[] STARTING_ROW = {
            ROOK, KNIGHT, BISHOP, KING, QUEEN, BISHOP, KNIGHT, ROOK
    };
    public static final int BLACK = 0, WHITE = 1;
    public static boolean isSecondClick = false;
    public static String firstClick = "";
    public static String secondClick = "";
    public static ArrayList<Piece> pieces = new ArrayList<>();
    public static String colorMoved = "Black";
    public static HashMap<String, ImageIcon> pieceNameToImage = new HashMap<>();
    public static HashMap<Position, Integer> repetitionsTracker = new HashMap<>();
    public static Position previousPosition = null;
    public static Position currentPosition;
    public static ArrayList<Position> moves = new ArrayList<>();
    public static boolean isAIMove = true;



    ChessBoard() {
        initializeGui();
    }

    public final void initializeGui() {
        // create the images for the chess pieces
        createImages();

        // set up the main GUI
        gui.setBorder(new EmptyBorder(5, 5, 5, 5));
        JToolBar tools = new JToolBar();
        tools.setFloatable(false);
        gui.add(tools, BorderLayout.PAGE_START);
        Action newGameAction = new AbstractAction("New") {

            @Override
            public void actionPerformed(ActionEvent e) {
                setupNewGame();
            }
        };
        tools.add(newGameAction);
//        tools.add(new JButton("Save")); // TODO - add functionality!


        tools.add(new JButton("Save"));
        tools.add(new JButton("Restore")); // TODO - add functionality!
        tools.addSeparator();
        tools.add(new JButton("Resign")); // TODO - add functionality!
        tools.addSeparator();
        tools.add(message);

        gui.add(new JLabel("?"), BorderLayout.LINE_START);

        chessBoard = new JPanel(new GridLayout(0, 9)) {

            /**
             * Override the preferred size to return the largest it can, in
             * a square shape.  Must (must, must) be added to a GridBagLayout
             * as the only component (it uses the parent as a guide to size)
             * with no GridBagConstaint (so it is centered).
             */
            @Override
            public final Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                Dimension prefSize = null;
                Component c = getParent();
                if (c == null) {
                    prefSize = new Dimension(
                            (int)d.getWidth(),(int)d.getHeight());
                } else if (c!=null &&
                        c.getWidth()>d.getWidth() &&
                        c.getHeight()>d.getHeight()) {
                    prefSize = c.getSize();
                } else {
                    prefSize = d;
                }
                int w = (int) prefSize.getWidth();
                int h = (int) prefSize.getHeight();
                // the smaller of the two sizes
                int s = (w>h ? h : w);
                return new Dimension(s,s);
            }
        };
        chessBoard.setBorder(new CompoundBorder(
                new EmptyBorder(8,8,8,8),
                new LineBorder(Color.BLACK)
        ));
        // Set the BG to be ochre
        Color ochre = new Color(204,119,34);
        chessBoard.setBackground(ochre);
        JPanel boardConstrain = new JPanel(new GridBagLayout());
        boardConstrain.setBackground(ochre);
        boardConstrain.add(chessBoard);
        gui.add(boardConstrain);

        // create the chess board squares
        Insets buttonMargin = new Insets(0, 0, 0, 0);
        for (int ii = 0; ii < chessBoardSquares.length; ii++) {
            for (int jj = 0; jj < chessBoardSquares[ii].length; jj++) {
                JButton b = new JButton();
                b.setActionCommand(Integer.toString(ii) + jj);
                b.addActionListener(new ButtonListener());

                b.setMargin(buttonMargin);
                // our chess pieces are 64x64 px in size, so we'll
                // 'fill this in' using a transparent icon..
                ImageIcon icon = new ImageIcon(
                        new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
                b.setIcon(icon);
                if ((jj % 2 == 1 && ii % 2 == 1)
                        //) {
                        || (jj % 2 == 0 && ii % 2 == 0)) {
                    b.setBackground(Color.WHITE);
                } else {
                    b.setBackground(Color.BLACK);
                }
                chessBoardSquares[jj][ii] = b;
            }
        }

        /*
         * fill the chess board
         */
        chessBoard.add(new JLabel(""));
        // fill the top row
        for (int ii = 0; ii < 8; ii++) {
            chessBoard.add(
                    new JLabel(COLS.substring(ii, ii + 1),
                            SwingConstants.CENTER));
        }
        // fill the black non-pawn piece row
        for (int ii = 0; ii < 8; ii++) {
            for (int jj = 0; jj < 8; jj++) {
                switch (jj) {
                    case 0:
                        chessBoard.add(new JLabel("" + (9-(ii + 1)),
                                SwingConstants.CENTER));
                    default:
                        chessBoard.add(chessBoardSquares[jj][ii]);
                }
            }
        }
    }

    public final JComponent getGui() {
        return gui;
    }

    private static final void createImages() {
        try {
            URL url = new URL("http://i.stack.imgur.com/memI0.png");
            BufferedImage bi = ImageIO.read(url);
            for (int ii = 0; ii < 2; ii++) {
                for (int jj = 0; jj < 6; jj++) {
                    chessPieceImages[ii][jj] = bi.getSubimage(
                            jj * 64, ii * 64, 64, 64);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static Piece makePiece(int row, int col, String side, boolean isPawn, ImageIcon image) {
        String square = convertRowColToSquare(row, col);
        String key = side;
        if (isPawn) {
            return new Pawn(square, side);
        }
        int piece = STARTING_ROW[col];
        if (piece == ROOK) {
            key += "Rook";
            pieceNameToImage.put(key, image);
            return new Rook(square, side);
        } else if (piece == KNIGHT) {
            key += "Knight";
            pieceNameToImage.put(key, image);
            return new Knight(square, side);
        } else if (piece == BISHOP) {
            key += "Bishop";
            pieceNameToImage.put(key, image);
            return new Bishop(square, side);
        } else if (piece == KING) {
            key += "Queen";
            pieceNameToImage.put(key, image);
            return new Queen(square, side);
        } else {
            key += "King";
            pieceNameToImage.put(key, image);
            return new King(square, side);
        }


    }

    /**
     * Initializes the icons of the initial chess board piece places
     */
    private static final void setupNewGame() {
        message.setText("Make your move!");
        // set up the black pieces
        for (int ii = 0; ii < STARTING_ROW.length; ii++) {
            ImageIcon image = new ImageIcon(
                    chessPieceImages[BLACK][STARTING_ROW[ii]]);
            chessBoardSquares[ii][0].setIcon(image);
            Piece piece = makePiece(0, ii, "Black", false, image);
            pieces.add(piece);
            updatePieceAtSquare(0, ii, piece);


        }
        for (int ii = 0; ii < STARTING_ROW.length; ii++) {
            ImageIcon image = new ImageIcon(
                    chessPieceImages[BLACK][PAWN]);
            chessBoardSquares[ii][1].setIcon(image);
            Piece piece = makePiece(1, ii, "Black", true, image);
            pieces.add(piece);
            ChessEngineUtils.pawns.add(piece);
            updatePieceAtSquare(1, ii, piece);
        }
        // set up the white pieces
        for (int ii = 0; ii < STARTING_ROW.length; ii++) {
            ImageIcon image = new ImageIcon(
                    chessPieceImages[WHITE][PAWN]);
            chessBoardSquares[ii][6].setIcon(image);
            Piece piece = makePiece(6, ii, "White", true, image);
            pieces.add(piece);
            ChessEngineUtils.pawns.add(piece);
            updatePieceAtSquare(6, ii, piece);
        }
        for (int ii = 0; ii < STARTING_ROW.length; ii++) {
            ImageIcon image = new ImageIcon(
                    chessPieceImages[WHITE][STARTING_ROW[ii]]);
            chessBoardSquares[ii][7].setIcon(image);
            Piece piece = makePiece(7, ii, "White", false, image);
            pieces.add(piece);
            updatePieceAtSquare(7, ii, piece);
        }

        // This is to ensure that the Kings are at the end of the pieces ArrayList
        // As a result, they will always be the last pieces to update it's moves
        // This is to make sure handling possible moves for the King is easy

        int firstIndexKing = getIndexOfFirstKing(pieces);
        int secondIndexKing = getIndexOfSecondKing(pieces);
        Collections.swap(pieces, firstIndexKing, pieces.size() - 1);
        Collections.swap(pieces, secondIndexKing, pieces.size() - 2);

        for (Piece p : pieces) {
            p.updatePossibleMoves();
        }

        System.out.println(pieces);

        printBoard();

        System.out.println(pieceNameToImage);

        currentPosition = new Position(gameBoard, previousPosition, pieces);
        addPositionToRepetitionTrackerAndCheckDraw(currentPosition);
        ChessEngineUtils.initializePieceSquareArrays();
        String otherColor = getOtherColor(colorMoved);

        System.out.println(colorMoved + ": " + currentPosition.evaluatePosition(pieces, colorMoved));
        System.out.println(otherColor + ": " + currentPosition.evaluatePosition(pieces, otherColor));



    }

    public static void printBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = getPieceAtSquare(i, j);
                System.out.println(piece);
                System.out.println(i * 10 + j);
            }
        }
    }

    public static boolean isValidRowCol(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    public static int getIndexOfFirstKing(ArrayList<Piece> items) {

        for (int i = 0; i < items.size(); i++) {
            Piece piece = items.get(i);
            if (piece.name == "King") {
                return i;
            }
        }

        return items.size() - 1;
    }

    public static int getIndexOfSecondKing(ArrayList<Piece> items) {

        int index = 0;

        for (int i = 0; i < items.size(); i++) {
            Piece piece = items.get(i);
            if (piece.name == "King") {
                index = i;
            }
        }

        return index;
    }





    public static int getCol(String square) {
        int coordinates = Integer.parseInt(square);
        return coordinates % 10;
    }

    public static int getRow(String square) {
        int coordinates = Integer.parseInt(square);
        return coordinates / 10;
    }

    public static String convertRowColToSquare(int row, int col) {
        String rowString = Integer.toString(row);
        String colString = Integer.toString(col);
        return rowString.concat(colString);   }

    public static JButton getButtonAtSquare(int row, int col) {
        return chessBoardSquares[col][row];
    }

    public static Piece getPieceAtSquare(int row, int col) {
        return gameBoard[col][row];
    }

    public static String getOtherColor(String color) {
        if (color == "White") {
            return "Black";
        } else {
            return "White";
        }
    }

    public static Piece[][] copyBoard(Piece[][] currentBoard) {
        Piece[][] newBoard = new Piece[currentBoard.length][currentBoard[0].length];
        for (int i = 0; i < newBoard.length; i++) {
            for (int j = 0; j < newBoard[0].length; j++) {
                if (currentBoard[i][j] != null) {
                    newBoard[i][j] = currentBoard[i][j].clone();
                } else {
                    newBoard[i][j] = currentBoard[i][j];
                }

            }
        }

        return newBoard;
    }

    public static ArrayList<Piece> copyPiecesArrayList(ArrayList<Piece> piecesArray) {
        ArrayList<Piece> newList = new ArrayList<>();
        for (Piece p : piecesArray) {
            newList.add(p.clone());
        }

        return newList;
    }

    public static void updatePieceAtSquare(int row, int col, Piece piece) {
        gameBoard[col][row] = piece;
    }

    public static boolean pieceExistsAtSquare(int row, int col) {
        return gameBoard[col][row] != null;
    }

    public static boolean pieceOfColorExistsAtSquare(int row, int col, String color) {
        return pieceExistsAtSquare(row, col) && getPieceAtSquare(row, col).color.equals(color);
    }

    public static boolean pieceOfNameAndColorExistsAtSquare(int row, int col, String color, String name) {
        return pieceOfColorExistsAtSquare(row, col, color) && getPieceAtSquare(row, col).name.equals(name);
    }

    public static void updateBoard(int row, int col, Piece piece, Piece[][] board) {
        board[col][row] = piece;
    }

    public static void updatePositionWithMove(String initialSquare, String newSquare, Position position) {
        int initialRow = getRow(initialSquare);
        int initialCol = getCol(initialSquare);
        int newRow = getRow(newSquare);
        int newCol = getCol(newSquare);
        Piece gamePiece  = getPieceAtSquareInBoard(initialRow, initialCol, position.board);

        String color = gamePiece.color;
        String otherColor = getOtherColor(colorMoved);

        updateBoard(initialRow, initialCol, null, position.board);
        Piece pieceAtSecondSquare = getPieceAtSquareInBoard(newRow, newCol, position.board);
        if (pieceAtSecondSquare != null) {
            position.gamePieces.remove(pieceAtSecondSquare);
            if (pieceAtSecondSquare.name.equals("Pawn")) {
                ChessEngineUtils.pawns.remove(pieceAtSecondSquare);
            }
        }

        updateBoard(newRow, newCol, gamePiece, position.board);

        int index = getIndexOfPiece(position.gamePieces, gamePiece);
        position.gamePieces.remove(gamePiece);
        gamePiece.currentSquare = newSquare;
        position.gamePieces.add(index, gamePiece);

        if (gamePiece.name.equals("King")) {
            gamePiece.kingMoved = true;
        }

        if (gamePiece.name.equals("Rook")) {
            gamePiece.rookMoved = true;
        }

        if (isPawnPromotion(color, gamePiece, initialRow, newRow)) {
            Piece newQueen = new Queen(newSquare, colorMoved);
            updateBoard(newRow, newCol, newQueen, position.board);
            index = getIndexOfPiece(position.gamePieces, gamePiece);
            position.gamePieces.remove(gamePiece);
            position.gamePieces.add(index, newQueen);
        }

        if (gamePiece.name.equals("King") && newSquare.equals(gamePiece.originalSquare)
                && newSquare.equals(gamePiece.kingSideCastleSquare)) {
            int row = newRow;
            int col = newCol + 1;
            Piece rook = getPieceAtSquareInBoard(row, col, position.board);
            updateBoard(row, col - 2, rook, position.board);
            rook.currentSquare = convertRowColToSquare(row, col - 2);
            updateBoard(row, col, null, position.board);
            gamePiece.kingSideCastled = true;

        }

        if (gamePiece.name.equals("King") && newSquare.equals(gamePiece.originalSquare)
                && newSquare.equals(gamePiece.queenSideCastleSquare)) {
            int row = newRow;
            int col = newCol - 2;
            Piece rook = getPieceAtSquareInBoard(row, col, position.board);
            updateBoard(row, col + 3, rook, position.board);
            rook.currentSquare = convertRowColToSquare(row, col + 3);
            updateBoard(row, col, null, position.board);
            gamePiece.queenSideCastled = true;

        }

        if (gamePiece.enPassant && gamePiece.currentSquare.equals(gamePiece.enPassantSquare)) {
            int row = getRow(gamePiece.enPassantOpponentPawnSquare);
            int col = getCol(gamePiece.enPassantOpponentPawnSquare);
            pieceAtSecondSquare = getPieceAtSquareInBoard(row, col, position.board);
            position.gamePieces.remove(pieceAtSecondSquare);
            ChessEngineUtils.pawns.remove(pieceAtSecondSquare);
            updateBoard(row, col, null, position.board);

        }

//        checkEnPassant(initialRow, initialCol, newRow, newCol, gamePiece, position.board);

        for (Piece p : position.gamePieces) {
            p.updatePossibleMoves();
            int row = getRow(p.currentSquare);
            int col = getCol(p.currentSquare);
            Piece p2 = getPieceAtSquareInBoard(row, col, position.board);
            p2.updatePossibleMoves();

        }



    }

    public static boolean pieceExistsAtSquare(int row, int col, Piece[][] board) {
        return board[col][row] != null;
    }

    public static Piece getPieceAtSquareInBoard(int row, int col, Piece[][] board) {
        if (pieceExistsAtSquare(row, col, board)) {
            return board[col][row];
        } else {
            return null;
        }
    }

    public static boolean pieceExistsAtSquare(String square) {
        int row = getRow(square);
        int col = getCol(square);
        return pieceExistsAtSquare(row, col);
    }

    public static boolean whitePieceExistsAtSquare(int row, int col, Piece[][] board) {
        return pieceExistsAtSquare(row, col, board) && getPieceAtSquareInBoard(row, col, board).color.equals("White");
    }

    public static boolean blackPieceExistsAtSquare(int row, int col, Piece[][] board) {
        return pieceExistsAtSquare(row, col, board) && getPieceAtSquareInBoard(row, col, board).color.equals("Black");
    }

    // Checks whether a piece of a particular color is attacking a square
    public static boolean pieceAttackingSquare(int row, int col, String color) {
        String square = convertRowColToSquare(row, col);
        for (Piece p : pieces) {
            if (p.color.equals(color) && p.attackingSquares.contains(square)) {
                return true;
            }
        }

        return false;
    }

    public static boolean pieceAttackingSquare(String square, String color) {
        for (Piece p : pieces) {
            if (p.color == color && p.attackingSquares.contains(square)) {
                return true;
            }
        }

        return false;
    }

    public static Piece getKing(String color) {
        if (color == "White") {
            return pieces.get(pieces.size() - 2);
        } else {
            return pieces.get(pieces.size() - 1);
        }
    }

    public static Piece getRook(String color, String originalSquare) {
        for (Piece p : pieces) {
            if (p.color.equals(color) && p.originalSquare.equals(originalSquare) && p.name.equals("Rook")) {
                return p;
            }
        }

        return null;
    }


    // Returns if the given color's king is in Check
    public static boolean isKingInCheck(String color) {
        Piece king = getKing(color);
        return pieceAttackingSquare(king.currentSquare, getOtherColor(color));
    }

    public static boolean canKingSideCastle(String color) {
        Piece king = getKing(color);
        if (king.kingMoved) {
            return false;
        }

        int row = getRow(king.currentSquare);
        int col = getCol(king.currentSquare);
        col += 3;

        Piece rook = getRook(color, convertRowColToSquare(row, col));

        if (rook == null || rook.rookMoved) {
            return false;
        }


        col -= 2;
        if (pieceExistsAtSquare(row, col) || pieceExistsAtSquare(row, col + 1)) {
            return false;
        }

        if (isKingInCheck(color)) {
            return false;
        }
        String otherColor = getOtherColor(color);
        if (pieceAttackingSquare(row, col, otherColor) || pieceAttackingSquare(row, col + 1, otherColor)) {
            return false;
        }

        return true;


    }

    public static boolean canQueenSideCastle(String color) {
        Piece king = getKing(color);
        if (king.kingMoved) {
            return false;
        }

        int row = getRow(king.currentSquare);
        int col = getCol(king.currentSquare);

        col -= 4;

        Piece rook = getRook(color, convertRowColToSquare(row, col));

        if (rook == null || rook.rookMoved) {
            return false;
        }

        col += 2;
        if (pieceExistsAtSquare(row, col) || pieceExistsAtSquare(row, col + 1)
                || pieceExistsAtSquare(row, col - 1)) {
            return false;
        }

        if (isKingInCheck(color)) {
            return false;
        }
        String otherColor = getOtherColor(color);
        if (pieceAttackingSquare(row, col, otherColor) || pieceAttackingSquare(row, col + 1, otherColor)
                || pieceAttackingSquare(row, col - 1, otherColor)) {
            return false;
        }

        return true;
    }



    public static boolean isPawnPromotion(String color, Piece piece, int initialRow, int newRow) {
        if (piece.name != "Pawn") {
            return false;
        }

        if (color.equals("White")) {
            if (initialRow == 1 && newRow == 0) {
                return true;
            }
            return false;
        } else {
            if (initialRow == 6 && newRow == 7) {
                return true;
            }
            return false;
        }


    }

    public static void handlePawnPromotion(String square2, JButton secondSquare, Piece gamePiece, int newRow, int newCol) {
        String[] options = new String[] {"Queen", "Rook", "Bishop", "Knight"};
        int response = JOptionPane.showOptionDialog(null, "To which piece would you like to promote your pawn?",
                "Pawn Promotion",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);
        while (response == -1) {
            response = JOptionPane.showOptionDialog(null, "To which piece would you like to promote your pawn?",
                    "Pawn Promotion",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, options, options[0]);
        }
        if (response == 0) {
            Piece newQueen = new Queen(square2, colorMoved);
            updatePieceAtSquare(newRow, newCol, newQueen);
            int index = getIndexOfPiece(pieces, gamePiece);
            pieces.remove(gamePiece);
            pieces.add(index, newQueen);
            String key = colorMoved + "Queen";
            ImageIcon image = pieceNameToImage.get(key);
            secondSquare.setIcon(image);
        } else if (response == 1) {
            Piece newRook = new Rook(square2, colorMoved);
            updatePieceAtSquare(newRow, newCol, newRook);
            int index = getIndexOfPiece(pieces, gamePiece);
            pieces.remove(gamePiece);
            pieces.add(index, newRook);
            String key = colorMoved + "Rook";
            ImageIcon image = pieceNameToImage.get(key);
            secondSquare.setIcon(image);
        } else if (response == 2) {
            Piece newBishop = new Bishop(square2, colorMoved);
            updatePieceAtSquare(newRow, newCol, newBishop);
            int index = getIndexOfPiece(pieces, gamePiece);
            pieces.remove(gamePiece);
            pieces.add(index, newBishop);
            String key = colorMoved + "Bishop";
            ImageIcon image = pieceNameToImage.get(key);
            secondSquare.setIcon(image);
        } else {
            Piece newKnight = new Knight(square2, colorMoved);
            updatePieceAtSquare(newRow, newCol, newKnight);
            int index = getIndexOfPiece(pieces, gamePiece);
            pieces.remove(gamePiece);
            pieces.add(index, newKnight);
            String key = colorMoved + "Knight";
            ImageIcon image = pieceNameToImage.get(key);
            secondSquare.setIcon(image);
        }
    }







    public static void makeMove(String square1, String square2) {


        int initialRow = getRow(square1);
        int initialCol = getCol(square1);
        int newRow = getRow(square2);
        int newCol = getCol(square2);
        JButton firstSquare = getButtonAtSquare(initialRow, initialCol);
        Icon piece = firstSquare.getIcon();
        Piece gamePiece = getPieceAtSquare(initialRow, initialCol);
        if (!gamePiece.possibleDestinations.contains(square2)) {
            message.setText("Invalid move!");
            return;
        }

        if (gamePiece.color == colorMoved) {
            message.setText("It is " + getOtherColor(colorMoved) + "'s turn to move!");
            return;
        }

        if (isKingInCheck(gamePiece.color) && !isValidMoveOutOfCheck(gamePiece.color, square1, square2)) {
            message.setText("Invalid Move: Still in Check!");
            return;
        }

        if (!isValidMoveOutOfCheck(gamePiece.color, square1, square2)) {
            message.setText("Invalid Move: Piece is Pinned to the King!");
            return;
        }

        colorMoved = gamePiece.color;
        message.setText(colorMoved + " moved!");
        String otherColor = getOtherColor(colorMoved);
        firstSquare.setIcon(null);
        previousPosition = currentPosition;
        updatePieceAtSquare(initialRow, initialCol, null);
        JButton secondSquare = getButtonAtSquare(newRow, newCol);
        Piece pieceAtSecondSquare = getPieceAtSquare(newRow, newCol);
        if (pieceAtSecondSquare != null) {
            pieces.remove(pieceAtSecondSquare);
            if (pieceAtSecondSquare.name.equals("Pawn")) {
                ChessEngineUtils.pawns.remove(pieceAtSecondSquare);
            }
        }


        secondSquare.setIcon(piece);
        updatePieceAtSquare(newRow, newCol, gamePiece);
        gamePiece.currentSquare = square2;

        if (gamePiece.name.equals("King")) {
            gamePiece.kingMoved = true;
        }

        if (gamePiece.name.equals("Rook")) {
            gamePiece.rookMoved = true;
        }

        if (isPawnPromotion(colorMoved, gamePiece, initialRow, newRow)) {

            handlePawnPromotion(square2, secondSquare, gamePiece, newRow, newCol);
        }
        // handles king side castling
        if (gamePiece.name.equals("King") && square1.equals(gamePiece.originalSquare)
                && square2.equals(gamePiece.kingSideCastleSquare)) {
            int row = newRow;
            int col = newCol + 1;
            Piece rook = getPieceAtSquare(row, col);
            updatePieceAtSquare(row, col - 2, rook);
            rook.currentSquare = convertRowColToSquare(row, col - 2);
            updatePieceAtSquare(row, col, null);
            JButton rookButton = getButtonAtSquare(row, col);
            Icon rookIcon = rookButton.getIcon();
            getButtonAtSquare(row, col - 2).setIcon(rookIcon);
            rookButton.setIcon(null);
            gamePiece.kingSideCastled = true;

        }

        if (gamePiece.name.equals("King") && square1.equals(gamePiece.originalSquare)
                && square2.equals(gamePiece.queenSideCastleSquare)) {
            int row = newRow;
            int col = newCol - 2;
            Piece rook = getPieceAtSquare(row, col);
            updatePieceAtSquare(row, col + 3, rook);
            rook.currentSquare = convertRowColToSquare(row, col + 3);
            updatePieceAtSquare(row, col, null);
            JButton rookButton = getButtonAtSquare(row, col);
            Icon rookIcon = rookButton.getIcon();
            getButtonAtSquare(row, col + 3).setIcon(rookIcon);
            rookButton.setIcon(null);
            gamePiece.queenSideCastled = true;

        }
        if (gamePiece.enPassant && gamePiece.currentSquare.equals(gamePiece.enPassantSquare)) {
            int row = getRow(gamePiece.enPassantOpponentPawnSquare);
            int col = getCol(gamePiece.enPassantOpponentPawnSquare);
            pieceAtSecondSquare = getPieceAtSquare(row, col);
            pieces.remove(pieceAtSecondSquare);
            ChessEngineUtils.pawns.remove(pieceAtSecondSquare);
            updatePieceAtSquare(row, col, null);
            getButtonAtSquare(row, col).setIcon(null);
        }
        checkEnPassant(initialRow, initialCol, newRow, newCol, gamePiece, gameBoard);

        for (Piece p : pieces) {
            p.updatePossibleMoves();

        }

        if (isKingInCheck(otherColor)) {
            boolean isCheckMate = positionIsCheckmate(otherColor);


            if (isCheckMate) {
                message.setText("Checkmate! " + colorMoved + " wins!");
            } else {
                message.setText(otherColor + " is in check!");
            }


        } else if (positionIsStalemate(otherColor)) {
            message.setText("Stalemate! Game is a Draw!");
        }
//        printBoard();
        currentPosition = new Position(gameBoard, previousPosition, pieces);
        moves.add(currentPosition);
        if (addPositionToRepetitionTrackerAndCheckDraw(currentPosition)) {
            message.setText("Game is a draw by 3 move repetition!");
        }

        System.out.println(colorMoved + ": " + currentPosition.evaluatePosition(pieces, colorMoved));
        System.out.println(otherColor + ": " + currentPosition.evaluatePosition(pieces, otherColor));




    }

    public static boolean addPositionToRepetitionTrackerAndCheckDraw(Position currentPosition) {
        if (!repetitionsTracker.containsKey(currentPosition)) {
            repetitionsTracker.put(currentPosition, 1);
        } else {
            int currentRepetitions = repetitionsTracker.get(currentPosition);
            repetitionsTracker.put(currentPosition, currentRepetitions + 1);
        }

        return repetitionsTracker.get(currentPosition) >= 3;

    }



    public static boolean positionIsStalemate(String otherColor) {
        ArrayList<Piece> copy = copyPiecesArrayList(pieces);
        for (Piece p : copy) {
            if (p.color != otherColor) {
                continue;
            }
            String currentSquare = p.currentSquare;
            for (String possibleSquare : p.possibleDestinations) {
                if (isValidMoveOutOfCheck(otherColor, currentSquare, possibleSquare)) {
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean positionIsCheckmate(String otherColor) {
        boolean isCheckMate = true;
        ArrayList<Piece> copy = copyPiecesArrayList(pieces);
        for (Piece p : copy) {
            if (p.color != otherColor) {
                continue;
            }
            String currentSquare = p.currentSquare;
            for (String possibleSquare : p.possibleDestinations) {
                if (isValidMoveOutOfCheck(otherColor, currentSquare, possibleSquare)) {
                    isCheckMate = false;
                }
            }
        }
        return isCheckMate;
    }

//    public static boolean isValidMoveOutOfCheck(String color, String square1, String square2) {
//        Piece[][] boardCopy = copyBoard(gameBoard);
//        Piece
//    }

    public static boolean isValidMoveOutOfCheck(String color, String square1, String square2) {
        boolean result = true;
        boolean enPassant = false;
        int enPassantRow = -1, enPassantCol = -1;
        int initialRow = getRow(square1);
        int initialCol = getCol(square1);
        int newRow = getRow(square2);
        int newCol = getCol(square2);
        int index = 0;
        Piece gamePiece = getPieceAtSquare(initialRow, initialCol);
        updatePieceAtSquare(initialRow, initialCol, null);
        Piece pieceAtSecondSquare = getPieceAtSquare(newRow, newCol);
        if (pieceAtSecondSquare != null) {
            index = getIndexOfPiece(pieces, pieceAtSecondSquare);
            pieces.remove(pieceAtSecondSquare);
        }
        updatePieceAtSquare(newRow, newCol, gamePiece);
        gamePiece.currentSquare = square2;
        if (gamePiece.enPassant && gamePiece.currentSquare.equals(gamePiece.enPassantSquare)) {
            enPassant = true;
            int row = getRow(gamePiece.enPassantOpponentPawnSquare);
            int col  = getCol(gamePiece.enPassantOpponentPawnSquare);
            enPassantRow = row;
            enPassantCol = col;
            pieceAtSecondSquare = getPieceAtSquare(row, col);
            index = getIndexOfPiece(pieces, pieceAtSecondSquare);
            pieces.remove(pieceAtSecondSquare);
            updatePieceAtSquare(row, col, null);
        }

        for (Piece p : pieces) {
            p.updatePossibleMoves();

        }

        if (isKingInCheck(color)) {
            result = false;
        }

        updatePieceAtSquare(initialRow, initialCol, gamePiece);
        gamePiece.currentSquare =  square1;
        if (pieceAtSecondSquare != null) {
            pieces.add(index, pieceAtSecondSquare);
        }

        if (enPassant) {
            updatePieceAtSquare(newRow, newCol, null);
            updatePieceAtSquare(enPassantRow, enPassantCol, pieceAtSecondSquare);

        } else{
            updatePieceAtSquare(newRow, newCol, pieceAtSecondSquare);
        }

        for (Piece p : pieces) {
            p.updatePossibleMoves();

        }

        return result;


    }

    public static int getIndexOfPiece(ArrayList<Piece> items, Piece piece) {
        int index = 0;
        for (int i = 0; i < items.size() - 1; i++) {
            Piece currentPiece = items.get(i);
            if (piece.equals(currentPiece)) {
                index = i;
            }
        }

        return index;
    }

    public static void checkEnPassant(int initialRow, int initialCol, int newRow, int newCol,
                                      Piece gamePiece, Piece[][] gameBoard) {

        for (Piece p : pieces) {
            p.enPassant = false;
            p.enPassantSquare = "";
            p.enPassantOpponentPawnSquare = "";
        }

        if (!(gamePiece.name.equals("Pawn"))) {
            return;
        }
        // En Passant for black pawns
        if (gamePiece.color.equals("White") && initialRow == 6 && newRow == 4) {
            int leftCol = newCol - 1;
            int rightCol = newCol + 1;
            if (isValidRowCol(newRow, leftCol) && blackPieceExistsAtSquare(newRow, leftCol, gameBoard)) {
                Piece leftPiece = getPieceAtSquare(newRow, leftCol);
                if (leftPiece.name.equals("Pawn")) {
                    leftPiece.enPassant = true;
                    leftPiece.enPassantSquare = convertRowColToSquare(newRow + 1, newCol);
                    leftPiece.enPassantOpponentPawnSquare = convertRowColToSquare(newRow, newCol);
                }
            }

            if (isValidRowCol(newRow, rightCol) && blackPieceExistsAtSquare(newRow, rightCol, gameBoard)) {
                Piece rightPiece = getPieceAtSquare(newRow, rightCol);
                if (rightPiece.name.equals("Pawn")) {
                    rightPiece.enPassant = true;
                    rightPiece.enPassantSquare = convertRowColToSquare(newRow + 1, newCol);
                    rightPiece.enPassantOpponentPawnSquare = convertRowColToSquare(newRow, newCol);
                }
            }
        } else if (gamePiece.color.equals("Black") && initialRow == 1 && newRow == 3) {
            int leftCol = newCol - 1;
            int rightCol = newCol + 1;
            if (isValidRowCol(newRow, leftCol) && whitePieceExistsAtSquare(newRow, leftCol, gameBoard)) {
                Piece leftPiece = getPieceAtSquare(newRow, leftCol);
                if (leftPiece.name.equals("Pawn")) {
                    leftPiece.enPassant = true;
                    leftPiece.enPassantSquare = convertRowColToSquare(newRow - 1, newCol);
                    leftPiece.enPassantOpponentPawnSquare = convertRowColToSquare(newRow, newCol);
                }
            }

            if (isValidRowCol(newRow, rightCol) && whitePieceExistsAtSquare(newRow, rightCol, gameBoard)) {
                Piece rightPiece = getPieceAtSquare(newRow, rightCol);
                if (rightPiece.name.equals("Pawn")) {
                    rightPiece.enPassant = true;
                    rightPiece.enPassantSquare = convertRowColToSquare(newRow - 1, newCol);
                    rightPiece.enPassantOpponentPawnSquare = convertRowColToSquare(newRow, newCol);
                }
            }

        }
    }

    public static void main(String[] args) {
        Runnable r = new Runnable() {

            @Override
            public void run() {
                ChessBoard cg = new ChessBoard();

                JFrame f = new JFrame("ChessAI");
                f.add(cg.getGui());
                // Ensures JVM closes after frame(s) closed and
                // all non-daemon threads are finished
                f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                // See http://stackoverflow.com/a/7143398/418556 for demo.
                f.setLocationByPlatform(true);

                // ensures the frame is the minimum size it needs to be
                // in order display the components within it
                f.pack();
                // ensures the minimum size is enforced.
                f.setMinimumSize(f.getSize());
                f.setVisible(true);
            }
        };
        // Swing GUIs should be created and updated on the EDT
        // http://docs.oracle.com/javase/tutorial/uiswing/concurrency
        SwingUtilities.invokeLater(r);


    }

    class ButtonListener implements ActionListener {

        ButtonListener() {

        }
        @Override
        public void actionPerformed(ActionEvent e) {
            String firstSquare = e.getActionCommand();
            String secondSquare = e.getActionCommand();
            if (!pieceExistsAtSquare(firstSquare) && !isSecondClick) {
                return;
            }
            if (!ChessBoard.isSecondClick) {
                ChessBoard.firstClick = firstSquare;
                ChessBoard.isSecondClick = true;
            } else {
                ChessBoard.secondClick = secondSquare;
                ChessBoard.isSecondClick = false;
                ChessBoard.makeMove(ChessBoard.firstClick, ChessBoard.secondClick);
                ChessEngineUtils.move(currentPosition, getOtherColor(colorMoved));
            }





        }
    }


}