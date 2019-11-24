package notesDown;

import java.io.*;
import java.net.URL;

import javax.swing.*;

public class PreviewPane extends JEditorPane{
	FileFactory fileFactory;
	File tempFile;
	File previewFile;
	
	public PreviewPane (FileFactory fileFactory) {
		File dir = new File(System.getProperty("user.dir") + "/.NotesDown");
		if (!dir.exists()) {
			dir.mkdir();
		}
		tempFile = new File(dir.getPath() + "/tmp.md");
		previewFile = new File(dir.getPath() + "/preview.html");
		this.fileFactory = fileFactory;
	}
	
	public void update() {
		fileFactory.save(tempFile);
		fileFactory.export(tempFile, previewFile, true);
		try {
			setPage(new URL("file:///" + previewFile.getPath()));
		} catch (IOException e) {
			e.printStackTrace(); 
		}
	}
}
