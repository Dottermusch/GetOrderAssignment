
import java.sql.*;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class GetOrderItemsAndTotal
{
	
	public static void main(String[] args)
	{
		/*
		 * GetOrderItemsAndTotal takes the input of an integer order number
		 * and returns a summary order header with customer information, the
		 * order line items, and a summary total at the bottom of the line 
		 * items.  
		 * 
		 */
		
		// Declare starting variables
		
		Scanner sc = new Scanner(System.in);
		Integer orderNumber;
		String goAgain = "y";
		
		do
		{	
			// get the requested order number
				
			orderNumber = getInt(sc, "Please input an integer order number: ");
			
			Order order = getOrder(orderNumber);
			
			if (order != null)
			{
				// get the associated line items
				ArrayList<LineItem> lineItems = getLineItems(orderNumber);
				
				printOrderHeader(orderNumber, order);
				
				printLineItems(lineItems);
				
				printOrderTrailer(order);
				
				goAgain = getString(sc, "\nContinue? (y/n): ");
			}
			else
			{
				System.out.println("\nNo order found for order no: " + orderNumber + "\n");
				
				goAgain = getString(sc, "\nContinue? (y/n): ");
			}
			
		} while (goAgain.equalsIgnoreCase("y"));
		
		System.out.println("\nApplication Ended.");
		
	}
	
	public static Order getOrder(Integer order_number)
	{
		Order order = null;
		
		try
		{
			// step 1 Load the driver class
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// step 2 create the driver connection object
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "password");
			
			// step 3 - create the query string
			String query = "SELECT order_id,  order_timeStamp, order_total, cust_first_name, cust_last_name , cust_street_address1, cust_city, cust_state " +
						   "FROM testuser2.demo_orders " +
						   "JOIN testuser2.demo_customers " +
						   "ON testuser2.demo_orders.customer_id = testuser2.demo_customers.customer_id " +
						   "WHERE order_id = ?"; // + order_number.toString();
			
			// step 4 create the prepared statement and supply the parameter value
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setInt(1, order_number);
			
			// step 5 - execute the query with no parameters to maintain values of step 4
			ResultSet rs = pstmt.executeQuery();
				
			// return what should be a single order and populate the Order object
			while (rs.next())
			{
				order = new Order();
				
				order.setOrder_id(rs.getBigDecimal(1)) ;
				order.setOrder_date(rs.getDate(2));
				order.setOrder_total(rs.getBigDecimal(3));
				order.setFirstName(rs.getString(4));
				order.setLastName(rs.getString(5));
				order.setCustAddress(rs.getString(6));
				order.setCustCity(rs.getString(7));
				order.setCustState(rs.getString(8));
			}
			
			con.close();
			
		} catch (Exception e)
		{
			System.out.println(e);
			return null;
		}
		
		return order;
	}

	public static ArrayList<LineItem> getLineItems(Integer order_number)
	{
		ArrayList<LineItem> lineItems = new ArrayList<LineItem>();
		
		try
		{
			// step 1 Load the driver class
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// step 2 create the driver connection object
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "password");
			
			// step 3 - create the query string
						String query = "SELECT order_id, quantity, product_name, unit_price, quantity*unit_price " +
									   "FROM testuser2.demo_order_items " +
									   "JOIN testuser2.demo_product_info " +
									   "ON testuser2.demo_order_items.product_id = testuser2.demo_product_info.product_id " +
									   "WHERE order_id = ?"; 
						
			// step 4 create the prepared statement and supply the parameter value
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setInt(1, order_number);
			
			// step 5 - execute the query with no parameters to maintain values of step 4
			ResultSet rs = pstmt.executeQuery();
			
			// step 6 - return line items for the specified order to Order Objects and 
			// add to lineItems ArrayList<LineItem>
			while (rs.next())
			{
				LineItem lineItem = new LineItem();
				
				lineItem.setOrderID(rs.getBigDecimal(1));
				lineItem.setQuantity(rs.getBigDecimal(2));
				lineItem.setProductName(rs.getString(3));
				lineItem.setItemPrice(rs.getBigDecimal(4));
				lineItem.setItemTotalPrice(rs.getBigDecimal(5));
				
				lineItems.add(lineItem);
			}
			
			con.close();
			
		} catch (Exception e)
		{
			// e.printStackTrace();
			System.out.println(e);
			return null;
		}
		
		return lineItems;
	}
	
	public static void printOrderHeader(int orderNumber, Order order)
	{
		DateFormat dateFormatObject = DateFormat.getDateInstance(DateFormat.MEDIUM);
		String qty = "Qty";
		String description = "Description";
		String amount = "Amount";
		
		// print the order date and number in header first line
		System.out.println("\n\nOrder date: " + dateFormatObject.format(order.getOrder_date()) + 
				"                              Order ID: " + order.getOrder_id() + "\n");
		
		// print name and address info
		System.out.println(order.getFirstName() + " " + order.getLastName() + 
				"\n" + order.getCustAddress() +
				"\n" + order.getCustCity() + ", " + order.getCustState() + "\n\n");
		
		String lineItemHeader = padStringToLength(qty, 5) + 
								padStringToLength(description, 50) +
								amount;
		
		System.out.println(lineItemHeader);
		
		String dottedLineHeader = "----------------------------------------------------------------";
		
		System.out.println(dottedLineHeader);
		
	}
	
	public static void printLineItems(ArrayList<LineItem> lineItems)
	{
		NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
		
		for (LineItem line : lineItems)
		{
			String lineString = padStringToLength(line.getQuantity().toString(), 5) +
								padStringToLength(line.getProductName(), 50) +
								currencyFormat.format(line.getItemTotalPrice());
			
			System.out.println(lineString);
					
		}
	}
	
	public static void printOrderTrailer(Order order)
	{
		NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
		
		String padSpace = padStringToLength(new String(), 55);
		
		System.out.println(padSpace + "--------");
		
		String tempTotal = "Total: " + currencyFormat.format(order.getOrder_total());
		
		int tempTotalLength = tempTotal.length();
		
		String totalLine = padStringToLength(new String(), (63 - tempTotalLength)) + 
				"Total: " + currencyFormat.format(order.getOrder_total());
		
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
