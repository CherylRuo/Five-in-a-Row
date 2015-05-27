
/**
 * Factory pattern
 * choose between "player vs player mode" or "player vs computer mode"
 * @author CherylRuo
 */
public class GameFactory {
	public static FIRGame getGame(String gameType, DataModel data)
	{
		if(gameType == null)
	    {
	       return null;
	    }		
	    if(gameType.equalsIgnoreCase("PVP"))
	    {
	       return new PVPMode(data);
	    } 
	    else if(gameType.equalsIgnoreCase("PVC"))
	    {
	       return new PVCMode(data);
	    }
	    return null;
	}
}
