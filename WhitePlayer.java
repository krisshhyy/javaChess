import java.util.Collection;

public class WhitePlayer extends Player {

	public WhitePlayer(Board board, Collection<Move> whiteStandardLegalMoves,
			Collection<Move> blackStandardLegalMoves) {
                super(board,whiteStandardLegalMoves,blackStandardLegalMoves);
	}

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getWhitePieces();
    }

    @Override
    public Alliance getAlliance() {
        // TODO Auto-generated method stub
        return Alliance.WHITE;
    }

    @Override
    public Player getOpponent() {
        // TODO Auto-generated method stub
        return this.board.blackPlayer();
    }

    @Override
    protected Collection<Move> calculateKingCastles(Collection<Move> playerLegals, Collection<Move> OpponentLegals) {
        // TODO Auto-generated method stub
        return null;
    }


}
