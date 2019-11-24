package notesDown;

import javax.swing.*;
import javax.swing.text.*;

public class EditorPane extends JEditorPane{
	private Document document;
	private EditorKit editorKit;
	
	public EditorPane() {
		editorKit = createDefaultEditorKit();
		document = getDocument();
	}

	
}
