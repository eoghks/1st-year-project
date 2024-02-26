package entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "Namespace")
@Table(name = "cloud_namespace")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@EqualsAndHashCode
public class Namespace {
	@EmbeddedId
	private NamespacePK pk;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "host_name", insertable = false, updatable = false)
	private AccessKey accessKey;

	public Namespace(NamespacePK pk, AccessKey accessKey) {
		this.pk = pk;
		this.accessKey = accessKey;
	}
}
