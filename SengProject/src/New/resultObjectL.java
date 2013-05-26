package New;


public class resultObjectL {

	public resultObjectL( int averageTime, double aPercentage){
		time = averageTime;
		percentage = aPercentage;
	}
	
	public int getTime(){
		return time;
	}
	public double getPercentage(){
		return percentage;
	}
	
	private int time;
	private double percentage;
	
}
