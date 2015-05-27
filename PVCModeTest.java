import static org.junit.Assert.*;

import org.junit.Test;


public class PVCModeTest {

	@Test
	public void testPlaceChess() {
		DataModel testData = new DataModel();
		PVCMode testGame = new PVCMode(testData);
		testGame.StartGame();
		//place first chess
		testGame.PlaceChess(100, 200);
		//place chess at same position
		assertEquals(false,testGame.PlaceChess(100, 200));
	}
	
	@Test
	public void testStartGame() {
		DataModel testData = new DataModel();
		PVCMode testGame = new PVCMode(testData);
		testGame.StartGame();
		//place first chess
		testGame.PlaceChess(100, 200);
		//place chess at same position
		assertEquals(true,testData.isBlack());
		assertEquals(true,testData.isCanPlay());
		assertEquals(true,testData.isVsComputer());
	}

}
