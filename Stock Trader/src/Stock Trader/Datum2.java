
//code generated by https://www.jsonschema2pojo.org/

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum2 {

	@SerializedName("name")
	@Expose
	private String name;
	@SerializedName("ticker")
	@Expose
	private String ticker;
	@SerializedName("startDate")
	@Expose
	private String startDate;
	@SerializedName("stockBrokers")
	@Expose
	private Integer stockBrokers;
	@SerializedName("description")
	@Expose
	private String description;
	@SerializedName("exchangeCode")
	@Expose
	private String exchangeCode;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getTicker() {
		return ticker;
	}
	
	public void setTicker(String ticker) {
		this.ticker = ticker;
	}
	
	public String getStartDate() {
		return startDate;
	}
	
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getExchangeCode() {
		return exchangeCode;
	}
	
	public void setExchangeCode(String exchangeCode) {
		this.exchangeCode = exchangeCode;
	}
	
	public Integer getStockBrokers() {
		return stockBrokers;
	}

	public void setStockBrokers(Integer stockBrokers) {
		this.stockBrokers = stockBrokers;
	}
	
	
}
