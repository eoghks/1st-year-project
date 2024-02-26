
package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dto.CloudDto;
import dto.CountNamespaceDto;
import dto.PropertyDto;
import dto.SearchDto;
import entity.AccessKey;
import entity.Namespace;
import entity.NamespacePK;
import repositories.AccessKeyRepository;
import repositories.NamespaceRepository;
import repositories.QueryDslRepository;

@Service

@Transactional(readOnly = true)
public class CloudServiceImpl3 implements CloudService3 {
	@Autowired
	EntityManagerFactory emf;
	
	@Autowired
	QueryDslRepository queryDslRepository;
	
	public List<CloudDto> selectCloud1(SearchDto searchDto) throws Exception {
		EntityManager em = emf.createEntityManager();
		List<CloudDto> cl = new ArrayList<>();
		try {
			List<AccessKey> ac = queryDslRepository.findAllAccessKey(em);
			cl=ac.stream().map(a -> new CloudDto(a)).collect(Collectors.toList());
		}finally {
			em.close();
		}
		return cl;
	}
	
	@Override
	public List<CloudDto> selectCloud2(SearchDto searchDTO) throws Exception {
		EntityManager em = emf.createEntityManager();
		List<CloudDto> cl = new ArrayList<>();
		try {
			List<AccessKey> ac = queryDslRepository.fetchAccessKey(em);
			cl= ac.stream().map(a -> new CloudDto(a)).collect(Collectors.toList());
		}finally {
			em.close();
		}
		return cl;
	}

	@Override
	public List<CloudDto> selectCloud3(SearchDto searchDTO) throws Exception {
		EntityManager em = emf.createEntityManager();
		List<CloudDto> cl = new ArrayList<>();
		try {
			cl = queryDslRepository.findAllCloudDto(em);
		}finally {
			em.close();
		}
		return cl;
	} 
	
	public List<CloudDto> searchCloud(SearchDto searchDto) throws Exception {
		EntityManager em = emf.createEntityManager();
		List<CloudDto> cl = new ArrayList<>();
		try {
			cl = queryDslRepository.SearchCloudDto(em, searchDto);
		}finally {
			em.close();
		}
		return cl;
	}

	@Override
	public PropertyDto findCloud(String host) throws Exception {
		EntityManager em = emf.createEntityManager();
		PropertyDto pr = new PropertyDto();
		try {
			AccessKey ac = queryDslRepository.findOnePropertyDto(em, host);
			pr = new PropertyDto(ac);
		}finally {
			em.close();
		}
		return pr;
	}

	@Override

	@Transactional
	public void save(PropertyDto propertyDto) throws Exception {
		EntityManager em = emf.createEntityManager();
		EntityTransaction et = em.getTransaction();
		try {
			et.begin();
			AccessKey ac =em.find(AccessKey.class, propertyDto.getHost_name());
			List<Namespace> namespaces = new ArrayList<Namespace>();
			// insert
			if(ac==null) {
				for (String namespace : propertyDto.getNamespaces()) {
					NamespacePK pk = NamespacePK.builder().host(propertyDto.getHost_name()).namespace(namespace).build();
					Namespace na = Namespace.builder().pk(pk).build();
					namespaces.add(na);
				}
				AccessKey newAccessKey = AccessKey.builder().hostName(propertyDto.getHost_name())
						.scanKey(propertyDto.getScan_key()).scanType(propertyDto.getScan_type())
						.accessKeyId(propertyDto.getAccess_key_id()).region(propertyDto.getRegion())
						.scanInterval(propertyDto.getScan_interval()).namespaces(namespaces).build();
				em.persist(newAccessKey);
			}else {
				ArrayList<String> newNamespaces =propertyDto.getNamespaces();
				List<Namespace> deleteNamespaces = new ArrayList<>();
				for(Namespace n: ac.getNamespaces()) {
					System.out.println(n.getPk().getNamespace());
					if(newNamespaces.contains(n.getPk().getNamespace())) {
						newNamespaces.remove(n.getPk().getNamespace());
					}else {
						em.remove(n);
						deleteNamespaces.add(n);
					}
				}
				for(Namespace n : deleteNamespaces) {
					ac.getNamespaces().remove(n);
				}
				for(String namespace : newNamespaces) {
					NamespacePK pk = NamespacePK.builder().host(propertyDto.getHost_name()).namespace(namespace).build();
					Namespace na = Namespace.builder().pk(pk).build();
					ac.getNamespaces().add(na);
				}
				ac.changeAccessKey(propertyDto);
			}
			et.commit();
			// update
		}catch(Exception e){
			et.rollback();
		}finally {
			em.close();
		}
	}

	@Override
	@Transactional
	public List<CloudDto> deleteCloud(ArrayList<String> host_names) throws Exception {
		EntityManager em = emf.createEntityManager();
		try {
			queryDslRepository.deleteAll(em, host_names);
		}finally {
			em.close();
		}
		return selectCloud3(new SearchDto());
	}

	
  }
