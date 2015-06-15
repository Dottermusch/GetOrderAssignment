import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;



public class GetCustomerOrdersTotal
{

	public static void main(String[] args)
	{
		/*
		 * GetCustomerOrdersTotal takes an integer customer number as an input
		 * prints a header with customer information, lists the order dates,
		 * order numbers, and amounts, with a total at the bottom of the displayed
		 * customer.
		 * 
		 */
		
		// Declare the starting variables
		Scanner sc = new Scanner(System.in);
		Integer customer_number;
		String goAgain = "y";
		
		do
		{
			customer_number = getInt(sc, "Please input a customer integer number: ");
			
			Customer customer = getCustomer(customer_number);
			
			if (customer != null)
			{
			
				ArrayList<Order> orders = getOrders(customer_number);
				
				printInvoicesHeader(customer_number, customer);
				
				printOrders(orders);
				
				printInvoicesTrailer(customer);
				
				goAgain = getString(sc, "\nContinue? (y/n): ");
			}
			else
			{
				System.out.println("\nNo customer found for customer no: " + customer_number + "\n");
				goAgain = getString(sc, "\nContinue? (y/n): ");
			}
			
		} while (goAgain.equalsIgnoreCase("y"));
		
		System.out.println("\nApplication Ended.");

	}
	
	public static Customer getCustomer (Integer customer_number)
	{
		Customer customer = null;
		
		try
		{
			// step 1 Load the driver class
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// step 2 create the driver connection object
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "password");
			
			// step 3 - create the query string
			String query = "SELECT demo_customers.cust_first_name, demo_customers.cust_last_name , demo_customers.cust_street_address1, " + 
					 	   "demo_customers.cust_city, demo_customers.cust_state, SUM(order_total) " +
						   "FROM testuser2.demo_customers " +
						   "JOIN testuser2.demo_orders " +
						   "ON testuser2.demo_orders.customer_id = testuser2.demo_customers.customer_id " +
						   "WHERE demo_customers.customer_id = ? " +
						   "GROUP BY demo_customers.cust_first_name, demo_customers.cust_last_name , demo_customers.cust_street_address1, demo_customers.cust_city, demo_customers.cust_state"; 
			
			// step 4 create the prepared statement and supply the parameter value
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setInt(1, customer_number);
			
			
			// step 5 - execute the query with no parameters to maintain values of step 4
			ResultSet rs = pstmt.executeQuery();
			
			
			// step 6 - return what should be a single customer and populate the Customer object
			while (rs.next())
			{
				customer = new Customer();
				customer.setFirstName(rs.getString(1));
				customer.setLastName(rs.getString(2));
				customer.setAddress(rs.getString(3));
				customer.setCity(rs.getString(4));
				customer.setState(rs.getString(5));
				customer.setOrderTotal(rs.getBigDecimal(6));
			}
			
			con.close();
			
		} catch (Exception e)
		{
			System.out.println(e);
			return null;
		}
		
		return customer;
	}
	
	public static ArrayList<Order> getOrders(Integer customer_number)
	{
		ArrayList<Order> orders = new ArrayList<Order>();
		
		try
		{
			// step 1 Load the driver class
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// step 2 create the driver connection object
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "password");
			
			// step 3 - create the query string
			String query = "SELECT order_timeStamp, order_id,  order_total " +
						   "FROM testuser2.demo_orders " +
						   "JOIN testuser2.demo_customers " +
						   "ON testuser2.demo_orders.customer_id = testuser2.demo_customers.customer_id " +
						   "WHERE testuser2.demo_customers.customer_id = ?"; 
						
			// step 4 create the prepared statement and supply the parameter value
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setInt(1, customer_number);
			
			// step 5 - execute the query with no parameters to maintain values of step 4
			ResultSet rs = pstmt.executeQuery();
			
			// step 6 - return orders for the specified customer to Order Objects and 
			// add to orders ArrayList<Order>
			while (rs.next())
			{
				Order order = new Order();
				
				order.setOrderDate(rs.getDate(1));
				order.setOrderNumber(rs.getBigDecimal(2));
				order.setOrderTotal(rs.getBigDecimal(3));
				
				orders.add(order);
			}
			
			con.close();
			
		} catch (Exception e)
		{
			System.out.println(e);
			return null;
		}
		
		return orders;
	}
	
	public static void printInvoicesHeader(int customer_no , Customer customer)
	{
		DateFormat dateFormatObject = DateFormat.getDateInstance(DateFormat.MEDIUM);
		String dateString = "Date";
		String orderNoString = "Order No.";
		String amount = "Amount";
		Date now = new Date();
		
		// print the order date and number in header first line
		System.out.println("\n\nReport date: " + dateFormatObject.format(now) + 
				"      Cust. ID: " + customer_no + "\n");
		
		// print name and address info
		System.out.println(customer.getFirstName() + " " + customer.getLastName() + 
				"\n" + customer.getAddress() +
				"\n" + customer.getCity() + ", " + customer.getState() + "\n\n");
		
		String lineItemHeader = padStringToLength(dateString, 15) + 
								padStringToLength(orderNoString, 15) +
								amount;
		
		System.out.println(lineItemHeader);
		
		String dottedLineHeader = "-----------------------------------------";
		
		System.out.println(dottedLineHeader);
		
	}
	
	public static void printOrders(ArrayList<Order> orders)
	{
		NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
		DateFormat dateFormatObject = DateFormat.getDateInstance(DateFormat.MEDIUM);
		
		for (Order order : orders)
		{
			String lineString = padStringToLength(dateFormatObject.format(order.getOrderDate()), 15) +
								padStringToLength(order.getOrderNumber().toString(), 15) +
								currencyFormat.format(order.getOrderTotal());
			
			System.out.println(lineString);
					
		}
	}
	
	public static void printInvoicesTrailer(Customer customer)
	{
		NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
		
		String padSpace = padStringToLength(new String(), 32);
		
		System.out.println(padSpace + "--------");
		
		String tempTotal = "Total: " + currencyFormat.format(customer.getOrderTotal());
		
		int tempTotalLength = tempTotal.length();
		
		String totalLine = padStringToLength(new String(), (40 - tempTotalLength)) + 
				"Total: " + currencyFormat.format(customer.getOrderTotal());
		
		System.out.println(totalLine);
	}
	
	public static String padStringToLength(String inputString, int paddedLength)
	{
		StringBuilder sb = new StringBuilder(inputString);
		
		for (int i = inputString.length(); i <= paddedLength; i++)
		{
			sb.append(' ');
		}
		
		String paddedString = sb.toString();
		
		return paddedString;
	}
	
	public static String getString(Scanner sc, String prompt)
    {
        System.out.print(prompt);
        String s = sc.next();        // read the first string on the line
        sc.nextLine();               // discard any other data entered on the line
        return s;
    }
	
	public static int getInt(Scanner sc, String prompt)
    {
        boolean isValid = false;
        int i = 0;
        while (isValid == false)
        {
            System.out.print(prompt);
            if (sc.hasNextInt())
            {
                i = sc.nextInt();
                isValid = true;
            }
            else
            {
                System.out.println("Error! Invalid integer value. Try again.");
            }
            sc.nextLine();  // discard any other data entered on the line
        }
        return i;
    }

}
