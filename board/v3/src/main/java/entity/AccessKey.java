package entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import dto.PropertyDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "AccessKey")
@Table(name = "cloud_account_key")
@Getter
@Builder
@EqualsAndHashCode
public class AccessKey {

	@Id
	@Column(name = "host_name")
	private String hostName;
	@Column(name = "scan_type")
	private int scanType;
	@Column(name = "scan_key")
	private String scanKey;
	@Column(name = "access_key_id")
	private String accessKeyId;
	private String region;
	@Column(name = "scan_interval")
	private int scanInterval;

	@Builder.Default
	@OneToMany(mappedBy = "accessKey", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Namespace> namespaces = new ArrayList<>();

	public AccessKey(String host_name, int scan_type, String scan_key, String access_key_id, String region,
			int scan_interval, List<Namespace> namespaces) {
		this.hostName = host_name;
		this.scanType = scan_type;
		this.scanKey = scan_key;
		this.accessKeyId = access_key_id;
		this.region = region;
		this.scanInterval = scan_interval;
		this.namespaces = namespaces;
	}
	
	public void changeAccessKey(PropertyDto propertyDto) {
		this.hostName = propertyDto.getHost_name();
		this.scanType = propertyDto.getScan_type();
		this.scanKey = propertyDto.getScan_key();
		this.accessKeyId = propertyDto.getAccess_key_id();
		this.region = propertyDto.getRegion();
		this.scanInterval = propertyDto.getScan_interval();
	}
	
	public void initNamspaces() {
		this.namespaces= new ArrayList<>();
	}
	public void changeNamesapces(List<Namespace> namespaces) {
		this.namespaces = namespaces;
	}
}
