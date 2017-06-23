import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class gui extends JFrame{

    //THINGS I WOULD LIKE TO ADD
    /*
    Make inner games that you can not play in grayed out and opaque
    lines using graphics to create ttt board in inner games
    Networking

     */

    private final int GAME_WIDTH = 621;
    private final int GAME_HEIGHT = 651;
    private final int INNER_WIDTH = 200;
    private final int INNER_HEIGHT = 200;
    private final int BUTTON_SIZE = 61;

    private int turn = 1; //1 is player 1. -1 is player two
    private int[] location = {-1,-1};//-1 means can play anywhere


    private InnerGame[][] uGame = new InnerGame[3][3];
    private Game parellelUGame = new Game();


    public gui(){
        setSize(GAME_WIDTH,GAME_HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());
        getContentPane().setBackground(Color.black);

        for(int r = 0; r <3; r++){
            for(int c = 0; c < 3; c++) {
                InnerGame temp = new InnerGame();
                getContentPane().add(temp);
                uGame[r][c] = temp;
            }
        }

        setVisible(true);
    }

    private class playButton extends JButton{

        private int row;
        private int col;

        public playButton(int r, int c){
            row = r;
            col = c;
            setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
            setFocusPainted(false);
            setContentAreaFilled(false);
        }

        public int getRow() {
            return row;
        }

        public void setRow(int row) {
            this.row = row;
        }

        public int getCol() {
            return col;
        }

        public void setCol(int col) {
            this.col = col;
        }
    }

    private class InnerGame extends JPanel{

        private Game game = new Game();
        private JButton[][] buttons = new JButton[3][3];
        private int winner = 0;

        public InnerGame(){
           setPreferredSize(new Dimension(INNER_WIDTH,INNER_HEIGHT));
            for(int r = 0; r <3; r++){
                for(int c = 0; c < 3; c++) {
                    playButton temp = new playButton(r,c);
                    temp.addActionListener(
                            new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    temp.setEnabled(false);
                                    String mark = "";
                                    switch (turn) {
                                        case 1:
                                            mark = "X";
                                            break;
                                        case -1:
                                            mark = "O";
                                            break;
                                    }
                                    temp.setText(mark);
                                    int[] rc = {temp.getRow(),temp.getCol()};
                                    game.setBoard(rc, turn);
                                    location = rc;
                                    if(uGame[rc[0]][rc[1]].getWinner()!=0){
                                        location[0]=-1;
                                        location[1]=-1;
                                    }
                                    update();
                                }
                            }
                    );
                    add(temp);
                    buttons[r][c] = temp;
                }

           }
        }

        private void update(){
            //TODO this code may or may not be reduntant
            //This method has two functions
            //1. update and check the state of the current innerGame
            //2. update and check the state of the uGame


            //Phase 1
           winner = game.checkWinner();
            if(winner != 0){
                String mark = "X";
                if(winner == -1){
                    mark = "O";
                }

                    setLayout(new BorderLayout());
                    for(int r = 0; r <3; r++) {
                        for (int c = 0; c < 3; c++) {
                            buttons[r][c].setVisible(false);
                        }
                    }
                    JLabel jLabelWinner = new JLabel(mark);
                    //TODO fix this fucking alignment
                    jLabelWinner.setFont(new Font("Arial", Font.BOLD, 230));
                    add(jLabelWinner);
            }

            //Phase 2
            updateUGame();
            turn*=-1;
            //TODO change turn and playing location

        }


        public Game getGame() {
            return game;
        }

        public int getWinner() {
            return winner;
        }

        public void setWinner(int winner) {
            this.winner = winner;
        }

        public JButton[][] getButtons() {
            return buttons;
        }
    }

    private void updateUGame(){
        for(int r = 0; r <3; r++) {
            for (int c = 0; c < 3; c++) {
                if(uGame[r][c].getWinner()!=0){
                    int rc [] = {r,c};
                    parellelUGame.setBoard(rc,uGame[r][c].getWinner());
                }
            }
        }

        //Below sets all buttons disabled except those in the playing location
        for(int r = 0; r <3; r++) {
            for (int c = 0; c < 3; c++) {
                if(location[0]==-1&&location[1]==-1){
                    for(int rr = 0; rr <3; rr++) {
                        for (int cc = 0; cc < 3; cc++) {
                            if(uGame[r][c].getButtons()[rr][cc].getText()=="")
                                uGame[r][c].getButtons()[rr][cc].setEnabled(true);
                        }
                    }
                }
                else if(r==location[0]&&c==location[1]){
                    for(int rr = 0; rr <3; rr++) {
                        for (int cc = 0; cc < 3; cc++) {
                            if(uGame[r][c].getButtons()[rr][cc].getText()=="")
                                uGame[r][c].getButtons()[rr][cc].setEnabled(true);
                        }
                    }
                }else{
                    for(int rr = 0; rr <3; rr++) {
                        for (int cc = 0; cc < 3; cc++) {
                            uGame[r][c].getButtons()[rr][cc].setEnabled(false);
                        }
                    }
                }
            }
        }

        if(parellelUGame.checkWinner()!=0){
            for(int r = 0; r <3; r++) {
                for (int c = 0; c < 3; c++) {
                    for(int rr = 0; rr <3; rr++) {
                        for (int cc = 0; cc < 3; cc++) {
                            uGame[r][c].getButtons()[rr][cc].setEnabled(false);
                        }
                    }
                }
            }

        }
    }

/*
    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }


    public int[] getPlayingLocation() {
        return location;
    }

    public void setPlayingLocation(int[] location) {
        this.location = location;
    }

    public void setuGame(InnerGame[][] uGame) {
        this.uGame = uGame;
    }

    public Game getParellelUGame() {
        return parellelUGame;
    }

    public void setParellelUGame(Game parellelUGame) {
        this.parellelUGame = parellelUGame;
    }

*/

}
