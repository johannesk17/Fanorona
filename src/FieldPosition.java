public class FieldPosition
{
    private int stone; //0 = no stone, 1 = white, 2 = black

    private boolean up;
    private boolean upRight;
    private boolean right;
    private boolean downRight;
    private boolean down;
    private boolean downLeft;
    private boolean left;
    private boolean upLeft;

    public int getStone() {
        return stone;
    }

    public void setStone(int stone) {
        if(stone == 0 || stone == 1 || stone == 2){
        this.stone = stone;
        }
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isUpRight() {
        return upRight;
    }

    public void setUpRight(boolean upRight) {
        this.upRight = upRight;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isDownRight() {
        return downRight;
    }

    public void setDownRight(boolean downRight) {
        this.downRight = downRight;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public boolean isDownLeft() {
        return downLeft;
    }

    public void setDownLeft(boolean downLeft) {
        this.downLeft = downLeft;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isUpLeft() {
        return upLeft;
    }

    public void setUpLeft(boolean upLeft) {
        this.upLeft = upLeft;
    }

    public FieldPosition() {
        this.stone = 0;
        this.up = false;
        this.upRight = false;
        this.right = false;
        this.downRight = false;
        this.down = false;
        this.downLeft = false;
        this.left = false;
        this.upLeft = false;
    }
}
