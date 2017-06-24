/**
 * Created by Matt Ognibene on 6/24/2017.
 *
 * THINGS I WOULD LIKE TO ADD
 * Make inner games that you can not play in grayed out and opaque
 * lines using graphics to create ttt board in inner games
 * Networking
 * Reset game
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UltimateTTT extends JFrame{

    private final int GAME_WIDTH = 621;
    private final int GAME_HEIGHT = 677;
    private final int INNER_WIDTH = 200;
    private final int INNER_HEIGHT = 200;
    private final int BUTTON_SIZE = 61;
    private final int MESSAGES_WIDTH = 630;
    private final int MESSAGES_HEIGHT = 20;
    private final String GAME_FONT = "Arial";
    private final int WINNER_MARK_SIZE = 230;
    private final int BUTTON_MARK_SIZE = 35;

    JTextField jTextFieldMessages;

    private int turn = 1; //1 is player 1. -1 is player two
    private int[] playingLocation = {-1,-1};//The board you are allowed to play in. -1 means can play anywhere
    //                             row  col

    private InnerGame[][] innerGames = new InnerGame[3][3];
    private Game ultimateGame = new Game();


    public UltimateTTT(){
        super("Ultimate TTT");
        setSize(GAME_WIDTH,GAME_HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());
        getContentPane().setBackground(Color.black);

        jTextFieldMessages = new JTextField();
        jTextFieldMessages.setEditable(false);
        jTextFieldMessages.setHorizontalAlignment(JTextField.CENTER);
        jTextFieldMessages.setFont(new Font(GAME_FONT,Font.BOLD,20));
        jTextFieldMessages.setPreferredSize(new Dimension(MESSAGES_WIDTH,MESSAGES_HEIGHT));
        jTextFieldMessages.setText("X turn");
        getContentPane().add(jTextFieldMessages);

        for(int r = 0; r <3; r++)
            for(int c = 0; c < 3; c++) {
                InnerGame temp = new InnerGame();
                getContentPane().add(temp);
                innerGames[r][c] = temp;
            }
        setVisible(true);
    }

    private class PlayButton extends JButton{

        private int row;
        private int col;

        private PlayButton(int r, int c){
            row = r;
            col = c;
            setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
            setFocusPainted(false);
            setContentAreaFilled(false);
        }
        public int getRow() {
            return row;
        }
        public int getCol() {
            return col;
        }
    }

    private class InnerGame extends JPanel{

        private Game game = new Game();
        private PlayButton[][] buttons = new PlayButton[3][3];
        private int winner = 0;

        public InnerGame(){
            setPreferredSize(new Dimension(INNER_WIDTH,INNER_HEIGHT));
            for(int r = 0; r <3; r++)
                for(int c = 0; c < 3; c++) {
                    PlayButton thisPlayButton = new PlayButton(r,c);
                    thisPlayButton.setFont(new Font(GAME_FONT,Font.BOLD,BUTTON_MARK_SIZE));
                    thisPlayButton.addActionListener(
                            new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    thisPlayButton.setEnabled(false);
                                    String mark = "";
                                    switch (turn) {
                                        case 1:
                                            mark = "X";
                                            break;
                                        case -1:
                                            mark = "O";
                                            break;
                                    }
                                    thisPlayButton.setText(mark);
                                    int[] thisPlayButtonCoordinates = {thisPlayButton.getRow(),thisPlayButton.getCol()};
                                    game.setBoard(thisPlayButtonCoordinates, turn);
                                    playingLocation = thisPlayButtonCoordinates;
                                    if(innerGames[thisPlayButtonCoordinates[0]][thisPlayButtonCoordinates[1]].getWinner()!=0){
                                        playingLocation[0]=-1;
                                        playingLocation[1]=-1;
                                    }
                                    updateInnerGameWinners();
                                    updateUltimateGameWinners();
                                    setInnerGamesDisabled();
                                    turn*=-1;
                                    updateMessage();
                                    updateFinalWinner();
                                }
                            }
                    );
                    add(thisPlayButton);
                    buttons[r][c] = thisPlayButton;
                }
        }

        private void updateInnerGameWinners(){
            //changes InnerGame if theres a winner
            winner = game.checkWinner();
            if( winner!= 0){
                String mark = "X";
                if(winner == -1)
                    mark = "O";
                setLayout(new BorderLayout());
                for(int r = 0; r <3; r++) 
                    for (int c = 0; c < 3; c++) 
                        buttons[r][c].setVisible(false);
                
                JLabel jLabelWinner = new JLabel(mark);
                jLabelWinner.setHorizontalAlignment(JLabel.CENTER);
                jLabelWinner.setFont(new Font(GAME_FONT, Font.BOLD, WINNER_MARK_SIZE));
                add(jLabelWinner);
            }
        }

        public int getWinner() {
            return winner;
        }

        public PlayButton[][] getButtons() {
            return buttons;
        }

        public void setWinner(int winner) {
            this.winner = winner;
        }
    }

    private void updateUltimateGameWinners(){
        for(int r = 0; r <3; r++)
            for (int c = 0; c < 3; c++)
                if(innerGames[r][c].getWinner()!=0){
                    int finishedInnerGameCoordinates [] = {r,c};
                    ultimateGame.setBoard(finishedInnerGameCoordinates,innerGames[r][c].getWinner());
                }


    }
    private void setInnerGamesDisabled(){
        for(int innerGameRow = 0; innerGameRow <3; innerGameRow++)
            for (int innerGameCol = 0; innerGameCol < 3; innerGameCol++) {
                if(playingLocation[0]==-1&&playingLocation[1]==-1) {//If they can go anywhere
                    for (int r = 0; r < 3; r++)
                        for (int c = 0; c < 3; c++)
                            if (innerGames[innerGameRow][innerGameCol].getButtons()[r][c].getText() == "")
                                innerGames[innerGameRow][innerGameCol].getButtons()[r][c].setEnabled(true);
                }else if(innerGameRow==playingLocation[0]&&innerGameCol==playingLocation[1]){
                    for(int r = 0; r <3; r++)
                        for (int c = 0; c < 3; c++)
                            if(innerGames[innerGameRow][innerGameCol].getButtons()[r][c].getText()=="")
                                innerGames[innerGameRow][innerGameCol].getButtons()[r][c].setEnabled(true);
                }else{
                    for(int r = 0; r <3; r++)
                        for (int c = 0; c < 3; c++)
                            innerGames[innerGameRow][innerGameCol].getButtons()[r][c].setEnabled(false);
                }
            }
    }

    private void updateFinalWinner(){
        //Disables everything
        if(ultimateGame.checkWinner()!=0) {
            for (int r = 0; r < 3; r++)
                for (int c = 0; c < 3; c++)
                    for (int rr = 0; rr < 3; rr++)
                        for (int cc = 0; cc < 3; cc++)
                            innerGames[r][c].getButtons()[rr][cc].setEnabled(false);
        if(ultimateGame.checkWinner()==1)
            jTextFieldMessages.setText("X Wins!");
        else
            jTextFieldMessages.setText("O Wins!");
        }
    }
    private void updateMessage(){
        if(turn == -1)
            jTextFieldMessages.setText("O's turn");
        else
            jTextFieldMessages.setText("X's turn");
    }
}