import java.util.Objects;

/**
 * Attack represents one possible attack move storing the attacking stone (row and column), the direction
 * of the attack and the attack type
 */

public class Attack {
    int row;
    int column;
    int direction; //1 = up, 2 = up right, 3 = right, 4 = down right, 5 = down, 6 = down left, 7 = left, 8 = up left
    int moveType; //0 = attack, 1 = retreat, 2 = move

    public int getRow() {
        return row;
    }

    public Attack(int row, int column, int direction, int moveType) {
        this.row = row;
        this.column = column;
        this.direction = direction;
        this.moveType = moveType;
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

    public int getmoveType() {
        return moveType;
    }
    public void setmoveType(int moveType) {
        if(moveType >= 1 && direction <= 3)
        {
            this.moveType = moveType;
        }
    }
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attack attack = (Attack) o;
        return row == attack.row &&
                column == attack.column &&
                direction == attack.direction&&
                moveType == attack.moveType;
    }
    @Override
    public int hashCode() {
        return Objects.hash(row, column, direction, moveType);
    }
}
