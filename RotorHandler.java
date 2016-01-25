import java.io.*;
import java.util.Calendar;
import java.util.Scanner;
import java.util.ArrayList;

public class RotorHandler
{
	//Attributes:
	private Rotor[] rotor = new Rotor[3];

	//constructor:
	public RotorHandler() throws IOException
	{
		Calendar cal = Calendar.getInstance(); // gets current date
		int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
		String dayOfMonthStr = String.valueOf(dayOfMonth);
		// System.out.println(dayOfMonthStr);

		Scanner settingsFile = new Scanner(new File("SETTINGS.txt"));

		while (settingsFile.hasNext())
			// searches SETTINGS.txt for text at current date
			if (settingsFile.next().equals(dayOfMonthStr))
			{
				// sets the rotor positions
				for (int i = 0; i < 3; i++)
				{
					String pos = settingsFile.next();
					if (pos.equals("3"))
						rotor[i] = new Rotor("ROTOR_3.txt", 21);
					else if (pos.equals("2"))
						rotor[i] = new Rotor("ROTOR_2.txt", 4);
					else
						rotor[i] = new Rotor("ROTOR_1.txt", 16);
				}
				// sets starting position for the rotors
				for (int i = 0; i < 3; i++)
				{
					rotor[i].setCurrent(Integer.parseInt(settingsFile.next()));
				}
			}
			else
				settingsFile.nextLine();
		settingsFile.close();
	}//end of constructor

	// encrypts a single character:
	public String encryptMe(String toBeEncrypted) throws IOException
	{
		if (toBeEncrypted.equals("0") || toBeEncrypted.equals("1") || toBeEncrypted.equals("2") || toBeEncrypted.equals("3") || toBeEncrypted.equals("4") || toBeEncrypted.equals("5") || toBeEncrypted.equals("6") || toBeEncrypted.equals("7")
				|| toBeEncrypted.equals("8") || toBeEncrypted.equals("9") || toBeEncrypted.equals("!") || toBeEncrypted.equals("?") || toBeEncrypted.equals("#"))
			return toBeEncrypted;

		Scanner inputFile = new Scanner(new File("LETTERS.txt"));

		PlugboardSwap swapper = new PlugboardSwap();
		Reflector reflect = new Reflector();
		ArrayList<String> letters = new ArrayList<String>();

		while (inputFile.hasNext())
			letters.add(inputFile.next());

		// toBeEncrypted = swapper.Swap(toBeEncrypted);
		// System.out.println("Swapped: " + toBeEncrypted);

		if (rotor[2].turn()) // turn and turn next if true
		{ 
			// System.out.println("#####");
			if (rotor[1].turn())
				rotor[0].turn();
		}

		// System.out.println(toBeEncrypted);

		if (rotor[2].getCurrent() + letters.indexOf(toBeEncrypted) > 51)
			toBeEncrypted = rotor[2].getList().get(letters.indexOf(toBeEncrypted) - (51 - rotor[2].getCurrent()) - 1).getSecond();
		else
			toBeEncrypted = rotor[2].getList().get(rotor[2].getCurrent() + letters.indexOf(toBeEncrypted)).getSecond();

		// System.out.println(toBeEncrypted); // by returing index at letter index + curr index

		toBeEncrypted = rotor[2].change(rotor[1], toBeEncrypted); // change and print
		// System.out.println(toBeEncrypted);

		toBeEncrypted = rotor[1].change(rotor[0], toBeEncrypted);
		// System.out.println(toBeEncrypted);

		toBeEncrypted = reflect.reflect(rotor[0], toBeEncrypted);
		// System.out.println(toBeEncrypted);

		toBeEncrypted = rotor[0].changeBack(rotor[1], toBeEncrypted);
		// System.out.println(toBeEncrypted);

		toBeEncrypted = rotor[1].changeBack(rotor[2], toBeEncrypted);
		// System.out.println(toBeEncrypted);

		// toBeEncrypted = swapper.Swap(toBeEncrypted);

		String compare = "";
		ArrayList<pair> compareList = rotor[2].getList(); // creates an instance of robot3's pair list to be compared
		boolean found = false; // ADDED HERE

		for (int i = 0; i < 26; i++)
		{
			if (i + rotor[2].getCurrent() > 51)
				compare = compareList.get(i + rotor[2].getCurrent() - 51 - 1).getSecond();
			else
				compare = compareList.get(i + rotor[2].getCurrent()).getSecond();

			if (compare.equals(toBeEncrypted) && !found)
			{
				toBeEncrypted = letters.get(i);
				found = true;
			}
		}
		return toBeEncrypted;
	}//end of encryptMe
}//end of RotorHandler
