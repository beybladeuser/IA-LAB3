import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

public class AStar {
	static class State implements Iterable<State>
	{
		private Ilayout layout;
		private State father;
		private double g;
		private double f;
		
		public State(Ilayout l, State n, Ilayout goal)
		{
			layout = l;
			father = n;
			if (father != null)
			{
				g = father.g + l.getG();
			}
			else
			{
				g = 0.0;
			}
			f = g + layout.getH(goal);
		}
		
		public String toString()
		{
			return layout.toString();
		}
		
		public double getG()
		{
			return g;
		}
		
		public double getF()
		{
			return f;
		}
		
		/*this equals is to make the contains called in fechados on the func solve work.
		this optimizes the program by removing the duplicates in fechados, being that if you save the dups
		this will occupy too much memory and making the program slower because the system has now to manage memory*/
		@Override
		public boolean equals(Object other)
		{
			if (!(other instanceof State)) {return false;}
			return layout.equals(((State)other).layout);
		}
		
//		@Override
//		public boolean equals(Object other)
//		{
//			return /*other instanceof State &&*/ this.hashCode() == other.hashCode();
//		}
//		
//		@Override
//		public int hashCode()
//		{
//			return layout.hashCode();
//		}
		
		public class StateIterator implements Iterator<State>
		{
			Stack<State> States = new Stack<>();
			
			public StateIterator(State last)
			{
				States.push(last);
				State f = last.father;
				while(f != null)
				{
					States.push(f);
					f = f.father;
				}
			}
			
			@Override
			public boolean hasNext() {
				return !States.isEmpty();
			}

			@Override
			public State next() {
				return States.pop();
			}
			
		}

		@Override
		public Iterator<State> iterator() {
			return new StateIterator(this);
		}


	}
	
	protected Queue<State> abertos;
	private List<State> fechados;
	private State actual;
	private Ilayout objective;
	
	final private List<State> sucessores(State n)
	{
		List<State> sucs = new ArrayList<>();
		List<Ilayout> children = n.layout.children();
		for(Ilayout e : children)
		{
			if (n.father == null || !e.equals(n.father.layout))
			{
				State nn = new State(e,n, objective);
				sucs.add(nn);
			}
		}
		return sucs;
	}
	
	final public Iterator<State> solve(Ilayout s, Ilayout goal)
	{
		objective = goal;
		abertos = new PriorityQueue<>
		(
			10,
			(s1, s2) -> (int) Math.signum(s1.getF() - s2.getF())
		);
		fechados = new ArrayList<>();
		abertos.add(new State(s, null, objective));
		List<State> sucs;
		while(true)
		{
			if (abertos.isEmpty())
				return null;
			actual = abertos.poll();
			if (actual.layout.isGoal(objective))
				return actual.iterator();
			else
			{
				sucs = sucessores(actual);
				if (!fechados.contains(actual))
					fechados.add(actual);
				for (State suc : sucs)
					if (!fechados.contains(suc) && !abertos.contains(suc))
						abertos.add(suc);
			}
			
		}
	}
}
