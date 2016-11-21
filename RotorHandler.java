import java.io.*;
import java.util.Calendar;
import java.util.Scanner;
import java.util.ArrayList;

public class RotorHandler
{
	private Rotor[] rotor = new Rotor[3];

	//constructor:
	public RotorHandler() throws IOException
	{
		Calendar cal = Calendar.getInstance(); // gets current date
		int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
		String dayOfMonthStr = String.valueOf(dayOfMonth);

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
	}

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

		if (rotor[2].turn()) // turn and turn next if true
		{ 
			if (rotor[1].turn())
				rotor[0].turn();
		}

		if (rotor[2].getCurrent() + letters.indexOf(toBeEncrypted) > 51)
			toBeEncrypted = rotor[2].getList().get(letters.indexOf(toBeEncrypted) - (51 - rotor[2].getCurrent()) - 1).getSecond();
		else
			toBeEncrypted = rotor[2].getList().get(rotor[2].getCurrent() + letters.indexOf(toBeEncrypted)).getSecond();

		toBeEncrypted = rotor[2].change(rotor[1], toBeEncrypted);
		toBeEncrypted = rotor[1].change(rotor[0], toBeEncrypted);
		toBeEncrypted = reflect.reflect(rotor[0], toBeEncrypted);
		toBeEncrypted = rotor[0].changeBack(rotor[1], toBeEncrypted);
		toBeEncrypted = rotor[1].changeBack(rotor[2], toBeEncrypted);

		String compare = "";
		ArrayList<pair> compareList = rotor[2].getList(); // creates an instance of rotor3's pair list to be compared
		boolean found = false; 

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
	}
}
