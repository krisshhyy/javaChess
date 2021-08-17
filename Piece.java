import java.util.*;
import java.io.*;

abstract public class Piece implements Serializable{
    
    protected final PieceType pieceType;
    protected final int piecePosition;
    protected final Alliance pieceAlliance;
    protected final boolean isFirstMove;
    private final int cachedHashCode;

    Piece(final PieceType pieceType,
            final Alliance pieceAlliance,
            final int piecePosition,
            final boolean isFirstMove) {

        this.pieceType= pieceType;
        this.pieceAlliance = pieceAlliance;
        this.piecePosition = piecePosition;
        this.isFirstMove = isFirstMove;
        this.cachedHashCode = computeHashCode();
    }

    private int computeHashCode(){
        int result = pieceType.hashCode();
        result = 31* result + pieceAlliance.hashCode();
        result = 31 * result +  piecePosition;
        result = 31  * result + (isFirstMove ? 1  : 0);
        return result;
    }

    @Override
    public boolean equals(final Object other) {
        if(this == other) {
            return true;
        }
        if(!(other instanceof Move)){
            return false;
        }
        final Piece otherPiece = (Piece) other;
        return piecePosition == otherPiece.getPiecePosition() && pieceType == otherPiece.getPieceType()
                && pieceAlliance == otherPiece.getPieceAlliance();  
    }

    @Override
    public int hashCode(){

        return this.cachedHashCode;
    }



    public Alliance getPieceAlliance(){

        return this.pieceAlliance;
    }

    public int getPiecePosition() {
        return this.piecePosition;
    }

    public boolean isFirstMove(){
        return this.isFirstMove;
    }

    public PieceType getPieceType(){
        return this.pieceType;

    }
    public int getPieceValue() {
        return this.pieceType.getPieceValue();
    }
    public abstract Piece movePiece(Move move);
    public abstract List<Move> calculateLegalMoves(final Board board);




    public enum PieceType{
        PAWN("P",100) {
            @Override
            public boolean isKing() {
                return false;
            }

        },
        KNIGHT("N",300) {
            @Override
            public boolean isKing() {
                
                return false;
            }
        },
        BISHOP("B",300) {
            @Override
            public boolean isKing() {
               
                return false;
            }
        },
        ROOK("R",500) {
            @Override
            public boolean isKing() {
               
                return false;
            }
        },
        QUEEN("Q",900) {
            @Override
            public boolean isKing() {
                
                return false;
            }
        },
        KING("K",10000) {
            @Override
            public boolean isKing() {
                return true;
            }
        };

        private String pieceName;
        private int pieceValue;

        PieceType(final String pieceName,final int pieceValue) {
            this.pieceName = pieceName;
            this.pieceValue  = pieceValue;
        }

        @Override
        public String toString() {
            return this.pieceName;
        }

        public int getPieceValue(){
            return this.pieceValue;
        }
        public abstract boolean isKing();
    }
}
