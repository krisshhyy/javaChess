
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.util.*;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.SwingUtilities.*;

public class Table {
    
    public final JFrame gameFrame;
    private final BoardPanel boardPanel;
    final MoveLog moveLog;
    public Board chessBoard;

    Client n;

    private Tile sourceTile;
    private Tile destinationTile;
    private Piece humanMovedPiece;

    private boolean highLightLegalMoves;

    final TakenPiecesPanel takenPiecesPanel;
    private final Color lightTileColor = Color.decode("#ceebe1");
    private final Color darkTileColor = Color.decode("#19574f");
    private final static Dimension OUTER_FRAME_DIMENSION = new Dimension(600, 600);
    private final static Dimension BOARD_PANEL_DIMENSION = new Dimension(400, 350);
    private final static Dimension TILE_PANEL_DIMENSION = new Dimension(10, 10);
    private static String defaultPieceImagesPath = "";

    public Table() {
        this.gameFrame = new JFrame("Jchess");

        final JMenuBar tableMenuBar = createTableMenuBar();
        this.gameFrame.setJMenuBar(tableMenuBar);
        this.chessBoard = Board.createStandardBoard();
        this.takenPiecesPanel = new TakenPiecesPanel();
        this.moveLog = new MoveLog();
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.boardPanel = new BoardPanel();
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.gameFrame.add(this.takenPiecesPanel,BorderLayout.WEST);
        this.gameFrame.setVisible(true);
        this.highLightLegalMoves = false;
        n= new Client("localhost",2222,chessBoard);
    }
    public void repaintWholeBoard(Board chess) {
        boardPanel.drawBoard(chess);
    }
    private void checkIsCheck(){
        if(chessBoard.whitePlayer().isInCheck() == true){
            System.out.println("White is in check");

        }
        if(chessBoard.blackPlayer().isInCheck() == true){
            System.out.println("Black is in check");

        }
    }

    private JMenuBar createTableMenuBar() {
        final JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        return tableMenuBar;
    }

    private JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("FILE");

        final JMenuItem openPGN = new JMenuItem("Load PGn File");
        openPGN.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("open up that pgnfile");
            }
        });

        fileMenu.add(openPGN);

        final JMenuItem exitMenuItem = new JMenuItem("EXIT");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }

        });
        fileMenu.add(exitMenuItem);

        fileMenu.addSeparator();

        final JCheckBoxMenuItem legalMoveHighlighterCheckBox = new JCheckBoxMenuItem("highlight legal moves", false);
        legalMoveHighlighterCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                highLightLegalMoves = legalMoveHighlighterCheckBox.isSelected();
            }
        });
        fileMenu.add(legalMoveHighlighterCheckBox);

        return fileMenu;
    }

    

    public class BoardPanel extends JPanel {

        final java.util.List<TilePanel> boardTiles;

        BoardPanel() {
            super(new GridLayout(8, 8));
            this.boardTiles = new ArrayList<>();
            for (int i = 0; i < 64; i++) {
                final TilePanel tilePanel = new TilePanel(this, i);
                this.boardTiles.add(tilePanel);
                add(tilePanel);
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            validate();
        }

        public void drawBoard(final Board board) {
            removeAll();
            for (final TilePanel tilePanel : boardTiles) {
                tilePanel.drawTile(board);
                add(tilePanel);
            }
            validate();
            repaint();
        }

    }

    /*public java.util.List<Move> getMoveLog () {
        return MoveLog.getMoves();
    }*/
    
    public static class MoveLog {

       public final java.util.List<Move> moves;

        MoveLog() {
            this.moves = new ArrayList<>();
        }

        public java.util.List<Move> getMoves() {
            return this.moves;
        }
        public void addMove(final Move move){
            this.moves.add(move);
        }
        public int size() {
            return this.moves.size();
        }
        public void clear() {
            this.moves.clear();
        }
        public Move removeMove(int index){
            return this.moves.remove(index);
        }
        public boolean removeMove(final Move move) {
            return this.moves.remove(move);
        }

    }

    public class TilePanel extends JPanel {

        private final int tileId;

        TilePanel(final BoardPanel boardPanel, final int tileId) {
            super(new GridBagLayout());

            this.tileId = tileId;
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTileColor();
            assignTilePieceIcon(chessBoard);
            addMouseListener(new MouseListener() {

                @Override
                public void mouseClicked(final MouseEvent e) {

                    if (SwingUtilities.isRightMouseButton(e)) {
                        sourceTile = null;
                        destinationTile = null;
                        humanMovedPiece = null;
                        System.out.println("Move not allowed");
                    } else if (SwingUtilities.isLeftMouseButton(e)) {
                        System.out.println("left");
                        if (sourceTile == null) {
                            sourceTile = chessBoard.getTile(tileId);
                            System.out.println("the SOURCE TILE IS " + tileId);
                            humanMovedPiece = sourceTile.getPiece();
                            if (humanMovedPiece == null) {
                                sourceTile = null;
                            }
                        } else {
                            destinationTile = chessBoard.getTile(tileId);
                            final Move move = Move.MoveFactory.createMove(chessBoard, sourceTile.getTileCoordinate(),
                                    destinationTile.getTileCoordinate());
                            System.out.println("destination tile is " + tileId);
                            final MoveTransition transition = chessBoard.currentPlayer().makeMove(move);

                            if (transition.getMoveStatus().isDone()) {

                                chessBoard = transition.getTransitionBoard();
                                moveLog.addMove(move);
                                checkIsCheck();
                                n.sendData(move);//////////////////////////////
                            

                            }
                           /* if(chessBoard!=null){
                                System.out.println("Chess Board is not null");
                            }
                            else{
                                System.out.println("null");
                            }*/
                       
                      n.sendData(chessBoard);
                       
                      
                        sourceTile =null;
                        destinationTile =null;
                        humanMovedPiece = null;
                        //System.out.println("NULLED EVEYTHING");
                    }
                    SwingUtilities.invokeLater(new Runnable(){
                        @Override
                        public void run() {
                           takenPiecesPanel.redo(moveLog); 
                           repaintWholeBoard(chessBoard);
                        }
                    });
                }
                 
            }

                @Override
                public void mousePressed(final MouseEvent e) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void mouseReleased(final MouseEvent e) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void mouseEntered(final MouseEvent e) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void mouseExited(MouseEvent e) {
                    // TODO Auto-generated method stub

                }
            });

            validate();
        }

        public void drawTile( Board board) {
            assignTileColor();
            assignTilePieceIcon(board);
            highlightLegals(board);
            validate();
            repaint();
        }


        private void assignTilePieceIcon( Board board){
            this.removeAll();
            if(board.getTile(this.tileId).isTileOccupied()){
             
            try {
                final BufferedImage  image = ImageIO.read(new File(defaultPieceImagesPath + board.getTile(this.tileId).getPiece().getPieceAlliance().toString().substring(0,1)
                + board.getTile(this.tileId).getPiece().toString() + ".gif"));
                add(new JLabel(new ImageIcon(image)));
            }catch(IOException e){
                e.printStackTrace();
            }
            
        }
        
    }

        private void highlightLegals( Board board) {
            if(true){
                
                for (final Move move : pieceLegalMoves(board)) {
                    if (move.getDestinationCoordinate() == this.tileId) {
                        try {
                            add(new JLabel(new ImageIcon(ImageIO.read(new File("green_dot.png")))));
                        }
                        catch (final IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        private Collection<Move> pieceLegalMoves(final Board board) {
            if(humanMovedPiece != null && humanMovedPiece.getPieceAlliance() == board.currentPlayer().getAlliance()) {
                return humanMovedPiece.calculateLegalMoves(board);
            }
            return Collections.emptyList();
        }

        private void assignTileColor() {
            if (BoardUtils.INSTANCE.FIRST_ROW.get(this.tileId) ||
                BoardUtils.INSTANCE.THIRD_ROW.get(this.tileId) ||
                BoardUtils.INSTANCE.FIFTH_ROW.get(this.tileId) ||
                BoardUtils.INSTANCE.SEVENTH_ROW.get(this.tileId)) {
                setBackground(this.tileId % 2 == 0 ? lightTileColor : darkTileColor);
            } else if(BoardUtils.INSTANCE.SECOND_ROW.get(this.tileId) ||
                      BoardUtils.INSTANCE.FOURTH_ROW.get(this.tileId) ||
                      BoardUtils.INSTANCE.SIXTH_ROW.get(this.tileId)  ||
                      BoardUtils.INSTANCE.EIGHTH_ROW.get(this.tileId)) {
                setBackground(this.tileId % 2 != 0 ? lightTileColor : darkTileColor);
            }
        }
         
    }
}