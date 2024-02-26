package entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;



@Entity(name ="Namespace")
@Table(name ="cloud_namespace")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Namespace {
	@EmbeddedId
	private NamespacePK pk;
	
	public Namespace(NamespacePK pk) {
		this.pk=pk;
	}
}

