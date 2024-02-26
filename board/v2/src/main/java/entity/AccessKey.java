package entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name ="AccessKey")
@Table(name ="cloud_account_key")
@Getter
@ToString(exclude="scanInterval")
@Builder
public class AccessKey {
	@Id
	@Column(name="host_name")
	private String hostName;
	@Column(name="scan_type")
	private int scanType;
	@Column(name="scan_key")
	private String scanKey;
	@Column(name="access_key_id")
	private String accessKeyId;
	private String region;
	@Column(name="scan_interval")
	private int scanInterval;
	
	public AccessKey(String host_name, int scan_type, String scan_key, String access_key_id, String region, int scan_interval){
		this.hostName= host_name;
		this.scanType=scan_type;
		this.scanKey=scan_key;
		this.accessKeyId=access_key_id;
		this.region=region;
		this.scanInterval=scan_interval;
	}
}
