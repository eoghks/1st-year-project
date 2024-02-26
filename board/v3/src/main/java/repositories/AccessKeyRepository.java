package repositories;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import entity.AccessKey;

@Repository
//@Transactional(readOnly = true)
public interface AccessKeyRepository extends JpaRepository<AccessKey, String> {
	@Query("select distinct a from AccessKey a left join fetch a.namespaces")
	List<AccessKey> findAllAccessKey();
	
	@Transactional
	void deleteByHostNameIn(ArrayList<String> host_names);

	@Query("select distinct a from AccessKey a left join fetch a.namespaces where a.hostName like :val")
	List<AccessKey> findByHostNameContaining(@Param("val") String val);
	
	@Query("select distinct a from AccessKey a left join fetch a.namespaces where a.scanType = :val")
	List<AccessKey> findByScanType(@Param("val")int val);

	@Query("select distinct a from AccessKey a left join fetch a.namespaces where a.scanKey like :val")
	List<AccessKey> findByScanKeyContaining(@Param("val")String val);

	@Query("select distinct a from AccessKey a left join fetch a.namespaces where a.accessKeyId like :val")
	List<AccessKey> findByAccessKeyIdContaining(@Param("val")String val);

	@Query("select distinct a from AccessKey a left join fetch a.namespaces where a.region like :val")
	List<AccessKey> findByRegionContaining(@Param("val")String val);
	
	
	
	
}
