package Trading_Engine;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import gui.Mainmenu;

public class Main {
	public static void main(String[]args){
		Result result = JUnitCore.runClasses(AllTestSuite.class);
		for(Failure failure : result.getFailures()){
			System.out.println(failure.toString());
		}
		
		myDatabase db = new myDatabase();
		new Mainmenu(db);
	}
}
