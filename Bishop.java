
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Bishop extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDINATES = {-9, -7, 7, 9};

    public Bishop(final Alliance alliance,
                  final int piecePosition) {
         super(PieceType.BISHOP, alliance, piecePosition, true);
    }

    public Bishop(final Alliance alliance,
                  final int piecePosition,
                   final boolean isFirstMove) {
        super(PieceType.BISHOP, alliance, piecePosition, isFirstMove);
    }

    @Override
    public List<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        for (final int currentCandidate : CANDIDATE_MOVE_COORDINATES) {
            int candidateDestinationCoordinate = this.piecePosition;
            while (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                if (isFirstColumnExclusion(currentCandidate, candidateDestinationCoordinate) ||
                    isEighthColumnExclusion(currentCandidate, candidateDestinationCoordinate)) {
                    break;
                }
               candidateDestinationCoordinate += currentCandidate;
               // candidateDestinationCoordinate = this.piecePosition + currentCandidate;
                if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                    //final Piece pieceAtDestination = board.getPiece(candidateDestinationCoordinate);
                    final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                    if (!candidateDestinationTile.isTileOccupied()) {
                        legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                    }
                    else {
                        final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                        final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
                        if (this.pieceAlliance != pieceAlliance) {
                            legalMoves.add(new Move.MajorAttackMove(board, this, candidateDestinationCoordinate,
                                    pieceAtDestination));
                        }break;
                        
                    }
                }
            }
        }
        return Collections.unmodifiableList(legalMoves);
    }

    /*@Override
    public int locationBonus() {
        return this.pieceAlliance.bishopBonus(this.piecePosition);
    }*/

    @Override
    public Bishop movePiece(final Move move) {
        //return PieceUtils.INSTANCE.getMovedBishop(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
        return new Bishop(move.getMovedPiece().getPieceAlliance(),move.getDestinationCoordinate());
    }

    @Override
    public String toString() {
        return this.pieceType.toString();
    }

    private static boolean isFirstColumnExclusion(final int currentCandidate,
                                                  final int candidateDestinationCoordinate) {
        return (BoardUtils.INSTANCE.FIRST_COLUMN.get(candidateDestinationCoordinate) &&
                ((currentCandidate == -9) || (currentCandidate == 7)));
    }

    private static boolean isEighthColumnExclusion(final int currentCandidate,
                                                   final int candidateDestinationCoordinate) {
        return BoardUtils.INSTANCE.EIGHTH_COLUMN.get(candidateDestinationCoordinate) &&
                        ((currentCandidate == -7) || (currentCandidate == 9));
    }

}