import java.util.Objects;

/**
 * Move represents one possible move storing the moving stone (row and column) and the direction
 */

public class Move
{
    int row;
    int column;
    int direction; //1 = up, 2 = up right, 3 = right, 4 = down right, 5 = down, 6 = down left, 7 = left, 8 = up left

    public int getRow() {
        return row;
    }

    public Move(int row, int column, int direction) {
        this.row = row;
        this.column = column;
        this.direction = direction;
    }

    public void setRow(int row) {
        if(row >= 0 && row <= 4)
        {
        this.row = row;
        }
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        if(column >= 0 && column <= 8)
        {
        this.column = column;
        }
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        if(direction >= 1 && direction <= 8)
        {
            this.direction = direction;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return row == move.row &&
                column == move.column &&
                direction == move.direction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column, direction);
    }
}
