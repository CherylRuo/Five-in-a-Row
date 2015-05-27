import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Game controller
 * @author CherylRuo
 */
public abstract class FIRGame {
	abstract public void StartGame();
	abstract public boolean PlaceChess(int x, int y);
	protected DataModel mData;
	
	/**
	 * Serialize a DataModel Object to File
	 * Learn from http://www.tutorialspoint.com/java/java_serialization.htm
	 * @param fileName
	 */
	public final void SaveToFile(String fileName)
	{
		try 
		{
			FileOutputStream fileOut = new FileOutputStream(fileName);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(mData);
			out.close();
			fileOut.close();
			System.out.println("Serialized data is saved in "+ fileName);
		}
		catch(IOException i)
	    {
	          i.printStackTrace();
	    }
	}
	
	/**
	 * Deserialize a DataModel Object from File
	 * Learn from http://www.tutorialspoint.com/java/java_serialization.htm
	 * @param fileName
	 * @return
	 */
	public final DataModel LoadFromFile(String fileName)
	{
	      try
	      {
	         FileInputStream fileIn = new FileInputStream(fileName);
	         ObjectInputStream in = new ObjectInputStream(fileIn);
	         mData = (DataModel) in.readObject();
	         in.close();
	         fileIn.close();
	         System.out.println("Deserialized data is loaded from "+ fileName);
	         return mData;
	      }catch(IOException i)
	      {
	         i.printStackTrace();
	         return null;
	      }catch(ClassNotFoundException c)
	      {
	         c.printStackTrace();
	         return null;
	      }
	}
	/**
	 * check how many chess are in a row
	 * @param xChange
	 * @param yChange
	 * @param color
	 * @return
	 */
	private final int checkCount(int xChange, int yChange, int color)
	{
		int count = 1;
		int tempX = xChange;
		int tempY = yChange;
		
		while(mData.getX()+xChange >= 0 && mData.getX()+xChange <= 18 && mData.getY()+yChange >= 0 && 
				mData.getY()+yChange <= 18 && color == mData.getAllChess()[mData.getX()+xChange][mData.getY()+yChange])
		{
			count++;
			if(xChange != 0)
			{
				xChange++;
			}
			if(yChange != 0)
			{
				if(yChange > 0)
				yChange++;
				else
					yChange--;
			}
		}
		
		xChange = tempX;
		yChange = tempY;
		
		while(mData.getX()-xChange >= 0 && mData.getX()-xChange <= 18 && mData.getY()-yChange >= 0 && 
				mData.getY()-yChange <= 18 && color == mData.getAllChess()[mData.getX()-xChange][mData.getY()-yChange])
		{
			count++;
			if(xChange != 0)
			{
				xChange++;
			}
			if(yChange != 0)
			{
				if(yChange > 0)
					yChange++;
					else
						yChange--;
			}
		}
		return count;
		
	}
	/**
	 * check whether there are five chess in a row (a situation to win) in 4 directions
	 * @return
	 */
	public final boolean CheckWin() {
		boolean flag = false;		
		int color = mData.getAllChess()[mData.getX()][mData.getY()];
		int count = 1; 

		count = this.checkCount(1, 0, color); // check horizontal first: row+1
		if(count >= 5)
		{
			flag = true;
		} else
		{
			count = this.checkCount(0, 1, color); // then vertical: column+1
			if(count >= 5)
			{
				flag = true;
			} else
			{
				count = this.checkCount(1, -1, color); // then left(\) diagonal: row+1, column-1
				if(count >= 5)
				{
					flag = true;
				} else
				{
					count = this.checkCount(1, 1, color); // finally right(/) diagonal: row+1, column+1
					if(count >= 5)
					{
						flag = true;
					}
				}
			}
		}
		return flag;	
	}
}
