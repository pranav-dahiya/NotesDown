package notesDown;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.io.*;
import java.util.Timer;
import javax.swing.undo.*;

public class EditorWindow extends JFrame implements ActionListener{
	private EditorPane editorPane;
	private PreviewPane previewPane;
	private FileFactory fileFactory;
	private File documentFile;
	private File outputFile;
	private UndoManager undoMgr__; //Manages the undo operation

    enum UndoActionType {

        UNDO, REDO
    };
	public EditorWindow() {
		setLayout(new BorderLayout());
		 undoMgr__ = new UndoManager();
		JPanel documentPanel = new JPanel(new GridLayout(1,2));
		editorPane = new EditorPane();
		fileFactory = new FileFactory(editorPane.getDocument(), editorPane.getEditorKit());
		previewPane = new PreviewPane(fileFactory);
		documentPanel.add(new JScrollPane(editorPane));
		documentPanel.add(new JScrollPane(previewPane));
		add(documentPanel, BorderLayout.CENTER);
		
		JButton[] buttons = {new JButton("Open"), new JButton("Save"), new JButton("Save As"), 
				new JButton("Export"), new JButton("Export As")};
		
		JButton undoButton = new JButton("Undo");
        	undoButton.addActionListener(new UndoActionListener(UndoActionType.UNDO));//implements undo action
        	JButton redoButton = new JButton("Redo");
        	redoButton.addActionListener(new UndoActionListener(UndoActionType.REDO)); //implements redo action

		JPanel buttonPanel = new JPanel();
		add(buttonPanel, BorderLayout.NORTH);
		for (JButton button: buttons) {
			button.addActionListener(this);
			buttonPanel.add(button);
		}			
		buttonPanel.add(undoButton);
		buttonPanel.add(redoButton);
		Timer timer = new Timer();
		UpdateTask updateTask = new UpdateTask(previewPane);
		timer.scheduleAtFixedRate(updateTask, 100, 50);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		switch (arg0.getActionCommand()) {
		case "Open":
			documentFile = fileFactory.open();
			break;
		case "Save":
			documentFile = fileFactory.save(documentFile);
			break;
		case "Save As":
			documentFile = fileFactory.save(null);
			break;
		case "Export":
			outputFile = fileFactory.export(documentFile, outputFile, false);
			break;
		case "Export As":
			outputFile = fileFactory.export(documentFile, null, false);
		}	
	}
	
	    private class UndoEditListener implements UndoableEditListener {

        @Override
        public void undoableEditHappened(UndoableEditEvent e) {

            undoMgr__.addEdit(e.getEdit()); // remember the edit
        }
    }

    private class UndoActionListener implements ActionListener {

        private UndoActionType undoActionType;

        public UndoActionListener(UndoActionType type) {

            undoActionType = type;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            switch (undoActionType) {

                case UNDO:
                    if (!undoMgr__.canUndo()) {

                        editor__.requestFocusInWindow();
                        return; // no edits to undo
                    }

                    undoMgr__.undo(); //performs undo
                    break;

                case REDO:
                    if (!undoMgr__.canRedo()) { //checks if its possible undo or redo

                        editor__.requestFocusInWindow();
                        return; // no edits to redo
                    }

                    undoMgr__.redo(); 
            }

            editor__.requestFocusInWindow();
        }
    } // UndoActionListener
	
	public static void main(String[] args) {
		EditorWindow ew = new EditorWindow();
		ew.setVisible(true);
		ew.setSize(700, 500);
		ew.setMinimumSize(new Dimension(300, 100));
		ew.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
}
