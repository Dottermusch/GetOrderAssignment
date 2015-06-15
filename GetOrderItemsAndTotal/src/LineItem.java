
import java.math.BigDecimal;

public class LineItem
{
	private BigDecimal orderID;
	private BigDecimal quantity;
	private String productName;
	private BigDecimal itemPrice;
	private BigDecimal itemTotalPrice;
	
	public LineItem()
	{
		
	}

	public BigDecimal getOrderID()
	{
		return orderID;
	}

	public void setOrderID(BigDecimal orderID)
	{
		this.orderID = orderID;
	}

	public BigDecimal getQuantity()
	{
		return quantity;
	}

	public void setQuantity(BigDecimal quantity)
	{
		this.quantity = quantity;
	}

	public String getProductName()
	{
		return productName;
	}

	public void setProductName(String productDesc)
	{
		this.productName = productDesc;
	}

	public BigDecimal getItemPrice()
	{
		return itemPrice;
	}

	public void setItemPrice(BigDecimal itemPrice)
	{
		this.itemPrice = itemPrice;
	}

	public BigDecimal getItemTotalPrice()
	{
		return itemTotalPrice;
	}

	public void setItemTotalPrice(BigDecimal itemTotalPrice)
	{
		this.itemTotalPrice = itemTotalPrice;
	}
	
}
