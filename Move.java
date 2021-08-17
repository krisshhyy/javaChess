import java.io.Serializable;

public abstract class Move implements Serializable{
    final Board board;
    final Piece movedPiece;
    final int destinationCoordinate;

    public static final Move NULL_MOVE = new NullMove();

    Move(final Board board, final Piece movedPiece, final int destinationCoordinate) {

        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
    }

    
    public Board execute() { 
    // creates a new board the old board is never mutated
        final  Board.Builder builder = new Board.Builder();
        for(final Piece piece : this.board.currentPlayer().getActivePieces()){
            
            if(!this.movedPiece.equals(piece)){
                builder.setPiece(piece);

                
            }
        }
        for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()){
            builder.setPiece(piece);
        }

        

        builder.setPiece(this.movedPiece.movePiece(this));//move the selected piece through this 
        builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());// changes the move maker to the opposite alliance
        
        return builder.build();
       
        
    }

    @Override
    public int hashCode(){
        final  int prime = 31;
        int result =  1;

        result = prime * result + this.destinationCoordinate;
        result = prime * result +  this.movedPiece.hashCode();
        return result;
        
    }

    @Override
    public boolean equals(final Object other){
        if(this == other) {
            return true;
        }
        if(!(other instanceof Move)){
            return false;
        }
        final Move otherMove = (Move) other;
        return  getDestinationCoordinate() == otherMove.getDestinationCoordinate()
                && getMovedPiece().equals(otherMove.getMovedPiece());  
    }
    

    public int getCurrentCoordinate(){
        return this.getMovedPiece().getPiecePosition();
    }

    public int getDestinationCoordinate() {
        return this.destinationCoordinate;


    }
    public Piece getMovedPiece(){
        return this.movedPiece;
    }
    
    public boolean isAttack(){
        return false;        
    }

    public boolean isCastlingMove(){
        return false;        
    }

    public Piece getAttackedPiece(){
        return null;        
    }

        // normal moves of a piece
        
        public static final class MajorMove extends Move{

            public MajorMove(Board board, Piece movedPiece, int destinationCoordinate) {
                super(board, movedPiece, destinationCoordinate);
                
            }
        }

        public static class MajorAttackMove extends Move{

            final Piece attackedPiece;

            public MajorAttackMove(Board board, Piece movedPiece, int destinationCoordinate, final Piece attackedPiece) {
                super(board, movedPiece, destinationCoordinate);
                this.attackedPiece = attackedPiece;
            }

            @Override
            public int hashCode() {
                return this.attackedPiece.hashCode() + super.hashCode();
            }

            @Override
            public boolean equals ( final Object other){
                if(this == other) {
                    return true;
                }
                if(!(other instanceof MajorAttackMove)){
                    return false;
                }
                final MajorAttackMove otherAttackMove = (MajorAttackMove) other;
                return  super.equals(otherAttackMove) && getAttackedPiece().equals(otherAttackMove.getAttackedPiece());  
            }

            @Override
            public boolean isAttack() {
                return true;
            }

            @Override
            public Piece getAttackedPiece(){
                return this.attackedPiece;
            }

            }

            

        

        public static class PawnMove extends Move {
            public PawnMove(final Board board,
                            final Piece movedPiece,
                            final int destinationCoordinate){
                super(board, movedPiece, destinationCoordinate);
            }

        }

        public static class PawnAttackMove extends MajorAttackMove {
            public PawnAttackMove(final Board board,
                            final Piece movedPiece,
                            final int destinationCoordinate,
                            final Piece attackedPiece){
                super(board, movedPiece, destinationCoordinate,  attackedPiece);
            }

        }
        public static final class PawnEnPassantAttackMove extends PawnAttackMove {
            public PawnEnPassantAttackMove(final Board board,
                                           final Piece movedPiece,
                                           final int destinationCoordinate,
                                           final Piece attackedPiece){
            super(board, movedPiece, destinationCoordinate,  attackedPiece);
            }
        }

        public static final class PawnJump extends Move {
            public PawnJump(final Board board,
                            final Piece movedPiece,
                            final int destinationCoordinate){
                super(board, movedPiece, destinationCoordinate);
            }

            @Override
            public Board execute(){
                final Board.Builder builder = new Board.Builder();
                for(final Piece piece : this.board.currentPlayer().getActivePieces()){
                    if(!this.movedPiece.equals(piece)){
                        builder.setPiece(piece);
                    }
                }
                for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()){
                    builder.setPiece(piece);
                }
                final Pawn movedPawn = (Pawn) this.movedPiece.movePiece(this);
                builder.setPiece(movedPawn);
               // builder.setEnPassant(movedPawn);
                builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
                return builder.build();
                
            }

        }

        static abstract class CastleMove extends Move {
            public CastleMove(final Board board,
                            final Piece movedPiece,
                            final int destinationCoordinate){
                super(board, movedPiece, destinationCoordinate);
            }

        }

        public static class KingSideCastleMove extends CastleMove {
            public KingSideCastleMove(final Board board,
                            final Piece movedPiece,
                            final int destinationCoordinate){
                super(board, movedPiece, destinationCoordinate);
            }

        }       

        public static class QueenSideCastleMove extends CastleMove {
            public QueenSideCastleMove(final Board board,
                            final Piece movedPiece,
                            final int destinationCoordinate){
                super(board, movedPiece, destinationCoordinate);
            }

        }

        public static final class NullMove extends Move {
            public NullMove(){
                super(null,null,-1);
            }

            @Override
            public Board execute() {
                throw new RuntimeException("cannot execute a null move");
            }
        }

        public static class MoveFactory {
            private MoveFactory() {
                throw new RuntimeException("not instantiable"); 
            }
            public static Move createMove(final Board board,
                                            final int currentCoordinate,
                                            final int destinationCoordinate){
                for(final Move move : board.getAllLegalMoves()) {
                    if(move.getDestinationCoordinate() == destinationCoordinate
                        && move.getCurrentCoordinate() == currentCoordinate){

                            return move;

                    }
                }
                return NULL_MOVE;

            }
        }


}