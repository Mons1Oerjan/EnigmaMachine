import java.util.ArrayList;
import java.io.*;
import java.util.Scanner;

public class Rotor
{
	private ArrayList<pair> pairList = new ArrayList<pair>();
	private int current;
	private int pointer;

	public Rotor(String fileName, int notch) throws IOException
	{
		Scanner inputFile = new Scanner(new File(fileName));
		while (inputFile.hasNext())
			pairList.add(new pair(inputFile.next(), inputFile.next()));
		inputFile.close();

		pairList.get(notch).setNotch(true);
		pairList.get(notch + 26).setNotch(true);
	}

	public void setCurrent(int start)
	{
		this.current = start;
	}

	public int getCurrent()
	{
		return current;
	}

	public int getPointer()
	{
		return pointer;
	}

	public void setPointer(int pointer)
	{
		this.pointer = pointer;
	}

	public ArrayList<pair> getList()
	{ 
		return pairList;
	}

	public String change(Rotor next, String C)
	{ 
		String temp = "";
		int i = current;
		boolean loop = false;
		;

		while (!temp.equals(C))// get index of letter in first row in order to return second row letter on next rotor at index
		{ 
			temp = pairList.get(i).getFirst();
			i++;
			if (i > 51)
			{
				i = 0;
				loop = true;
			}
		}

		if (loop)
			return next.getList().get(i + next.getCurrent() + 51 - current).getSecond();

		if (next.getCurrent() + i - current - 1 > 51)
			return next.getList().get(i - current - 2 - (51 - next.getCurrent())).getSecond();
		else
			return next.getList().get(next.getCurrent() + i - current - 1).getSecond(); // return index of next rotor
	}

	//change in reverse
	public String changeBack(Rotor next, String C) 
	{ 
		String temp = "";
		int i = current;
		boolean loop = false;

		while (!temp.equals(C))
		{
			temp = pairList.get(i).getSecond(); // gets the second letter in the pair from the pairList instead of the first
			i++;
			if (i > 51)
			{
				i = 0;
				loop = true;
			}
		}
		if (loop)
			return next.getList().get(i + next.getCurrent() + 51 - current - 1).getFirst();

		if (next.getCurrent() + i - current - 1 > 51)
			return next.getList().get(i - current - 2 - (51 - next.getCurrent())).getFirst();
		else
			return next.getList().get(next.getCurrent() + i - current - 1).getFirst(); // return index of next rotor
	}

	public boolean turn()
	{
		if (current == 51) // advance current position
			current = 0;
		else
			current++;
		if (current == 0)
			return pairList.get(51).getNotch();
		return pairList.get(current - 1).getNotch(); // return true if notch
	}

}
