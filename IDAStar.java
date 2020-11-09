import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class IDAStar {
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

	static class SearchReturnParams{
		private double thresholdCadidate;
		private State finalState;
		
		public SearchReturnParams(double thresholdCadidate, State finalState)
		{
			this.thresholdCadidate = thresholdCadidate;
			this.finalState = finalState;
		}
	}
	
	private Ilayout objective;
	private final double maxDepth = 20;
	
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
		State Root = new State(s, null, objective);
		double threshold = Root.getF();
		
		while(true)
		{
			//the threshold can increase after a call of search
			SearchReturnParams temp = search(Root, threshold);
			if (temp.finalState != null)
				return temp.finalState.iterator();
			if (maxDepth < temp.thresholdCadidate)
				return null;
			threshold = temp.thresholdCadidate;
		}
		
	}
	
	final private SearchReturnParams search(State n, double threshold)
	{
		double f = n.getF();
		if (f > threshold)
		{
			return new SearchReturnParams(++threshold, null);
		}
		if (n.layout.isGoal(objective))
		{
			return new SearchReturnParams(threshold, n);
		}
		double min = Double.MAX_VALUE;
		List<State> sucs = sucessores(n);
		for(State suc : sucs)
		{
			SearchReturnParams temp = search(suc, threshold);
			if (temp.finalState != null)
				return temp;
			if (temp.thresholdCadidate < min)
				min = temp.thresholdCadidate;
		}
		return new SearchReturnParams(min, null);
	}
}
