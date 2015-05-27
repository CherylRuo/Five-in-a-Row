import java.io.*;
import java.util.Arrays;
//DATA container
public class DataModel implements java.io.Serializable{
	private int x = 0;
	private int y = 0;
	
	// save all points that have been placed with chess
	// 0: this point has no chess; 1: this point has black chess; 2: this point has white chess.
	private int[][] allChess = new int[19][19];
	// judge the color of chess to play
	private boolean isBlack = true;
	// judge whether the game is able to continue
	private boolean canPlay = true;
	// save the left time of black and white
	private int blackTime = 0;
	private int whiteTime = 0;
	// save information of players' left time
	private boolean vsComputer = false;
	//Overriding equals() to compare two DataModel objects
    @Override
    public boolean equals(Object o) {
 
        // If the object is compared with itself then return true  
        if (o == this)
            return true;
 
        /* Check if o is an instance of DataModel or not
          "null instanceof [type]" also returns false */
        if (!(o instanceof DataModel)) {
            return false;
        }
         
        // typecast o to datamodel so that we can compare data members 
        DataModel c = (DataModel) o;
        boolean result = true;
        if(this.isBlack != c.isBlack()||this.canPlay !=c.isCanPlay()||
           this.blackTime !=c.getBlackTime()||this.whiteTime!=c.getWhiteTime()||
           this.vsComputer!=c.isVsComputer())
        	result = false;
        if(!Arrays.deepEquals(this.allChess,c.getAllChess()))
        	result = false;
        // Compare the data members and return accordingly 
        return result;
    }
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int[][] getAllChess() {
		return allChess;
	}
	public void setAllChess(int[][] allChess) {
		this.allChess = allChess;
	}
	public boolean isBlack() {
		return isBlack;
	}
	public void setBlack(boolean isBlack) {
		this.isBlack = isBlack;
	}
	public boolean isCanPlay() {
		return canPlay;
	}
	public void setCanPlay(boolean canPlay) {
		this.canPlay = canPlay;
	}
	public synchronized int getBlackTime() {
		return blackTime;
	}
	public synchronized void setBlackTime(int blackTime) {
		this.blackTime = blackTime;
	}
	public synchronized int getWhiteTime() {
		return whiteTime;
	}
	public synchronized void setWhiteTime(int whiteTime) {
		this.whiteTime = whiteTime;
	}
	public boolean isVsComputer() {
		return vsComputer;
	}
	public void setVsComputer(boolean vsComputer) {
		this.vsComputer = vsComputer;
	}
}
