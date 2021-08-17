import java.io.Serializable;
import java.util.*;
import  java.io.*;

public abstract class Player implements Serializable{

    protected  final Board board;
   protected final King playerKing;
    protected final Collection<Move> legalMoves;
    private final boolean isInCheck;

    Player(final Board board,final Collection<Move> legalMoves,final  Collection<Move> opponentMoves){
        this.board =board;
        this.playerKing = establishKing();
        this.legalMoves = legalMoves;
        this.isInCheck = !Player.calculateAttacksOnTile(this.playerKing.getPiecePosition(),opponentMoves).isEmpty();
    
  }

  public King getPlayerKing() {
    return this.playerKing;
  }

  public boolean isInCheck() {
    return this.isInCheck;
}


  public Collection<Move> getLegalMoves() {
      return this.legalMoves;
  }

  private static Collection<Move> calculateAttacksOnTile(int piecePosition, Collection<Move> moves){
      final List<Move> attackMoves = new ArrayList<>();
      for(final Move move : moves){
          if(piecePosition == move.getDestinationCoordinate()){
              attackMoves.add(move);
          }
      }
      return List.copyOf(attackMoves);
  }

  private King establishKing() {

      for(final Piece piece :  getActivePieces()) {
          if(piece.getPieceType().isKing()){
              return (King) piece;
          }
      }
      throw new RuntimeException("SHOULD NOT REACH HERE");
  }

  public boolean isMoveLegal(final Move move){
     return this.legalMoves.contains(move);
  }

  public boolean isInStaleMate(){
    return this.isInCheck && !hasEscapeMoves();
 }
 public boolean isCastled(final Move move){
    return false;
 }

public boolean isInCheckMate(final Move move){
    return this.isInCheck && !hasEscapeMoves();
 }


 protected boolean hasEscapeMoves() {
    for(final Move move : this.legalMoves){
        final MoveTransition transition = makeMove(move);
        if(transition.getMoveStatus().isDone()){
            return true;
        }
    }
    return false;
 }


 public MoveTransition makeMove(final Move move){

    System.out.println("in make move");
     if(!isMoveLegal(move)){
        System.out.println("checking if move is legal....................................");
         return new MoveTransition(this.board,move,MoveStatus.ILLEGAL_MOVE); 
         
     }
     final Board transitionBoard = move.execute();

     System.out.println("Out of execute...........");

                               
     return new MoveTransition(transitionBoard, move, MoveStatus.DONE);
 }


  public abstract Collection<Piece> getActivePieces();
  public abstract Alliance getAlliance();
  public abstract Player getOpponent();
  protected abstract Collection<Move> calculateKingCastles(Collection<Move> playerLegals,Collection<Move> OpponentLegals);
  
}
