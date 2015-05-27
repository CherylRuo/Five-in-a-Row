import static org.junit.Assert.*;

import org.junit.Test;


public class GameFactoryTest {

	@Test
	public void testPVPClass() {
		DataModel mData = new DataModel();
		FIRGame mGame = GameFactory.getGame("PVP", mData);
		assertTrue(mGame instanceof PVPMode);
	}
	@Test
	public void testPVClass() {
		DataModel mData = new DataModel();
		FIRGame mGame = GameFactory.getGame("PVC", mData);
		assertTrue(mGame instanceof PVCMode
				);
	}
}
