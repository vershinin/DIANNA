package org.dianna.gui;

import org.dianna.core.Dianna;
import org.dianna.core.DiannaSettings;
import org.dianna.gui.debug.DebugWindow;

public class DiannaUI {
	private Dianna dianna;

	public static void main(String[] args) {
		DiannaSettings settings = new DiannaSettings(args);
		Dianna dianna = new Dianna(settings);
		//
		DebugWindow debug = new DebugWindow(dianna);
		debug.pack();
		debug.setVisible(true);

		// try {
		// dianna.connect();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}
}
