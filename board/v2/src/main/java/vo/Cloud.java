package vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Cloud {
	private String host_name;
	private int scan_type;
	private String scan_key;
	private String access_key_id;
	private String region;
	private int namespaces;
}
