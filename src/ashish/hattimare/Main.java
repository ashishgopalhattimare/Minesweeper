package ashish.hattimare;

/*
 * Name        :  Ashish Gopal Hattimare
 *
 * Program     :  Minesweeper Pro
 *
 * Description :  The program is build to make a replica of Minesweeper game version found in windows. The purpose of this
 *                is to successfully complete my ICS3U course with the understanding of the course curriculum and apply
 *                it in form of game. The game as four levels (beginner, intermediate, expert, and custom). The first three level
 *                take you straight into the game, while the fourth option i.e. custom takes you to another of option where
 *                you set your own level (by changing he row, column and number of mine in the game).
 ***/

import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.Timer;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

class MinesweeperGame extends JFrame implements ActionListener, MouseListener
{

    private static void main(Object object)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    Container container = getContentPane();

    //SETTING VARIABLES FOR THE MAIN GAME (HEART OF THE GAME - MINESWEEPER GAME)
    public static JPanel minePanel, scorePanel, centerPanel, timePanel, mineLeft, smilePanel;
    public static JLabel firstNum, secondNum, thirdNum, firstMine, secondMine,thirdMine, mineLabel, smileLabel, timeLabel;
    public static Box horizontalTime, horizontalMine;
    public static JButton smileButton;

    public static boolean gameLose = false, timerStart = false;
    public static int tiles=0, mines=0, minesweeperRow=0, minesweeperColumn=0, flags=0;
    public static String digital = "", mineText = "";

    public static long startTime, endTime;
    public static Timer minesweeperTime;

    public static JButton[][] mineButton; // Create a 2D array for the minesweeper buttons
    public static int[][] hintMines; // Create a 2D array for the number around the mines
    public static String[][] cheatArray; // Create a 2D array for the cheat

    //collect the position of the mines set in the game
    public static ArrayList<Integer> minePosition = new ArrayList<Integer>();

    //Collect the zeros in order to open the conneced zeros in the game
    public static ArrayList<String> fillZero      = new ArrayList<String> ();

    //Collect the position of the cell that separate the connected zero from the non-connected cells
    public static ArrayList<String> borderZero    = new ArrayList<String> ();

    //Collect the position of the cells that only open themselves
    public static ArrayList<String> singleHint    = new ArrayList<String> ();

    //Collect the position of the flag in the game
    public static ArrayList<String> flagCollector = new ArrayList<String> ();

    Font integerFont  = new Font("Consolas",Font.PLAIN + Font.BOLD, 15);
    Font minesDisplay = new Font("Times New Roman",Font.PLAIN + Font.BOLD, 50);
    public static Font menuText = new Font("Verdana", Font.BOLD,11);

    //Display confused face when mouseButton is pressed on any cell
    public void mousePressed(MouseEvent event )
    {
        if(!gameLose) {
            smileLabel.setIcon(new ImageIcon("images/confusedSmile.png"));
        }
    }

    //Display normal face when mouseButton is released
    public void mouseReleased  (MouseEvent event )
    {
        if(!gameLose) smileLabel.setIcon(new ImageIcon("images/normalSmile.png"));
    }

    public void mouseEntered   (MouseEvent event ){}
    public void mouseExited    (MouseEvent event ){}

    //Set the mines on the minesweeper table
    public static void settingMines(int mines, int minesweeperRow, int minesweeperColumn)
    {
        minePosition.clear();

        boolean mineList;
        int randomPosition;
        for(int x=1; x<=mines; x++) {

            mineList = true;
            while(mineList) {

                //In every loop take a random number between 0 to the area of the minesweeper table (row*column)
                randomPosition = (int)(Math.random()*(minesweeperRow*minesweeperColumn));

                //If the ranodmPosition is not in the minePosition, add the int to the minePosition and go to next randomNumber
                if(!minePosition.contains(randomPosition)) {

                    mineList = false;
                    minePosition.add(randomPosition);
                }// end if
            }// end while
        }// end for

    }// end settingMines

    //Increase the value of cell around every bomb by 1
    public static void mineSurrouding(String cheatArray[][], int hintMines[][], ArrayList minePosition, int minesweeperRow, int minesweeperColumn)
    {
        int tileInteger = 0;
        for(int row=0; row<minesweeperRow; row++) {
            for(int column=0; column<minesweeperColumn; column++){

                if(minePosition.contains(tileInteger)){

                    // change the cheatString of position [row][column] with "X" to use it as a cheat later in the game
                    cheatArray[row][column] = "X";
                    int temporaryRow = minesweeperRow-1 , temporaryColumn = minesweeperColumn-1;

                    if(row+0>=0 && row+0<=temporaryRow && column-1>=0 && column-1<=temporaryColumn) hintMines[row+0][column-1]++; // leftTile
                    if(row+0>=0 && row+0<=temporaryRow && column+1>=0 && column+1<=temporaryColumn) hintMines[row+0][column+1]++; // rightTile
                    if(row+1>=0 && row+1<=temporaryRow && column-1>=0 && column-1<=temporaryColumn) hintMines[row+1][column-1]++; // downLeft Tile
                    if(row+1>=0 && row+1<=temporaryRow && column+0>=0 && column+0<=temporaryColumn) hintMines[row+1][column+0]++; // downTile
                    if(row+1>=0 && row+1<=temporaryRow && column+1>=0 && column+1<=temporaryColumn) hintMines[row+1][column+1]++; // downRight Tile
                    if(row-1>=0 && row-1<=temporaryRow && column-1>=0 && column-1<=temporaryColumn) hintMines[row-1][column-1]++; // upLeft Tile
                    if(row-1>=0 && row-1<=temporaryRow && column+0>=0 && column+0<=temporaryColumn) hintMines[row-1][column+0]++; // upTile
                    if(row-1>=0 && row-1<=temporaryRow && column+1>=0 && column+1<=temporaryColumn) hintMines[row-1][column+1]++; // upRight Tile
                }

                // else change with "_" to indicate no bomb at that position
                else {
                    cheatArray[row][column] = "_";
                }

                tileInteger++;
            }// end for
        }// end for

    }// end mineSurrounding()

    //Reset the game and go back to gameOption menu
    public static void resetGame()
    {
        minesweeperTime.stop(); //Stop the time
        gameLose = false; timerStart = false;
        minePosition.clear(); fillZero.clear(); borderZero.clear(); singleHint.clear(); flagCollector.clear();
        Main.main(null); // Go to main and restart the game
    }

    //Display the help to the user to understand the purpose of the game and how to play
    public static void helpMinesweeper() {

        //Required objects for the help Option
        JPanel helpPanel = new JPanel(new GridLayout(2,1,1,1)) ;
        JLabel helpLabel = new JLabel();

        helpLabel.setIcon(new ImageIcon("images/helpPlay.png"));
        helpPanel.add(helpLabel,BorderLayout.CENTER);
        helpPanel.setBackground(Color.BLACK);
        JTextArea helpInfo = new JTextArea(10,9);
        JScrollPane scroll = new JScrollPane(helpInfo);

        helpInfo.setFont(menuText);
        scroll = new JScrollPane(helpInfo);

        helpInfo.setEditable(false);
        helpInfo.setLineWrap(true);
        helpInfo.setWrapStyleWord(false);

        helpInfo.setBackground(Color.BLACK);
        helpInfo.setForeground(Color.WHITE);
        helpPanel.add(scroll,BorderLayout.CENTER);

        String description=
                "\n CONTROLS :\n\n"+
                        " - Right click over a closed cell to\n   place a flag. Once flagged, right\n   click again to unflag that\n   perticular cell.\n"+
                        " - Left click to open the cell.\n\n"+
                        " DESCRIPTION :\n\n"+
                        "  The goal of the game is to\n  uncover all cells that are not\n  contain mines without detonating\n  a mine.\n\n"+
                        "  If opened cell is not contain the\n  mine, add digit is revealed in that\n  cell, the digit indicating the\n  number of adjacent cells"+
                        " which\n  contain mines. Player can use\n  information to deduce that\n  certain other cells are mine-free\n  (or mine-filled).\n\n"+
                        "  The player can place a flag\n  graphic on any cell believed to\n  contain a mine.\n";

        helpInfo.append(description);

        //Display the Help Option to the player
        JOptionPane.showMessageDialog(null, helpPanel, "Minesweeper Pro", JOptionPane.PLAIN_MESSAGE);
    }

    //Display cheat to the user if he/she wants it
    public static void cheating()
    {
        String cheatString = "";
        for(int row=0; row<minesweeperRow; row++) {
            for(int column=0; column<minesweeperColumn; column++) {

                cheatString += (cheatArray[row][column] + "    ");
            }// end for

            cheatString+="\n";
        }// end for

        // Change the color of the cheat text to WHITE
        UIManager.put("OptionPane.messageForeground",Color.WHITE);

        //Display the cheat to the user
        JOptionPane.showMessageDialog(null, cheatString, "Minesweeper Pro", JOptionPane.INFORMATION_MESSAGE);

    }// end cheating()

    //Calculate the number of cell left to the user to solve the game and win
    public static int cheatTiles()
    {
        int tilesLeft = 0;
        for(int row=0; row<minesweeperRow; row++){
            for(int column=0; column<minesweeperColumn; column++){

                // increase the number of tiles left only if the cheat[row][column] is equal to "_" : not used
                if(cheatArray[row][column].equals("_")) {
                    tilesLeft++;
                }
            }// end for
        }// end for

        return tilesLeft;

    }// end cheatTiles()

    // Display the about Option to the player if he/she clicks on the about in the menubar
    public static void aboutMinesweeper()
    {

        //Required objects for the about Display
        JPanel aboutPanel = new JPanel();
        JPanel aboutCenter = new JPanel(new BorderLayout(5,5));
        JTextArea aboutText = new JTextArea(7,30);

        //set the text, color of the text and the background of the textArea
        aboutText.setFont(menuText); aboutText.setForeground(Color.WHITE); aboutText.setBackground(Color.BLACK);

        String aboutString = "\n  "+ (char)169 +" IT Benefit company, 2012. All rights\n  reserved.\n  -Replica of Minesweeper Classis\n  (Version : 1.17.14) \n  Help me improve Minesweeper Pro! Send\n  your feedback!\n";

        aboutText.append(aboutString); aboutText.setEditable(false); //append the aboutString to the textArea and make it no rewritable
        JLabel aboutTitle = new JLabel(new ImageIcon("images/minesweeperTitle.png"));
        aboutCenter.add(aboutTitle,BorderLayout.CENTER); // add the title image to the top Panel of the aboutDisplay

        Box verticalAbout = Box.createVerticalBox(); // Create a vertical box
        // add the topPanel and the text to the verticalBox and set the dimensions of the screen
        verticalAbout.add(aboutCenter); verticalAbout.add(aboutText); verticalAbout.setPreferredSize(new Dimension(275,200));

        aboutPanel.add(verticalAbout,BorderLayout.CENTER);
        //Display the about Option to the player
        JOptionPane.showMessageDialog(null, aboutPanel,"C:\\HattimareAG_StringAssignment\\About",JOptionPane.PLAIN_MESSAGE);

    }// end aboutMinesweeper()

    //Display the Programmer Info the the player if he/she clicks on the programmer option in the menubar
    public static void programmerInfo()
    {
        JPanel programmerPanel = new JPanel();
        JPanel titleCenter = new JPanel(new BorderLayout(5,5));
        JTextArea programmerText = new JTextArea(7,30);

        programmerText.setFont(menuText); programmerText.setBackground(Color.BLACK); programmerText.setForeground(Color.WHITE);

        String programInfo = "\n"+
                "   Name                          :   Ashish Gopal Hattimare\n"+
                "   Program Name           :   minesweeper Pro\n"+
                "   Course                        :   Introduction to Computer Science\n"+
                "   Date                            :   11th June, 2015 \n"+
                "   Teacher (superisor)   :   Ms. Strelkovska\n";

        programmerText.append(programInfo); programmerText.setEditable(false);
        JLabel titleProgram = new JLabel(new ImageIcon("images/minesweeperTitle.png"));
        titleCenter.add(titleProgram,BorderLayout.CENTER);

        Box verticalProgrammer = Box.createVerticalBox();
        verticalProgrammer.add(titleCenter);
        verticalProgrammer.add(programmerText);
        programmerPanel.add(verticalProgrammer,BorderLayout.CENTER);

        JOptionPane.showMessageDialog(null, programmerPanel,"C:\\HattimareAG_StringAssignment\\Programmer",JOptionPane.PLAIN_MESSAGE);

    }// end programmerinfo()

    //Check whether the value at this perticular coordinate is 0 or not
    public static void zeroBorder(int mineX,int mineY,String tileCoordinate)
    {
        tileCoordinate = String.format("%d %d",mineX,mineY);

        //If the flag is not set with flag, continue the method. Else do nothing
        if(!flagCollector.contains(mineX+" "+mineY)) {

            //If the cell value is 0, then add the cell coordinates into the fillZero ArrayList and set the text to null
            if (hintMines[mineX][mineY] == 0){
                if (!fillZero.contains(tileCoordinate))
                    fillZero.add(tileCoordinate); //add the tileCoordinate to the fillZero ArrayList
                mineButton[mineX][mineY].setText(""); //show the cell with no text
            }

            //If the cell value is not 0, then add the cell coordinates into the borderZero ArrayList and set text to valueText
            else {
                //If the tileCoordinate is not in the singleHint or in the borderZero
                if (!singleHint.contains(tileCoordinate) && !borderZero.contains(tileCoordinate)) {
                    //add the tileCoordinate to the borderZero ArrayList
                    borderZero.add(tileCoordinate);

                }// end if
                mineButton[mineX][mineY].setText("" + hintMines[mineX][mineY]); //show the value to the player on the cell

            }// end if

            colorCheat(mineX, mineY); // color the cell
            mineButton[mineX][mineY].setEnabled(false); // disable the cell
            mineButton[mineX][mineY].setBorder (new SoftBevelBorder(SoftBevelBorder.LOWERED));

        }// end if

    }// end zeroBorder()

    //Color the cell depending on the value of cell
    public static void colorCheat(int row, int column)
    {
        //add the value to the cheatArray corresponding the hintMines[row][column]
        cheatArray[row][column] = "" + hintMines[row][column];

        //Decide the color of the cell on the value
        if      (hintMines[row][column] == 0) mineButton[row][column].setBackground(Color.DARK_GRAY); // 0
        else if (hintMines[row][column] == 1) mineButton[row][column].setBackground(Color.BLACK)    ; // 1
        else if (hintMines[row][column] == 2) mineButton[row][column].setBackground(Color.BLUE)     ; // 2
        else if (hintMines[row][column] == 3) mineButton[row][column].setBackground(Color.MAGENTA)  ; // 3
        else if (hintMines[row][column] == 4) mineButton[row][column].setBackground(Color.CYAN)     ; // 4
        else if (hintMines[row][column] == 5) mineButton[row][column].setBackground(Color.ORANGE)   ; // 5
        else if (hintMines[row][column] == 6) mineButton[row][column].setBackground(Color.WHITE)    ; // 6
        else if (hintMines[row][column] == 7) mineButton[row][column].setBackground(Color.PINK)     ; // 7
        else mineButton[row][column].setBackground(Color.GREEN); // more than 7

    }// end colorChear()

    //Display exploded mine to the player after he/she loses
    public static void displayMines(int row,int column, ArrayList minePosition, int counter)
    {
        //Color the cell with red display mine (this is the cell that contains mine and is exploded after this cell is opened)
        mineButton[row][column].setBackground(Color.RED);
        mineButton[row][column].setIcon(new ImageIcon("images/mineImage.png"));
        mineButton[row][column].setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
        smileLabel.setIcon(new ImageIcon("images/loseSmile.png"));

        smileButton.add(smileLabel);
        smilePanel.add(smileButton);
        smileButton.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));

        //After the exploded mine is displayed, display the remining mines
        String win = "no";
        allMinesFlag(minePosition, counter=-1, win);

    }// end displayMines()

    //Display all the remaining mines to the player
    public static void allMinesFlag(ArrayList minePosition, int counter, String win)
    {
        int mineShow = 0;

        //If the player has lost the game, display all the wrong flag placed (placed th flag on the cells with no mine in it)
        if(!(win.equals("win")) && flagCollector.size()>0){
            for(int flagInt=0; flagInt<flagCollector.size(); flagInt++){
                int flagIndex = flagCollector.get(flagInt).indexOf(" "); // find the index position of the space

                int flagX = Integer.parseInt(flagCollector.get(flagInt).substring(0,flagIndex)); // grab the xValue of the flag position from the flagCoordinate
                int flagY = Integer.parseInt(flagCollector.get(flagInt).substring(flagIndex+1)); // grab the yValue of the flag position from the flagCoordinate

                //If the cell are flagged but the flags are not placed in the cells with bomb, then display flag with cross
                if(!(minePosition.contains(flagX*minesweeperColumn+flagY))) {

                    mineButton[flagX][flagY].setIcon(new ImageIcon("images/wrongFlag.png"));
                }// end if
            }// end for
        }// end if

        //Display all the remaining mines to the player when game is lost
        for(int mineRow = 0; mineRow<minesweeperRow; mineRow++){ // Go through a for loop to get the x and y Value of the mine
            for(int mineColumn = 0; mineColumn<minesweeperColumn; mineColumn++){

                if(minePosition.contains(mineShow)) {
                    if(mineShow!=counter) {
                        String flagCoordinate = String.format("%d %d",mineRow,mineColumn);

                        //If the flag is placed on the mine, display a red mine (defused mine)
                        if(flagCollector.contains(flagCoordinate)){
                            mineButton[mineRow][mineColumn].setIcon(new ImageIcon("images/defusedBomb.png"));
                        }

                        //If the cell has no flag on it
                        else{
                            //If the game is won, then display winMines (Gold mines)
                            if (win.equals("yes")) {
                                mineButton[mineRow][mineColumn].setIcon(new ImageIcon("images/winBomb.png"));
                            }

                            //If the game is lost, then display the normal Mines
                            else {
                                mineButton[mineRow][mineColumn].setIcon(new ImageIcon("images/mineImage.png"));
                            }// end if
                        }// end if

                        mineButton[mineRow][mineColumn].setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
                    }// end if
                }// end if

                mineShow++;
            }// end for
        }// end for

    }// end allMinesFlag()

    //Initialize the variables required for the game (the values are depend on the Height, Width and mines)
    public static void initializer(int minesweeperHeight, int minesweeperWidth, int minesweeperMine)
    {
        minesweeperRow  = minesweeperHeight; minesweeperColumn = minesweeperWidth;
        tiles = minesweeperRow*minesweeperColumn ; mines = minesweeperMine;

        hintMines  = new int[minesweeperRow][minesweeperColumn]    ;
        cheatArray = new String[minesweeperRow][minesweeperColumn] ;
        mineButton = new JButton[minesweeperRow][minesweeperColumn];

    }// end initializer()

    //Display the time in digital Form
    public static void digitalTime(String digital)
    {
        horizontalTime = Box.createHorizontalBox();
        for(int digiInt = 0; digiInt<digital.length(); digiInt++){

            horizontalTime.add(Box.createRigidArea(new Dimension(0,15)));
            String initialDigit = ""+digital.charAt(digiInt);

            //left hand side digit
            if(digiInt == 0){
                firstNum.setIcon(new ImageIcon("images/"+initialDigit+".png"));
                horizontalTime.add(firstNum);
            }
            //center digit
            else if (digiInt == 1){
                secondNum.setIcon(new ImageIcon("images/"+initialDigit+".png"));
                horizontalTime.add(secondNum);
            }
            //right hand side digit
            else{
                thirdNum.setIcon(new ImageIcon("images/"+initialDigit+".png"));
                horizontalTime.add(thirdNum);
            }// end if
        }// end for

        horizontalTime.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));

    }// end digitalTime()

    //Display the number of mines left in digital form
    public static void mineDigital(String mineText)
    {
        horizontalMine = Box.createHorizontalBox();
        for(int mineInt = 0; mineInt<mineText.length(); mineInt++){

            horizontalMine.add(Box.createRigidArea(new Dimension(0,15)));
            String mineDigit = ""+mineText.charAt(mineInt);

            if(mineInt == 0)
            {
                // if negative, display "-"
                if(mineDigit.equals("-")) {
                    firstMine.setIcon(new ImageIcon("images/dash.png"  ));
                }
                // if positive, display 0<100 and if more than 100, display hundreds digit
                else {
                    firstMine.setIcon(new ImageIcon("images/"+mineText.charAt(mineInt)+".png"));
                }// end if

                horizontalMine.add(firstMine);
            }
            //display the tens digit
            else if(mineInt == 1){

                secondMine.setIcon(new ImageIcon("images/"+mineText.substring(mineText.length()-2,mineText.length()-1)+".png"));
                horizontalMine.add(secondMine);
            }
            // display the ones digit
            else{
                thirdMine.setIcon(new ImageIcon("images/"+mineText.substring(mineText.length()-1)+".png"));
                horizontalMine.add(thirdMine);

            }// end if
        }// end for

        horizontalMine.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));

    }// end mineDigital()

    //Game body : MINESWEEPER JFRAME where the entire game is displayed
    public MinesweeperGame(int minesweeperHeight, int minesweeperWidth, int minesweeperMine){

        //Set up the required variables
        container.setLayout(new BorderLayout(10,10));

        mineLabel   = new JLabel(); smileLabel = new JLabel(); timeLabel = new JLabel(); thirdMine  = new JLabel();
        firstNum    = new JLabel(); secondNum  = new JLabel(); thirdNum  = new JLabel(); firstMine = new JLabel() ; secondMine = new JLabel();
        minePanel   = new JPanel(new BorderLayout(5,5));
        centerPanel = new JPanel(new BorderLayout(6,6));
        scorePanel  = new JPanel(new GridLayout(1,3));
        timePanel   = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        mineLeft = new JPanel(new FlowLayout(FlowLayout.LEFT));

        smilePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        smileButton = new JButton();
        smileButton.setBorder(new EmptyBorder(0,0,0,0));
        smileButton.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));

        setTitle("Minesweeper Pro"); //title
        container.setBackground(Color.LIGHT_GRAY)  ; centerPanel.setBackground(Color.LIGHT_GRAY);
        scorePanel.setBackground(null) ; minePanel.setBackground(Color.LIGHT_GRAY);

        // Take the mine, minesweeperRow, and minesweeperColumn value to the settingMines method to set the mines and buttons of the
        // minesweeper heart
        initializer(minesweeperHeight,minesweeperWidth, minesweeperMine);

        //Set the position of the mines on the minesweeper heart
        settingMines(mines, minesweeperRow, minesweeperColumn);
        Collections.sort(minePosition);

        //Go the mineSurrounding method to increase the value of cell around the mine by 1
        mineSurrouding(cheatArray, hintMines, minePosition, minesweeperRow, minesweeperColumn);

        /****
         * MENU BAR of the game
         */

        JMenuBar minesweeperMenu = new JMenuBar();
        //Main options
        JMenu game = new JMenu("Game"); JMenu help = new JMenu("help"); JMenu about = new JMenu("About");
        //sub options under main options
        String[] gameOptions = {"New Game","Cheat", "Exit"}; String[] helpOptions = {"View Help"}; String[] aboutOption = {"About","Programmer"};
        //Assign the JMenuItems' withe the name
        JMenuItem newGame   = new JMenuItem(gameOptions[0]); JMenuItem cheatGame = new JMenuItem(gameOptions[1]); JMenuItem endGame   = new JMenuItem(gameOptions[2]);
        JMenuItem helpGame  = new JMenuItem(helpOptions[0]);
        JMenuItem aboutGame = new JMenuItem(aboutOption[0]); JMenuItem progAbout = new JMenuItem(aboutOption[1]);

        //Add the subOptions to the main Options respectively
        game.add(newGame); game.add(cheatGame); game.add(endGame); help.add(helpGame); about.add(aboutGame); about.add(progAbout);

        //Adding ActionListener to the sub Options
        newGame.addActionListener(event -> {
            setVisible(false);
            dispose();
            resetGame();
        });

        cheatGame.addActionListener(event -> cheating());
        endGame.addActionListener(event -> System.exit(0));
        helpGame.addActionListener(event -> helpMinesweeper());
        aboutGame.addActionListener(event -> aboutMinesweeper());
        progAbout.addActionListener(event -> programmerInfo());

        minesweeperMenu.add(game);
        minesweeperMenu.add(help);
        minesweeperMenu.add(about);

        setJMenuBar(minesweeperMenu);
        dispose();

        //Display the number of mines to the player(beginning number of mines)
        mineText = String.format("%03d",mines) ;
        mineDigital(mineText);
        mineLeft.add(horizontalMine,BorderLayout.CENTER);

        //Display the face to the player : confused, normal, win, lose
        smileLabel.setIcon(new ImageIcon("images/normalSmile.png"));

        smileButton.add(smileLabel);
        smilePanel.add(smileButton);

        //Display time to the player (begining time)
        digital = "000";
        digitalTime(digital);
        timePanel.add(horizontalTime,BorderLayout.CENTER);

        //Display the mines, smiley face and the time at the top of the minesweeper game
        scorePanel.add(mineLeft);
        scorePanel.add(smilePanel);
        scorePanel.add(timePanel);

        mineLeft.setBackground(null);
        smilePanel.setBackground(null);
        timePanel.setBackground(null);

        minePanel.setLayout(new GridLayout(mineButton.length,mineButton[0].length,2,2));

        //Go through the mineButton of the heart
        int mineInteger = 0;
        for (int row = 0; row<minesweeperRow; row++)
        {
            for (int column = 0; column<minesweeperColumn; column++)
            {
                mineButton[row][column] = new JButton();

                if(!minePosition.contains(mineInteger)){
                    mineButton[row][column].setFont(integerFont);
                }// end if

                mineButton[row][column].addActionListener(this); //add actionListener to every button
                mineButton[row][column].addMouseListener(this) ; //add mouseListener to every button
                minePanel.add(mineButton[row][column]); // add it to the minePanel
                mineButton[row][column].setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
                mineInteger++;

            }// end for
        }// end for

        container.add(new JLabel(""),BorderLayout.EAST ); container.add(new JLabel(""),BorderLayout.WEST );
        container.add(new JLabel(""),BorderLayout.SOUTH); container.add(new JLabel(""),BorderLayout.NORTH);

        scorePanel.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
        minePanel.setBorder (new SoftBevelBorder(SoftBevelBorder.LOWERED));

        centerPanel.add(scorePanel,BorderLayout.NORTH)  ; centerPanel.add(minePanel,BorderLayout.CENTER)  ;
        container.add(centerPanel,BorderLayout.CENTER)  ; //Display the minsweeper to the player
        minesweeperTime = new Timer(100,this); // set the timer

    }// end MinesweeperGame()

    // If mouse Button is clicked
    public void mouseClicked(MouseEvent event )
    {
        JButton actionButton = (JButton)event.getSource() ;

        //If the right-mouse button is clicked on the cell
        if(event.isMetaDown()){

            if(!gameLose){
                //Start the timer when the cell is is placed with flag
                if(!timerStart){
                    startTime = System.currentTimeMillis();
                    minesweeperTime.start();
                    timerStart = true;
                }// end if

                //Go through each and every cell of the minesweeper game
                for(int row=0; row<minesweeperRow; row++)
                {
                    for(int column=0; column<minesweeperColumn; column++)
                    {
                        if(actionButton.equals(mineButton[row][column]))
                        {
                            // Only place flag on the cell, if the cell is not opened
                            if(cheatArray[row][column].equals("_") || cheatArray[row][column].equals("X")) {
                                String flagCoordinate = String.format("%d %d", row, column);

                                //If the flagCollector ArrayList contains the coordinates of the flag position then reset the cell to normal and decrease mineLeft by 1
                                if (flagCollector.contains(flagCoordinate)){
                                    flagCollector.remove(flagCoordinate); //remove the flag position from the flagCoordinate ArrayList
                                    mines++;
                                    mineButton[row][column].setIcon(null); // remove that flag and make that cell back to reset (as it was  in the beginning of the game)
                                }
                                //If the flagCollector ArrayList not contains the coordinates of the flag position then place the flag on the cell and increase the mineLeft by 1
                                else {
                                    mineButton[row][column].setIcon(new ImageIcon("images/flagChange.png")); //add flag to the cell
                                    mines--;
                                    flagCollector.add(flagCoordinate); //append the flag position from the flagCoordinate ArrayList
                                }// end if

                                //Display the number of tiles left to the player
                                int tilesLeft = cheatTiles();
                                mineLabel.setText(null);
                                mineText = String.format("%03d", mines);
                                mineDigital(mineText); mineLeft.revalidate(); mineLeft.removeAll() ; mineLeft.repaint();

                                mineLeft.add(horizontalMine,BorderLayout.CENTER);

                            }// end if
                        }// end if
                    }// end for
                }// end for
            }// end if
        }// end if

    }// end mouseClicked()

    //Go through this, if any action takes place in the game
    public void actionPerformed(ActionEvent event)
    {
        if(event.getSource() == minesweeperTime){
            repaint();
        }

        else {
            int singleTile = 0;

            if (!gameLose){
                JButton actionButton = (JButton) event.getSource();

                //Start the timer if any cell is opened
                if (timerStart == false) {
                    startTime = System.currentTimeMillis();
                    minesweeperTime.start();
                    timerStart = true;
                }
                fillZero.clear();borderZero.clear(); // clear the arayList of fillZero and borderZero

                int counter = 0 ;
                //Go through the cells of the minesweeper
                for (int row = 0; row < minesweeperRow; row++)
                    for (int column = 0; column < minesweeperColumn; column++) {

                        // Only open the cell if the cell is not placed with flag
                        if (actionButton.equals(mineButton[row][column]) && !flagCollector.contains(row+" "+column)) {

                            //If the cell is opened and is found to have mine in it, then the player loses
                            if (minePosition.contains(counter)) {
                                displayMines(row, column, minePosition, counter); //display the mines
                                gameLose = true; minesweeperTime.stop(); break; // stop the timer
                            }

                            //If the cell is not mine
                            else {
                                String tileCoordinate = String.format("%d %d", row, column);

                                //if the value of cell is 0
                                if (hintMines[row][column] == 0) {
                                    mineButton[row][column].setText(""); // set text to null
                                    colorCheat(row, column); // color the cell

                                    //reset the fillZero ArrayList and then add tileCoordinates to the fillZero.
                                    fillZero.clear(); fillZero.add(tileCoordinate);

                                    //Go through this while until the closed cell and zero cell are not separated by cell value more than 0 or has filled the heart
                                    int zeroCounter = 1, temporaryRow = minesweeperRow - 1, temporaryColumn = minesweeperColumn - 1;
                                    while (zeroCounter <= fillZero.size()) {

                                        String zeroCoordinate = fillZero.get(zeroCounter - 1);
                                        int index = zeroCoordinate.indexOf(" ");
                                        int tempRow = Integer.parseInt(zeroCoordinate.substring(0, index)) ; //get the xValue of the cell around every cell
                                        int tempCol = Integer.parseInt(zeroCoordinate.substring(index + 1)); //get the yValue of the cell around every cell

                                        //Go through the cell around zero cells
                                        //Read the left cell
                                        if (tempRow >= 0 && tempRow <= temporaryRow && tempCol - 1 >= 0 && tempCol - 1 <= temporaryColumn) {
                                            int mineX = tempRow + 0, mineY = tempCol - 1;
                                            //Go to zeroBorder methods to decide whether the cell has to add to added to fillZero or not
                                            zeroBorder(mineX, mineY, tileCoordinate);
                                        }
                                        //Read the right cell
                                        if (tempRow >= 0 && tempRow <= temporaryRow && tempCol + 1 >= 0 && tempCol + 1 <= temporaryColumn) {
                                            int mineX = tempRow + 0, mineY = tempCol + 1;
                                            zeroBorder(mineX, mineY, tileCoordinate);
                                        }
                                        //Read the bottom-left cell
                                        if (tempRow + 1 >= 0 && tempRow + 1 <= temporaryRow && tempCol - 1 >= 0 && tempCol - 1 <= temporaryColumn) {
                                            int mineX = tempRow + 1, mineY = tempCol - 1;
                                            zeroBorder(mineX, mineY, tileCoordinate);
                                        }
                                        //Read the bottom cell
                                        if (tempRow + 1 >= 0 && tempRow + 1 <= temporaryRow && tempCol >= 0 && tempCol <= temporaryColumn) {
                                            int mineX = tempRow + 1, mineY = tempCol + 0;
                                            zeroBorder(mineX, mineY, tileCoordinate);
                                        }
                                        //Read the bottom-right cell
                                        if (tempRow + 1 >= 0 && tempRow + 1 <= temporaryRow && tempCol + 1 >= 0 && tempCol + 1 <= temporaryColumn) {
                                            int mineX = tempRow + 1, mineY = tempCol + 1;
                                            zeroBorder(mineX, mineY, tileCoordinate);
                                        }
                                        //Read the top-left cell
                                        if (tempRow - 1 >= 0 && tempRow - 1 <= temporaryRow && tempCol - 1 >= 0 && tempCol - 1 <= temporaryColumn) {
                                            int mineX = tempRow - 1, mineY = tempCol - 1;
                                            zeroBorder(mineX, mineY, tileCoordinate);
                                        }
                                        //Read the top cell
                                        if (tempRow - 1 >= 0 && tempRow - 1 <= temporaryRow && tempCol >= 0 && tempCol <= temporaryColumn) {
                                            int mineX = tempRow - 1, mineY = tempCol + 0;
                                            zeroBorder(mineX, mineY, tileCoordinate);
                                        }
                                        //Read the top-right cell
                                        if (tempRow - 1 >= 0 && tempRow - 1 <= temporaryRow && tempCol + 1 >= 0 && tempCol + 1 <= temporaryColumn) {
                                            int mineX = tempRow - 1, mineY = tempCol + 1;
                                            zeroBorder(mineX, mineY, tileCoordinate);
                                        }
                                        zeroCounter++;
                                    }
                                }

                                //If the value of cell is not 0
                                else {
                                    colorCheat(row, column); //color the cell
                                    mineButton[row][column].setText("" + hintMines[row][column]); //set the value of the cell
                                    tileCoordinate = String.format("%d %d", row, column); // fing the tile Coordinates
                                    singleHint.add(tileCoordinate); // append the tileCoordinates to the singleHint ArrayList
                                    singleTile++;
                                }
                            }
                            mineButton[row][column].setEnabled(false); //disable the cell
                            mineButton[row][column].setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
                        }
                        counter++;
                    }

                //Display the number of mines left to the player
                int tilesLeft = cheatTiles();
                mineText = String.format("%03d",mines);
                mineDigital(mineText); mineLeft.revalidate(); mineLeft.removeAll() ; mineLeft.repaint();
                mineLeft.add(horizontalMine,BorderLayout.CENTER);

                //If the number of tiles left to fill is 0 and you win the game
                if (tilesLeft == 0) {
                    smileLabel.setIcon(new ImageIcon("images/winSmile.png")); //Display winSmile

                    smileButton.add(smileLabel);smilePanel.add(smileButton);
                    smileButton.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));

                    String win = "yes";
                    allMinesFlag(minePosition, counter, win);
                    gameLose = true; minesweeperTime.stop();
                }
            }
        }
    }

    public void paint(Graphics g){
        super.paint(g);
        endTime = System.currentTimeMillis();
        int timeTaken = (int)(endTime-startTime)/1000;
        digital = String.format("%03d",timeTaken);

        //Update timer after every second
        if(timerStart){
            digitalTime(digital); timePanel.revalidate(); timePanel.removeAll(); timePanel.repaint();
            timePanel.add(horizontalTime,BorderLayout.CENTER);
        }

    }// actionPerformed()
}// end MinesweeperGame()

public class Main
{
    public static int minesweeperHeight = 6, minesweeperWidth = 7, minesweeperMine = 1;
    public static boolean customDisplay  = false, customLayout = false, customBoolean = true;

    JSlider heightSlide,widthSlide, mineSlide;

    JPanel screen,customScreen, heightPanel,widthPanel,minePanel, titlePanel, levelPanel, beginnerPanel, interPanel;
    JPanel expertPanel, customPanel, customChoice, verticalTitle, verticalLeveling;

    JLabel heightText, widthText, mineText, titleLabel, maxHeight, maxWidth, maxMine, minHeight, minWidth;
    JLabel minMine, titleIcon, levelTitle;

    JButton beginner, intermediate, expert, custom, customOk, customCancel;

    Box verticalLevel;

    Font customFont = new Font("Times New Roman",Font.PLAIN + Font.BOLD, 18), titleFont = new Font("Times New Roman",Font.PLAIN + Font.BOLD, 25);

    public Main()
    {
        //Set of the required variables for the level Option and custom
        customDisplay = false; customLayout  = false;
        minesweeperHeight = 6; minesweeperWidth = 7; minesweeperMine = 1;

        screen = new JPanel(new BorderLayout(5,5)); customScreen  = new JPanel(new GridLayout(4,1));
        levelPanel = new JPanel(new GridLayout(5,1,5,5));

        levelPanel.setPreferredSize(new Dimension(275,220)); screen.setPreferredSize(new Dimension(275,220));

        //Create Separate Panel for each level Option so that each buttom could be positioned at the center of the Display Screen
        beginnerPanel = new JPanel(new FlowLayout());
        interPanel = new JPanel(new FlowLayout());
        expertPanel = new JPanel(new FlowLayout());
        customPanel = new JPanel(new FlowLayout());

        //Create a vertical Box for the level Option
        verticalTitle = new JPanel(new BorderLayout());
        verticalLeveling = new JPanel(new BorderLayout());
        verticalLevel = Box.createVerticalBox();
        verticalLevel.setPreferredSize(new Dimension(260,240));

        titlePanel  = new JPanel(new FlowLayout(FlowLayout.LEFT));
        heightPanel = new JPanel(new FlowLayout());
        widthPanel  = new JPanel(new FlowLayout());
        minePanel   = new JPanel(new FlowLayout());

        //Assign name to each level Button
        beginner = new JButton("Beginner");
        beginner.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));

        intermediate = new JButton("Intermediate");
        intermediate.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));

        expert = new JButton("Expert");
        expert.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));

        custom = new JButton("Custom...");
        custom.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));

        //Resize the level Buttons
        beginner.setPreferredSize(new Dimension(110, 35));
        beginner.setBackground(Color.WHITE);

        intermediate.setPreferredSize(new Dimension(110, 35));
        intermediate.setBackground(Color.WHITE);

        expert.setPreferredSize(new Dimension(110, 35));
        expert.setBackground(Color.WHITE);

        custom.setPreferredSize(new Dimension(110, 35));
        custom.setBackground(Color.WHITE);

        //Make the level Option and custom Options BLACk in color
        UIManager UI=new UIManager();
        UI.put("OptionPane.background", Color.BLACK);
        UI.put("Panel.background", Color.BLACK);
        UIManager.put("OptionPane.messageForeground",Color.WHITE);

        levelPanel.setBackground(Color.BLACK);
        beginnerPanel.setBackground(Color.BLACK);
        interPanel.setBackground(Color.BLACK);
        expertPanel.setBackground(Color.BLACK);
        customPanel.setBackground(Color.BLACK);

        screen.setBackground(Color.BLACK);
        levelTitle = new JLabel(new ImageIcon("images/minesweeperTitle.png"));

        //add the buttoms to the respective Panels so that they display themselves in order
        beginnerPanel.add(beginner);
        interPanel.add(intermediate);
        expertPanel.add(expert);
        customPanel.add(custom);

        levelPanel.add(levelTitle);
        levelPanel.add(beginnerPanel);
        levelPanel.add(interPanel);
        levelPanel.add(expertPanel);
        levelPanel.add(customPanel);

        //Add the panels to the vertical Box
        verticalTitle.add(levelTitle,BorderLayout.CENTER);
        verticalLeveling.add(levelPanel,BorderLayout.CENTER);
        verticalTitle.setBackground(Color.BLACK);

        verticalLevel.add(verticalTitle);
        verticalLevel.add(verticalLeveling);

        //If the player chooses "Beginner" level, set the game to beginner level
        beginner.addActionListener(event -> {
            minesweeperHeight = 7;
            minesweeperWidth = 7;
            minesweeperMine = 9;

            JOptionPane.getRootFrame().dispose();
            customDisplay = true;
            customBoolean = false;
        });

        //If the player chooses "Intermediate" level, set the game to intermediate level
        intermediate.addActionListener(event -> {
            minesweeperHeight = 10;
            minesweeperWidth = 16;
            minesweeperMine = 20;

            JOptionPane.getRootFrame().dispose();
            customDisplay = true;
            customBoolean = false;
        });

        //If the player chooses "Expert" level, set the game to expert level
        expert.addActionListener(event -> {
            minesweeperHeight = 13;
            minesweeperWidth = 28;
            minesweeperMine = 100;
            JOptionPane.getRootFrame().dispose();
            customDisplay = true;
            customBoolean = false;
        });

        //If the player chooses "Custom..." option, go to custom level
        custom.addActionListener(event -> {
            JOptionPane.getRootFrame().dispose();
            customDisplay = true;
            customLayout = true;
        });

        //Create the slider for the row, column, and number of mines in the game
        heightSlide = new JSlider(JSlider.HORIZONTAL,6,13,6);
        widthSlide = new JSlider(JSlider.HORIZONTAL,7,28,7);
        mineSlide = new JSlider(JSlider.HORIZONTAL,1,99,1);

        heightSlide.setPaintTicks(true);
        widthSlide.setPaintTicks(true);
        mineSlide.setPaintTicks(true);

        //Display the row(height), column(width), and number of mines to the user
        heightText = new JLabel("06");
        widthText = new JLabel("07");
        mineText = new JLabel("01");

        heightText.setForeground(Color.WHITE);
        widthText.setForeground(Color.WHITE);
        mineText.setForeground(Color.WHITE);

        //Display maximum number of row(height), column(width), and mine to the user
        maxHeight = new JLabel("13  ");
        maxWidth = new JLabel("28  ");
        maxMine = new JLabel("99  ") ;

        maxHeight.setForeground(Color.WHITE);
        maxWidth.setForeground(Color.WHITE);
        maxMine.setForeground(Color.WHITE);

        //Display minimum number of row(height), column(width), and mine to the user
        minHeight = new JLabel("6");
        minWidth = new JLabel("7");
        minMine = new JLabel("1");

        minHeight.setForeground(Color.WHITE);
        minWidth.setForeground(Color.WHITE);
        minMine.setForeground(Color.WHITE);

        titleIcon = new JLabel(new ImageIcon("images/mineImage.png"));
        titlePanel.add(titleIcon);

        titleLabel = new JLabel("Custom Game");
        titleLabel.setFont(titleFont);
        titlePanel.add(titleLabel);
        titleLabel.setForeground(Color.WHITE);

        widthText = new JLabel("07");
        heightText = new JLabel("06");
        mineText = new JLabel("01");

        widthText.setForeground(Color.WHITE);
        heightText.setForeground(Color.WHITE);
        mineText.setForeground(Color.WHITE);

        widthText.setFont(customFont);
        heightText.setFont(customFont);
        mineText.setFont(customFont);

        //Give black color to every custom panel
        widthPanel.setBackground(Color.BLACK);
        widthSlide.setBackground(Color.BLACK);
        heightPanel.setBackground(Color.BLACK);
        heightSlide.setBackground(Color.BLACK);
        minePanel.setBackground(Color.BLACK);
        mineSlide.setBackground(Color.BLACK);
        titlePanel.setBackground(Color.BLACK);

        //add the labels to the respective panels
        widthPanel.add(minWidth);
        widthPanel.add(widthSlide);
        widthPanel.add(maxWidth);
        widthPanel.add(widthText);

        heightPanel.add(minHeight);
        heightPanel.add(heightSlide);
        heightPanel.add(maxHeight);
        heightPanel.add(heightText);

        minePanel.add(minMine);
        minePanel.add(mineSlide);
        minePanel.add(maxMine);
        minePanel.add(mineText);

        heightPanel.setBorder(BorderFactory.createTitledBorder("HEIGHT"))    ; // Give the number of rows the "HIEGHT" name
        widthPanel.setBorder(BorderFactory.createTitledBorder("WIDTH"))      ; // Give the number of columns the "WIDTH" name
        minePanel.setBorder(BorderFactory.createTitledBorder("MINES COUNT")) ; // Give the number of mines the "MINE COUNT" name

        customScreen.add(titlePanel,BorderLayout.WEST);
        customScreen.add(widthPanel);
        customScreen.add(heightPanel);
        customScreen.add(minePanel);
        screen.add(customScreen,BorderLayout.CENTER);

        event e = new event();
        heightSlide.addChangeListener(e);
        widthSlide.addChangeListener(e);
        mineSlide.addChangeListener(e);

        //Ask the player for the level (beginner, intermediate, expert, or custom game)
        JOptionPane.showOptionDialog(null, verticalLevel,"Minesweeper Pro", JOptionPane.DEFAULT_OPTION,JOptionPane.PLAIN_MESSAGE, null, new Object[]{}, null);

        //If the player chooses customG Game
        if(customDisplay && customLayout){

            //Let the player set the game
            int okCancel = JOptionPane.showConfirmDialog(null,screen, "Minesweeper Pro", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            //Go to minesweeper game if clicked OK
            if(!(okCancel == JOptionPane.OK_OPTION)){
                customLayout = false;
                if(okCancel == -1) {
                    System.exit(0);
                }// end if
            }// end if
        }// end if

        if(!customDisplay) {
            System.exit(0);
        }// end if

    }// end Main()

    //Change the value of the row(height), column(width), and the nuber of mines as the slider changes
    public class event implements ChangeListener
    {
        public void stateChanged(ChangeEvent e)
        {
            minesweeperHeight = heightSlide.getValue();
            minesweeperWidth = widthSlide.getValue();
            minesweeperMine = mineSlide.getValue();

            heightText.setText(String.format("%02d",minesweeperHeight));
            widthText.setText(String.format("%02d",minesweeperWidth));
            mineText.setText(String.format("%02d",minesweeperMine));

        }// end stateChanged()
    }// end event()

    public static void main(String[] args) {

        customBoolean = true;
        //Run this loop for the player to decide whether he wants to play easy, intermediate, expert or has to set the game by his/her own
        while(customBoolean){
            Main customGame = new Main();

            // if the player has decided, break the loop
            if(minesweeperMine<((minesweeperWidth*minesweeperHeight)-10) && customLayout == true)
                customBoolean = false;
        }

        //Run the main Minesweeper Loop
        MinesweeperGame game = new MinesweeperGame(minesweeperHeight,minesweeperWidth, minesweeperMine);
        game.setSize(40+(45*minesweeperWidth),145+(45*minesweeperHeight));
        game.setLocationRelativeTo(null);
        game.setIconImage(new ImageIcon("images/gameTitle.png").getImage());
        game.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        game.dispose(); game.setResizable(false); game.setVisible(true);

    }// end Main()

}// end Main() class