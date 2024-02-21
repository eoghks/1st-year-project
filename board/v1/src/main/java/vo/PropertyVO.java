package vo;

import java.util.ArrayList;

public class PropertyVO {
	private String host_name;
	private int scan_type;
	private String scan_key;
	private String access_key_id;
	private String region;
	private int scan_interval;
	private ArrayList<String> namespaces;

	public String getHost_name() {
		return host_name;
	}

	public int getScan_type() {
		return scan_type;
	}

	public String getScan_key() {
		return scan_key;
	}

	public String getAccess_key_id() {
		return access_key_id;
	}

	public String getRegion() {
		return region;
	}

	public int getScan_interval() {
		return scan_interval;
	}

	public ArrayList<String> getNamespaces() {
		return namespaces;
	}

	public void setHost_name(String host_name) {
		this.host_name = host_name;
	}

	public void setScan_type(int scan_type) {
		this.scan_type = scan_type;
	}

	public void setScan_key(String scan_key) {
		this.scan_key = scan_key;
	}

	public void setAccess_key(String access_key_id) {
		this.access_key_id = access_key_id;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public void setScan_interval(int scan_interval) {
		this.scan_interval = scan_interval;
	}

	public void setNameapces(ArrayList<String> namespaces) {
		this.namespaces = namespaces;
	}

}
