import org.w3c.dom.ls.LSOutput;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

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
    }

    /**
     * Start print welcome screen and start choices
     * -> user can input 1 for pvp 2 for player vs AI and 3 for AI vs AI
     * -> depending on userInput different methods are called
     */
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

    /**
     * PvpMode main loop for player vs. player game
     * calls InitializeField to setup board
     * while boardState does not return 0 for one of the player
     * -> or draw condition is triggered the game continues
     * counter indicating who's turn it is updates when no attack is possible or stone was only moved
     * method calls DrawField, CheckForPossibleMoves and CheckForPossibleAttacks to draw board and fill lists
     * if the last turn was an attack the method calls TrimToNodeActions trimming the possibleAttacks list
     * -> setting attacked to false and checking if no attacks are possible
     * -> counter is updated and loop start anew if no attacks are possible
     * if possibleAttacks list is not empty userInput occurs
     * -> depending on input board is updated
     */
    private static void PvpMode(){
        System.out.println("PVP");
        InitializeField();
        boolean attacked = false;
        int counter = 1;
        while(winCondition(boardArray)==3){

            possibleMoves=CheckForPossibleMoves(counter,boardArray);
            possibleAttack=CheckForPossibleAttacks(counter,boardArray,possibleMoves);
            if(attacked){
                TrimToNodeActions(possibleAttack,userInput);

                if(possibleAttack.isEmpty()){
                    attacked = false;
                    counter = (counter==1) ? 2:1;
                    continue;
                }
            }
            DrawField();
            if(counter==1) System.out.println("Turn of white (o) player!");
            if(counter==2) System.out.println("Turn of black (#) player!");
            FieldPosition[][] cloneBoard= copyArray(boardArray);


            Attack BestMove = minimax(counter,cloneBoard); //TODO
            int[]BestMoveConv = convertToAttack(BestMove);
            System.out.printf("Best Move: Row- %d  Column- %d newRow- %d NewColumn- %d MoveType- %d Dir- %d",BestMoveConv[0],BestMoveConv[1],BestMoveConv[2],BestMoveConv[3],BestMoveConv[4],BestMoveConv[5]);
            System.out.println("");

            if(GetUserInput()){
                attacked = false;
                boardArray = ChangeStateNotes(userInput, boardArray);
                if(userInput[4]!=2) attacked = true;
                else{
                    attacked = false;
                    counter = (counter==1) ? 2:1;
                }
            }
        }
        if(winCondition(boardArray)==1) System.out.println("BLACK WON!");
        else if(winCondition(boardArray)==0) System.out.println("WHITE WON!");
        else{
            System.out.println("DRAW!");
        }

    }

    /**
     * winCondition checks boardState of parameter board  and returns result
     * @param board
     * @return int 1 if black won, 0 if white won, 2 if draw, 3 if game continues
     */
    private static int winCondition(FieldPosition[][] board){
        int [] boardState = CheckState(board); // 0 is white  1 is black
        if(boardState[0] == 0){
            return 1;
        }
        else if (boardState[1] == 0){
            return 0;
        }
        else if((boardState[0] + boardState[1]) == 2 && possibleAttack.isEmpty()){
            return  2;
        }
        else{
            return 3;
        }
    }

    private static void PvAIMode(){
        System.out.println("PvAI");
    }

    private static void  AIvsAiMode(){
        System.out.println("AIvAI");
    }

    /**
     * TrimToNodeActions trims possibleAttack list to userInput node actions
     * all attack nodes are cleared except the last user input node destination
     * this method gets called before a new user input if an attack was launched last turn
     */
    private static void TrimToNodeActions(ArrayList<Attack> possibleAttackList,int [] userInputArray){

       for(int i=0 ; i<possibleAttackList.size();i++){
            Attack att = possibleAttackList.get(i);
            if(att.column != userInputArray[3] || att.row != userInputArray[2])
            {
                possibleAttackList.remove(i);
                i--;
            }
            else {
                if(Math.abs(att.direction-userInputArray[5])==4){
                    possibleAttackList.remove(i);
                    i--;
                }
            }
        }
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

    private static FieldPosition[][] ChangeStateNotes(int[] currentUserInput, FieldPosition[][] board)
    {
        /*currentUserInput[0] --> row of old position
         currentUserInput[1] --> column of old position
         currentUserInput[2] --> row of new position
         currentUserInput[3] --> column of new position
         currentUserInput[4] --> move type --> 0: Attack, 1: Reverse, 2: simple move without action
         */
        FieldPosition[][] newBoard= copyArray(board);
        int rowChange = currentUserInput[2] - currentUserInput[0];
        int columnChange = currentUserInput[3] - currentUserInput[1];
        int currentPlayer = newBoard[currentUserInput[0]][currentUserInput[1]].getStone();
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

                    if (newBoard[stoneToCheckRow][stoneToCheckColumn].getStone() != currentPlayer && newBoard[stoneToCheckRow][stoneToCheckColumn].getStone() != 0) {
                        newBoard[stoneToCheckRow][stoneToCheckColumn].setStone(0);
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

                    if (newBoard[stoneToCheckRow][stoneToCheckColumn].getStone() != currentPlayer && newBoard[stoneToCheckRow][stoneToCheckColumn].getStone() != 0) {
                        newBoard[stoneToCheckRow][stoneToCheckColumn].setStone(0);
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
        newBoard[currentUserInput[2]][currentUserInput[3]].setStone(newBoard[currentUserInput[0]][currentUserInput[1]].getStone());
        newBoard[currentUserInput[0]][currentUserInput[1]].setStone(0);

        return newBoard;
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
        System.out.println("  0 1 2 3 4 5 6 7 8");
        for(FieldPosition[] f : boardArray){
            System.out.print(i+" ");
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
            System.out.print(" "+i);
            System.out.println();
            i ++;
            if(i % 2 !=0 && i<5)  System.out.println("  |\\|/|\\|/|\\|/|\\|/|");
            else if(i % 2 == 0 && i < 5){
                System.out.println("  |/|\\|/|\\|/|\\|/|\\|");
            }
        }
        System.out.println("  0 1 2 3 4 5 6 7 8");
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

    private static  ArrayList<Move> CheckForPossibleMoves(int state,FieldPosition[][] board) //state: which team; 1 = white, 2 = black
    {
        //possibleMoves.clear();
        ArrayList<Move> returnMove = new ArrayList<>();
        for(int row=0;row<5;row++)
        {
            for (int column = 0; column < 9; column++)
            {
                if(board[row][column].getStone() == state)
                {

                    if (board[row][column].isUp() && board[row - 1][column].getStone() == 0) {
                        returnMove.add(new Move(row, column, 1));
                    }

                    if (board[row][column].isUpRight() && board[row - 1][column + 1].getStone() == 0) {
                        returnMove.add(new Move(row, column, 2));
                    }

                    if (board[row][column].isRight() && board[row][column + 1].getStone() == 0) {
                        returnMove.add(new Move(row, column, 3));
                    }

                    if (board[row][column].isDownRight() && board[row + 1][column + 1].getStone() == 0) {
                        returnMove.add(new Move(row, column, 4));
                    }

                    if (board[row][column].isDown() && board[row + 1][column].getStone() == 0) {
                        returnMove.add(new Move(row, column, 5));
                    }

                    if (board[row][column].isDownLeft() && board[row + 1][column - 1].getStone() == 0) {
                        returnMove.add(new Move(row, column, 6));
                    }

                    if (board[row][column].isLeft() && board[row][column - 1].getStone() == 0) {
                        returnMove.add(new Move(row, column, 7));
                    }

                    if (board[row][column].isUpLeft() && board[row - 1][column - 1].getStone() == 0) {
                        returnMove.add(new Move(row, column, 8));
                    }
                }
            }
        }
        return returnMove;
    }
    private static ArrayList<Attack> CheckForPossibleAttacks(int state,FieldPosition[][] board,ArrayList<Move> availableMoves) //state: which team; 1 = white, 2 = black
    {
            //possibleAttack.clear();
            ArrayList<Attack> returnAttack = new ArrayList<>();
            for (Move m: availableMoves)
            {

                //isUp
                if(m.row >=2){
                    if ( m.direction==1 && board[m.row-1][m.column].isUp()) {

                        if (board[m.row - 2][m.column].getStone() != state && board[m.row - 2][m.column].getStone() != 0) {

                            returnAttack.add(new Attack(m.row, m.column, m.direction, 0));
                        }
                    }
                }
                if(m.row >=1 && m.row <=3 ) {
                    if (m.direction == 1 && board[m.row - 1][m.column].getStone() == 0) {

                        if (board[m.row + 1][m.column].getStone() != 0 && board[m.row + 1][m.column].getStone() != state) {

                            returnAttack.add(new Attack(m.row, m.column, m.direction, 1));
                        }
                    }
                }

                //isUpRight
                if(m.row >=2 && m.column <=6){
                    if ( m.direction==2 && board[m.row-1][m.column+1].isUpRight()) {

                        if (board[m.row - 2][m.column+2].getStone() != state && board[m.row - 2][m.column+2].getStone() != 0) {

                            returnAttack.add(new Attack(m.row, m.column, m.direction, 0));
                        }
                    }
                }
                if(m.row >=1 && m.row <=3 && m.column<=7 && m.column>=1) {
                    if(m.direction==2 && board[m.row-1][m.column+1].getStone()==0){

                        if(board[m.row+1][m.column-1].getStone()!=0 && board[m.row+1][m.column-1].getStone()!=state){

                            returnAttack.add(new Attack(m.row, m.column, m.direction,1));
                        }
                    }
                }

                //isRight
                if(m.column <= 6){
                    if ( m.direction==3 && board[m.row][m.column+1].isRight()) {

                        if (board[m.row][m.column+2].getStone() != state && board[m.row][m.column+2].getStone() != 0) {

                            returnAttack.add(new Attack(m.row, m.column, m.direction, 0));
                        }
                    }
                }
                if(m.column<=7 && m.column>=1){
                    if(m.direction==3 && board[m.row][m.column+1].getStone()==0){

                        if(board[m.row][m.column-1].getStone()!=0 && board[m.row][m.column-1].getStone()!=state){

                            returnAttack.add(new Attack(m.row, m.column, m.direction,1));
                        }
                    }
                }

                //isDownRight
                if(m.row <=2 && m.column <=6){
                    if ( m.direction==4 && board[m.row+1][m.column+1].isDownRight()) {

                        if (board[m.row+2][m.column+2].getStone() != state && board[m.row+2][m.column+2].getStone() != 0) {

                            returnAttack.add(new Attack(m.row, m.column, m.direction, 0));
                        }
                    }
                }
                if(m.row <=3 && m.row >=1 && m.column<=7 && m.column>=1) {
                    if(m.direction==4 && board[m.row+1][m.column+1].getStone()==0){

                        if(board[m.row-1][m.column-1].getStone()!=0 && board[m.row-1][m.column-1].getStone()!=state){

                            returnAttack.add(new Attack(m.row, m.column, m.direction,1));
                        }
                    }
                }

                //isDown
                if(m.row <=2){
                    if ( m.direction==5 && board[m.row+1][m.column].isDown()) {

                        if (board[m.row +2][m.column].getStone() != state && board[m.row + 2][m.column].getStone() != 0) {

                            returnAttack.add(new Attack(m.row, m.column, m.direction, 0));
                        }
                    }
                }
                if(m.row <=3 && m.row >=1 ) {
                    if(m.direction==5 && board[m.row+1][m.column].getStone()==0){

                        if(board[m.row-1][m.column].getStone()!=0 && board[m.row-1][m.column].getStone()!=state){

                            returnAttack.add(new Attack(m.row, m.column, m.direction,1));
                        }
                    }
                }

                //isDownLeft
                if(m.row <=2 && m.column>=2){
                    if ( m.direction==6 && board[m.row+1][m.column-1].isDownLeft()) {

                        if (board[m.row +2][m.column-2].getStone() != state && board[m.row + 2][m.column-2].getStone() != 0) {

                            returnAttack.add(new Attack(m.row, m.column, m.direction, 0));
                        }
                    }
                }

                if(m.row <=3 && m.row >=1 && m.column >=1 && m.column<=7) {
                    if(m.direction==6 && board[m.row+1][m.column-1].getStone()==0){

                        if(board[m.row-1][m.column+1].getStone()!=0 && board[m.row-1][m.column+1].getStone()!=state){

                            returnAttack.add(new Attack(m.row, m.column, m.direction,1));
                        }
                    }
                }

                //isLeft
                if(m.column>=2){
                    if ( m.direction==7 && board[m.row][m.column-1].isLeft()) {

                        if (board[m.row][m.column-2].getStone() != state && board[m.row][m.column-2].getStone() != 0) {

                            returnAttack.add(new Attack(m.row, m.column, m.direction, 0));
                        }
                    }
                }
                if( m.column>=1 && m.column<=7){
                    if(m.direction==7 && board[m.row][m.column-1].getStone()==0){

                        if(board[m.row][m.column+1].getStone()!=0 && board[m.row][m.column+1].getStone()!=state){

                            returnAttack.add(new Attack(m.row, m.column, m.direction,1));
                        }
                    }
                }

                //isUpLeft
                if(m.column>=2 && m.row>=2){
                    if ( m.direction==8 && board[m.row-1][m.column-1].isUpLeft()) {

                        if (board[m.row - 2][m.column-2].getStone() != state && board[m.row - 2][m.column-2].getStone() != 0) {

                            returnAttack.add(new Attack(m.row, m.column, m.direction, 0));
                        }
                    }
                }
                if(m.column>=1 && m.column<= 7 && m.row>=1 && m.row<=3){
                    if(m.direction==8 && board[m.row-1][m.column-1].getStone()==0){

                        if(board[m.row+1][m.column+1].getStone()!=0 && board[m.row+1][m.column+1].getStone()!=state){

                            returnAttack.add(new Attack(m.row, m.column, m.direction,1));
                        }
                    }
                }
            }
            return returnAttack;
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

    //TODO AI functions ->


    static public Attack minimax (int state,FieldPosition[][] board){
        FieldPosition[][] nextBoard= copyArray(board);
        int value, alpha = Integer.MIN_VALUE, beta =Integer.MAX_VALUE;
        Attack bestMove;

        ArrayList<Move> checkMoves=CheckForPossibleMoves(state,nextBoard);
        ArrayList<Attack> checkAttack=CheckForPossibleAttacks(state,nextBoard,checkMoves);

        if(checkAttack.size() != 0) {

            bestMove = checkAttack.get(0);

            for (Attack possibleMove : checkAttack) {


                nextBoard = ChangeStateNotes(convertToAttack(possibleMove),nextBoard);  //todo enable boardchange based on move

                checkMoves=CheckForPossibleMoves(state,nextBoard);
                checkAttack=CheckForPossibleAttacks(state,nextBoard,checkMoves);
                //todo trimm function
                //todo check if another move is possible // wenn ja dann maxmove


                state = (state==1) ? 2:1;
                value = minMove(state, nextBoard, 5, alpha, beta);

                if (value > alpha) {
                    alpha = value;
                    bestMove = possibleMove;
                }
            }
        } else {

            ArrayList<Attack> possibleConvertedMoves = new ArrayList<>();

            for (Move convMove: checkMoves){
                Attack conversionAttack = new Attack(convMove.row,convMove.column,convMove.direction,2);
                possibleConvertedMoves.add(conversionAttack);
            }

            bestMove = possibleConvertedMoves.get(0);
            for (Attack possibleMove : possibleConvertedMoves){

                nextBoard = ChangeStateNotes(convertToAttack(possibleMove),nextBoard);  //todo enable boardchange based on move
                state = (state==1) ? 2:1;
                value = minMove(state, nextBoard, 5, alpha, beta);

                if (value > alpha) {
                    alpha = value;
                    bestMove = possibleMove;
                }

            }

        }


        return bestMove;
    }

    static public int minMove (int state, FieldPosition[][] board, int depth, int alpha, int beta){

        if (winCondition(board)==0 || winCondition(board)==1 || depth <= 0 ){
            return evaluateBoard(board);
        }
        FieldPosition[][] nextBoard= copyArray(board);
        int value;

        ArrayList<Move> checkMoves=CheckForPossibleMoves(state,nextBoard);
        ArrayList<Attack> checkAttack=CheckForPossibleAttacks(state,nextBoard,checkMoves);

        for (Attack possibleMove: checkAttack){


            nextBoard = ChangeStateNotes(convertToAttack(possibleMove),nextBoard);   //todo enable boardchange based on move
            state = (state==1) ? 2:1;
            value = maxMove (state, nextBoard, depth - 1, alpha, beta);

            if (value < beta) {
                beta = value;
            }

            if (beta < alpha) {
                return alpha;
            }
        }
        return beta;
    }

    static public int maxMove (int state, FieldPosition[][] board, int depth, int alpha, int beta){

        if (winCondition(board)==0 || winCondition(board)==1 || depth <= 0 ){
            return evaluateBoard(board);
        }


        FieldPosition[][] nextBoard= copyArray(board);
        int value;

        ArrayList<Move> checkMoves=CheckForPossibleMoves(state,nextBoard);
        ArrayList<Attack> checkAttack=CheckForPossibleAttacks(state,nextBoard,checkMoves);

        for (Attack possibleMove: checkAttack){

            if(convertToAttack(possibleMove)[4]!=2) {
                nextBoard = ChangeStateNotes(convertToAttack(possibleMove),nextBoard);
                value = maxMove (state, nextBoard, depth, alpha, beta);
            }else {
                nextBoard = ChangeStateNotes(convertToAttack(possibleMove),nextBoard);   //todo enable boardchange based on move
                state = (state==1) ? 2:1;
                value = minMove (state, nextBoard, depth - 1, alpha, beta);
            }

            if (value > alpha) {
                alpha = value;
            }
            if (alpha > beta) {
                return beta;
            }
        }
        return alpha;
    }

    static int evaluateBoard(FieldPosition[][] board){
        int value = 0;

        for (int x = 0; x < 5; x++){
            for (int y = 0; y < 9; y++){
                if(board[x][y].getStone() == 1) value++;
                if(board[x][y].getStone() == 0) value--;
            }
        }
        return value;
    }

    static int[] convertToAttack(Attack attack){
        int[] newAttack = new int[6];
        int newRow,newColumn;

        switch (attack.direction){
            case 1:
                newRow = attack.row -1;
                newColumn= attack.column;
                break;
            case 2:
                newRow = attack.row -1;
                newColumn= attack.column+1;
                break;
            case 3:
                newRow = attack.row;
                newColumn= attack.column+1;
                break;
            case 4:
                newRow = attack.row +1;
                newColumn= attack.column+1;
                break;
            case 5:
                newRow = attack.row +1;
                newColumn= attack.column;
                break;
            case 6:
                newRow = attack.row +1;
                newColumn= attack.column-1;
                break;
            case 7:
                newRow = attack.row;
                newColumn= attack.column-1;
                break;
            case 8:
                newRow = attack.row -1;
                newColumn= attack.column-1;
                break;
            default:
                newRow =attack.row;
                newColumn=attack.column;
        }
        newAttack[0] = attack.row;
        newAttack[1] = attack.column;
        newAttack[2]= newRow;
        newAttack[3]=newColumn;
        newAttack[4]=attack.moveType;
        newAttack[5]=attack.direction;

        return newAttack;
    }

    static FieldPosition[][] copyArray(FieldPosition[][] input) {
        if (input == null){
            return null;
        }

        FieldPosition[][] result = new FieldPosition[input.length][input[0].length];

        try
        {
            for (int r = 0; r < input.length; r++)
            {
                for (int s=0;s<input[r].length; s++)
                {
                    result [r][s] = input[r][s].DeepCopy();
                }

            }
        }
        catch (NullPointerException e)
        {
            System.out.println(e.getMessage());
        }

        return result;
    }



}
