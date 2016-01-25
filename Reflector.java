import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Reflector
{
	ArrayList<String> charList = new ArrayList<String>();
	ArrayList<String> compareList = new ArrayList<String>();

	public Reflector() throws IOException
	{
		Scanner inputFile = new Scanner(new File("REFLECTOR.txt"));
		while (inputFile.hasNext())
			charList.add(inputFile.next());
		inputFile.close();
	}

	// Reflect method: [go from rotor1 --> reflector --> find corresponding letter --> rotor1]
	public String reflect(Rotor rotor, String s)
	{
		int count = 0;
		String compare = "";
		ArrayList<pair> compareList = rotor.getList();
		for (int i = 0; i < 26; i++)
		{
			compare = compareList.get(i + rotor.getCurrent()).getFirst();
			if (compare.equals(s))
				count = i;
		}

		String reflectMatch = charList.get(count);
		// fetches the letter that is at the same position as the input letter
		String outputLetter = ""; // will be the letter that is returned to go back through the three rotors

		for (int i = 0; i < charList.size(); i++)
		{
			if (charList.get(i).equals(reflectMatch) && i != count)
			{ // if the letter at position i in the reflector.txt is the same as the letter that
				// is passed through by rotor 3, AND it is not at the same index as the rotor it came
				// through initially, it is the corresponding letter

				outputLetter = rotor.getList().get(i + rotor.getCurrent()).getFirst(); // matches up the index of the corresponding letter in the reflector with the letter at the index i
				// in rotor 3. Sets the output letter to be returned to that value
			}
		}
		return outputLetter; // returns the letter to be matched with the third rotor again
	}
}
