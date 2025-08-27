package pos_server;

import java.lang.*;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.net.*;

import eu.nets.*;
import eu.nets.baxi.client.*;

public class Main implements Runnable
{
	
	// Connect status constants

	public final static int NULL = 0;
	public final static int DISCONNECTED = 1;
	public final static int DISCONNECTING = 2;
	public final static int BEGIN_CONNECT = 3;
	public final static int CONNECTED = 4;
	public final static int RESET = 5;

	// Other constants
	public final static String statusMessages[] = { " Error! Could not connect!", " Disconnected", " Disconnecting...", " Connecting...", " Connected", " Halted and reseted..." };
	public final static Main PosServerObj = new Main();
	public final static String END_CHAT_SESSION = new Character((char) 0).toString(); // Indicates the end of a session

	// Connection state info
	public static String m_HostIP = "192.168.1.132";
	public static int m_Port = 1234;
	public static int m_ConnectionStatus = DISCONNECTED;
	public static boolean m_IsHost = true;
	public static String m_StatusString = statusMessages[m_ConnectionStatus];
	public static StringBuffer m_ToAppend = new StringBuffer("");
	public static StringBuffer m_ToSend = new StringBuffer("");

	// Various GUI components and info
	public static JFrame m_MainFrame = null;
	public static JTextArea m_logText = null;
	public static JPanel m_StatusBar = null;
	public static JLabel m_StatusField = null;
	
	public static JTextField m_StatusColor = null;
	public static JTextField m_ChatLine = null;
	public static JTextField m_IpField = null;
	public static JTextField m_PortField = null;
	
	public static JRadioButton m_HostOption = null;
	public static JRadioButton m_GuestOption = null;
	
	public static JButton m_ConnectButton = null;
	public static JButton m_DisconnectButton = null;
	public static JButton m_ResetButton = null;

	// TCP Components
	public static ServerSocket m_HostServer = null;
	public static Socket m_Socket = null;
	public static BufferedReader m_Input = null;
	public static PrintWriter m_Output = null;
	
	// BAXI Object
	//public static BaxiCtrl m_Baxi = new BaxiCtrl();
	
	// BAXI Object
	public static jbaxi_functions m_Baxi_func = new jbaxi_functions();

	// ///////////////////////////////////////////////////////////////

	private static JPanel InitLogPane()
	{
		JPanel LogPane = new JPanel(new BorderLayout());
		
		m_logText = new JTextArea(10, 20);
		m_logText.setLineWrap(true);
		m_logText.setEditable(false);
		m_logText.setForeground(Color.blue);
		
		JScrollPane LogTextPane = new JScrollPane(m_logText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		m_ChatLine = new JTextField();
		m_ChatLine.setEnabled(false);
		m_ChatLine.addActionListener(new ActionAdapter() {
			public void actionPerformed(ActionEvent e)
			{
				String s = m_ChatLine.getText();
				if (!s.equals(""))
				{
					AppendToChatBox(" [OUT]: " + s + "\n");
					m_ChatLine.selectAll();
					SendString(s);
				}
			}
		});
		
		LogPane.add(m_ChatLine, BorderLayout.SOUTH);
		LogPane.add(m_logText, BorderLayout.CENTER);
		LogPane.setPreferredSize(new Dimension(200, 200));
		
		return LogPane;
	}
	
	// ///////////////////////////////////////////////////////////////
	
	private static JPanel InitOptionsPane()
	{
		// Create an options pane
		JPanel OptionsPane = new JPanel(new GridLayout(4, 1));
		
		ActionAdapter ButtonListener = null;

		// IP address input
		JPanel IpPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
		IpPane.add(new JLabel("Host IP:"));
		m_IpField = new JTextField(10);
		m_IpField.setText(m_HostIP);
		m_IpField.setEnabled(true);
		m_IpField.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e)
			{
				m_IpField.selectAll();
				// Should be editable only when disconnected
				if (m_ConnectionStatus != DISCONNECTED)
				{
					ChangeStatusTS(NULL, true);
				}
				else
				{
					m_HostIP = m_IpField.getText();
				}
			}
		});
		IpPane.add(m_IpField);
		OptionsPane.add(IpPane);

		// Port input
		JPanel PortPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
		PortPane.add(new JLabel("Port:"));
		m_PortField = new JTextField(10);
		m_PortField.setEditable(true);
		m_PortField.setText((new Integer(m_Port)).toString());
		m_PortField.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e)
			{
				// should be editable only when disconnected
				if (m_ConnectionStatus != DISCONNECTED)
				{
					ChangeStatusTS(NULL, true);
				}
				else
				{
					try
					{
						int iTemp = Integer.parseInt(m_PortField.getText());
						m_Port = iTemp;
					}
					catch (NumberFormatException nfe)
					{
						m_PortField.setText((new Integer(m_Port)).toString());
						m_MainFrame.repaint();
					}
				}
			}
		});
		PortPane.add(m_PortField);
		OptionsPane.add(PortPane);

		// Host/guest option
		ButtonListener = new ActionAdapter() {
			public void actionPerformed(ActionEvent e)
			{
				if (m_ConnectionStatus != DISCONNECTED)
				{
					ChangeStatusTS(NULL, true);
				} 
				else
				{
					m_IsHost = e.getActionCommand().equals("host");

					// Cannot supply host IP if host option is chosen
					if (m_IsHost)
					{
						m_IpField.setEnabled(false);
						m_IpField.setText("192.168.1.132");
						m_HostIP = "192.168.1.132";
					}
					else
					{
						m_IpField.setEnabled(true);
					}
				}
			}
		};
		
		ButtonGroup ButtonGroup = new ButtonGroup();
		m_HostOption = new JRadioButton("Host", true);
		m_HostOption.setMnemonic(KeyEvent.VK_H);
		m_HostOption.setActionCommand("host");
		m_HostOption.addActionListener(ButtonListener);
		
		m_GuestOption = new JRadioButton("Guest", false);
		m_GuestOption.setMnemonic(KeyEvent.VK_G);
		m_GuestOption.setActionCommand("guest");
		m_GuestOption.addActionListener(ButtonListener);
		
		ButtonGroup.add(m_HostOption);
		ButtonGroup.add(m_GuestOption);
		
		JPanel RadioButtonPane = new JPanel(new GridLayout(1, 2));
		RadioButtonPane.add(m_HostOption);
		RadioButtonPane.add(m_GuestOption);
		OptionsPane.add(RadioButtonPane);

		// Connect/disconnect buttons
		JPanel ButtonPane = new JPanel(new GridLayout(1, 3));
		ButtonListener = new ActionAdapter() {
			public void actionPerformed(ActionEvent e)
			{
				// Request a connection initiation
				if (e.getActionCommand().equals("connect"))
				{
					ChangeStatusTS(BEGIN_CONNECT, true);
				}
				
				if (e.getActionCommand().equals("disconnect"))
				{
					ChangeStatusTS(DISCONNECTING, true);
				}
				
				if (e.getActionCommand().equals("reset"))
				{
					ChangeStatusTS(RESET, true);
				}
				
				
			}
		};
		
		m_ConnectButton = new JButton("Connect");
		m_ConnectButton.setMnemonic(KeyEvent.VK_C);
		m_ConnectButton.setActionCommand("connect");
		m_ConnectButton.addActionListener(ButtonListener);
		m_ConnectButton.setEnabled(true);
		
		m_DisconnectButton = new JButton("Disconnect");
		m_DisconnectButton.setMnemonic(KeyEvent.VK_D);
		m_DisconnectButton.setActionCommand("disconnect");
		m_DisconnectButton.addActionListener(ButtonListener);
		m_DisconnectButton.setEnabled(false);
	
		m_ResetButton = new JButton("Reset");
		m_ResetButton.setMnemonic(KeyEvent.VK_Q);
		m_ResetButton.setActionCommand("reset");
		m_ResetButton.addActionListener(ButtonListener);
		m_ResetButton.setEnabled(false);
		
		ButtonPane.add(m_ConnectButton);
		ButtonPane.add(m_DisconnectButton);
		ButtonPane.add(m_ResetButton);
		
		OptionsPane.add(ButtonPane);

		return OptionsPane;
	}

	// ///////////////////////////////////////////////////////////////

	// Initialize all the GUI components and display the frame
	private static void InitGUI()
	{
		// Set up the status bar
		m_StatusField = new JLabel();
		m_StatusField.setText(statusMessages[DISCONNECTED]);
		m_StatusColor = new JTextField(1);
		m_StatusColor.setBackground(Color.red);
		m_StatusColor.setEditable(false);
		m_StatusBar = new JPanel(new BorderLayout());
		m_StatusBar.add(m_StatusColor, BorderLayout.WEST);
		m_StatusBar.add(m_StatusField, BorderLayout.CENTER);

		JPanel OptionsPane = InitOptionsPane();
		JPanel LogPane = InitLogPane();
		
		// Set up the main pane
		JPanel MainPane = new JPanel(new BorderLayout());
		MainPane.add(m_StatusBar, BorderLayout.SOUTH);
		MainPane.add(OptionsPane, BorderLayout.WEST);
		MainPane.add(LogPane, BorderLayout.CENTER);

		// Set up the main frame
		m_MainFrame = new JFrame("POS Server");
		m_MainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		m_MainFrame.setContentPane(MainPane);
		m_MainFrame.setSize(m_MainFrame.getPreferredSize());
		m_MainFrame.setLocation(200, 200);
		m_MainFrame.pack();
		m_MainFrame.setVisible(true);
	}

	// ///////////////////////////////////////////////////////////////

	// The thread-safe way to change the GUI components while changing state
	private static void ChangeStatusTS(int newConnectStatus, boolean noError)
	{
		// Change state if valid state
		if (newConnectStatus != NULL)
		{
			m_ConnectionStatus = newConnectStatus;
		}

		// If there is no error, display the appropriate status message
		if (noError)
		{
			m_StatusString = statusMessages[m_ConnectionStatus];
		}
		else // Otherwise, display error message
		{
			m_StatusString = statusMessages[NULL];
		}

		// Call the run() routine (Runnable interface) on the error-handling and GUI-update thread
		SwingUtilities.invokeLater(PosServerObj);
	}

	// ///////////////////////////////////////////////////////////////

	/*
	// The non-thread-safe way to change the GUI components while changing state
	private static void ChangeStatusNTS(int newConnectStatus, boolean noError)
	{
		// Change state if valid state
		if (newConnectStatus != NULL)
		{
			m_ConnectionStatus = newConnectStatus;
		}

		// If there is no error, display the appropriate status message
		if (noError)
		{
			m_StatusString = statusMessages[m_ConnectionStatus];
		}
		else // Otherwise, display error message
		{
			m_StatusString = statusMessages[NULL];
		}

		// Call the run() routine (Runnable interface) on the current thread
		PosServerObj.run();
	}
	*/

	// ///////////////////////////////////////////////////////////////

	// Thread-safe way to append to the chat box
	private static void AppendToChatBox(String s)
	{
		synchronized (m_ToAppend)
		{
			m_ToAppend.append(new Date() + s);
		}
	}

	// ///////////////////////////////////////////////////////////////

	// Add text to send-buffer
	private static void SendString(String s)
	{
		synchronized (m_ToSend)
		{
			m_ToSend.append(s + "\n");
		}
	}

	// ///////////////////////////////////////////////////////////////

	// Cleanup for disconnect
	private static void CleanUp()
	{
		try
		{
			if (m_HostServer != null)
			{
				m_HostServer.close();
				m_HostServer = null;
			}
		}
		catch (IOException e)
		{
			m_HostServer = null;
		}

		try
		{
			if (m_Socket != null)
			{
				m_Socket.close();
				m_Socket = null;
			}
		}
		catch (IOException e)
		{
			m_Socket = null;
		}

		try
		{
			if (m_Input != null)
			{
				m_Input.close();
				m_Input = null;
			}
		}
		catch (IOException e)
		{
			m_Input = null;
		}

		if (m_Output != null)
		{
			m_Output.close();
			m_Output = null;
		}
	}

	// ///////////////////////////////////////////////////////////////

	// Checks the current state and sets the enables/disables accordingly
	public void run()
	{
		switch (m_ConnectionStatus)
		{
		case DISCONNECTED:
			m_ConnectButton.setEnabled(true);
			m_DisconnectButton.setEnabled(false);
			m_ResetButton.setEnabled(false);
			m_IpField.setEnabled(true);
			m_PortField.setEnabled(true);
			m_HostOption.setEnabled(true);
			m_GuestOption.setEnabled(true);
			m_ChatLine.setText("");
			m_ChatLine.setEnabled(false);
			m_StatusColor.setBackground(Color.red);
			break;

		case DISCONNECTING:
			m_ConnectButton.setEnabled(false);
			m_DisconnectButton.setEnabled(false);
			m_ResetButton.setEnabled(false);
			m_IpField.setEnabled(false);
			m_PortField.setEnabled(false);
			m_HostOption.setEnabled(false);
			m_GuestOption.setEnabled(false);
			m_ChatLine.setEnabled(false);
			m_StatusColor.setBackground(Color.orange);
			break;

		case CONNECTED:
			m_ConnectButton.setEnabled(false);
			m_DisconnectButton.setEnabled(true);
			m_ResetButton.setEnabled(false);
			m_IpField.setEnabled(false);
			m_PortField.setEnabled(false);
			m_HostOption.setEnabled(false);
			m_GuestOption.setEnabled(false);
			m_ChatLine.setEnabled(true);
			m_StatusColor.setBackground(Color.green);
			break;

		case BEGIN_CONNECT:
			m_ConnectButton.setEnabled(false);
			m_DisconnectButton.setEnabled(false);
			m_ResetButton.setEnabled(true);
			m_IpField.setEnabled(false);
			m_PortField.setEnabled(false);
			m_HostOption.setEnabled(false);
			m_GuestOption.setEnabled(false);
			m_ChatLine.setEnabled(false);
			m_ChatLine.grabFocus();
			m_StatusColor.setBackground(Color.orange);
			break;
		case RESET:
			m_ConnectButton.setEnabled(true);
			m_DisconnectButton.setEnabled(false);
			m_ResetButton.setEnabled(false);
			m_IpField.setEnabled(true);
			m_PortField.setEnabled(true);
			m_HostOption.setEnabled(true);
			m_GuestOption.setEnabled(true);
			m_ChatLine.setText("");
			m_ChatLine.setEnabled(false);
			m_StatusColor.setBackground(Color.red);
			break;
		}

		// Make sure that the button/text field states are consistent with the internal states
		m_IpField.setText(m_HostIP);
		m_PortField.setText((new Integer(m_Port)).toString());
		m_HostOption.setSelected(m_IsHost);
		m_GuestOption.setSelected(!m_IsHost);
		m_StatusField.setText(m_StatusString);
		m_logText.append(m_ToAppend.toString());
		m_ToAppend.setLength(0);

		m_MainFrame.repaint();
	}

	// ///////////////////////////////////////////////////////////////

	// The main procedure
	public static void main(String args[])
	{
		String s;

		InitGUI();

		//m_Baxi.open();
		
		int iReturn1 = m_Baxi_func.TransferAmount();
		//int iReturn2 = PosServerObj.TransferAmount();
		
		PosServerObj.AppendToChatBox(" Terminal returned: " + iReturn1 + "\n");
		
		while (true)
		{
			try
			{ // Poll every ~10 ms
				Thread.sleep(10);
			} 
			catch (InterruptedException e) {}

			switch (m_ConnectionStatus)
			{
			case BEGIN_CONNECT:
				try
				{
					// Try to set up a server if host
					if (m_IsHost)
					{
						m_HostServer = new ServerSocket(m_Port);
						m_Socket = m_HostServer.accept();
					}
					else // If guest, try to connect to the server
					{
						m_Socket = new Socket(m_HostIP, m_Port);
					}

					m_Input = new BufferedReader(new InputStreamReader(m_Socket.getInputStream()));
					m_Output = new PrintWriter(m_Socket.getOutputStream(), true);
					
					ChangeStatusTS(CONNECTED, true);
					
				}
				catch (IOException e) // If error, clean up and output an error message
				{
					CleanUp();
					ChangeStatusTS(DISCONNECTED, false);
				}
				
				break;

			case CONNECTED:
				try
				{
					// Send data
					if (m_ToSend.length() != 0)
					{
						m_Output.print(m_ToSend);
						m_Output.flush();
						m_ToSend.setLength(0);
						ChangeStatusTS(NULL, true);
					}

					// Receive data
					if (m_Input.ready())
					{
						s = m_Input.readLine();
						if ((s != null) && (s.length() != 0))
						{
							if (s.equals(END_CHAT_SESSION)) // Check if it is the end of a trasmission
							{
								ChangeStatusTS(DISCONNECTING, true);
							}
							else // Otherwise, receive what text
							{
								AppendToChatBox(" [IN]: " + s + "\n");
								ChangeStatusTS(NULL, true);
							}
						}
					}
				} 
				catch (IOException e)
				{
					CleanUp();
					ChangeStatusTS(DISCONNECTED, false);
				}
				break;

			case DISCONNECTING:
				// Tell other chatter to disconnect as well
				m_Output.print(END_CHAT_SESSION);
				m_Output.flush();

				// Clean up (close all streams/sockets)
				CleanUp();
				ChangeStatusTS(DISCONNECTED, true);
				break;
				
			case RESET:
				// Tell other chatter to disconnect as well
				m_Output.print(END_CHAT_SESSION);
				m_Output.flush();

				// Clean up (close all streams/sockets)
				CleanUp();
				ChangeStatusTS(RESET, true);
				break;

			default:
				break; // do nothing
			}
		}
	}
}

// //////////////////////////////////////////////////////////////////

// Action adapter for easy event-listener coding
class ActionAdapter implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	}
}

// //////////////////////////////////////////////////////////////////