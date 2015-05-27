import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author alche
 *
 */
public class DataModelTest {

	@Test
	public void testequalsfalse() {
		DataModel testModel1 = new DataModel();
		DataModel testModel2 = new DataModel();
		testModel1.setCanPlay(true);
		testModel2.setCanPlay(true);
		int[][] testChess1 = new int[19][19];
		testChess1[0][1]  = 2;
		testModel1.setAllChess(testChess1);
		int[][] testChess2 = new int[19][19];
		testChess2[2][4]  = 2;
		testModel2.setAllChess(testChess2);
		assertEquals(false,testModel1.equals(testModel2));
	}
	
	@Test
	public void testequalstrue() {
		DataModel testModel1 = new DataModel();
		DataModel testModel2 = new DataModel();
		testModel1.setCanPlay(true);
		testModel2.setCanPlay(true);
		int[][] testChess1 = new int[19][19];
		testChess1[0][1]  = 2;
		testModel1.setAllChess(testChess1);
		int[][] testChess2 = new int[19][19];
		testChess2[0][1]  = 2;
		testModel2.setAllChess(testChess2);
		assertEquals(true,testModel1.equals(testModel2));
	}

}
