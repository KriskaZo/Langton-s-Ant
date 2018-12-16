import java.util.*;
import java.util.stream.Collectors;

public class Board implements Grid {

    private List<StateColor> possibleColors;
    private History history;
    private Ant ant;
    private int width;
    private int height;
    private int stepCount;
    private Coordinate antCoordinate;
    private Configuration configuration;
    private LinkedHashMap<Coordinate, Cell> cells;

    public Board (int columns, int rows, Configuration configuration) {
        this.width = columns;
        this.height = rows;
        this.configuration = configuration;
        cells = new LinkedHashMap<>();
        possibleColors = new ArrayList<>();
        ant = new Ant();
        history = new History();

        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j ++){
                cells.put(new Coordinate(i, j), new BoardCell(new State(StateColor.COLOR_0, null, 0)));
            }
        }
        ArrayList<StateColor> allColors = new ArrayList<StateColor>(Arrays.asList(StateColor.values()));
        for(int i = 0; i < configuration.getConfiguration().length; i ++){
            possibleColors.add(allColors.get(i));
        }

    }
    @Override
    public void setAnt(Ant object, int col, int row) {

        ant = object;
        if(antCoordinate!=null) {
            cells.get(antCoordinate).getState().setAnt(null);
        }
        antCoordinate = new Coordinate(col, row);
        cells.get(antCoordinate).getState().setAnt(ant);
    }

    @Override
    public Map<Coordinate, Ant> getAnts() {
        Map <Coordinate, Ant> coordinatesWithAnts = new HashMap <Coordinate, Ant>() {
        };
        for (Map.Entry<Coordinate, Cell> entry : cells.entrySet()) {
            State cellState = entry.getValue().getState();
            if(cellState.hasAnt()) {
                coordinatesWithAnts.put(entry.getKey(), cellState.getAnt());
            }
        }
        return coordinatesWithAnts;
    }

    public LinkedHashMap<Coordinate, Cell> getEntireBoard () {
        return cells;
    }
    @Override
    public void clearAnts() {
        Map <Coordinate, Ant> coordinatesWithAnts = getAnts();
        for (Map.Entry<Coordinate, Ant> entry: coordinatesWithAnts.entrySet()) {
            State state = cells.get(entry.getKey()).getState();
            state.setAnt(null);
        }
    }

    private void setNextStateColor () {
        int confState = cells.get(antCoordinate).getState().getConfigState();
        StateColor color = possibleColors.get(confState);
        cells.get(antCoordinate).getState().setStateColor(color);

    }
    @Override
    public void performStep() {
        Direction antCurrentDirection = ant.getOrientation();
        history.addBoardHistory(cells);
        history.addBoardDimensions(new BoardDimensions(width, height));
        cells.get(antCoordinate).getState().setAnt(null);
        cells.get(antCoordinate).getState().setNextState(configuration.getConfiguration());
        setNextStateColor();

        int x = antCoordinate.getX();
        int y = antCoordinate.getY();

        switch (antCurrentDirection) {
            case WEST:
                int xWest = antCoordinate.getX() == 0 ? width-1 : x-1;
                antCoordinate.setX(xWest);
                break;
            case EAST:
                int xEast = antCoordinate.getX() == width-1 ? 0 : x+1;
                antCoordinate.setX(xEast);
                break;
            case NORTH:
                int yNorth = antCoordinate.getY() == 0 ? height-1 : y-1;
                antCoordinate.setY(yNorth);
                break;
            case SOUTH:
                int ySouth = antCoordinate.getY() == height-1 ? 0 : y+1;
                antCoordinate.setY(ySouth);
                break;
            default:
                break;

            }
        setCorrectOrientation();
        cells.get(antCoordinate).getState().setAnt(ant);
        stepCount +=1;
        }

    private void setCorrectOrientation() {
        int stateConf = cells.get(antCoordinate).getState().getConfigState();
        String [] conf = configuration.getConfiguration();
        String nextConf = conf[stateConf];
        DoubleLinkedNode<Direction> east = new DoubleLinkedNode<>(Direction.EAST);
        DoubleLinkedList<Direction> directions = new DoubleLinkedList<>(east);
        directions.add(Direction.SOUTH);
        directions.add(Direction.WEST);
        directions.add(Direction.NORTH);
        Direction currentDirection = ant.getOrientation();
        Direction leftDirection = Direction.EAST;
        Direction rightDirection = Direction.WEST;

        int length = 4;
        for (int i = 0; i < length; i++) {
            if (currentDirection == directions.get(i)) {

                leftDirection = directions.get(i-1);
                rightDirection = directions.get(i+1);
                break;
            }

        }
        ant = new Ant();
         switch (nextConf) {
            case "L":
                ant.setOrientation(leftDirection);
                break;
            case "R":
                ant.setOrientation(rightDirection);
                break;
            default:
                break;

        }
    }

    @Override
    public void performStep(int number) {
        for(int i = 0; i < number; i++) {
            performStep();
        }

    }

    @Override
    public void reset(int number) {
        LinkedHashMap<Coordinate, Cell> newCells = history.getBoardHistory(number);
        BoardDimensions boardDimensions = history.getBoardDimensions(number);
        width = boardDimensions.getWidth();
        height = boardDimensions.getHeight();
        cells = new LinkedHashMap<>();
        for (Map.Entry<Coordinate, Cell> entry : newCells.entrySet()) {
            StateColor color = entry.getValue().getState().getStateColor();
            Ant ant = entry.getValue().getState().getAnt();
            int state = entry.getValue().getState().getConfigState();
            State configState = new State (color, ant, state);
           cells.put(entry.getKey(), new BoardCell(configState));
        }
        antCoordinate = getAnts().entrySet().iterator().next().getKey();
        ant = getAnts().get(antCoordinate);
        stepCount = (number > stepCount) ? 0 : stepCount - number;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public List<Cell> getColumn(int i) {
        return cells.entrySet()
                .stream()
                .filter(x -> x.getKey().getX() == i)
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    @Override
    public List<Cell> getRow(int j) {
        return cells.entrySet()
                .stream()
                .filter(x -> x.getKey().getY() == j)
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    private void addOnAxis (String axis, int valForAxis, int valOFOther ) {
        switch (axis) {
            case "X":
                for (int i=width; i<valForAxis; i++){
                    for (int j = 0; j< valOFOther; j++) {
                        cells.put(new Coordinate(i, j), new BoardCell((new State(StateColor.COLOR_0, null, 0))));
                    }
                }
                break;
            case "Y" :
                for (int i = 0; i< valOFOther; i++) {
                    for(int j = height; j< valForAxis; j++) {
                        cells.put(new Coordinate(i, j), new BoardCell(new State(StateColor.COLOR_0, null, 0)));
                    }
                }
                break;
            default:
                break;

        }
    }

    private void removeFromAxis (String axe, int newValue) {
        switch (axe) {
            case "X":
                cells.entrySet().removeIf(x -> x.getKey().getX() >= newValue );
                break;

            case "Y" :
                cells.entrySet().removeIf(x->x.getKey().getY() >= newValue);
                break;
            default:
                break;
        }

    }
    @Override
    public void resize(int cols, int rows) {
        int differenceX = cols- width;
        int differenceY = rows- height;
        int pushDiffX = getPushDifference(cols - width);
        int pushDifY = getPushDifference(rows-height);
        width = cols;
        height = rows;
        if (differenceX > 0 && differenceY>0){
            for (int i = 0; i < cols; i++) {
                for (int j = 0; j < rows; j ++) {
                    if(!cells.containsKey(new Coordinate(i,j)))
                    cells.put(new Coordinate(i, j), new BoardCell(new State(StateColor.COLOR_0, null, 0)));
                }
            }
            expandBoard(pushDiffX, pushDifY);
        } else if (differenceX > 0 && differenceY <=0) {
           addOnAxis("X", cols, rows);
            removeFromAxis("Y", rows);
            expandBoard(pushDiffX, 0);
        } else if (differenceX <= 0 && differenceY >0) {
            addOnAxis("Y", rows, cols);
            removeFromAxis("X", cols);
            expandBoard(0, pushDifY);
        } else {
            cells.entrySet().removeIf(x->x.getKey().getX() >=cols || x.getKey().getY()>=rows);
            resetAnt();
        }
    }

    private void resetAnt () {
        try {
            antCoordinate = getAnts().entrySet().iterator().next().getKey();
        } catch (NoSuchElementException e) {
            antCoordinate = null;
        }
    }
    private int getPushDifference (int difference) {

        return difference /2;
    }

    private void expandBoard (int differenceX, int differenceY) {

        for(int i= width-1; i >= 0; i--) {
            for (int j = height -1; j >=0; j--){
                Coordinate currentCoordinate = new Coordinate(i,j);
                Coordinate previous = new Coordinate (i -differenceX, j -differenceY);
                if (cells.containsKey(previous)) {
                    cells.put(currentCoordinate, cells.get(previous));

                } else {
                    cells.put(currentCoordinate, new BoardCell(new State(StateColor.COLOR_0, null, 0)));
                }
            }
        }

        resetAnt();
    }
    private void clearCells () {
        clearAnts();
        for (Map.Entry<Coordinate, Cell> entry: cells.entrySet()) {
            entry.getValue().getState().resetColor();
        }
    }
    @Override
    public void clear() {
        antCoordinate = null;
        stepCount = 0;
        this.history = new History();
        clearCells();


    }

    @Override
    public int getStepCount() {
        return stepCount;
    }
}
