package de.htwg.fivewins.tui;

import java.util.Scanner;

import de.htwg.fivewins.controller.FiveWinsController;
import de.htwg.fivewins.field.AIAdapter;
import de.htwg.util.observer.IObserver;

public class TextUI implements IObserver{
	
	private FiveWinsController controller;
	private Scanner scanner;
	

	public TextUI(FiveWinsController controller){
		this.controller = controller;
		controller.addObserver(this);
		scanner = new Scanner (System.in);	
	}

	@Override
	public void update() {
		printTUI();
	}

	public boolean iterate() {
		boolean returnValue = false;
		AIAdapter npc = controller.getSecondPlayer();
		if(npc != null && npc.getWhichPlayer().equals(controller.getPlayerSign())) {
			returnValue = handleInputOrQuit(npc.getCommand());
		} else {
			returnValue = handleInputOrQuit(scanner.next());
		}
		return returnValue;
	}

	public void printTUI() {
		System.out.print("\n" + controller.getFieldString() + "\n");
		System.out.print(controller.getStatus() + "\n");
		System.out.print("\n");
		System.out.print("Please enter a command( q - quit, u - update, n - new, x,y - set cell(x,y)):\n"); 
	}
	
	public boolean handleInputOrQuit(String line) {
		boolean quit=false;
		if (line.equalsIgnoreCase("q")) {
			quit=true;
		}
		if (line.equalsIgnoreCase("u")) {
			//Do nothing, just redraw the updated grid
			update();
		}
		if (line.equalsIgnoreCase("n")) {
			//Restart game
			reset();
		}
		
		if (line.matches("[0-9]{1,2}?,[0-9]{1,2}?")){
			String[] numbers = line.split(",");
			int arg0 = Integer.parseInt(numbers[0]);
			int arg1 = Integer.parseInt(numbers[1]);
			boolean successfulFieldChange = controller.setValue(arg0, arg1, controller.getPlayerSign());
			if(successfulFieldChange) {
				String winnerSign = controller.winRequest();
				if(winnerSign.equals("X") || winnerSign.equals("Y")) {
					System.out.print("Der Gewinner ist " + winnerSign + "\n");
					quit = true;
				}
				controller.countTurn();
			}
			
		}

		return quit;
	}

	private void reset() {
		controller.reset();
		update();
	}
	

}
