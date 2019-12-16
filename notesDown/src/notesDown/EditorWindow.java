package notesDown;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.io.*;
import java.util.Timer;

public class EditorWindow extends JFrame implements ActionListener {
	private EditorPane editorPane;
	private PreviewPane previewPane;
	private FileFactory fileFactory;
	private File documentFile;
	private File outputFile;
	
	public EditorWindow() {
		setLayout(new BorderLayout());
		setTitle("Untitled.md");
		JPanel documentPanel = new JPanel(new GridLayout(1, 2));
		editorPane = new EditorPane();
		fileFactory = new FileFactory(editorPane.getDocument(), editorPane.getEditorKit());
		previewPane = new PreviewPane(fileFactory);
		documentPanel.add(new JScrollPane(editorPane));
		JScrollPane previewScrollPane = new JScrollPane(previewPane);
		previewScrollPane.setBorder(new LineBorder(Color.WHITE));
		documentPanel.add(previewScrollPane);
		add(documentPanel, BorderLayout.CENTER);

		JButton[] buttons = { new JButton("Open"), new JButton("Save"), new JButton("Save As"), new JButton("Export"),
				new JButton("Export As") };
		
		JPanel buttonPanel = new JPanel();
		add(buttonPanel, BorderLayout.NORTH);
		for (JButton button : buttons) {
			button.addActionListener(this);
			buttonPanel.add(button);
		}
		Timer timer = new Timer();
		UpdateTask updateTask = new UpdateTask(previewPane);
		timer.scheduleAtFixedRate(updateTask, 1000, 1000);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		switch (arg0.getActionCommand()) {
		case "Open":
			documentFile = fileFactory.open();
			outputFile = null;
			setTitle(documentFile.getName());
			break;
		case "Save":
			documentFile = fileFactory.save(documentFile);
			setTitle(documentFile.getName());
			break;
		case "Save As":
			documentFile = fileFactory.save(null);
			setTitle(documentFile.getName());
			break;
		case "Export":
			outputFile = fileFactory.export(previewPane.getTempFile(), outputFile, false);
			break;
		case "Export As":
			outputFile = fileFactory.export(previewPane.getTempFile(), null, false);
		}
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
		} catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		} 

		EditorWindow ew = new EditorWindow();
		ew.setLocationRelativeTo(null);
		ew.setVisible(true);
		ew.setSize(700, 500);
		ew.setMinimumSize(new Dimension(300, 100));
		ew.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
}
