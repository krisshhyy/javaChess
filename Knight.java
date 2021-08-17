import java.util.*;

public class Knight extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDINATES = {-17,17,6,-6,-15,15,-10,10};

    public Knight( final Alliance pieceAlliance,final int piecePosition ) {
        super(PieceType.KNIGHT, pieceAlliance,piecePosition, true);
    }

    public Knight(final Alliance pieceAlliance,
                  final int piecePosition,
                  final boolean isFirstMove){
                    super(PieceType.KNIGHT, pieceAlliance ,piecePosition, isFirstMove);
    }

    @Override
    public List<Move> calculateLegalMoves( Board board) {

        int candidateDestinationCoordinate;
        final List<Move> legalMoves = new ArrayList<>();

        for (final int currentCandidate : CANDIDATE_MOVE_COORDINATES){

            if(isFirstColumnExclusion(this.piecePosition, currentCandidate) ||
               isSecondColumnExclusion(this.piecePosition, currentCandidate) ||
               isSeventhColumnExclusion(this.piecePosition, currentCandidate) ||
               isEighthColumnExclusion(this.piecePosition, currentCandidate)) {
                continue;
            }

            candidateDestinationCoordinate = this.piecePosition + currentCandidate;

            if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){

                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                
                if(!candidateDestinationTile.isTileOccupied()){ // if tile is not occupied it is a legal move
                    legalMoves.add(new Move.MajorMove(board ,this, candidateDestinationCoordinate));
                }
                else { 
                    /* if the tile is occupied then the alliance of the piece is checked.
                     if the piece is of the opponent alliance A ATTACKING move is added */

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
        return PieceType.KNIGHT.toString();
    }

    @Override
    public Piece movePiece(Move move) {
        
        return new Knight(move.getMovedPiece().getPieceAlliance(),move.getDestinationCoordinate());
    }

    private static boolean isFirstColumnExclusion(final int currentPosition,
    final int candidateOffset) {
return BoardUtils.INSTANCE.FIRST_COLUMN.get(currentPosition) && ((candidateOffset == -17) ||
(candidateOffset == -10) || (candidateOffset == 6) || (candidateOffset == 15));
}

private static boolean isSecondColumnExclusion(final int currentPosition,
     final int candidateOffset) {
return BoardUtils.INSTANCE.SECOND_COLUMN.get(currentPosition) && ((candidateOffset == -10) || (candidateOffset == 6));
}

private static boolean isSeventhColumnExclusion(final int currentPosition,
      final int candidateOffset) {
return BoardUtils.INSTANCE.SEVENTH_COLUMN.get(currentPosition) && ((candidateOffset == -6) || (candidateOffset == 10));
}

private static boolean isEighthColumnExclusion(final int currentPosition,
     final int candidateOffset) {
return BoardUtils.INSTANCE.EIGHTH_COLUMN.get(currentPosition) && ((candidateOffset == -15) || (candidateOffset == -6) ||
(candidateOffset == 10) || (candidateOffset == 17));
}

}