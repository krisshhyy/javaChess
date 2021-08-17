import java.util.*;

public class King extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDINATE = {-9,-8,-7,-1,1,7,8,9} ;

    King(Alliance pieceAlliance,int piecePosition){
        super(PieceType.KING,pieceAlliance,piecePosition,true);
    }

    King(Alliance pieceAlliance,int piecePosition, boolean isFirstMove){
        super(PieceType.KING,pieceAlliance,piecePosition,isFirstMove);
    }

    @Override
    public List<Move> calculateLegalMoves(Board board) {

        final List<Move> legalMoves = new ArrayList<>();
        for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATE) {
            if (isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                isEighthColumnExclusion(this.piecePosition, currentCandidateOffset)) {
                continue;
            }

            final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;
            if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                if(!candidateDestinationTile.isTileOccupied())
                { 
                    legalMoves.add(new Move.MajorMove(board ,this, candidateDestinationCoordinate));
                }
                else {

                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
                    if (this.pieceAlliance != pieceAlliance )
                    legalMoves.add(new Move.MajorAttackMove(board ,this, candidateDestinationCoordinate, pieceAtDestination));
                }
            }
        }
        return List.copyOf(legalMoves);
    }
    @Override
    public String toString() {
        return PieceType.KING.toString();
    }
    
    @Override
    public Piece movePiece(Move move) {
        
        return new King(move.getMovedPiece().getPieceAlliance(),move.getDestinationCoordinate());
    }

    private static boolean isFirstColumnExclusion(final int currentCandidate,
    final int candidateDestinationCoordinate) {
return BoardUtils.INSTANCE.FIRST_COLUMN.get(currentCandidate)
&& ((candidateDestinationCoordinate == -9) || (candidateDestinationCoordinate == -1) ||
(candidateDestinationCoordinate == 7));
}

private static boolean isEighthColumnExclusion(final int currentCandidate,
     final int candidateDestinationCoordinate) {
return BoardUtils.INSTANCE.EIGHTH_COLUMN.get(currentCandidate)
&& ((candidateDestinationCoordinate == -7) || (candidateDestinationCoordinate == 1) ||
(candidateDestinationCoordinate == 9));
}
}
