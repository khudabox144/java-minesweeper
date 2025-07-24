import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
public class Minesweeper {

  // another class for minetile
  private class MineTile extends JButton{
    int r; 
    int c;
    public MineTile(int r , int c){
      this.r=r;
      this.c=c;
    }
  }

  //initialize 
  int tileSize=70;
  int numrows=8;
  int numCols=numrows;
  int boardWidth=numCols*tileSize;
  int boardHeight=numCols*tileSize;




   JFrame frame=new JFrame("Minesweeper");


  //  label
  JLabel label =new JLabel();

  // panel
  JPanel panel =new JPanel();

  // board panel
  JPanel boardpanel =new JPanel();

  // declare array
  int mineCount=10;
  MineTile[][] board=new MineTile[numrows][numCols];
  ArrayList<MineTile> minelist ;//duh
  Random random =new Random();

    int tilseClicked=0;
    boolean gameOver=false;
   Minesweeper(){
    // frame.setVisible(true);
    frame.setSize(boardWidth,boardHeight);
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setResizable(false);
    frame.setLayout(new BorderLayout());


    // adjust label
    label.setFont(new Font("Arial",Font.BOLD,25));
    label.setHorizontalAlignment(JLabel.CENTER);
    label.setText("Minesweeper: "+Integer.toString(mineCount));
    label.setOpaque(true);

    // adjust 
    panel.setLayout(new BorderLayout());
    panel.add(label);
    frame.add(panel,BorderLayout.NORTH);


    // adjust board panel
    boardpanel.setLayout(new GridLayout(numrows,numCols));
    //boardpanel.setBackground(Color.green);
    frame.add(boardpanel);

    for (int i = 0; i < numrows; i++) {
      for (int j = 0; j < numCols; j++) {
        MineTile tile =new MineTile(i, j);
        board[i][j]=tile;


        tile.setFocusable(false);
        tile.setMargin(new Insets(0, 0, 0, 0));
        tile.setFont(new Font("Arial Unicode MS",Font.PLAIN,45));
        //tile.setText("💣");
        tile.addMouseListener(new MouseAdapter() {
          @Override
          public void mousePressed(MouseEvent e){

            if(gameOver){
              return;
            }

            MineTile tile =(MineTile) e.getSource();

            // left click
            if(e.getButton()==MouseEvent.BUTTON1){
              if(tile.getText()==""){
                if(minelist.contains(tile)){
                  revealMines();
                }
                else {
                  checkMine(tile.r,tile.c);
                }
              }
            }
 
            else if(e.getButton()==MouseEvent.BUTTON3){
              if(tile.getText()=="" && tile.isEnabled() ){
                tile.setText("🚩");
              }
              else if(tile.getText()=="🚩"){
                tile.setText("");
              }
            }
          }
        });
        boardpanel.add(tile);

      }
    }

    frame.setVisible(true);
    setMines();

   }
    void setMines(){
      minelist=new ArrayList<MineTile>();

      // minelist.add(board[2][2]);
      // minelist.add(board[2][3]);
      // minelist.add(board[5][6]);
      // minelist.add(board[3][4]);
      // minelist.add(board[1][1]);

      int mineleft=mineCount;
      while (mineleft>0) {
        int r=random.nextInt(numrows);
        int c=random.nextInt(numCols);  

        MineTile tile = board[r][c];
        if(!minelist.contains(tile)){
          minelist.add(tile);
          mineleft-=1;
        }

      }
  
    }

    void revealMines(){
      for (int i = 0; i < minelist.size(); i++) {
        MineTile tile = minelist.get(i);
        tile.setText("💣");
      }
      gameOver=true;
      label.setText("Game Over");
    }

    void checkMine(int r , int c){

      if(r<0 || r>=numrows || c<0 || c>=numCols){
        return ;
      } 

      MineTile tile = board[r][c];
      if(!tile.isEnabled()){
        return;
      }
      tile.setEnabled(false);
      tilseClicked+=1;
      int minesFound=0;

      // top 3
      minesFound += countmine(r-1,c-1);//topleft
      minesFound += countmine(r-1,c);//top
      minesFound += countmine(r-1,c+1);//top right

      // left and right
      minesFound += countmine(r,c-1);//left
      minesFound += countmine(r,c+1);//right
      
      // bottom 3

      minesFound += countmine(r+1,c-1);//bottom left
      minesFound += countmine(r-1,c);//bottom
      minesFound += countmine(r-1,c+1);//bottom right

      if(minesFound> 0){
        tile.setText(Integer.toString(minesFound));
      }
      else{
        tile.setText("");
        // top 3
        checkMine(r-1,c-1);
        checkMine(r-1,c);
        checkMine(r-1,c+1);

        // left and right
        checkMine(r,c-1);//left
        checkMine(r,c+1);//right

        //bottom
        checkMine(r+1,c-1);//bottom left
        checkMine(r-1,c);//bottom
        checkMine(r-1,c+1);//bottom right

      }

      if(tilseClicked==numCols*numrows-minelist.size()){
        gameOver=true;
        label.setText("Mines Cleared");
      }
    }
    int countmine(int r , int c){
      if(r<0 || r>=numrows || c<0 || c>=numCols){
        return 0;
      }
      if(minelist.contains(board[r][c])){
        return 1 ;
      }
      return 0;
    }
}
