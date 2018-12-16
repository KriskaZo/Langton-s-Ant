public class BoardCell implements Cell {
    private State state;

    public BoardCell (State state) {
        this.state = state;
    }
    @Override
    public State getState() {

        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
