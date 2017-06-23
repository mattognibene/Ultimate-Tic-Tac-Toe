
public class Game {

    public Game(){

    }

    private int board[][] = new int[3][3];

    public boolean isOpen(int[] rc){
        return(board[rc[0]][rc[1]]==0);
    }

    public void setBoard(int[] rc, int player){
        if(isOpen(rc)){
            board[rc[0]][rc[1]] = player;
        }
    }

    public int checkWinner(){
        for(int r=0;r<3;r++) {
            int sum =0;
            for (int c = 0; c < 3; c++)
                sum+=board[r][c];
            if(sum==3)
                return 1;
            else if(sum ==-3)
                return -1;
        }

        for(int c=0;c<3;c++) {
            int sum =0;
            for (int r = 0; r < 3; r++)
                sum+=board[r][c];
            if(sum==3)
                return 1;
            else if(sum ==-3)
                return -1;
        }

        int sum=0;
        for(int r=0, c=0;r<3;r++,c++)
            sum+=board[r][c];
        if(sum==3)
            return 1;
        else if(sum ==-3)
            return -1;

        sum=0;
        for(int r=2, c=0;r>=0;r--,c++)
            sum+=board[r][c];
        if(sum==3)
            return 1;
        else if(sum ==-3)
            return -1;

        return 0;
    }

    public void printBoard(){

        for(int r=0;r<3;r++){
            System.out.println();
            for(int c=0;c<3;c++){
                char out = '.';
                if(board[r][c]==-1)
                    out='o';
                else if(board[r][c]==1)
                    out='x';
                System.out.print(out + "\t");
            }
        }

    }

    //HERE BE LEGACY CODE (AND DRAGONS)

    private int[] rowCol(int location){
        //Returns the location in row,col form instead of 1,2,3,4...
        int r;
        int c;
        if(location%3==0){
            c=2;
        }else if(location%3==2){
            c=1;
        }else{
            c=0;
        }

        if(location<=3){
            r=0;
        }else if(location<=6){
            r=1;
        }else{
            r=2;
        }
        int[] rc = {r,c};
        return rc;
    }

    public boolean isOpen(int location){
        int[] rc = rowCol(location);
        return(board[rc[0]][rc[1]]==0);
    }

    public void setBoard(int location, int player){
        if(isOpen(location)){
            int[] rc = rowCol(location);
            board[rc[0]][rc[1]] = player;
        }
    }

}
