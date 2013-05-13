package Trading_Engine;

import gui.Mainmenu;

public class Main {
	public static void main(String[]args){
		myDatabase db = new myDatabase();
		new Mainmenu(db);
	}
}
