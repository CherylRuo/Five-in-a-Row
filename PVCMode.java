/**
 * construct the player vs computer mode
 * @author CherylRuo
 */
public class PVCMode extends FIRGame
{
	public PVCMode(DataModel pData)
	{
		super.mData = pData;
	}
	private boolean[][][] ptable = new boolean[19][19][1020];  
	private boolean[][][] ctable = new boolean[19][19][1020];  
	private int[][] cgrades = new int[19][19];  
    private int[][] pgrades = new int[19][19];  
    private int cgrade,pgrade;  
    private int[][] win = new int[2][1020]; 
    /**
     * 1. clear the chess board; 
     * 2. make the next player to black.
     */
	@Override
	public void StartGame() {
		for(int i = 0; i<19; i++)
		{
			for(int j = 0; j<19; j++)
			{
				mData.getAllChess()[i][j] = 0;
			}
		}
		mData.setCanPlay(true);
		mData.setBlack(true);
		mData.setVsComputer(true);
		InitialAIGrade();
	}
	/** 
	 * AI algorithm: 
	 * Formula to calculate total number of five in a row conditions = 4(n-2)(n-4) = 4*(19-2)*(19-4)=1024.
	 * Explanation:
	 * Horizontal + vertical: n(n-4)*2
	 * Left + right diagonal: (n-4)^2 *2
	 * combination 4 conditions, is the formula above
	 */
	private void InitialAIGrade()
	{
		int icount = 0;
		 for(int i=0;i<19;i++)  
             for(int j=0;j<15;j++){  
                 for(int k=0;k<5;k++){  
                     this.ptable[j+k][i][icount] = true;  
                     this.ctable[j+k][i][icount] = true;  
                 }  
                 icount++;  
             }  
         //vertical
		 for(int i=0;i<19;i++)  
             for(int j=0;j<15;j++){ 
                 for(int k=0;k<5;k++){  
                     this.ptable[i][j+k][icount] = true;  
                     this.ctable[i][j+k][icount] = true;  
                 }  
                 icount++;  
             }  
         //right Diagonal
		 for(int i=0;i<15;i++)  
             for(int j=0;j<15;j++){   
                 for(int k=0;k<5;k++){  
                     this.ptable[j+k][i+k][icount] = true;  
                     this.ctable[j+k][i+k][icount] = true;  
                 }  
                 icount++;  
             }  
         //left Diagonal
		 for(int i=0;i<15;i++)  
              for(int j=18;j>=4;j--){   
                 for(int k=0;k<5;k++){  
                     this.ptable[j-k][i+k][icount] = true;  
                     this.ctable[j-k][i+k][icount] = true;  
                 }  
                 icount++;  
             }  
         for(int i=0;i<=1;i++) 
             for(int j=0;j<1020;j++)  
                 this.win[i][j] = 0; 
	}

	@Override
	public boolean PlaceChess(int x, int y) {
		mData.setX(x/20);
		mData.setY((y-60)/20);

		if(mData.getAllChess()[mData.getX()][mData.getY()] == 0)
		{
			// check the color of chess you want to place now
			mData.getAllChess()[mData.getX()][mData.getY()] = 1;
			mData.setBlack(true);
			int m = mData.getX();
			int n = mData.getY();
			for(int i=0;i<1020;i++){ 
				// 5 is because there is 4 switch cases, we need to use a number higher than 4
                if(this.ptable[m][n][i] && this.win[0][i] <5)  
                    this.win[0][i]++; 
                if(this.ctable[m][n][i]){  
                    this.ctable[m][n][i] = false;  
                    this.win[1][i]=5;  
                }  
            }
			// check whether this chess is in a Five in a Row condition.
			boolean winFlag = CheckWin();
			if(winFlag == true)
			{
					mData.setCanPlay(false);
					return true;
			}
			ComTurn();
			winFlag = CheckWin();
			if(winFlag == true)
			{
					mData.setCanPlay(false);
			}
			return true;
		}
		else
		{
			return false;
		}
	}

	
	public void ComTurn(){
		int i = 0;
		int j = 0;
		int k = 0;
		int mBlack = 0;
		int nBlack = 0;
		int mWhite = 0;
		int nWhite = 0;
		int m = 0;
		int n = 0;
        for(i=0;i<19;i++)     
             for(j=0;j<19;j++){     
                 pgrades[i][j]=0;  
                 if(mData.getAllChess()[i][j] == 0)  
                     for(k=0;k<1020;k++)   
                         if(this.ptable[i][j][k])
                         {
                        	 /* give the condition of more chess in a row a higher value, so the computer can calculate 
                        	  * which way to place the next chess is the best
                        	  */
                             switch(this.win[0][k])
                             {     
                                 case 1: 
                                     this.pgrades[i][j]+=5;  // one chess in a row + 5
                                     break;  
                                 case 2: 
                                     this.pgrades[i][j]+=50;  // two chess in a row + 50
                                     break;  
                                 case 3: 
                                     this.pgrades[i][j]+=180;  // three chess in a row + 180
                                     break;  
                                 case 4: 
                                     this.pgrades[i][j]+=400;  // four chess in a row + 400
                                     break;  
                             }  
                         }  
                 this.cgrades[i][j]=0;
                 if(mData.getAllChess()[i][j] == 0) 
                     for(k=0;k<1020;k++) 
                         if(this.ctable[i][j][k])
                         {  
                             switch(this.win[1][k])
                             {    
                                 case 1: 
                                     this.cgrades[i][j]+=5;  // one chess in a row + 5
                                     break;  
                                 case 2: 
                                     // 52>50 because when c and p both have 2 in a row, c choose to place its own chess first
                                     this.cgrades[i][j]+=52;  // two chess in a row + 52
                                     break;  
                                 case 3: 
                                     // 100<180 because when c and p both have 3 in a row, c choose to block p's next step
                                     this.cgrades[i][j]+=100;  // three chess in a row + 100
                                     break;  
                                 case 4: 
                                     this.cgrades[i][j]+=400;  // four chess in a row + 400
                                     break;  
                             }  
                         }  
             }  
             for(i=0;i<19;i++)  
                 for(j=0;j<19;j++)  
                     if(mData.getAllChess()[i][j] ==0)
                     {  
                         if(this.cgrades[i][j]>=this.cgrade){  
                             this.cgrade = this.cgrades[i][j];     
                             mWhite = i;  
                             nWhite = j;  
                         }  
                         if(this.pgrades[i][j]>=this.pgrade){  
                             this.pgrade = this.pgrades[i][j];     
                             mBlack = i;  
                             nBlack = j;  
                         }  
                     }  
             if(this.cgrade>=this.pgrade)
             {   
                 m = mWhite;  
                 n = nWhite;  
             }
             else
             {  
                 m = mBlack;  
                 n = nBlack;  
         }  
         this.cgrade = 0;          
         this.pgrade = 0;  
         mData.getAllChess()[m][n] = 2; 
         mData.setX(m);
         mData.setY(n);
         for(i=0;i<1020;i++){  
             if(this.ctable[m][n][i] && this.win[1][i] != 5)  
                 this.win[1][i]++;      
             if(this.ptable[m][n][i]){  
                 this.ptable[m][n][i] = false;  
                 this.win[0][i]=5;  
             }  
         }  
     }   

}
