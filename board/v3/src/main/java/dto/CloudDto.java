package dto;

import entity.AccessKey;
import lombok.Data;

@Data
public class CloudDto {
	private String host_name;
	private int scan_type;
	private String scan_key;
	private String access_key_id;
	private String region;
	private long namespaces;
	
	public CloudDto(AccessKey ac) {
		this.host_name= ac.getHostName();
		this.scan_type= ac.getScanType();
		this.scan_key=ac.getScanKey();
		this.access_key_id=ac.getAccessKeyId();
		this.region=ac.getRegion();
		this.namespaces=ac.getNamespaces().size();
	}
	
	public CloudDto(String hostName, int scanType, String scanKey, String accessKeyId, String region, long count) {
		this.host_name= hostName;
		this.scan_type= scanType;
		this.scan_key=scanKey;
		this.access_key_id=accessKeyId;
		this.region=region;
		this.namespaces = count;
	}
	
	public CloudDto() {
	}
}
