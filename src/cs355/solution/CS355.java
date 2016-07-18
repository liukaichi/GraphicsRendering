package cs355.solution;

import cs355.GUIFunctions;
import cs355.controller.Controller;
import cs355.model.Model;
import cs355.view.View;

/**
 * This is the main class. The program starts here.
 * Make you add code below to initialize your model,
 * view, and controller and give them to the app.
 */
public class CS355 {


	/**
	 * This is where it starts.
	 * @param args = the command line arguments
	 */
	public static void main(String[] args) {

		Model model = new Model();
		// Fill in the parameters below with your controller and view.
		GUIFunctions.createCS355Frame(new Controller(model), new View(model));
		GUIFunctions.setVScrollBarMax(2048);
		GUIFunctions.setHScrollBarMax(2048);
		GUIFunctions.setHScrollBarKnob((int) (512));
		GUIFunctions.setVScrollBarKnob((int) (512));
		GUIFunctions.refresh();
	}
}
