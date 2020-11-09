import java.util.Iterator;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) throws Exception {
		Scanner sc = new Scanner(System.in);
		
		IDAStar s = new IDAStar();
		Iterator<IDAStar.State> it = s.solve
		(
			new Table(sc.nextLine()),
			new Table(sc.nextLine())
		);
		if (it == null)
			System.out.println("no solution was found");
		else
		{
			IDAStar.State LastState = it.next();
			while(it.hasNext())
			{
				LastState = it.next();
			}
			System.out.println((int)LastState.getG());
		}
		sc.close();
	}
}
