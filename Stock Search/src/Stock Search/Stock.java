
//code generated by https://www.jsonschema2pojo.org/

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Stock {

	@SerializedName("data")
	@Expose
	private List<Datum> data;

	public List<Datum> getData() {
		return data;
	}
	
	public int getListSize() {
		return data.size();
	}
	
	public void appendData(Datum entry) {
		data.add(entry);
	}
	
	public void removeData(Datum entry) {
		data.remove(entry);
	}

	public void setData(List<Datum> data) {
		this.data = data;
	}
	
	public Datum getDatabyIndex(int index) {
		return data.get(index);
	}
}
