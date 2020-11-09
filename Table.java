import java.util.List;
import java.util.ListIterator;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

public class Table implements Ilayout, Cloneable {
	
	private static final int maxDim = 7;
	private List<Stack<Character>> stacks;
	private int totalBlocks = 0;
	private int correctBlocks = 0;

	public Table() {
		stacks = new ArrayList<Stack<Character>>();
	}

	public Table(String str) throws IllegalStateException {
		String stringStacks[] = str.split(" ");

		if (stringStacks.length > maxDim)
			throw new IllegalStateException("Invalid arg in Board constructor");

		int numOfBlocks = 0;

		stacks = new ArrayList<Stack<Character>>();
		for (int i = 0; i < stringStacks.length; i++) {
			stacks.add(new Stack<Character>());
		}

		for (int i = 0; i < stringStacks.length; i++) {
			for (int j = 0; j < stringStacks[i].length(); j++) {
				if (++numOfBlocks > maxDim)
					throw new IllegalStateException("Invalid arg in Board constructor");
				stacks.get(i).push(stringStacks[i].charAt(j));
			}
		}

	}

	@Override
	public List<Ilayout> children() {
		List<Ilayout> result = new ArrayList<>();
		for (int i = 0; i < stacks.size(); i++) {
			result.addAll(getAllPossibleMovesFromStack(i));
		}
		return result;
	}

	private List<Ilayout> getAllPossibleMovesFromStack(int index) {
		List<Ilayout> result = new ArrayList<>();
		for (int i = 0; i < stacks.size(); i++) {
			Table temp;
			try {
				temp = (Table) this.clone();
			} catch (CloneNotSupportedException e) {
				System.out.println("Cloning is not suported");
				return null;
			}
			Stack<Character> tempStack = temp.stacks.get(index);
			temp.stacks.get(i).push(tempStack.pop());
			if (tempStack.isEmpty())
				temp.stacks.remove(index);
			if (!temp.equals(this))
				result.add(temp);
		}

		// adds the head of this.stacks.get(index) in the right most position of
		// this.stacks
		Table temp;
		try {
			temp = (Table) this.clone();
		} catch (CloneNotSupportedException e) {
			System.out.println("Cloning is not suported");
			return null;
		}
		Stack<Character> tempStack = temp.stacks.get(index);
		temp.stacks.add(new Stack<>());
		temp.stacks.get(temp.stacks.size() - 1).push(tempStack.pop());
		if (tempStack.isEmpty())
			temp.stacks.remove(index);
		if (!temp.equals(this))
			result.add(temp);
		return result;
	}

	@Override
	public boolean isGoal(Ilayout l) {
		return this.equals(l);
	}

	@Override
	public double getG() {
		return 1;
	}
	
	@Override
	public double getH(Ilayout goal) {
		totalBlocks = 0;
		correctBlocks = 0;
		
		if (!(goal instanceof Table))
			return 0;
		Table tableGoal = (Table)goal;
		
		List<ListIterator<Character>> thisStackIterators = new ArrayList<>();
		List<ListIterator<Character>> goalStackIterators = new ArrayList<>();
		for (int i = 0; i < this.stacks.size() || i < tableGoal.stacks.size(); i++) {
			if (i < this.stacks.size())
				thisStackIterators.add(this.stacks.get(i).listIterator());
			if (i < tableGoal.stacks.size())
				goalStackIterators.add(tableGoal.stacks.get(i).listIterator());
		}
		
		while(!thisStackIterators.isEmpty())
		{
			ListIterator<Character> thisTempIterator = thisStackIterators.get(0);
			Character thisTempCharacter = thisTempIterator.next();
			totalBlocks++;
			for (int i = 0; i < goalStackIterators.size(); i++) {
				ListIterator<Character> goalTempIterator = goalStackIterators.get(i);
				Character goalTempCharacter = goalTempIterator.next();
				if(thisTempCharacter.equals(goalTempCharacter))
				{
					correctBlocks++;
					getChainedCorrectBlocks(thisTempIterator, goalTempIterator);
				}
				else
					goalTempIterator.previous();
				if (!goalTempIterator.hasNext())
					goalStackIterators.remove(i);
			}
			if (!thisTempIterator.hasNext())
				thisStackIterators.remove(0);
		}
		
		return totalBlocks - correctBlocks;

	}
	
	private void getChainedCorrectBlocks(ListIterator<Character> thisIterator, ListIterator<Character> goalIterator)
	{
		boolean canRun = true;
		while(canRun && thisIterator.hasNext() && goalIterator.hasNext())
		{
			Character thisTempChar = thisIterator.next();
			Character goalTempChar = goalIterator.next();
			totalBlocks++;
			if (thisTempChar.equals(goalTempChar))
				correctBlocks++;
			else
				canRun = false;
		}
		while(thisIterator.hasNext()) 
		{
			thisIterator.next();
			totalBlocks++;
		}
		while(goalIterator.hasNext()) {goalIterator.next();}
	}
	

	@Override
	public boolean equals(Object other) {
		boolean temp = false;
		if (!(other instanceof Table)) {
			return temp;
		}
		Table otherTable = (Table) other;
		for (int i = 0; i < stacks.size(); i++) {
			temp = false;
			Stack<Character> thisStack = stacks.get(i);
			for (int j = 0; j < otherTable.stacks.size(); j++) {
				Stack<Character> otherStack = otherTable.stacks.get(j);
				temp = temp ? true : thisStack.equals(otherStack);
			}
			if (!temp)
				return false;
		}
		return temp;
	}

	public Object clone() throws CloneNotSupportedException {
		Table result = new Table();
		for (int i = 0; i < stacks.size(); i++) {
			Iterator<Character> temp = stacks.get(i).iterator();
			result.stacks.add(new Stack<Character>());
			while(temp.hasNext())
				result.stacks.get(i).push(temp.next());
		}
		return result;
	}

	public String toString() {
		StringWriter writer = new StringWriter();
		PrintWriter pw = new PrintWriter(writer);
		for (Stack<Character> stack : stacks) {
			if (!stack.isEmpty())
				pw.println(stack.toString());
		}
		return writer.toString();
	}


}
