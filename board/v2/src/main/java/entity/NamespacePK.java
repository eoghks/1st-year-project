package entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Embeddable
@Getter
@Builder
@EqualsAndHashCode
public class NamespacePK implements Serializable{
	@Column(name="host_name")
	String host;
	String namespace;
	
	public NamespacePK() {
		
	}
	
	public NamespacePK(String host_name, String namespace) {
		this.host=host_name;
		this.namespace=namespace;
	}
	

}