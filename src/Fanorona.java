import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Author: Kainz, Kaltenbäck, Katalinic
 */
public  class Fanorona
{
    private static FieldPosition[][] boardArray = new FieldPosition[5][9];

    private static ArrayList<Move> possibleMoves = new ArrayList<>();
    private static ArrayList<Attack> possibleAttack = new ArrayList<>();

    private static int[] userInput = new int[6];

    public static void main(String[] args) {
        System.out.println("Start");
        Start();
        // Select loop pvp / pv AI / AIv AI
        //loop while !victoryCondition(1 spieler hat keine steine mehr) unentschieden wenn(maximale zugdauer ereicht oder X züge ohne angriff oder beide spieler haben nur noch einen stein mit abstand  )



        /**
        for (Move m : possibleMoves) {
            System.out.println("Possible Move");
            System.out.println("Row: " + m.row);
            System.out.println("Column: " + m.column);
            System.out.println("Direction: " + m.direction);
            System.out.println("");
        }

        for (int row = 0; row < 5; row++) {
            for (int column = 0; column < 9; column++) {
                System.out.print(boardArray[row][column].getStone());
            }
            System.out.println();
        }*/


    }

    private static void Start(){
        System.out.println("Pick mode: \n[1]Player versus Player \n[2] Player versus AI \n[3] Ai versus AI");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        try {
            String user_selected_mode = in.readLine();
            user_selected_mode.trim();
            switch(user_selected_mode){
                case "1":
                    PvpMode();
                    break;
                case "2":
                    PvAIMode();
                    break;
                case "3":
                    AIvsAiMode();
                    break;
                default:
                    System.out.println("Wrong input!");
                    Start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void PvpMode(){
        System.out.println("PVP");
        InitializeField();
        int [] boardState = CheckState(boardArray);
        int counter = 1;
        while(boardState[0]!=0 || boardState[1] !=0){
            DrawField();
            CheckForPossibleMoves(counter);
            CheckForPossibleAttacks(counter);
            if(GetUserInput()){
                ChangeStateNotes(userInput);
                boardState = CheckState(boardArray);
                if(counter==1)counter++;
                else{
                    counter=1;
                }
            }
        }
        if(boardState[0]==0) System.out.println("BLACK WON!");
        if(boardState[1]==0) System.out.println("WHITE WON!");


    }

    private static void PvAIMode(){
        System.out.println("PvAI");
    }

    private static void  AIvsAiMode(){
        System.out.println("AIvAI");
    }

    private static boolean GetUserInput()
    {
        int oldRow;
        int oldColumn;
        int newRow;
        int newColumn;
        int moveType;
        int rowChange;
        int columnChange;
        int direction = 0;

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try
        {
            System.out.println("Row of the stone you want to move:");
            oldRow = Integer.parseInt(reader.readLine());
            System.out.println("Column of the stone you want to move:");
            oldColumn = Integer.parseInt(reader.readLine());
            System.out.println("Row you want to move the stone to:");
            newRow = Integer.parseInt(reader.readLine());
            System.out.println("Column you want to move the stone to:");
            newColumn = Integer.parseInt(reader.readLine());
            System.out.println("Type of your move (0: Forward Attack, 1: Reverse Attack, 2: Move without an attack");
            moveType = Integer.parseInt(reader.readLine());
        }
        catch(NumberFormatException | IOException e)
        {
            System.out.println("Wrong input, please only type in numbers");
            return false;
        }

        rowChange = newRow - oldRow;
        columnChange = newColumn - oldColumn;

        if(rowChange == -1 && columnChange == 0) direction=1;
        else if(rowChange == -1 && columnChange == 1) direction=2;
        else if(rowChange == 0 && columnChange == 1) direction=3;
        else if(rowChange == 1 && columnChange == 1) direction=4;
        else if(rowChange == 1 && columnChange == 0) direction=5;
        else if(rowChange == 1 && columnChange == -1) direction=6;
        else if(rowChange == 0 && columnChange == -1) direction=7;
        else if(rowChange == -1 && columnChange == -1) direction=8;

        Move userMove = new Move(oldRow,oldColumn,direction);
        Attack userAttack = new Attack(oldRow,oldColumn,direction,moveType);


       // if(CheckIfInList(2,3,3)){       //give user input
          //  System.out.println("Valid input");
      //  }else System.out.println("Invalid input");

        if(!possibleAttack.isEmpty() && possibleAttack.contains(userAttack)){
            userInput[0] = oldRow;
            userInput[1] = oldColumn;
            userInput[2] = newRow;
            userInput[3] = newColumn;
            userInput[4] = moveType;
            userInput[5] = direction;
            System.out.println("The chosen move is possible.");
            return true;
        }else if(possibleAttack.isEmpty() && possibleMoves.contains(userMove))
       {
           userInput[0] = oldRow;
           userInput[1] = oldColumn;
           userInput[2] = newRow;
           userInput[3] = newColumn;
           userInput[4] = moveType;
           userInput[5] = direction;
           System.out.println("The chosen move is possible.");
           return true;
       }

       System.out.println("The chosen move is not possible.");
       return false;


    }
    private static void ChangeStateNotes(int[] currentUserInput)
    {
        /*currentUserInput[0] --> row of old position
         currentUserInput[1] --> column of old position
         currentUserInput[2] --> row of new position
         currentUserInput[3] --> column of new position
         currentUserInput[4] --> move type --> 0: Attack, 1: Reverse, 2: simple move without action
         */

        int rowChange = currentUserInput[2] - currentUserInput[0];
        int columnChange = currentUserInput[3] - currentUserInput[1];
        int currentPlayer = boardArray[currentUserInput[0]][currentUserInput[1]].getStone();
        boolean changeFinished = false;
        int stoneCounter = 1;
        int stoneToCheckRow;
        int stoneToCheckColumn;


        if(currentUserInput[4] == 0) //Attack
        {
            try {
                do {
                    stoneToCheckRow = currentUserInput[2] + (rowChange * stoneCounter);
                    stoneToCheckColumn = currentUserInput[3] + (columnChange * stoneCounter);

                    if (boardArray[stoneToCheckRow][stoneToCheckColumn].getStone() != currentPlayer && boardArray[stoneToCheckRow][stoneToCheckColumn].getStone() != 0) {
                        boardArray[stoneToCheckRow][stoneToCheckColumn].setStone(0);
                        stoneCounter++;
                    } else {
                        changeFinished = true;
                    }


                } while (!changeFinished);
            }
            catch (IndexOutOfBoundsException e)
            {

            }
        }

        else if(currentUserInput[4] == 1) //Reverse
        {
            stoneCounter++;
            try {

                do {
                    stoneToCheckRow = currentUserInput[2] - (rowChange * stoneCounter);
                    stoneToCheckColumn = currentUserInput[3] - (columnChange * stoneCounter);

                    if (boardArray[stoneToCheckRow][stoneToCheckColumn].getStone() != currentPlayer && boardArray[stoneToCheckRow][stoneToCheckColumn].getStone() != 0) {
                        boardArray[stoneToCheckRow][stoneToCheckColumn].setStone(0);
                        stoneCounter++;
                    } else {
                        changeFinished = true;
                    }


                } while (!changeFinished);


            }
            catch (IndexOutOfBoundsException e)
            {

            }
        }
        boardArray[currentUserInput[2]][currentUserInput[3]].setStone(boardArray[currentUserInput[0]][currentUserInput[1]].getStone());
        boardArray[currentUserInput[0]][currentUserInput[1]].setStone(0);
    }

    /**
     * Draws state of field
     * o -> white stone
     * # -> black stone
     * whitespace -> no stone on position
     */
    private static void DrawField(){

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
            possibleAttack.clear();
            for (Move m: possibleMoves)
            {
                //isUp
                if ( m.direction==1 && boardArray[m.row-1][m.column].isUp()) {

                    if (boardArray[m.row - 2][m.column].getStone() != state && boardArray[m.row - 2][m.column].getStone() != 0) {

                        possibleAttack.add(new Attack(m.row, m.column, m.direction, 0));

                    }
                } if(m.direction==1 && boardArray[m.row-1][m.column].getStone()==0){

                    if(boardArray[m.row+1][m.column].getStone()!=0 && boardArray[m.row+1][m.column].getStone()!=state){

                        possibleAttack.add(new Attack(m.row, m.column, m.direction,1));

                    }
                }
                //isUpRight
                if ( m.direction==2 && boardArray[m.row-1][m.column+1].isUpRight()) {

                    if (boardArray[m.row - 2][m.column+2].getStone() != state && boardArray[m.row - 2][m.column+2].getStone() != 0) {

                        possibleAttack.add(new Attack(m.row, m.column, m.direction, 0));

                    }
                } if(m.direction==2 && boardArray[m.row-1][m.column+1].getStone()==0){

                    if(boardArray[m.row+1][m.column-1].getStone()!=0 && boardArray[m.row+1][m.column-1].getStone()!=state){

                        possibleAttack.add(new Attack(m.row, m.column, m.direction,1));

                    }
                }
                //isRight
                if ( m.direction==3 && boardArray[m.row][m.column+1].isRight()) {

                    if (boardArray[m.row][m.column+2].getStone() != state && boardArray[m.row][m.column+2].getStone() != 0) {

                        possibleAttack.add(new Attack(m.row, m.column, m.direction, 0));

                    }
                } if(m.direction==3 && boardArray[m.row][m.column+1].getStone()==0){

                    if(boardArray[m.row][m.column-1].getStone()!=0 && boardArray[m.row][m.column-1].getStone()!=state){

                        possibleAttack.add(new Attack(m.row, m.column, m.direction,1));

                    }
                }
                //isDownRight
                if ( m.direction==4 && boardArray[m.row+1][m.column+1].isDownRight()) {

                    if (boardArray[m.row+2][m.column+2].getStone() != state && boardArray[m.row+2][m.column+2].getStone() != 0) {

                        possibleAttack.add(new Attack(m.row, m.column, m.direction, 0));

                    }
                } if(m.direction==4 && boardArray[m.row+1][m.column+1].getStone()==0){

                    if(boardArray[m.row-1][m.column-1].getStone()!=0 && boardArray[m.row-1][m.column-1].getStone()!=state){

                        possibleAttack.add(new Attack(m.row, m.column, m.direction,1));

                    }
                }
                //isDown
                if ( m.direction==5 && boardArray[m.row+1][m.column].isDown()) {

                    if (boardArray[m.row +2][m.column].getStone() != state && boardArray[m.row + 2][m.column].getStone() != 0) {

                        possibleAttack.add(new Attack(m.row, m.column, m.direction, 0));

                    }
                } if(m.direction==5 && boardArray[m.row+1][m.column].getStone()==0){

                    if(boardArray[m.row-1][m.column].getStone()!=0 && boardArray[m.row-1][m.column].getStone()!=state){

                        possibleAttack.add(new Attack(m.row, m.column, m.direction,1));

                    }
                }
                //isDownLeft
                if ( m.direction==6 && boardArray[m.row+1][m.column-1].isDownLeft()) {

                    if (boardArray[m.row +2][m.column-2].getStone() != state && boardArray[m.row + 2][m.column-2].getStone() != 0) {

                        possibleAttack.add(new Attack(m.row, m.column, m.direction, 0));

                    }
                } if(m.direction==6 && boardArray[m.row+1][m.column-1].getStone()==0){

                    if(boardArray[m.row-1][m.column+1].getStone()!=0 && boardArray[m.row-1][m.column+1].getStone()!=state){

                        possibleAttack.add(new Attack(m.row, m.column, m.direction,1));

                    }
                }
                //isLeft
                if ( m.direction==7 && boardArray[m.row][m.column-1].isLeft()) {

                    if (boardArray[m.row][m.column-2].getStone() != state && boardArray[m.row][m.column-2].getStone() != 0) {

                        possibleAttack.add(new Attack(m.row, m.column, m.direction, 0));

                    }
                } if(m.direction==7 && boardArray[m.row][m.column-1].getStone()==0){

                    if(boardArray[m.row][m.column+1].getStone()!=0 && boardArray[m.row][m.column+1].getStone()!=state){

                        possibleAttack.add(new Attack(m.row, m.column, m.direction,1));

                    }
                }
                //isUpLeft
                if ( m.direction==8 && boardArray[m.row-1][m.column-1].isUpLeft()) {

                    if (boardArray[m.row - 2][m.column-2].getStone() != state && boardArray[m.row - 2][m.column-2].getStone() != 0) {

                        possibleAttack.add(new Attack(m.row, m.column, m.direction, 0));

                    }
                } if(m.direction==8 && boardArray[m.row-1][m.column-1].getStone()==0){

                    if(boardArray[m.row+1][m.column+1].getStone()!=0 && boardArray[m.row+1][m.column+1].getStone()!=state){

                        possibleAttack.add(new Attack(m.row, m.column, m.direction,1));

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
