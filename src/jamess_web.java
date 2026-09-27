import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.Dimension;
import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilder;  
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;  
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.Node;  
import org.w3c.dom.Element;
public class jamess_web {
	private Dimension screenSize;
	private static JFrame main; 
	private static JEditorPane window; 
	private static JTextField addressBar; 
	private static JButton searchButton, bookmarkButton, reloadButton, backButton, forwardButton, bookmarkMenuButton, historyMenuButton, newWindowMenuButton, clearHistoryButton, clearBookmarksButton, createBookmarkBackupButton, createHistoryBackupButton, restoreHistoryButton, restoreBookmarksButton; 
	private JMenuBar menuBar; 
	private static JMenu miscMenu, clearMenu, restoreMenu; 
	private static ArrayList<String> historyUrls; 
	private static int historyIndex; 
	public jamess_web(){
		/*
		 * The name of the browser and idea is something I stole from Ridhvik
		 * 
		 * The entire project is heavily inspired by: 
		 * https://steemit.com/utopian-io/@will-ugo/creating-a-basic-web-browser-using-eclipse-ide
		 * 
		 * readHistory(), writeHistory(), addBookmark(), and populateBookmarks() are heavily inspired by: 
		 * read/write XML files in java: 
		 * https://www.javatpoint.com/how-to-read-xml-file-in-java
		 * https://mkyong.com/java/how-to-create-xml-file-in-java-dom/
		 * 
		 * Used in deleteHistory(): 
		 * ArrayList slicing: 
		 * https://stackoverflow.com/questions/1480663/how-can-i-slice-an-arraylist-out-of-an-arraylist-in-java
		 * 
		 * Used in restoreFile(): 
		 * Copying a file: 
		 * https://www.javatpoint.com/copy-content-data-from-one-file-to-another-in-java
		 * 
		 * */
		historyIndex = 0; 
		historyUrls = new ArrayList<String>(); 
		menuBar = new JMenuBar(); 
		restoreMenu = new JMenu("restore"); 
		clearMenu = new JMenu("clear"); 
		miscMenu = new JMenu("misc"); 
		screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
		main = new JFrame("James Webb Browser"); 
		window = new JEditorPane(); 
		addressBar = new JTextField("jw://home"); 
		searchButton = new JButton("🔎"); 
		bookmarkButton = new JButton("🔖"); 
		reloadButton = new JButton("🔄"); 
		backButton = new JButton("←"); 
		forwardButton = new JButton("→"); 
		bookmarkMenuButton = new JButton("bookmarks"); 
		historyMenuButton = new JButton("history"); 
		newWindowMenuButton = new JButton("new window"); 
		clearBookmarksButton  = new JButton("Clear Bookmarks"); 
		clearHistoryButton  = new JButton("Clear History"); 
		createBookmarkBackupButton = new JButton("Create bookmark backup"); 
		createHistoryBackupButton = new JButton("Create history backup"); 
		restoreHistoryButton = new JButton("restore history from backup"); 
		restoreBookmarksButton = new JButton("restore bookmarks from backup"); 
		
		miscMenu.getAccessibleContext().setAccessibleDescription("Miscellaneous functions");
		miscMenu.addSeparator(); 
		miscMenu.add(bookmarkMenuButton); 
		miscMenu.add(historyMenuButton); 
		miscMenu.add(newWindowMenuButton); 
		clearMenu.add(clearBookmarksButton); 
		clearMenu.add(clearHistoryButton); 
		restoreMenu.add(restoreHistoryButton); 
		restoreMenu.add(restoreBookmarksButton); 
		restoreMenu.add(createHistoryBackupButton); 
		restoreMenu.add(createBookmarkBackupButton); 
		bookmarkMenuButton.setMnemonic(KeyEvent.VK_B); 
		historyMenuButton.setMnemonic(KeyEvent.VK_H); 
		newWindowMenuButton.setMnemonic(KeyEvent.VK_N); 
		clearBookmarksButton.setMnemonic(KeyEvent.VK_B); 
		clearHistoryButton.setMnemonic(KeyEvent.VK_H); 
		restoreHistoryButton.setMnemonic(KeyEvent.VK_R); 
		restoreBookmarksButton.setMnemonic(KeyEvent.VK_E); 
		createHistoryBackupButton.setMnemonic(KeyEvent.VK_C); 
		createBookmarkBackupButton.setMnemonic(KeyEvent.VK_E); 
		miscMenu.setMnemonic(KeyEvent.VK_M); 
		clearMenu.setMnemonic(KeyEvent.VK_C); 
		restoreMenu.setMnemonic(KeyEvent.VK_R); 
		loadWebsite("jw://home", false); 
		updateFiles(); 
		restoreHistoryButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				if(restoreHistoryButton.getModel().isArmed()) 
				{
					restoreFile(new File("h_" + JOptionPane.showInputDialog(main,"Name of Backup") + ".xml"), new File("history.xml")); 
				}
			}
		});  
		restoreBookmarksButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				if(restoreBookmarksButton.getModel().isArmed())
				{
					restoreFile(new File("b_" + JOptionPane.showInputDialog(main,"Name of Backup") + ".xml"), new File("bookmarks.xml")); 
				}
			}
		}); 
		createHistoryBackupButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				if(createHistoryBackupButton.getModel().isArmed())
				{
					createBackup(JOptionPane.showInputDialog(main,"Name of Backup"), true); 
				}
			}
		});  
		createBookmarkBackupButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				if(createBookmarkBackupButton.getModel().isArmed())
				{
					createBackup(JOptionPane.showInputDialog(main,"Name of Backup"), false); 
				}
			}
		});  
		clearHistoryButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				if(clearHistoryButton.getModel().isArmed())
				{
					restoreFile(new File("history_original.xml"), new File("history.xml")); 
					updateFiles(); 
				}
			}
		}); 
		clearBookmarksButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				if(clearBookmarksButton.getModel().isArmed())
				{
					restoreFile(new File("bookmarks_original.xml"), new File("bookmarks.xml")); 
					updateFiles(); 
				}
			}
		}); 
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				if(searchButton.getModel().isArmed())
				{
					historyIndex++;
					loadWebsite(addressBar.getText(), false); 
					if(historyIndex < historyUrls.size() - 1)
						historyUrls = deleteHistory(historyIndex); 
				}
			}
		}); 
		bookmarkButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				if(bookmarkButton.getModel().isArmed())
				{
					addBookmark(JOptionPane.showInputDialog(main,"Name of Bookmark"), addressBar.getText()); 
					updateFiles(); 
				}
			}
		}); 
		reloadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				if(reloadButton.getModel().isArmed())
				{
					historyIndex++; 
					loadWebsite(addressBar.getText(), false); 
				}
			}
		}); 
		bookmarkMenuButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				if(bookmarkMenuButton.getModel().isArmed())
				{
					historyIndex++; 
					updateFiles(); 
					loadWebsite("jw://bookmarks", false); 
				}
			}
		}); 
		historyMenuButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				if(historyMenuButton.getModel().isArmed())
				{
					historyIndex++; 
					updateFiles(); 
					loadWebsite("jw://history", false); 
				}
			}
		}); 
		newWindowMenuButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				if(newWindowMenuButton.getModel().isArmed())
				{
					@SuppressWarnings("unused")
					jamess_web temp = new jamess_web(); 
				}
			}
		}); 
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				if(backButton.getModel().isArmed())
				{
					System.out.println(historyIndex);
					loadWebsite(historyUrls.get(--historyIndex), false); 
					historyUrls.remove(historyUrls.size() - 1); 
					updateFiles(); 
					System.out.println(historyUrls.toString() + " : " + historyIndex);
				}
			}
		}); 
		forwardButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				if(forwardButton.getModel().isArmed())
				{
					System.out.println(historyIndex);
					loadWebsite(historyUrls.get(++historyIndex), false); 
					historyUrls.remove(historyUrls.size() - 1); 
					updateFiles(); 
					System.out.println(historyUrls.toString() + " : " + historyIndex);
				}
			}
		}); 
		window.addHyperlinkListener( new HyperlinkListener() {
			@Override
			public void hyperlinkUpdate(HyperlinkEvent event) {
				if(event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					historyIndex++; 
					System.out.println(event.getURL().toString());
					loadWebsite(event.getURL().toString(), true);
				}
			}
		}); 
		addressBar.setPreferredSize(new Dimension(screenSize.width*30/40, 20)); 
		window.setPreferredSize(new Dimension(screenSize.width*49/50, screenSize.height*59/60)); 
		main.setLayout(new FlowLayout()); 
		main.setSize(screenSize.width, screenSize.height); 
		window.setEditable(false); 
		main.getRootPane().setDefaultButton(searchButton); 
		menuBar.add(miscMenu); 
		menuBar.add(clearMenu); 
		menuBar.add(restoreMenu); 
		main.setJMenuBar(menuBar); 
		main.add(backButton); 
		main.add(forwardButton); 
		main.add(reloadButton); 
		main.add(addressBar); 
		main.add(searchButton); 
		main.add(bookmarkButton); 
		main.add(new JScrollPane(window)); 
		main.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
		main.setVisible(true); 
	}
	public static void addBookmark(String n, String u) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); 
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new File("bookmarks.xml")); 
			doc.getDocumentElement().normalize();  
			Element rootElement = doc.getDocumentElement(); 
			Element bookmark = doc.createElement("bookmark"); 
			rootElement.appendChild(bookmark); 
			Element name = doc.createElement("name"); 
			name.setTextContent(n); 
			bookmark.appendChild(name); 
			Element url = doc.createElement("url"); 
			url.setTextContent(u); 
			bookmark.appendChild(url); 
			try(FileOutputStream output = new FileOutputStream("bookmarks.xml"))
			{
				TransformerFactory tf = TransformerFactory.newInstance(); 
				Transformer t = tf.newTransformer(); 
				t.setOutputProperty(OutputKeys.INDENT, "yes");
				DOMSource s = new DOMSource(doc); 
				StreamResult sr = new StreamResult(output); 
				t.transform(s, sr); 
			}
			catch(IOException e) {
				e.printStackTrace(); 
			} catch (TransformerConfigurationException e) {
				e.printStackTrace();
			} catch (TransformerException e) {
				e.printStackTrace();
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		updateFiles(); 
	}
	public static void populateBookmarks()
	{
		try {
			FileWriter fw = new FileWriter("bookmarks.html");
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("<!DOCTYPE html>\n<html>\n<body>\n");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); 
			DocumentBuilder db;
			Document doc = null;  
			try {
				db = dbf.newDocumentBuilder();
				doc = db.parse(new File("bookmarks.xml"));
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			doc.getDocumentElement().normalize(); 
			NodeList nl = doc.getElementsByTagName("bookmark"); 
			for(int i = 0; i < nl.getLength(); i++)
			{
				Node n = nl.item(i); 
				if(n.getNodeType() == Node.ELEMENT_NODE) {
					Element e = (Element) n; 
					try {
						bw.write("<a href=\"" + e.getElementsByTagName("url").item(0).getTextContent() + "\"><button>" + e.getElementsByTagName("name").item(0).getTextContent() + "</button></a>\n<hr/>\n");
					} catch (DOMException | IOException e1) {
						e1.printStackTrace();
					}
				}
			}
			try {
				bw.write("</body>\n</html>");
				bw.close(); 
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void loadWebsite(String address, Boolean hyperlink) {
		if(hyperlink)
		{
			addressBar.setText(address); 
			try {
				window.setPage(address);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(!address.substring(0, 8).equals("https://") && !address.substring(0, 7).equals("http://") && !address.substring(0, 5).equals("jw://") && !address.substring(0, 6).equals("jwx://") && !address.substring(0, 6).equals("file:/"))
			addressBar.setText("https://" + address); 
		try {
			if(address.substring(0, 5).equals("jw://"))
				window.setPage(new File(address.substring(5) + ".html").toURI().toURL());
			else if(address.substring(0, 6).equals("jwx://"))
				window.setPage(new File(address.substring(5) + ".xml").toURI().toURL());
			else
				window.setPage(addressBar.getText());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(main, "\"" + address +  "\"is an invalid URL", "Invalid URL", JOptionPane.WARNING_MESSAGE); 
			e.printStackTrace(); 
		}
		historyUrls.add(address); 
		writeHistory(address); 
		System.out.println(historyUrls.toString() + " : " + historyIndex + " : " + address);
	}
	public static void readHistory()
	{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); 
		DocumentBuilder db;
		Document doc = null;
		try {
			db = dbf.newDocumentBuilder();
			doc = db.parse(new File("bookmarks.xml"));
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		doc.getDocumentElement().normalize(); 
		NodeList nl = doc.getElementsByTagName("bookmark"); 
		for(int i = 0; i < nl.getLength(); i++)
		{
			Node n = nl.item(i); 
			if(n.getNodeType() == Node.ELEMENT_NODE) {
				Element e = (Element) n; 
				try {
					historyUrls.add(e.getElementsByTagName("url").item(0).getTextContent());
				} catch (DOMException e1) {
					e1.printStackTrace(); 
				}
			}
		}
	}
	public static void writeHistory(String u)
	{
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); 
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new File("history.xml")); 
			doc.getDocumentElement().normalize();  
			Element rootElement = doc.getDocumentElement(); 
			Element element = doc.createElement("element"); 
			rootElement.appendChild(element); 
			Element url = doc.createElement("url"); 
			url.setTextContent(u); 
			element.appendChild(url); 
			try(FileOutputStream output = new FileOutputStream("history.xml"))
			{
				TransformerFactory tf = TransformerFactory.newInstance(); 
				Transformer t = tf.newTransformer(); 
				t.setOutputProperty(OutputKeys.INDENT, "yes");
				DOMSource s = new DOMSource(doc); 
				StreamResult sr = new StreamResult(output); 
				t.transform(s, sr); 
			}
			catch(IOException e) {
				e.printStackTrace(); 
			} catch (TransformerConfigurationException e) {
				e.printStackTrace();
			} catch (TransformerException e) {
				e.printStackTrace();
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		updateFiles(); 
	}
	public static ArrayList<String> deleteHistory(int i) {
		System.out.println(i);
		historyIndex++; 
		return new ArrayList<String>(historyUrls.subList(0, i + 1)); 
	}
	public static void populateHistory()
	{
		try {
			FileWriter fw = new FileWriter("history.html");
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("<!DOCTYPE html>\n<html>\n<body>\n<h1>History</h1>");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); 
			DocumentBuilder db;
			Document doc = null;  
			try {
				db = dbf.newDocumentBuilder();
				doc = db.parse(new File("history.xml"));
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			doc.getDocumentElement().normalize(); 
			NodeList nl = doc.getElementsByTagName("element"); 
			for(int i = 0; i < nl.getLength(); i++)
			{
				Node n = nl.item(i); 
				if(n.getNodeType() == Node.ELEMENT_NODE) {
					Element e = (Element) n; 
					try {
						bw.write("<a href=\"" + e.getElementsByTagName("url").item(0).getTextContent() + "\"><button>" + e.getElementsByTagName("url").item(0).getTextContent() + "</button></a>\n<hr/>\n");
					} catch (DOMException | IOException e1) {
						e1.printStackTrace();
					}
				}
			}
			try {
				bw.write("</body>\n</html>");
				bw.close(); 
				fw.close(); 
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void restoreFile(File backup, File main) 
	{
		FileInputStream fis = null; 
		FileOutputStream fos = null; 
		try {
			fis = new FileInputStream(backup);
			fos = new FileOutputStream(main); 
			int i = 0; 
			while((i = fis.read()) != -1) 
				fos.write(i); 
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if (fis != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}  
			}  
			if (fos != null) {  
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}  
			}
		}
	}
	public static void updateFiles() {
		populateBookmarks(); 
		populateHistory(); 
		if(historyIndex <= 0)
			backButton.setEnabled(false); 
		else
			backButton.setEnabled(true); 
		if(historyIndex >= historyUrls.size() - 1)
			forwardButton.setEnabled(false); 
		else
			forwardButton.setEnabled(true); 
	}
	public static void createBackup(String name, boolean isHistory)
	{
		String n = (isHistory)?("h_" + name + ".xml"):("b_" + name + ".xml"); 
		File backup = new File(n); 
		try {
			if(backup.createNewFile())
				JOptionPane.showMessageDialog(main, "Backup created", "success!", JOptionPane.WARNING_MESSAGE); 
			else
				JOptionPane.showMessageDialog(main, "This name is taken. Pick another name. ", "File Already Exists", JOptionPane.WARNING_MESSAGE); 
			if(isHistory)
				restoreFile(new File("history.xml"), new File(n)); 
			else
				restoreFile(new File("bookmarks.xml"), new File(n)); 
		} catch (HeadlessException | IOException e) {
			e.printStackTrace(); 
		}
	}
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		jamess_web tab = new jamess_web(); 
	}
}