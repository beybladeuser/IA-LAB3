import static org.junit.jupiter.api.Assertions.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.Test;

class BlocksWorldUnitTests {

	@Test
	void testConstructor0() {
		Table b = new Table("AC B");
		StringWriter writer = new StringWriter();
		PrintWriter pw = new PrintWriter(writer);
		pw.println("[A, C]");
		pw.println("[B]");
		assertEquals(writer.toString(), b.toString());
		pw.close();
	}
	
	@Test
	void testConstructor1() {
		Table b = new Table("ABC");
		StringWriter writer = new StringWriter();
		PrintWriter pw = new PrintWriter(writer);
		pw.println("[A, B, C]");
		assertEquals(writer.toString(), b.toString());
		pw.close();
	}
	
	@Test
	void testConstructor2() {
		Table b = new Table("A B C");
		StringWriter writer = new StringWriter();
		PrintWriter pw = new PrintWriter(writer);
		pw.println("[A]");
		pw.println("[B]");
		pw.println("[C]");
		assertEquals(writer.toString(), b.toString());
		pw.close();
	}
	
	@Test
	void testEquals0() {
		Table b = new Table("A B C");
		Table c = new Table("A C B");
		assertEquals(true, b.equals(c));
	}
	
	@Test
	void testEquals1() {
		Table b = new Table("A B C");
		Table c = new Table("C B A");
		assertEquals(true, b.equals(c));
	}
	
	@Test
	void testEquals2() {
		Table b = new Table("C B A");
		Table c = new Table("C A B");
		assertEquals(true, b.equals(c));
	}
	
	@Test
	void testEquals3() {
		Table b = new Table("CB A");
		Table c = new Table("CB A");
		assertEquals(true, b.equals(c));
	}
	
	@Test
	void testEquals4() {
		Table b = new Table("CB A");
		Table c = new Table("A CB");
		assertEquals(true, b.equals(c));
	}
	
	@Test
	void testEquals5() {
		Table b = new Table("CB A");
		Table c = new Table("A BC");
		assertEquals(false, b.equals(c));
	}
	
	@Test
	void testEquals6() {
		Table b = new Table("BAD FEC");
		Table c = new Table("BAD FEC");
		assertEquals(true, b.equals(c));
	}
	
	@Test
	void testEquals7() {
		Table b = new Table("BAD FEC");
		Table c = new Table("FEC BAD");
		assertEquals(true, b.equals(c));
	}
	
	@Test
	void testEquals8() {
		Table b = new Table("A B C D E F G");
		Table c = new Table("G F E D C B A");
		assertEquals(true, b.equals(c));
	}
	
	@Test
	void testChildren0() {
		Table testTable = new Table("BAD FEC");

		String[] expectedTables = new String[4];
		
		expectedTables[0] = (new Table("BA FECD")).toString();
		expectedTables[1] = (new Table("BA FEC D")).toString();
		expectedTables[2] = (new Table("BADC FE")).toString();
		expectedTables[3] = (new Table("BAD FE C")).toString();

		List<Ilayout> actualChildren = testTable.children();
		
		for (int i = 0; i < expectedTables.length; i++) {
			assertEquals(expectedTables[i], actualChildren.get(i).toString());
		}
	}
	
	@Test
	void testChildren1() {
		Table testTable = new Table("A B C D E F G");

		String[] expectedTables = new String[6*7];
		int offSet = 0;
		expectedTables[offSet++] = (new Table("BA C D E F G")).toString();
		expectedTables[offSet++] = (new Table("B CA D E F G")).toString();
		expectedTables[offSet++] = (new Table("B C DA E F G")).toString();
		expectedTables[offSet++] = (new Table("B C D EA F G")).toString();
		expectedTables[offSet++] = (new Table("B C D E FA G")).toString();
		expectedTables[offSet++] = (new Table("B C D E F GA")).toString();
		
		expectedTables[offSet++] = (new Table("AB C D E F G")).toString();
		expectedTables[offSet++] = (new Table("A CB D E F G")).toString();
		expectedTables[offSet++] = (new Table("A C DB E F G")).toString();
		expectedTables[offSet++] = (new Table("A C D EB F G")).toString();
		expectedTables[offSet++] = (new Table("A C D E FB G")).toString();
		expectedTables[offSet++] = (new Table("A C D E F GB")).toString();
		
		expectedTables[offSet++] = (new Table("AC B D E F G")).toString();
		expectedTables[offSet++] = (new Table("A BC D E F G")).toString();
		expectedTables[offSet++] = (new Table("A B DC E F G")).toString();
		expectedTables[offSet++] = (new Table("A B D EC F G")).toString();
		expectedTables[offSet++] = (new Table("A B D E FC G")).toString();
		expectedTables[offSet++] = (new Table("A B D E F GC")).toString();

		expectedTables[offSet++] = (new Table("AD B C E F G")).toString();
		expectedTables[offSet++] = (new Table("A BD C E F G")).toString();
		expectedTables[offSet++] = (new Table("A B CD E F G")).toString();
		expectedTables[offSet++] = (new Table("A B C ED F G")).toString();
		expectedTables[offSet++] = (new Table("A B C E FD G")).toString();
		expectedTables[offSet++] = (new Table("A B C E F GD")).toString();
		
		List<Ilayout> actualChildren = testTable.children();
		
		for (int i = 0; i < offSet; i++) {
			assertEquals(expectedTables[i], actualChildren.get(i).toString());
		}
	}
	
	@Test
	void testGetH0()
	{
		Table start = new Table("FACE BDX");
		Table objective = new Table("FBCDEAX");
		
		double expectedH = 6;
		
		assertEquals(expectedH, start.getH(objective));
	}
	
	@Test
	void testSolve0() {
		Table start = new Table("AC B");
		Table objective = new Table("A C B");
		
		int expectedCost = 1;
		String[] expectedTables = new String[expectedCost + 1];
		
		expectedTables[0] = start.toString();
		expectedTables[expectedTables.length-1] = (new Table("A B C")).toString();
		
		IDAStar s = new IDAStar();
		Iterator<IDAStar.State> it = s.solve
		(
			start,
			objective
		);
		assertNotNull(it);
		int j = 0;
		while(it.hasNext())
		{
			IDAStar.State i = it.next();
			assertEquals(expectedTables[j++] ,i.toString());
			if (!it.hasNext())
				assertEquals(expectedCost, i.getG());
		}
	}
	
	@Test
	void testSolve1() {
		Table start = new Table("ABC");
		Table objective = new Table("A B C");
		
		int expectedCost = 2;
		String[] expectedTables = new String[expectedCost + 1];
		
		expectedTables[0] = start.toString();
		expectedTables[1] = (new Table("AB C")).toString();
		expectedTables[expectedTables.length-1] = (new Table("A C B")).toString();
		
		IDAStar s = new IDAStar();
		Iterator<IDAStar.State> it = s.solve
		(
			start,
			objective
		);
		assertNotNull(it);
		int j = 0;
		while(it.hasNext())
		{
			IDAStar.State i = it.next();
			assertEquals(expectedTables[j++] ,i.toString());
			if (!it.hasNext())
				assertEquals(expectedCost, i.getG());
		}
	}
	
	@Test
	void testSolve2() {
		Table start = new Table("CAB");
		Table objective = new Table("ABC");
		
		int expectedCost = 4;
		String[] expectedTables = new String[expectedCost + 1];
		
		expectedTables[0] = start.toString();
		expectedTables[1] = (new Table("CA B")).toString();
		expectedTables[2] = (new Table("C B A")).toString();
		expectedTables[3] = (new Table("C AB")).toString();
		expectedTables[expectedTables.length-1] = objective.toString();
		
		IDAStar s = new IDAStar();
		Iterator<IDAStar.State> it = s.solve
		(
			start,
			objective
		);
		assertNotNull(it);
		int j = 0;
		while(it.hasNext())
		{
			IDAStar.State i = it.next();
			assertEquals(expectedTables[j++] ,i.toString());
			if (!it.hasNext())
				assertEquals(expectedCost, i.getG());
		}
	}
	
	@Test
	void testSolve3() {
		Table start = new Table("BAD FEC");
		Table objective = new Table("ABCDEF");
		
		int expectedCost = 7;
		
		IDAStar s = new IDAStar();
		Iterator<IDAStar.State> it = s.solve
		(
			start,
			objective
		);
		assertNotNull(it);
		IDAStar.State LastState = it.next();
		while(it.hasNext())
		{
			LastState = it.next();
		}
		assertEquals(expectedCost, LastState.getG());
	}
	
	@Test
	void testSolve4() {
		Table start = new Table("GF EDC B A");
		Table objective = new Table("ABCDEFG");
		
		int expectedCost = 6;
		
		IDAStar s = new IDAStar();
		Iterator<IDAStar.State> it = s.solve
		(
			start,
			objective
		);
		assertNotNull(it);
		IDAStar.State LastState = it.next();
		while(it.hasNext())
		{
			LastState = it.next();
		}
		assertEquals(expectedCost, LastState.getG());
	}
	
	@Test
	void testSolve5() {
		Table start = new Table("DBCAEF");
		Table objective = new Table("ABCDEF");
		
		int expectedCost = 9;
		
		IDAStar s = new IDAStar();
		Iterator<IDAStar.State> it = s.solve
		(
			start,
			objective
		);
		assertNotNull(it);
		IDAStar.State LastState = it.next();
		while(it.hasNext())
		{
			LastState = it.next();
		}
		assertEquals(expectedCost, LastState.getG());
	}
	
	@Test
	void testSolve6() {
		Table start = new Table("FEDCBA");
		Table objective = new Table("ABCDEF");
		
		int expectedCost = 6;
		
		IDAStar s = new IDAStar();
		Iterator<IDAStar.State> it = s.solve
		(
			start,
			objective
		);
		assertNotNull(it);
		IDAStar.State LastState = it.next();
		while(it.hasNext())
		{
			LastState = it.next();
		}
		assertEquals(expectedCost, LastState.getG());
	}
	
	@Test
	void testSolve7() {
		Table start = new Table("CBA DE F");
		Table objective = new Table("ABCDEF");
		
		int expectedCost = 7;
		
		IDAStar s = new IDAStar();
		Iterator<IDAStar.State> it = s.solve
		(
			start,
			objective
		);
		assertNotNull(it);
		IDAStar.State LastState = it.next();
		while(it.hasNext())
		{
			LastState = it.next();
		}
		assertEquals(expectedCost, LastState.getG());
	}
	
	@Test
	void testSolve8() {
		Table start = new Table("FACE BD");
		Table objective = new Table("ABCDEF");
		
		int expectedCost = 9;
		
		IDAStar s = new IDAStar();
		Iterator<IDAStar.State> it = s.solve
		(
			start,
			objective
		);
		assertNotNull(it);
		IDAStar.State LastState = it.next();
		while(it.hasNext())
		{
			LastState = it.next();
		}
		assertEquals(expectedCost, LastState.getG());
	}
	
	@Test
	void testSolve9() {
		Table start = new Table("FACE BD");
		Table objective = new Table("FBCDEA");
		
		int expectedCost = 9;
		
		IDAStar s = new IDAStar();
		Iterator<IDAStar.State> it = s.solve
		(
			start,
			objective
		);
		assertNotNull(it);
		IDAStar.State LastState = it.next();
		while(it.hasNext())
		{
			LastState = it.next();
		}
		assertEquals(expectedCost, LastState.getG());
	}
	
	@Test
	void testSolve10() {
		Table start = new Table("FACE BDX");
		Table objective = new Table("FBCDEAX");
		
		int expectedCost = 11;
		
		IDAStar s = new IDAStar();
		Iterator<IDAStar.State> it = s.solve
		(
			start,
			objective
		);
		assertNotNull(it);
		IDAStar.State LastState = it.next();
		while(it.hasNext())
		{
			LastState = it.next();
		}
		assertEquals(expectedCost, LastState.getG());
	}
	
	@Test
	void testSolve11() {
		Table start = new Table("FACE BDG");
		Table objective = new Table("GFBCDEA");
		
		int expectedCost = 11;
		
		IDAStar s = new IDAStar();
		Iterator<IDAStar.State> it = s.solve
		(
			start,
			objective
		);
		assertNotNull(it);
		IDAStar.State LastState = it.next();
		while(it.hasNext())
		{
			LastState = it.next();
		}
		assertEquals(expectedCost, LastState.getG());
	}
	
	@Test
	void testSolve12() {
		Table start = new Table("GF EDC B A");
		Table objective = new Table("ABCDEFG");
		
		int expectedCost = 6;
		
		IDAStar s = new IDAStar();
		Iterator<IDAStar.State> it = s.solve
		(
			start,
			objective
		);
		assertNotNull(it);
		IDAStar.State LastState = it.next();
		while(it.hasNext())
		{
			LastState = it.next();
		}
		assertEquals(expectedCost, LastState.getG());
	}

}
