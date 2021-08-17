import java.util.Collection;

public class BlackPlayer extends Player{

	public BlackPlayer(Board board, Collection<Move> whiteStandardLegalMoves,
			Collection<Move> blackStandardLegalMoves) {
                super(board,blackStandardLegalMoves,whiteStandardLegalMoves);
    }
    
    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getBlackPieces();
    }

    @Override
    public Alliance getAlliance() {
        
        return Alliance.BLACK;
    }

    @Override
    public Player getOpponent() {
        
        return this.board.whitePlayer();
    }

    @Override
    protected Collection<Move> calculateKingCastles(Collection<Move> playerLegals, Collection<Move> OpponentLegals) {
        // TODO Auto-generated method stub
        return null;
    }

}
