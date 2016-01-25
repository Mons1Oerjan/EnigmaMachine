public class pair
{
	//Attributes:
	String first;
	String second;
	boolean notch;

	//Constructor:
	public pair(String first, String second)
	{
		this.first = first;
		this.second = second;
		notch = false;
	}

	//Get and set methods:
	public String getFirst()
	{
		return first;
	}
	
	public void setFirst(String first)
	{
		this.first = first;
	}
	
	public String getSecond()
	{
		return second;
	}

	public void setSecond(String second)
	{
		this.second = second;
	}

	public boolean getNotch()
	{
		return notch;
	}

	public void setNotch(boolean notch)
	{
		this.notch = notch;
	}
	
	//toString method: Is this even needed?
	public String toString()
	{
		return "pair [first=" + first + ", second=" + second + "]";
	}
}
