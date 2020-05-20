import java.util.ArrayList;

/**
 * Author: Kainz, Kaltenbäck, Katalinic
 */
public  class Fanorona
{
    private static FieldPosition[][] boardArray = new FieldPosition[5][9];

    private static ArrayList<Move> possibleMoves = new ArrayList<>();
    private static ArrayList<Attack> possibleAttack = new ArrayList<>();

    public static void main(String[] args)
    {
        System.out.println("Start");
        InitializeField();
        DrawField();
        CheckForPossibleMoves(1);   //state is optained from the user input / white or black is initiating the turn
        CheckForPossibleAttacks(1);


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


        if(CheckIfInList(3,2,3)){       //give user input
            System.out.println("Valid input");
        }else System.out.println("Invalid input");
    }


    /**
     * Draws state of field
     * o -> white stone
     * # -> black stone
     * whitespace -> no stone on position
     */
    private static void DrawField() {

        char white = 'o';
        char black = '#';
        int i = 0; //counts how many rows are printed
        for(FieldPosition[] f : boardArray){
            i ++;
            for(FieldPosition s: f){
                if(s.getStone()==1){
                    System.out.print(white);
                }
                else if(s.getStone()==2){
                    System.out.print(black);
                }
                else{
                    System.out.print(" ");
                }
                if(s.isRight()) System.out.print("-");
            }
            System.out.println();
            if(i % 2 !=0 && i<5)  System.out.println("|\\|/|\\|/|\\|/|\\|/|");
            else if(i % 2 == 0 && i < 5){
                System.out.println("|/|\\|/|\\|/|\\|/|\\|");
            }
        }
        System.out.println();

    }

    /**
     * @param board -> maybe AI checks future state of board
     * @return int [] array with number of white stones in index 0
     * and number of black stones in index 1
     */
    private static int[] CheckState(FieldPosition[][] board){
        int [] numberOfStones = new int [2];
        for(FieldPosition[] f : board){

            for(FieldPosition s: f){
                if(s.getStone()==1)numberOfStones[0]++;
                if(s.getStone()==2)numberOfStones[1]++;
            }
        }
        return numberOfStones;
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
    private static  void CheckForPossibleAttacks(int state) //state: which team; 1 = white, 2 = black
    {
            for (Move m: possibleMoves)
            {
                //isUp
                if ( m.direction==1 && boardArray[m.row-1][m.column].isUp()) {

                    if (boardArray[m.row - 2][m.column].getStone() != state && boardArray[m.row - 2][m.column].getStone() != 0) {

                        possibleAttack.add(new Attack(m.row, m.column, m.direction, 1));

                    }
                }else if(m.direction==1 && boardArray[m.row-1][m.column].getStone()==0){

                    if(boardArray[m.row+1][m.column].getStone()!=0 && boardArray[m.row+1][m.column].getStone()!=state){

                        possibleAttack.add(new Attack(m.row, m.column, m.direction,2));

                    }
                }
                //isUpRight
                if ( m.direction==2 && boardArray[m.row-1][m.column+1].isUpRight()) {

                    if (boardArray[m.row - 2][m.column+2].getStone() != state && boardArray[m.row - 2][m.column+2].getStone() != 0) {

                        possibleAttack.add(new Attack(m.row, m.column, m.direction, 1));

                    }
                }else if(m.direction==2 && boardArray[m.row-1][m.column+1].getStone()==0){

                    if(boardArray[m.row+1][m.column-1].getStone()!=0 && boardArray[m.row+1][m.column-1].getStone()!=state){

                        possibleAttack.add(new Attack(m.row, m.column, m.direction,2));

                    }
                }
                //isRight
                if ( m.direction==3 && boardArray[m.row][m.column+1].isRight()) {

                    if (boardArray[m.row][m.column+2].getStone() != state && boardArray[m.row][m.column+2].getStone() != 0) {

                        possibleAttack.add(new Attack(m.row, m.column, m.direction, 1));

                    }
                }else if(m.direction==3 && boardArray[m.row][m.column+1].getStone()==0){

                    if(boardArray[m.row][m.column-1].getStone()!=0 && boardArray[m.row][m.column-1].getStone()!=state){

                        possibleAttack.add(new Attack(m.row, m.column, m.direction,2));

                    }
                }
                //isDownRight
                if ( m.direction==4 && boardArray[m.row+1][m.column+1].isDownRight()) {

                    if (boardArray[m.row+2][m.column+2].getStone() != state && boardArray[m.row+2][m.column+2].getStone() != 0) {

                        possibleAttack.add(new Attack(m.row, m.column, m.direction, 1));

                    }
                }else if(m.direction==4 && boardArray[m.row+1][m.column+1].getStone()==0){

                    if(boardArray[m.row-1][m.column-1].getStone()!=0 && boardArray[m.row-1][m.column-1].getStone()!=state){

                        possibleAttack.add(new Attack(m.row, m.column, m.direction,2));

                    }
                }
                //isDown
                if ( m.direction==5 && boardArray[m.row+1][m.column].isDown()) {

                    if (boardArray[m.row +2][m.column].getStone() != state && boardArray[m.row + 2][m.column].getStone() != 0) {

                        possibleAttack.add(new Attack(m.row, m.column, m.direction, 1));

                    }
                }else if(m.direction==5 && boardArray[m.row+1][m.column].getStone()==0){

                    if(boardArray[m.row-1][m.column].getStone()!=0 && boardArray[m.row-1][m.column].getStone()!=state){

                        possibleAttack.add(new Attack(m.row, m.column, m.direction,2));

                    }
                }
                //isDownLeft
                if ( m.direction==6 && boardArray[m.row+1][m.column-1].isDownLeft()) {

                    if (boardArray[m.row +2][m.column-2].getStone() != state && boardArray[m.row + 2][m.column-2].getStone() != 0) {

                        possibleAttack.add(new Attack(m.row, m.column, m.direction, 1));

                    }
                }else if(m.direction==6 && boardArray[m.row+1][m.column-1].getStone()==0){

                    if(boardArray[m.row-1][m.column+1].getStone()!=0 && boardArray[m.row-1][m.column+1].getStone()!=state){

                        possibleAttack.add(new Attack(m.row, m.column, m.direction,2));

                    }
                }
                //isLeft
                if ( m.direction==7 && boardArray[m.row][m.column-1].isLeft()) {

                    if (boardArray[m.row][m.column-2].getStone() != state && boardArray[m.row][m.column-2].getStone() != 0) {

                        possibleAttack.add(new Attack(m.row, m.column, m.direction, 1));

                    }
                }else if(m.direction==7 && boardArray[m.row][m.column-1].getStone()==0){

                    if(boardArray[m.row][m.column+1].getStone()!=0 && boardArray[m.row][m.column+1].getStone()!=state){

                        possibleAttack.add(new Attack(m.row, m.column, m.direction,2));

                    }
                }
                //isUpLeft
                if ( m.direction==8 && boardArray[m.row-1][m.column-1].isUpLeft()) {

                    if (boardArray[m.row - 2][m.column-2].getStone() != state && boardArray[m.row - 2][m.column-2].getStone() != 0) {

                        possibleAttack.add(new Attack(m.row, m.column, m.direction, 1));

                    }
                }else if(m.direction==8 && boardArray[m.row-1][m.column-1].getStone()==0){

                    if(boardArray[m.row+1][m.column+1].getStone()!=0 && boardArray[m.row+1][m.column+1].getStone()!=state){

                        possibleAttack.add(new Attack(m.row, m.column, m.direction,2));

                    }
                }
            }
        }

    private static  boolean CheckIfInList(int row, int column, int direction )
    {
        if(!possibleAttack.isEmpty()){
            for (Attack a: possibleAttack)
            {
                if (row == a.row && column == a.column && direction == a.direction) {
                    return true;
                }
            }
        } else{
            for (Move m: possibleMoves)
            {
                if (row == m.row && column == m.column && direction == m.direction) {
                    return true;
                }
            }
        }
        return false;
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
