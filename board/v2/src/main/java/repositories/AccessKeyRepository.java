package repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dto.CloudMapping;
import entity.AccessKey;

@Repository
public interface AccessKeyRepository extends JpaRepository<AccessKey, Long>{
	List<AccessKey> findByHostNameContaining(String host_name);

	List<CloudMapping> findByScanType(Integer type);

	List<AccessKey> findByHostNameIn(List<String> hostNames);

	List<CloudMapping> findByScanKeyContaining(String name);

	List<CloudMapping> findByAccessKeyIdContaining(String name);

	List<CloudMapping> findByRegionContaining(String name);
}
