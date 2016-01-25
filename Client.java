import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class Client extends JFrame
{
	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private Socket connection;
	private String msg = "";
	private String serverIP;

	private char[] alphabet = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
	// private String msg;
	private String key = "NOTSAX";
	private String temp;

	public Client(String host)
	{
		super("Client");
		serverIP = host;
		userText = new JTextField();
		userText.setEditable(false);
		userText.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				sendMessage(event.getActionCommand());
				userText.setText("");
			}
		});
		add(userText, BorderLayout.NORTH);
		chatWindow = new JTextArea();
		add(new JScrollPane(chatWindow), BorderLayout.CENTER);
		setSize(600, 600);
		setVisible(true);
	}

	public void runClient()
	{
		try
		{
			connectToServer();
			setupStreams();
			chatting();
		}
		catch (EOFException eofException)
		{
			showMessage("\n Client terminated the connection");
		}
		catch (IOException ioException)
		{
			ioException.printStackTrace();
		}
		finally
		{
			closeAll();
		}
	}

	private void connectToServer() throws IOException
	{
		showMessage("Attempting connection... \n");
		connection = new Socket(InetAddress.getByName(serverIP), 6789);
		showMessage(" Connected to: " + connection.getInetAddress().getHostName());
	}

	private void setupStreams() throws IOException
	{
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("\n Streams now setup! \n");
	}

	private void chatting() throws IOException
	{
		ableToType(true);
		do
		{
			try
			{
				temp = msg;
				msg = (String) input.readObject();
				msg = decrypt(msg);
				showMessage("\nSERVER - " + msg);
			}
			catch (ClassNotFoundException clasNotFoundException)
			{
				showMessage("\n Illegal message type!");
			}
		}
		while (!temp.equals("END"));
	}

	private void closeAll()
	{
		showMessage("\n Closing connections...");
		ableToType(false);
		try
		{
			output.close();
			input.close();
			connection.close();
		}
		catch (IOException ioException)
		{
			ioException.printStackTrace();
		}
	}

	private void sendMessage(String msg)
	{
		try
		{
			if (msg.length() > 5)
			{
				if (msg.substring(0, 5).equals("//KEY"))
				{ //
					key = msg.substring(5, msg.length());
					showMessage("\n> KEY CHANGED TO " + key.toUpperCase());
				}
			} else
			{
				showMessage("\nCLIENT - " + msg);
				msg = encrypt(msg);
				output.writeObject(msg);
				output.flush();
			}
		}
		catch (IOException ioException)
		{
			chatWindow.append("\n an error occured while sending the message.");
		}
	}

	private void showMessage(final String m)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				chatWindow.append(m);
			}
		});
	}

	private void ableToType(final boolean check)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				userText.setEditable(check);
			}
		});
	}

	private String encrypt(String m)
	{

		msg = m.toUpperCase();

		char[] subArr = new char[msg.length()];
		char[] crypArr = new char[msg.length()];
		char[] keyArr = key.toCharArray();
		char[] msgArr = msg.toCharArray();

		int x = msg.length() / key.length();
		int y = msg.length() - (x * key.length());

		for (int i = 0; i < x; i++)
			for (int j = 0; j < key.length(); j++)
				subArr[i * key.length() + j] = keyArr[j];

		for (int i = 0; i < y; i++)
			if (msg.length() < key.length())
				subArr[i] = keyArr[i];
			else
				subArr[key.length() + i] = keyArr[i];

		int ltr_msg = 0;
		int ltr_key = 0;

		for (int i = 0; i < msg.length(); i++)
		{
			for (int j = 0; j < 26; j++)
			{
				if (msgArr[i] == alphabet[j])
					ltr_msg = j;
				if (subArr[i] == alphabet[j])
					ltr_key = j;

			}
			if (ltr_msg + ltr_key >= 26)
				crypArr[i] = alphabet[ltr_msg + ltr_key - 26];
			else
				crypArr[i] = alphabet[ltr_msg + ltr_key];
		}

		return new String(crypArr);

	}

	private String decrypt(String m)
	{

		msg = m.toUpperCase();

		char[] subArr = new char[msg.length()];
		char[] crypArr = new char[msg.length()];
		char[] keyArr = key.toCharArray();
		char[] msgArr = msg.toCharArray();

		int x = msg.length() / key.length();
		int y = msg.length() - (x * key.length());

		for (int i = 0; i < x; i++)
			for (int j = 0; j < key.length(); j++)
				subArr[i * key.length() + j] = keyArr[j];

		for (int i = 0; i < y; i++)
			if (msg.length() < key.length())
				subArr[i] = keyArr[i];
			else
				subArr[key.length() + i] = keyArr[i];

		int ltr_msg = 0;
		int ltr_key = 0;

		for (int i = 0; i < msg.length(); i++)
		{
			for (int j = 0; j < 26; j++)
			{
				if (msgArr[i] == alphabet[j])
					ltr_msg = j;
				if (subArr[i] == alphabet[j])
					ltr_key = j;

			}
			if (ltr_msg - ltr_key < 0)
				crypArr[i] = alphabet[ltr_msg - ltr_key + 26];
			else
				crypArr[i] = alphabet[ltr_msg - ltr_key];
		}

		return new String(crypArr);

	}

}