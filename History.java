import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class History {
    private ArrayList <LinkedHashMap<Coordinate,Cell>> boardHistory;
    private ArrayList <BoardDimensions> boardDimensions;


    public History () {
        boardHistory = new ArrayList <>();
        boardDimensions = new ArrayList<>();
    }

    public void addBoardDimensions (BoardDimensions dimensions) {
        boardDimensions.add(dimensions);
    }

    public BoardDimensions getBoardDimensions (int stateBack) {
        int state = stateBack >= boardDimensions.size() ? 0 : boardDimensions.size()-1 - stateBack;
        boardDimensions.subList(state+1, boardDimensions.size()-1).clear();
        return boardDimensions.get(state);
    }
    public void addBoardHistory (LinkedHashMap<Coordinate,Cell> board) {
        LinkedHashMap<Coordinate, Cell> copy = copy(board);
        boardHistory.add(copy);
    }

    private LinkedHashMap<Coordinate, Cell> copy (LinkedHashMap<Coordinate,Cell> board) {
        LinkedHashMap<Coordinate, Cell> theCopy = new LinkedHashMap<>();
        for (Map.Entry<Coordinate, Cell> entry : board.entrySet()) {
            State state = new State(entry.getValue().getState().getStateColor(), entry.getValue().getState().getAnt()
            , entry.getValue().getState().getConfigState());
            theCopy.put(entry.getKey(), new BoardCell(state));
        }
        return theCopy;
    }

    public LinkedHashMap<Coordinate, Cell> getBoardHistory (int stateBack) {
        int state = stateBack >= boardHistory.size() ? 0 : boardHistory.size()-1 - stateBack;
        boardHistory.subList(state+1, boardHistory.size()).clear();
        return boardHistory.get(state);
    }
}
