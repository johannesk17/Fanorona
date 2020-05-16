import java.util.ArrayList;

/**
 * Author: Kainz, Kaltenbäck, Katalinic
 */
public  class Fanorona
{
    private static FieldPosition[][] boardArray = new FieldPosition[5][9];

    private static ArrayList<Move> possibleMoves = new ArrayList<>();

    public static void main(String[] args)
    {
        System.out.println("Start");

        InitializeField();
        CheckForPossibleMoves(1);

        for (Move m: possibleMoves)
        {
            System.out.println("Possible Move");
            System.out.println("Row: "+m.row);
            System.out.println("Column: " + m.column);
            System.out.println("Direction: " + m.direction);
            System.out.println("");
        }

        for(int row=0;row<5;row++)
        {
            for (int column = 0; column < 9; column++)
            {
                System.out.print(boardArray[row][column].getStone());
            }
            System.out.println();
        }
    }

    private static  void CheckForPossibleMoves(int state) //state: which team; 1 = white, 2 = black
    {
        possibleMoves.clear();
        for(int row=0;row<5;row++)
        {
            for (int column = 0; column < 9; column++)
            {
                if(boardArray[row][column].getStone() == state)
                {

                    if (boardArray[row][column].isUp() && boardArray[row - 1][column].getStone() == 0) {
                        possibleMoves.add(new Move(row, column, 1));
                    }

                    if (boardArray[row][column].isUpRight() && boardArray[row - 1][column + 1].getStone() == 0) {
                        possibleMoves.add(new Move(row, column, 2));
                    }

                    if (boardArray[row][column].isRight() && boardArray[row][column + 1].getStone() == 0) {
                        possibleMoves.add(new Move(row, column, 3));
                    }

                    if (boardArray[row][column].isDownRight() && boardArray[row + 1][column + 1].getStone() == 0) {
                        possibleMoves.add(new Move(row, column, 4));
                    }

                    if (boardArray[row][column].isDown() && boardArray[row + 1][column].getStone() == 0) {
                        possibleMoves.add(new Move(row, column, 5));
                    }

                    if (boardArray[row][column].isDownLeft() && boardArray[row + 1][column - 1].getStone() == 0) {
                        possibleMoves.add(new Move(row, column, 6));
                    }

                    if (boardArray[row][column].isLeft() && boardArray[row][column - 1].getStone() == 0) {
                        possibleMoves.add(new Move(row, column, 7));
                    }

                    if (boardArray[row][column].isUpLeft() && boardArray[row - 1][column - 1].getStone() == 0) {
                        possibleMoves.add(new Move(row, column, 8));
                    }
                }
            }
        }
    }

    private static void InitializeField()
    {
        for(int row=0;row<5;row++)
        {
            for(int column=0;column<9;column++)
            {
                boardArray[row][column] = new FieldPosition();

                if (row!=0)
                {
                    boardArray[row][column].setUp(true);
                }

                if (row!=4)
                {
                    boardArray[row][column].setDown(true);
                }

                if (column!=8)
                {
                    boardArray[row][column].setRight(true);
                }

                if (column!=0)
                {
                    boardArray[row][column].setLeft(true);
                }

                if((row%2 == 0 && column%2 == 0) || (row%2 == 1 && column%2 == 1))
                {
                    if(row!=4 && column != 8)
                    {
                        boardArray[row][column].setDownRight(true);
                    }

                    if(row!=4 && column != 0)
                    {
                        boardArray[row][column].setDownLeft(true);
                    }

                    if(row!=0 && column != 8)
                    {
                        boardArray[row][column].setUpRight(true);
                    }

                    if(row!=0 && column != 0)
                    {
                        boardArray[row][column].setUpLeft(true);
                    }

                }

                if(row == 0 || row == 1)
                {
                    boardArray[row][column].setStone(2);
                }

                if(row == 3 || row == 4)
                {
                    boardArray[row][column].setStone(1);
                }

                if(row == 2 && (column == 0 || column==2 || column==5 || column==7))
                {
                    boardArray[row][column].setStone(2);
                }

                if(row == 2 && (column == 1 || column==3 || column==6 || column==8))
                {
                    boardArray[row][column].setStone(1);
                }

                if(row == 2 && column==4)
                {
                    boardArray[row][column].setStone(0);
                }
            }
        }
    }
}
