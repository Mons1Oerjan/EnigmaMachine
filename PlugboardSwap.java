public class PlugboardSwap
{
	// index determines pairs (A-W, C-Y...Q-V):
	private String[] first = { "A", "C", "R", "T", "P", "F", "H", "L", "M", "Q" };
	private String[] second = { "W", "Y", "N", "Z", "U", "O", "S", "I", "E", "V" };

	// letters not included: B, D, G, J, K, X

	public String Swap(String input)
	{
		for (int i = 0; i < 10; i++) // go through the array "first"
		{
			if (input.equals(first[i])) // check for equality
				return second[i]; // if found, return its pair
		}
		for (int i = 0; i < 10; i++) // go through the array "second"
		{
			if (input.equals(second[i])) // check for equality
				return first[i]; // if found, return its pair
		}
		return input; // if not found, just return the input
	}
}
