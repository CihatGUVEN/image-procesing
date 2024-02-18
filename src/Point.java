import lombok.Data;

@Data
public class Point {
    private int x;
    private int y;
    private int R;
    private int G;
    private int B;

    public Point(int x, int y, int r, int g, int b) {
        this.x = x;
        this.y = y;
        R = r;
        G = g;
        B = b;
    }
    public Point() {
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
