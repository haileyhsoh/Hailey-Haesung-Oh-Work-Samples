
//code generated by https://www.jsonschema2pojo.org/

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Stock2 {
	
	@SerializedName("data")
	@Expose
	private List<Datum2> data;

	public List<Datum2> getData() {
		return data;
	}
	
	public int getListSize() {
		return data.size();
	}
	
	public void appendData(Datum2 entry) {
		data.add(entry);
	}
	
	public void removeData(Datum2 entry) {
		data.remove(entry);
	}

	public void setData(List<Datum2> data) {
		this.data = data;
	}
	
	public Datum2 getDatabyIndex(int index) {
		return data.get(index);
	}
}
