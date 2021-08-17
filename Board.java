import java.util.*;
import java.io.*;

public class Board implements Serializable {

    private final Collection<Piece> whitePieces;
    private final Collection<Piece> blackPieces;
    private final List<Tile> gameBoard;

    private final WhitePlayer whitePlayer;
    private final BlackPlayer blackPlayer;
    private final  Player currentPlayer;

    private Board(final Builder builder) { // default constructor
        
        this.gameBoard = createGameBoard(builder);
       
        this.whitePieces = calculateActivePieces(this.gameBoard, Alliance.WHITE);
        this.blackPieces = calculateActivePieces(this.gameBoard, Alliance.BLACK);

        final Collection<Move> whiteStandardLegalMoves = calculateLegalMoves(this.whitePieces);
        final Collection<Move> blackStandardLegalMoves = calculateLegalMoves(this.blackPieces);

        this.whitePlayer = new WhitePlayer(this,whiteStandardLegalMoves,  blackStandardLegalMoves);
        this.blackPlayer = new BlackPlayer(this,whiteStandardLegalMoves,  blackStandardLegalMoves);
        this.currentPlayer = builder.nextMoveMaker.choosePlayerByAlliance(this.whitePlayer, this.blackPlayer);
    }

    private Collection<Move> calculateLegalMoves(final Collection<Piece> pieces){
        final List<Move> legalMoves = new ArrayList<>();

        for(final Piece piece: pieces){
            legalMoves.addAll(piece.calculateLegalMoves(this));
        }
        return List.copyOf(legalMoves);
    }

    

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder() ;
        for( int i = 0; i< 64; i++) {
            final String tileText = this.gameBoard.get(i).toString();
            builder.append(String.format("%3s", tileText));
            if((i+1) % 8 == 0) {
                builder.append("\n");
            }
        }
        return builder.toString();

    }
    public Player whitePlayer() {
        return this.whitePlayer;
    }
    public Player blackPlayer() {
        return this.blackPlayer;
    }

    public Player currentPlayer() {
		return this.currentPlayer;
	}

    public Collection<Piece> getWhitePieces(){
        return this.whitePieces;
    }

    public Collection<Piece> getBlackPieces(){
        return this.blackPieces;
    }

    public void printCurrentBoardState() {
        System.out.println(whitePieces);
    }


    private static Collection<Piece> calculateActivePieces(final List<Tile> gameBoard, final Alliance alliance){



        final List<Piece> activePieces = new ArrayList<>();
        for(final Tile tile : gameBoard){
            if(tile.isTileOccupied()) {
                final Piece piece = tile.getPiece();
                if (piece.getPieceAlliance() == alliance){
                    activePieces.add(piece);
                }
            }
        }
        System.out.println("calculate active pieces of each alliance");
        System.out.println(List.copyOf(activePieces));
        return List.copyOf(activePieces);
    }


    public Tile getTile(final int tileCoordinate){
        return gameBoard.get(tileCoordinate);
    }


    // this function cretes all the empty and occupied tiles.
    public static List<Tile> createGameBoard( final Builder builder){
        final Tile tile [] = new Tile[64];
        for(int i = 0 ; i < 64; i++){

            tile[i] = Tile.createTile(i ,builder.boardConfig.get(i)); //calls createTile in class Tile

        }
        System.out.println();
        List<Tile> tiles = new ArrayList<Tile>();
        Collections.addAll(tiles, tile);
        System.out.println("inside createGameBoard");
        System.out.println(List.copyOf(tiles));
            return List.copyOf(tiles);
            
    }

    public static Board createStandardBoard(){

        final Builder builder = new Builder();
        
        builder.setPiece(new Knight(Alliance.BLACK,1));
        builder.setPiece(new Knight( Alliance.WHITE,57));
        builder.setPiece(new Bishop(Alliance.BLACK, 2));
        builder.setPiece(new Bishop(Alliance.BLACK, 5));
        builder.setPiece(new Bishop(Alliance.WHITE, 58));
        builder.setPiece(new Bishop(Alliance.WHITE, 61));
        builder.setPiece(new Knight(Alliance.BLACK,6));
        builder.setPiece(new Knight( Alliance.WHITE,62));
        builder.setPiece(new King(Alliance.BLACK, 4));
        builder.setPiece(new King(Alliance.WHITE, 60));
        builder.setPiece(new Rook(Alliance.BLACK, 0));
        builder.setPiece(new Rook(Alliance.BLACK, 7));
        builder.setPiece(new Rook(Alliance.WHITE, 56));
        builder.setPiece(new Rook(Alliance.WHITE, 63));
        builder.setPiece(new Queen(Alliance.BLACK, 3));
        builder.setPiece(new Queen(Alliance.WHITE, 59));
        builder.setPiece(new Pawn(Alliance.BLACK, 8));
        builder.setPiece(new Pawn(Alliance.BLACK, 9));
        builder.setPiece(new Pawn(Alliance.BLACK, 10));
        builder.setPiece(new Pawn(Alliance.BLACK, 11));
        builder.setPiece(new Pawn(Alliance.BLACK, 12));
        builder.setPiece(new Pawn(Alliance.BLACK, 13));
        builder.setPiece(new Pawn(Alliance.BLACK, 14));
        builder.setPiece(new Pawn(Alliance.BLACK, 15));
        builder.setPiece(new Pawn(Alliance.WHITE, 48));
        builder.setPiece(new Pawn(Alliance.WHITE, 49));
        builder.setPiece(new Pawn(Alliance.WHITE, 50));
        builder.setPiece(new Pawn(Alliance.WHITE, 51));
        builder.setPiece(new Pawn(Alliance.WHITE, 52));
        builder.setPiece(new Pawn(Alliance.WHITE, 53));
        builder.setPiece(new Pawn(Alliance.WHITE, 54));
        builder.setPiece(new Pawn(Alliance.WHITE, 55));
        builder.setMoveMaker(Alliance.WHITE);

        return builder.build();
    }

    public Iterable<Move> getAllLegalMoves(){

        Collection<Move> lists = new ArrayList<>();
        for(Move l : this.whitePlayer.getLegalMoves()){
            lists.add(l);
        }
        for(Move n : this.blackPlayer.getLegalMoves()){
            lists.add(n);
        }
        Iterable<Move> result = lists;
        return result;
        
    }

            // new inner class BUILDER
            public static class Builder implements Serializable {
                // Builder is a class that helps building the game board.

                Map<Integer,Piece> boardConfig;
                Alliance nextMoveMaker;
               // Pawn enPassantPawn;

                public Builder() {
                    this.boardConfig = new HashMap<>();
                }

                public Builder setPiece(final Piece piece){
                    this.boardConfig.put(piece.getPiecePosition(), piece);
                    return this;
                }

                public Builder setMoveMaker( final Alliance nextMoveMaker) {
                    // decides which alliance's turn it is.
                    this.nextMoveMaker = nextMoveMaker;
                    return this;
                }

                public Board build () {
                    return new Board(this);
                }

				/*public void setEnPassant(Pawn enPassantPawn) {
                    this.enPassantPawn = enPassantPawn;
				}*/
            }





}