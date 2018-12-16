public class Ant {

    private Direction direction;

    public Ant () {
        direction = Direction.WEST;
    }
    public Direction getOrientation() {
        return direction;
    }

    public void setOrientation (Direction direction) {
        this.direction = direction;
    }
}

