import java.util.Date;
import java.math.BigDecimal;

public class Order
{
	Date orderDate;
	BigDecimal orderNumber;
	BigDecimal orderTotal;
	
	public Order()
	{
		
	}

	public Date getOrderDate()
	{
		return orderDate;
	}

	public void setOrderDate(Date orderDate)
	{
		this.orderDate = orderDate;
	}

	public BigDecimal getOrderNumber()
	{
		return orderNumber;
	}

	public void setOrderNumber(BigDecimal orderNumber)
	{
		this.orderNumber = orderNumber;
	}

	public BigDecimal getOrderTotal()
	{
		return orderTotal;
	}

	public void setOrderTotal(BigDecimal orderTotal)
	{
		this.orderTotal = orderTotal;
	}
	
	
}
