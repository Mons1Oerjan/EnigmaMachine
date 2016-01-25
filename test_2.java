import java.io.IOException;
import java.util.StringTokenizer;

public class test_2
{
	public static void main(String[] args) throws IOException
	{
		String line = "ENIGMA REVEALED NOW I AM THE WALRUS I LIKE YOU TO LIKE ME"; // should turn to QMJIDO MZWZJFJR
		String word;
		RotorHandler RH = new RotorHandler();

		StringTokenizer token = new StringTokenizer(line, " ");

		while (token.hasMoreTokens())
		{
			word = token.nextToken();
			for (int i = 0; i < word.length(); i++)
			{
				System.out.print(RH.encryptMe(word.charAt(i) + ""));

			}
			System.out.print(" ");
		}

	}

}
