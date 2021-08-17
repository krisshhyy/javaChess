public class MoveTransition {

    private final Board transitionBoard;
    private final MoveStatus moveStatus;
    private final Move move;

    public MoveTransition(final Board transitionBoard,
                          final Move move,
                          final MoveStatus moveStatus){
            this.transitionBoard = transitionBoard;
            this.move = move;
            this.moveStatus= moveStatus;
                          }

    public MoveStatus getMoveStatus () {
        return this.moveStatus;
    }

    public Board getTransitionBoard(){
        return this.transitionBoard;
    }
}
