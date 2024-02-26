package dto;

import java.util.Collection;

import lombok.Data;

@Data
public class CloudDto2 {
	private String host_name;
	private int scan_type;
	private String scan_key;
	private String access_key_id;
	private String region;
	private long namespaces;
	//, long count
	public CloudDto2(String hostName, int scanType, String scanKey, String accessKeyId, String region, long count) {
		this.host_name= hostName;
		this.scan_type= scanType;
		this.scan_key=scanKey;
		this.access_key_id=accessKeyId;
		this.region=region;
		this.namespaces = count;
	}
}
