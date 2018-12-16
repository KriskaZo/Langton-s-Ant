public class Coordinate  {
    private int x;
    private int y;

    public Coordinate (int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int hashCode (){
        return (x * 0x1f1f1f1f) ^ y;
    }
    @Override
    public boolean equals (Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        return x == ((Coordinate) other).getX() && y == ((Coordinate) other).getY();
    }
    public int getX () {
        return x;
    }

    public int getY () {
        return y;
    }

    public void setX (int x) {
        this.x = x;
    }

    public void setY (int y) {
        this.y = y;
    }
}
