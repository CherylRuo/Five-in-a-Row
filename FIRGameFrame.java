import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * construct the game frame
 * @author CherylRuo
 *
 */
public class FIRGameFrame extends JFrame implements MouseListener,Runnable {

	int width = Toolkit.getDefaultToolkit().getScreenSize().width;
	int height = Toolkit.getDefaultToolkit().getScreenSize().height;
	private DataModel mData;
	BufferedImage bimg = null;
	private FIRGame mGame = null;
	private final String blackDisplay = "Black's turn";
	private final String whiteDisplay = "White's turn";
	// Thread type to count down
	Thread t = new Thread(this);
	/**
	 * 1.double buffering technology
	 * 2.draw chess frame
	 * 3.construct messages
	 * 4.construct countdown timer
	 * @param pData
	 * @param pGame
	 */
	public FIRGameFrame(DataModel pData, FIRGame pGame )
	{
		mData = pData;
		mGame = pGame;
		this.setTitle("Five in a Row");
		this.setSize(500, 500);
		this.setLocation((width-500)/2, (height-500)/2);
		// Set the frame cannot be resized.
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		addMouseListener(this);
		
		t.start();
		t.suspend();
		
		// refresh screen to prevent black screen when start game
		this.repaint();
		
		try 
		{
			bimg = ImageIO.read(new File("src/FIRChaseFrame.jpg"));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void paint(Graphics g)
	{
		// double buffering technology to deal with the screen flickering
		BufferedImage bi = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);
		Graphics g2 = bi.createGraphics();
		
		g2.drawImage(bimg, 0, 20, this);
		g2.setFont(new Font("Times New Roman", Font.BOLD, 18));
		g2.drawString("Game Information: " + getDisplayMessage(mData.isBlack()), 11, 55);
		g2.setFont(new Font("Times New Roman", 0, 20));
		g2.setColor(Color.red);
		g2.drawString("Black's time: " + getTimeMessage(mData.getBlackTime()), 30, 470);
		g2.drawString("White's time: " + getTimeMessage(mData.getWhiteTime()), 250, 470);
		g2.setColor(Color.white);
		// draw lines
		for(int i=0; i<19; i++)
		{
			g2.drawLine(10, 70+20*i, 370, 70+20*i);
			g2.drawLine(10+20*i, 70, 10+20*i, 430);
		}
		
		// draw 9 points on chess board 
		g2.fillOval(68, 128, 4, 4);
		g2.fillOval(308, 128, 4, 4);
		g2.fillOval(308, 368, 4, 4);
		g2.fillOval(68, 368, 4, 4);
		g2.fillOval(68, 248, 4, 4);
		g2.fillOval(188, 128, 4, 4);
		g2.fillOval(68, 248, 4, 4);
		g2.fillOval(188, 368, 4, 4);
		g2.fillOval(188, 248, 4, 4);

		// draw all chess pieces
		for(int i=0; i<19; i++)
		{
			for(int j=0; j<19; j++)
			{
				if(mData.getAllChess()[i][j] == 1)
				{
					// black chess
					int tempX = i*20+10;
					int tempY = j*20+70;
					g2.setColor(Color.BLACK);
					g2.drawOval(tempX-7, tempY-7, 14, 14);
					g2.fillOval(tempX-7, tempY-7, 14, 14);
				}
				if(mData.getAllChess()[i][j] == 2)
				{
					// white chess
					int tempX = i*20+10;
					int tempY = j*20+70;
					g2.setColor(Color.WHITE);
					g2.drawOval(tempX-7, tempY-7, 14, 14);
					g2.fillOval(tempX-7, tempY-7, 14, 14);
				}
			}
		}
		g.drawImage(bi, 0, 0, this);
	}
	/**
	 * 
	 */
	public void mousePressed(MouseEvent e) 
	{
		if(mData.isCanPlay() == true)
		{
			int posX = e.getX();
			int posY = e.getY();
			if(posX>=10 && posX<=370 && posY>=70 &&posY<=430)
			{
				if(mGame.PlaceChess(posX, posY))
				{
					if(mData.isCanPlay()==false)
						JOptionPane.showMessageDialog(this, "Congratulations! " + 
					(mData.getAllChess()[mData.getX()][mData.getY()] == 1 ? "Black" : "White") + " Win!");	
				}
				else
					JOptionPane.showMessageDialog(this, "This place is unavailable, please choose another one.");
				repaint();
			}
		}

		
		// click Start Game button
		if(e.getX() >= 386 && e.getX() <= 490 && e.getY() >= 75 && e.getY() <= 105)
		{
			int result = JOptionPane.showConfirmDialog(this, "Do you really want to start a new game?");
			if(result == 0) // now restart the game
			{
				Method method = null;
				try {
					method = FIRGame.class.getMethod("StartGame");
				} catch (NoSuchMethodException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SecurityException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				try {
					method.invoke(mGame);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if(mData.getBlackTime()>0)
					t.resume();
				repaint();
			}
		}
		
		
		// click Setting button
		if(e.getX() >= 386 && e.getX() <= 490 && e.getY() >= 125 && e.getY() <= 155)
		{
			String input = JOptionPane.showInputDialog("Please input the maximum time(minutes, 0 means no time limit) for each turn: ");
			try{
				mData.setBlackTime(Integer.parseInt(input)*60);
				mData.setWhiteTime(Integer.parseInt(input)*60);
				if(mData.getBlackTime() < 0)
				{
					JOptionPane.showMessageDialog(this, "Please don't input negative number.");
				}
				if(mData.getBlackTime() == 0)
				{
					int result = JOptionPane.showConfirmDialog(this, "Setting is completed. Do you want to start a new game?");
					if(result == 0)
					{
						mGame.StartGame();
						this.repaint();
					}
				}
				if(mData.getBlackTime() > 0)
				{
					int result = JOptionPane.showConfirmDialog(this, "Setting is completed. Do you want to start a new game?");
					if(result == 0)
					{
						mGame.StartGame();
						t.resume();
						this.repaint();
					}			
				}
			
			} catch(NumberFormatException e1)
			{
				JOptionPane.showMessageDialog(this, "Please input correct information.");
			}
		}
		
		
		// click Instruction button
		if(e.getX() >= 386 && e.getX() <= 490 && e.getY() >= 175 && e.getY() <= 205)
		{
			JOptionPane.showMessageDialog(this, "Five in a Row is traditionally played with "
					+ "Go pieces \n(black and white stones) on a go board with \n19x19 intersections."
					+ " When one player has \nfive chess in a row, this player wins the game.");
		}
		// click game mode pattern. 
		if(e.getX() >= 386 && e.getX() <= 490 && e.getY() >= 225 && e.getY() <= 255)
		{
			int result = JOptionPane.showConfirmDialog(this, "Select Yes to play with Computer.  Select no to play with Player");
			if(result == 0)
			{
				mData.setVsComputer(true);
				mGame = GameFactory.getGame("PVC", mData);
				mGame.StartGame();
				this.repaint();
			}
			else if(result ==1)
			{
				mData.setVsComputer(false);
				mGame = GameFactory.getGame("PVP", mData);
				mGame.StartGame();
				this.repaint();
			}
		}
		// click Admit Defeat button
		if(e.getX() >= 386 && e.getX() <= 490 && e.getY() >= 275 && e.getY() <= 305)
		{
			int result = JOptionPane.showConfirmDialog(this, "Do you really want to admit defeat?");
			if(result == 0)
			{
				if(mData.isBlack())
					JOptionPane.showMessageDialog(this, "Black admits defeat. Game over.");
				else
					JOptionPane.showMessageDialog(this, "White admits defeat. Game over.");
				mData.setCanPlay(false);
			}
		}
		// click About button
		if(e.getX() >= 386 && e.getX() <= 490 && e.getY() >= 325 && e.getY() <= 355)
		{
			JOptionPane.showMessageDialog(this, "This game is made by Ruo Jia, Michael Yu, and Ye Yuan.");
		}
		// click Quit Game button
		if(e.getX() >= 386 && e.getX() <= 490 && e.getY() >= 375 && e.getY() <= 405)
		{
			JOptionPane.showMessageDialog(this, "Quit game");
			System.exit(0);
		}
		//https://docs.oracle.com/javase/tutorial/uiswing/components/filechooser.html
		//Click Load button
		if(e.getX() >= 386 && e.getX() <= 433 && e.getY() >= 410 && e.getY() <= 435)
		{
			final JFileChooser fc = new JFileChooser();
			int returnVal = fc.showOpenDialog(this);
		    if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = fc.getSelectedFile();
		            //This is where a real application would open the file.
		            DataModel newData = mGame.LoadFromFile(file.getAbsolutePath());
		            if(mData==null)
		            {
		            	JOptionPane.showMessageDialog(this, "Can not load game from selected. please load again or start a new game");
		            }
		            else if(mData.equals(newData))
		            {
		            	JOptionPane.showMessageDialog(this, "Loaded game is same as current one, please load again or continue");
		            }
		            else
		            {
		            	JOptionPane.showMessageDialog(this, "Successful loaded game");
		            	mData = newData;
		            	this.repaint();
		            }
		    }
		}
		//https://docs.oracle.com/javase/tutorial/uiswing/components/filechooser.html
		//Click Save button
		if(e.getX() >= 443 && e.getX() <= 490 && e.getY() >= 410 && e.getY() <= 435)
		{
			final JFileChooser fc = new JFileChooser();
			int returnVal = fc.showSaveDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                mGame.SaveToFile(file.getAbsolutePath());
			}
			this.repaint();
		}
	}
	private String getTimeMessage(int time)
	{
		if(time > 0)
		{
			// show time(seconds) in hour: minute: second format
			String result = (time/3600 + ":" + (time/60 - time/3600*60) + ":" + (time-time/60*60));
			return result;
		} 
		// mData.maxTime <= 0 means there is no time limit
		else
		{
			return "no limit";
		}
	}
	private String getDisplayMessage(boolean isBlack)
	{
		if(isBlack)
			return blackDisplay;
		else
			return whiteDisplay;
	}
	@Override
	public void run() 
	{
		// judge whether there is time limit
		if(mData.getBlackTime() > 0||mData.getWhiteTime()>0)
		{
			while(true)
			{
				if(mData.isCanPlay()==false)
					break;
				if(mData.isBlack())
				{
					mData.setBlackTime(mData.getBlackTime() - 1);
					if(mData.getBlackTime() == 0)
					{
						JOptionPane.showMessageDialog(this, "Black is overtime. Game over.");
						mData.setCanPlay(false);
						mData.setBlackTime(0);
						mData.setWhiteTime(0);
					}
				} else
				{
					mData.setWhiteTime(mData.getWhiteTime() - 1);
					if(mData.getWhiteTime() == 0)
					{
						JOptionPane.showMessageDialog(this, "White is overtime. Game over.");
						mData.setCanPlay(false);
						mData.setBlackTime(0);
						mData.setWhiteTime(0);
					}
				}
				this.repaint();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
