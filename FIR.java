
/**
 * Facade pattern
 * initialize the game with player with player pattern by default
 */
public class FIR {
	public void Start()
	{
		DataModel mData = new DataModel();
		FIRGame mGame = GameFactory.getGame("PVP", mData);
		FIRGameFrame newFrame = new FIRGameFrame(mData,mGame);
	}
}
