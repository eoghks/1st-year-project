package repositories;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import dto.CountNamespaceDto;
import entity.Namespace;
import entity.NamespacePK;

@Repository
public interface NamespaceRepository extends JpaRepository<Namespace, NamespacePK> {
	@Transactional
	void deleteByPkHostIn(ArrayList<String> host_names);

		//new  dto.CountNamespaceDto(n.pk.host, count(n)
	@Transactional
	@Query("SELECT n.pk.host, count(n) from Namespace n group by n.pk.host")
	List<?> countGroupByHostName();

}
