public class Punkt {
    public int x;
    public int y;

    public Punkt(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Punkt)) return false;
        Punkt p = (Punkt) o;
        return x == p.x && y == p.y;
    }

    @Override
    public int hashCode() {
        return 31 * x + y;
    }
}