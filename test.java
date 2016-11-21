import java.util.ArrayList;
import java.io.*;
import java.util.Scanner;

public class test
{

	public static void main(String[] args) throws IOException
	{
		Scanner inputFile = new Scanner(new File("LETTERS.txt")); // connect to file containing all alphabet for input
		ArrayList<String> letters = new ArrayList<String>(); // and fill in letters arraylist
		Reflector reflector = new Reflector();

		while (inputFile.hasNext())
			letters.add(inputFile.next());

		inputFile.close();

		Rotor rotor1 = new Rotor("ROTOR_1.txt", 16); // create new rotors with notches set
		Rotor rotor2 = new Rotor("ROTOR_2.txt", 4);
		Rotor rotor3 = new Rotor("ROTOR_3.txt", 21);

		PlugboardSwap swapper = new PlugboardSwap();

		rotor1.setCurrent(12); // set initial key
		rotor2.setCurrent(2);
		rotor3.setCurrent(10);

		String temp = "M"; // dummy variable
		temp = swapper.Swap(temp);
		System.out.println("Swapped: " + temp);

		if (rotor3.turn()) // turn and turn next if true
			rotor2.turn();

		temp = rotor3.getList().get(rotor3.getCurrent() + letters.indexOf(temp)).getSecond(); // get first changed letter at input
		System.out.println(temp); // by returing index at letter index + curr index

		temp = rotor3.change(rotor2, temp); // change and print
		System.out.println(temp);

		temp = rotor2.change(rotor1, temp);
		System.out.println(temp);

		temp = reflector.reflect(rotor1, temp);
		System.out.println(temp);

		temp = rotor1.changeBack(rotor2, temp);
		System.out.println(temp);

		temp = rotor2.changeBack(rotor3, temp);
		System.out.println(temp);

		temp = swapper.Swap(temp);

		String compare = "";
		ArrayList<pair> compareList = rotor3.getList(); // creates an instance of rotor3's pair list to be compared
		for (int i = 0; i < 26; i++)
		{
			compare = compareList.get(i + rotor3.getCurrent()).getSecond(); // takes the second letter from rotor3's pair
			if (compare.equals(temp)) // compares the "temp" variable to the "compare" variable
				temp = letters.get(i); // if they're the same, then the letter at position i in the letters arraylist becomes our new temp variable
		}

		System.out.println(temp);

	}

}
