package notesDown;

import java.io.*;
import javax.swing.*;
import javax.swing.text.*;

public class FileFactory {
	private final JFileChooser fileChooser = new JFileChooser();
	Document document;
	EditorKit editorKit;
	
	public FileFactory(Document document, EditorKit editorKit) {
		this.document = document;
		this.editorKit = editorKit;
	}
	
	public File chooseSaveFile() {
		File file = null;
		int returnVal = fileChooser.showSaveDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) 
			file = fileChooser.getSelectedFile();
		return file;
	}
	
	public File save(File file) {
		if (file == null)
			file = chooseSaveFile();
		try {
			editorKit.write(new FileOutputStream(file), document, 0, document.getLength());
		} catch (IOException | BadLocationException e) {
			e.printStackTrace();
		}
		return file;
	}
	
	public File export(File inputFile, File outputFile, Boolean suppress) {
		if (outputFile == null) 
			outputFile = chooseSaveFile();
		try {
			String command = "pandoc " + inputFile.getPath() + " -o " + outputFile.getPath();
			Process p = Runtime.getRuntime().exec(command);
			if (!suppress) { 
		        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
		        
		        String outputMessage = "";
		        String temp;
		        while ((temp = stdInput.readLine()) != null) {
		            outputMessage += temp;
		        }
		        if (!outputMessage.equals(""))
		        	JOptionPane.showMessageDialog(null, outputMessage);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return outputFile;
	}
	
	public File open() {
		File file = null;
		int returnVal = fileChooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			file = fileChooser.getSelectedFile();
			try {
				document.remove(0, document.getLength());
				editorKit.read(new FileReader(file), document, 0);
			} catch (IOException | BadLocationException e) {
				e.printStackTrace();
			}
		}
		return file;
	}
}
