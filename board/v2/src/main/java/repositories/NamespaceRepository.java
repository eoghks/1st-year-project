package repositories;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import entity.Namespace;

@Repository
public interface NamespaceRepository extends JpaRepository<Namespace, Long> {
	@Transactional
	void deleteByPkHostIn(ArrayList<String> hosts);
	
	List<Namespace> findByPkHostContaining(String host_name);

	List<Namespace> findByPkHostIn(List<String> hostNames);
}
