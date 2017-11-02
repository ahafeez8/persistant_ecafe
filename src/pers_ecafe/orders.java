package pers_ecafe;

public class orders {
	public static int o_id;
	public static int total;
	public static int del_type; //0 = delivered, 1 = self pickup
	public static String address;
	//public long punch_time;
	
	orders(){total = 0;
	//punch_time = System.currentTimeMillis();
	}
	public static void bill(items i)
	{
		total += i.price;
		//System.out.println("Your total bill is:  " + total);
	}
	public void get_time()
	{
		//System.out.println("Order punched at: " +punch_time + "seconds") ;
	}
}
