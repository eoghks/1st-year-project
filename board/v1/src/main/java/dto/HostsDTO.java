package dto;

import java.util.ArrayList;

public class HostsDTO {
	private String table;
	private ArrayList<String> host_names;

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public ArrayList<String> getHost_names() {
		return host_names;
	}

	public void setHost_names(ArrayList<String> host_names) {
		this.host_names = host_names;
	}

}
