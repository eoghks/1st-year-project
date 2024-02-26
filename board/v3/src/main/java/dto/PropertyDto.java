package dto;

import java.util.ArrayList;
import java.util.List;

import entity.AccessKey;
import entity.Namespace;
import lombok.Data;

@Data
public class PropertyDto {
	private String host_name;
	private int scan_type;
	private String scan_key;
	private String access_key_id;
	private String region;
	private int scan_interval;
	private ArrayList<String> namespaces;

	public PropertyDto() {
	}

	public PropertyDto(AccessKey ac) {
		this.host_name = ac.getHostName();
		this.scan_type = ac.getScanType();
		this.scan_key = ac.getScanKey();
		this.access_key_id = ac.getAccessKeyId();
		this.region = ac.getRegion();
		this.scan_interval = ac.getScanInterval();
		ArrayList<String> namespace = new ArrayList<String>();
		for (Namespace na : ac.getNamespaces()) {
			namespace.add(na.getPk().getNamespace());
		}
		this.namespaces = namespace;

	}
	
}
