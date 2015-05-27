import static org.junit.Assert.*;

import org.junit.Test;


public class FIRGameTest {

	@Test
	public void testSerialization() {
		DataModel testModel = new DataModel();
		FIRGame game = new PVPMode(testModel);
		game.SaveToFile("1.txt");
		DataModel testModel2 = new DataModel();
		FIRGame game2 = new PVCMode(testModel2);
		game2.LoadFromFile("1.txt");
		assertEquals(false, testModel2.isVsComputer());
	}

}
