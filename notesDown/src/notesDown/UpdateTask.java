package notesDown;

import java.util.TimerTask;

import javax.swing.JEditorPane;

public class UpdateTask extends TimerTask {
	private PreviewPane previewPane;
	
	public UpdateTask(PreviewPane previewPane) {
		this.previewPane = previewPane;
	}

	@Override
	public void run() {
		previewPane.update();
	}

}
