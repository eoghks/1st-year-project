package dto;

import lombok.Data;

@Data
public class TestDto {
	private String hostName;
	private long count;
	
	public TestDto(String host, long count){
		this.hostName=host;
		this.count = count;
	}
}
