package notesDown;

import java.io.*;
import java.net.*;
import javax.swing.*;
import javax.swing.text.*;


public class PreviewPane extends JEditorPane{
	private final FileFactory fileFactory;
	private final File tempFile;
	private final File previewFile;
	
	public PreviewPane (FileFactory fileFactory) {
		File dir = new File(System.getProperty("user.dir") + "/.NotesDown");
		if (!dir.exists()) {
			dir.mkdir();
		}
		tempFile = new File(dir.getPath() + "/tmp.md");
		previewFile = new File(dir.getPath() + "/preview.html");
		tempFile.deleteOnExit();
		previewFile.deleteOnExit();
		this.fileFactory = fileFactory;
		setContentType("text/html");
	}
	
	public void update() {
		fileFactory.save(tempFile);
		fileFactory.export(tempFile, previewFile, true);
		Document docs = getDocument();
		InputStream input;
		try {
			input = new URL("file:///" + previewFile.getPath()).openStream();
			setText("");
			getEditorKit().read(input, docs, docs.getLength());
		} catch (IOException | BadLocationException e) {
			e.printStackTrace();
		}
	}

	public File getTempFile() {
		return tempFile;
	}

	public File getPreviewFile() {
		return previewFile;
	}
}
