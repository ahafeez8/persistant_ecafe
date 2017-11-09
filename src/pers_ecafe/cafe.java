package pers_ecafe;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;


public class cafe {
	public static void main(String args[]){  
			
		try{  
			//initializing connection
		Scanner in = new Scanner(System.in);
		Class.forName("com.mysql.jdbc.Driver");  
		Connection con=DriverManager.getConnection(  
		"jdbc:mysql://localhost/ecafe","test",""); 
		
		//prepared statement for insertion 
		String prep_stmt = "INSERT INTO item_list (s_num, i_name, i_type, price) VALUES (?,?,?,?)";
		Statement stmt=con.createStatement();  
		PreparedStatement stmt2 = con.prepareStatement(prep_stmt);
		
		//setting up cafe 
		System.out.println("Welcome to the E-Cafe");
		System.out.println("===========================================================================");
		System.out.println("S#   Name                     Type                      Price ");
		System.out.println("===========================================================================");
		
		//create items for menu
		items[] i = new items[16];
		i[0] = new items(0, "Curly Fries", "Appetizer", 250);
		i[1] = new items(1, "Nachos    ", "Appetizer", 150);
		i[2] = new items(2, "MacNcheese", "Appetizer", 350);
		i[3] = new items(3, "Garlic Bread", "Appetizer", 100);
		i[4] = new items(4, "Chicken Strips", "Appetizer", 550);
	
		i[5] = new items(5, "Hot & Sour    ", "Soups", 500);
		i[6] = new items(6, "Chicken Soup", "Soups", 450);
		
		i[7] = new items(7, "Rice & Spice", "Main course", 550);
		i[8] = new items(8, "Tarragon Chicken", "Main course", 750);
		i[9] = new items(9, "Morrocon Chicken","Main course", 700);
		i[10] = new items(10,"Quesadillas", "Main course", 550);
		i[11] = new items(11,"Beast Burger Beef","Main course", 800);
		i[12] = new items(12,"Fish Platter", "Main course", 850);
		
		i[13] = new items(13, "Fries              ", "Sides", 50);
		i[14] = new items(14, "Mashed Potato","Sides", 100);
		i[15] = new items(15, "Baked Potato", "Sides", 80);
		int x = 0;
		while(x < 16)
		{
			i[x++].get_items();
		}
		//insert info of all items to item list table
		x = 0;
		//Prepared statement exeuction for insertion
		while(x < i.length)
		{
		//stmt.executeUpdate("INSERT INTO item_list " + "VALUES ("+i[x].s_num+",'"+i[x].name+"', '"+i[x].type+"',"+ i[x].price+")");
		stmt2.setInt(1, i[x].s_num);
		stmt2.setString(2, i[x].name);
		stmt2.setString(3, i[x].type);
		stmt2.setInt(4, i[x].price);
		x++;
		}
		//increase number of customers from 1 to more
		customer[] cust = new customer[1];
		orders[] ord = new orders[1];
		for(int a = 0; a < 1; a++)
		{
		//cust info taken 
		System.out.println("Please Enter your name: ");
		cust[a].cname = in.next();
		System.out.println("Enter your address if you want delivery. Else type 'none': ");
		cust[a].address = in.next();
		
		//cust info added to database
		int cid = 1 + (int) (Math.random() * 100000);
		cust[a].id = cid;
		int oid = 1 + (int) (Math.random() * 100300);
		stmt.executeUpdate("INSERT INTO customer " + "VALUES ("+ cid +",'"+cust[a].cname+"', '"+cust[a].address+"')");
		
		//Create order
		System.out.println("Place your order");
		System.out.println("Select items by specifying item S#");
		//push data of orders to array
		ArrayList<Integer> picks = new ArrayList<Integer>(); //items picked by S#
		//set sentinals
		int c = 1;
 		int y = 0; 
 		
		while(c == 1)
		{			
			picks.add(in.nextInt());//type item number
			System.out.println("1 : continue\n0 : done."); 
			c = in.nextInt();		//check sentinal
		}
		
		ord[a].o_id = oid;
		for(int b = 0; b < picks.size(); b++)
			
			{
				ord[a].bill(i[picks.get(b)]); //calculate bill
			}
		
		
		System.out.println("\nYour total bill is:" + ord[a].total); //print total
		System.out.println("Specify your pick up type:\n S: Self, D: Delievered at your address.");
		//select pick up type
		String p_type = in.next();
		if(p_type.equals("S"))
		{	
			ord[a].del_type  = 1;
			
		}
		else if(p_type.equals("D"))
		{
			System.out.println("Please Specify a delivery address:");//if delievered, assign address
			ord[a].del_type = 0;
			ord[a].address = cust[a].address;		
			
		}
		//insert order details to database
		stmt.executeUpdate("INSERT INTO orders " + "VALUES ("+ ord[a].o_id +","+ord[a].total+","+ord[a].del_type+")");
		for(int b = 0; b < picks.size(); b++)
		{	
			stmt.executeUpdate("INSERT INTO order_items " + "VALUES ("+oid+","+cid+", '"+i[picks.get(b)].name+"',"+i[picks.get(b)].price+",'"+cust[a].address+"')");
		}
		/*System.out.println("Order Receipt:");
		
		ResultSet rs2 = stmt.executeQuery("Select o_id, cid, item_name, price, del_type, total from order_items as oi, orders as o where o.o_id like oi.o_id");
		System.out.println("Order ID: " + ord[a].o_id);
		System.out.println("Customer ID: " +cust[a].id);
		
		while(rs2.next())
		{
			if(rs2.getInt(1) == 0 )
				System.out.print("Order Type: Home delievery");
			else
				System.out.print("Order Type: Self Pickup");					
				System.out.println(rs2.getString(3)+"          "+rs2.getString(4));
				System.out.println("Your total bill: " + rs2.getInt(2));
		}*/
		}
		//Prepared statements for select Query : 1
		PreparedStatement stmt3 = con.prepareStatement("Select o_id from orders where del_type=?");
		stmt3.setInt(1, 0);
		ResultSet rs3 = stmt3.executeQuery();
		System.out.println("Orders to be delievered: ");
		while(rs3.next())
		{
			System.out.println(rs3.getInt(1));
		}
		
		//Prepared statements for select Query : 2
				PreparedStatement stmt4 = con.prepareStatement("Select i_name from item_list where i_type=?");
				stmt4.setString(1, "Appetizer");
				ResultSet rs4 = stmt4.executeQuery();
				System.out.println("Items present in appetizers are: ");
				while(rs4.next())
				{
					System.out.println(rs4.getString(1));
				}
		//callable statements
		CallableStatement cs = con.prepareCall("{call selected_items('541')}");
		ResultSet rs5 = cs.executeQuery();
		while(rs5.next())
		{
			System.out.println(x);
		}
		
		
		con.close();  
		}
		catch(Exception e){ System.out.println(e);}  
		} 
}
