
import java.math.BigDecimal;
import java.util.Date;



public class Order
{
	private BigDecimal order_id;
	private Date order_date;
	private BigDecimal order_total;
	private String firstName;
	private String lastName;
	private String custAddress;
	private String custCity;
	private String custState;
	
	public Order()
	{
		
	}

	public BigDecimal getOrder_id()
	{
		return order_id;
	}

	public void setOrder_id(BigDecimal order_id)
	{
		this.order_id = order_id;
	}

	public Date getOrder_date()
	{
		return order_date;
	}

	public void setOrder_date(Date order_date)
	{
		this.order_date = order_date;
	}

	public BigDecimal getOrder_total()
	{
		return order_total;
	}

	public void setOrder_total(BigDecimal order_total)
	{
		this.order_total = order_total;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public String getCustAddress()
	{
		return custAddress;
	}

	public void setCustAddress(String custAddress)
	{
		this.custAddress = custAddress;
	}

	public String getCustCity()
	{
		return custCity;
	}

	public void setCustCity(String custCity)
	{
		this.custCity = custCity;
	}

	public String getCustState()
	{
		return custState;
	}

	public void setCustState(String custState)
	{
		this.custState = custState;
	}
	
}
