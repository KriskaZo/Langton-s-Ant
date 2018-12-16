import java.util.List;

public class State{
    private StateColor stateColor;
    private Ant ant;
    private int configState;

    public State (StateColor color, Ant ant, int configState) {
        this.stateColor = color;
        this.ant = ant;
        this.configState = configState;
    }

    public int getConfigState() {
        return configState;
    }

    public void setNextState (String [] configuration) {
        if (configState == configuration.length-1){
            configState = 0;
        } else {
            configState +=1;
        }
    }

    public void setStateColor (StateColor color) {
        stateColor = color;
    }
    public Ant getAnt () {
        return this.ant;
    }
    public void setAnt (Ant ant) {
        this.ant = ant;
    }
    public  StateColor getStateColor () {
        return stateColor;
    }
    public boolean hasAnt () {
        return this.ant!=null;
    }

    public void resetColor () {
        this.stateColor = StateColor.COLOR_0;
    }
}
