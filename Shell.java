import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Shell {
    private int innerStepCounter = 0;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String COLOR_0 = "\u001B[47m";
    public static final String COLOR_1 = "\u001B[37;40m";
    public static final String COLOR_2 = "\u001B[42m";
    public static final String COLOR_3 = "\u001B[41m";
    public static final String COLOR_4 = "\u001B[37;44m";
    public static final String COLOR_5 = "\u001B[43m";
    public static final String COLOR_6 = "\u001B[46m";
    public static final String COLOR_7 = "\u001B[45m";
    public static final String COLOR_8 = "\u001B[36;41m";
    public static final String COLOR_9 = "\u001B[31;44m";
    public static final String COLOR_10 = "\u001B[34;43m";
    public static final String COLOR_11 = "\u001B[32;45m";
    private static final String QUIT = "quit";
    private static Board board;
    private static final String PROMPT = "ant> ";
    private static String ERROR = "Error! ";
    private static String NO_ANTS_ERROR = "There are no ants to remove!";
    private static String ERROR_ARGUMENTS = "Invalid arguments.";
    private static String ANT_EXISTS_ERROR = "Ant exists already";
    private static final String HELPTEXT =
                    "Enter commands to manipulate the 2D board of the game.\n" +
                    "Type \"n <width> <height> <configuration>\" to create a new board\n" +
                    "Type \"a <i> <j>\" to set the ant on the i,j 2D point on the board\n" +
                    "Type \"u\" to clear the ants from the board\n" +
                    "Type \"s [<steps>] \" to perform steps or go back to a previous stage in the game.\n" +
                    "Type \"p\" to print the entire 2D board.\n" +
                    "Type \"c\" to clear the entire board and all operations until now.\n" +
                    "Type \"r <width> <height>\" to resize the board.\n" +
                    "Type \"h\" to get more info about the program.\n" +
                    "Type \"quit\" to exit the programm.\n";

    public static void main (String [] args){
        new Shell();
    }

    Shell () {
        boolean running = true;
        Scanner sc = new Scanner(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        String line;
        String [] tokens;
        while (running) {
            System.out.print(PROMPT);
            line = sc.nextLine();
            tokens = line.split("\\s+");
            tokens[0] = tokens[0].toLowerCase();
            String startingLetter = line.substring(0,1);
            switch (startingLetter) {
                case "n":
                    newCommand(tokens);
                    break;
                case "a":
                    newAnt(tokens);
                    break;
                case "u":
                    clearAnts();
                    break;
                case "c":
                    clear();
                    break;
                case "s":
                    checkStep(tokens);
                    break;
                case "p":
                    print();
                    break;
                case "r":
                    resize(tokens);
                    break;
                case "h":
                    System.out.println(HELPTEXT);
                    break;
                 default:
                     break;
            }
            running = !line.equals(QUIT);
        }
    }

    private void clearAnts () {
        if(board.getAnts().isEmpty()){
            showError(ERROR + NO_ANTS_ERROR);
        } else {
            board.clearAnts();
        }
    }

    private void checkStep (String [] tokens) {
        if (tokens.length > 1) {
            try {
                int steps = Integer.parseInt(tokens[1]);
                if (steps > 0) {
                    performStep(tokens);
                } else if(Math.abs(steps)<= board.getStepCount()){
                    reset(tokens);
                }
                innerStepCounter += steps;
            } catch (NumberFormatException e) {
                showError(ERROR + e);
            }
        } else {
            performStep(tokens);
            innerStepCounter +=1;
        }
        System.out.println(innerStepCounter);
    }

    private void showError (String error) {
        System.out.println(error);
    }

    private void newCommand (String [] tokens) {
        innerStepCounter = 0;
        try {
            if (tokens.length != 4 || !tokens[3].toUpperCase().equals(tokens[3])
            || tokens[3].length() > 12 || tokens[3].length() < 2) {
                showError(ERROR + ERROR_ARGUMENTS);
            } else {
                board = new Board(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]), new Configuration(tokens[3]));
            }
        } catch (NumberFormatException e) {
            showError(ERROR + e);
        }
    }

    private void reset (String [] tokens) {
        if(board != null) {
            int stateBack = Integer.parseInt(tokens[1]);
            int positiveStateBack = Math.abs(stateBack);
            board.reset(positiveStateBack);
        }
    }

    private void clear () {
        if (board != null) {
            board.clear();
        } else {
            showError(ERROR);
        }
    }

    private void resize (String [] tokens) {
        if (board != null) {
            try {
                board.resize(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));

            } catch (NumberFormatException e) {
                showError(ERROR + e);
            }
        } else {
            showError(ERROR);
        }
    }
    private void newAnt (String [] tokens) {
        try {
            if (tokens.length != 3 && board==null) {
                showError(ERROR + ERROR_ARGUMENTS);
            } else {
                Ant ant = new Ant();
                int x = Integer.parseInt(tokens[1]);
                int y = Integer.parseInt(tokens[2]);
                if (x >= board.getWidth() || x < 0 || y <0 || y >= board.getHeight()){
                    showError(ERROR + ERROR_ARGUMENTS);
                } else {
                    board.setAnt(ant, Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
                }

            }
        } catch (NumberFormatException  e) {
            showError(ERROR + e);
        }
    }

    private void print () {
        for (int i= 0; i < board.getHeight(); i++) {
            printRow(board.getRow(i));
        }
    }

    private String getColor (StateColor statecolor){
        switch (statecolor) {
            case COLOR_0:
                return COLOR_0;
            case COLOR_2:
                return COLOR_2;
            case COLOR_1:
                return COLOR_1;
            case COLOR_3:
                return COLOR_3;
            case COLOR_4:
                return  COLOR_4;
            case COLOR_5:
                return  COLOR_5;
            case COLOR_6:
                return COLOR_6;
            case COLOR_7:
                return COLOR_7;
            case COLOR_8:
                return  COLOR_8;
            case COLOR_9:
                return COLOR_9;
            case COLOR_10:
                return  COLOR_10;
            case COLOR_11:
                return  COLOR_11;
            default:
                return COLOR_0;
        }
    }

    private String getCorrectNumber (String color) {
        switch(color) {
            case COLOR_0:
                return "0";
            case COLOR_2:
                return "2";
            case COLOR_1:
                return "1";
            case COLOR_3:
                return "3";
            case COLOR_4:
                return  "4";
            case COLOR_5:
                return  "5";
            case COLOR_6:
                return "6";
            case COLOR_7:
                return "7";
            case COLOR_8:
                return  "8";
            case COLOR_9:
                return "9";
            case COLOR_10:
                return  "A";
            case COLOR_11:
                return  "B";
            default:
                return "0";
        }

    }

    private String getCorrectAntRepresentation (State state) {
        switch (state.getAnt().getOrientation()){
            case EAST:
                return (">");
            case WEST:
                return ("<");
            case NORTH:
                return ("^");
            case SOUTH:
                return ("v");
            default:
                return "";
        }
    }


    private void printRow (List<Cell> row){
            if (row.get(0) instanceof  BoardCell){
                for (int i = 0; i< row.size(); i ++) {
                    BoardCell element = (BoardCell)row.get(i);
                    State cellState = element.getState();
                    StringBuilder cell = new StringBuilder();
                    String color = getColor(cellState.getStateColor());
                    cell.append(color);
                    if (cellState.hasAnt()) {
                        cell.append(getCorrectAntRepresentation(cellState));
                        cell.append(ANSI_RESET);
                    }
                    if (!cellState.hasAnt()){
                        cell.append(getCorrectNumber(color));
                        cell.append(ANSI_RESET);
                    }
                    if (element.equals(row.get(row.size()-1))) {
                        System.out.println(cell);
                    } else {
                        System.out.print(cell);
                    }
                }
            }

    }
    private void performStep (String [] tokens) {
        if(board != null && !board.getAnts().isEmpty()){
            switch (tokens.length) {
                case 1:
                    board.performStep();
                    break;
                case 2:
                    try {
                        board.performStep(Integer.parseInt(tokens[1]));

                    } catch (NumberFormatException e) {
                        showError(ERROR + e);
                    }
                    break;
                default:
                    showError(ERROR + ERROR_ARGUMENTS);
            }
        }
    }
}
