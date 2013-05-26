package New;


public class orderObject {
	
	public orderObject( int aNumber, int aQuantity, double aPrice){
	
		id = aNumber;
		qauntity = aQuantity;
		price = aPrice;
	}
	
	
	public int getId(){
		return id;
	}
	public int getQauntity(){
		return qauntity;
	}
	public double getPrice(){
		return price;
	}
	
	private int id;
	private int qauntity;
	private double price;
}
