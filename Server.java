import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
// import package.Enigma.*;

import javax.swing.*;

public class Server extends JFrame
{

	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private ServerSocket server;
	private Socket connection;

	private char[] alphabet = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
	private String msg;
	private String key = "SAX";
	private String temp;

	{
		try
		{
			RotorHandler RH = new RotorHandler();
		}
		catch (IOException e)
		{
			System.out.println(e);
		}
		finally
		{
			System.out.println("ERROR");
		}
	}

	public Server()
	{
		super("My Instant Messanger");
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
		add(new JScrollPane(chatWindow));
		setSize(600, 600);
		setVisible(true);
	}

	public void runServer()
	{
		try
		{
			server = new ServerSocket(6789, 100);
			while (true)
			{
				try
				{
					waitForConnection();
					setupStreams();
					chatting();
				}
				catch (EOFException eofException)
				{
					showMessage("\n Server ended the connection!");
				}
				finally
				{
					closeAll();
				}
			}
		}
		catch (IOException ioException)
		{
			ioException.printStackTrace();
		}
	}

	private void waitForConnection() throws IOException
	{
		showMessage(" Waiting for someone to connect...\n");
		connection = server.accept();
		showMessage(" Now connected to " + connection.getInetAddress().getHostName());
	}

	private void setupStreams() throws IOException
	{
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("\n Streams are now setup! \n");
	}

	private void chatting() throws IOException
	{ // EDITING BEGINS
		String msg = " You are now connected! ";
		sendMessage(msg);
		ableToType(true);
		do
		{
			try
			{
				temp = msg; // temp to recognize end
				msg = (String) input.readObject();
				// msg = RH.encryptMe(msg);
				showMessage("\nCLIENT - " + msg); // decrypt here
			}
			catch (ClassNotFoundException classNotFoundException)
			{
				showMessage("\n User tried to send an illegal message! ");
			}
		}
		while (!temp.equals("END")); // removed CLIENT - //END DOES NOT WORK
										// IF KEYS ARE NOT SAME
	}

	private void closeAll()
	{
		showMessage("\n Closing connections... \n");
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
				{ // NOT COOL INPUT -
					// MAKE FIX
					key = msg.substring(5, msg.length());
					showMessage("\n> KEY CHANGED TO " + key.toUpperCase());
				}
			} else
			{
				showMessage("\nSERVER - " + msg);
				msg = encrypt(msg);
				output.writeObject(msg); // encrypt here REMOVED SERVER
				output.flush();
			}
		}
		catch (IOException ioException)
		{
			chatWindow.append("\n ***ERROR: CANNOT SEND MESSAGE!***");
		}
	}

	private void showMessage(final String text)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				chatWindow.append(text);
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
