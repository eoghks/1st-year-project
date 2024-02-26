package service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dto.CloudDto;
import dto.PageDto;
import dto.PropertyDto;
import dto.SearchDto;
import entity.AccessKey;
import entity.Namespace;
import entity.NamespacePK;

@Service
@Transactional(readOnly = true)
public class CloudServiceImpl2 implements CloudService2 {
	@Autowired
	EntityManagerFactory emf;
	@PersistenceContext
	EntityManager em;

	private final int pageSize =2;
	/******************* TEST ******************/
	@Override
	@Transactional
	public List<CloudDto> selectCloud1() throws Exception {
		//Entity 프로텍트 vs Embedded 프로텍트 vs scalar
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<AccessKey> cq = cb.createQuery(AccessKey.class);
		Root<AccessKey> ac = cq.from(AccessKey.class); // from 별칭(AccessKey) 별칭
		cq.select(ac).distinct(true);
		TypedQuery<AccessKey> typeQuery = em.createQuery(cq);
		List<AccessKey> accessKeys = typeQuery.getResultList();
		if (accessKeys == null)
			return null;
		return accessKeys.stream().map(a -> new CloudDto(a)).collect(Collectors.toList());
	}

	/******************* TEST ******************/
	@Override
	@Transactional
	public List<CloudDto> selectCloud2()throws Exception {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<AccessKey> cq = cb.createQuery(AccessKey.class);
		Root<AccessKey> ac = cq.from(AccessKey.class); // from 별칭(AccessKey) 별칭
		ac.fetch("namespaces", JoinType.LEFT);
		cq.select(ac).distinct(true);
		TypedQuery<AccessKey> typeQuery = em.createQuery(cq);
		List<AccessKey> accessKeys = typeQuery.getResultList();
		if (accessKeys == null)
			return null;
		return accessKeys.stream().map(a -> new CloudDto(a)).collect(Collectors.toList());
	}

	/******************* TEST ******************/
	@Override
	@Transactional
	public List<CloudDto> selectCloud3() throws Exception{
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<CloudDto> cq = cb.createQuery(CloudDto.class);
		Root<AccessKey> ac = cq.from(AccessKey.class);
		Join<AccessKey, Namespace> na = ac.join("namespaces", JoinType.LEFT);
		cq.groupBy(ac.get("hostName"));
		cq.multiselect(ac.get("hostName"), ac.get("scanType"), ac.get("scanKey"), ac.get("accessKeyId")
				, ac.get("region"), cb.count(na)).distinct(true);
		cq.orderBy(cb.asc(ac.get("hostName")));
		TypedQuery<CloudDto> typeQuery = em.createQuery(cq);
		List<CloudDto> cloudDtoList = typeQuery.getResultList();
		return cloudDtoList;
	}

	public List<CloudDto> searchCloud(SearchDto searchDto) throws Exception {
		/*********** 기본 셋팅 ************/
		String type = searchDto.getType();
		String val = searchDto.getVal();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<CloudDto> cq = cb.createQuery(CloudDto.class);
		Root<AccessKey> ac = cq.from(AccessKey.class);
		Join<AccessKey, Namespace> na = ac.join("namespaces", JoinType.LEFT);
		cq.groupBy(ac.get("hostName"));
		cq.multiselect(ac.get("hostName"), ac.get("scanType"), ac.get("scanKey"), ac.get("accessKeyId")
				, ac.get("region"), cb.count(na)).distinct(true);
		cq.orderBy(cb.asc(ac.get("hostName")));
		/********* 동적 쿼리 **********/
		if(type!=null && val!= null && !val.equals("") ) {
			if (type.equals("hostName") || type.equals("scanKey") 
				|| type.equals("accessKeyId") || type.equals("region")) {
				cq.where(cb.like(ac.get(type), "%"+val+"%"));
			} else if (type.equals("scanType")) {
				try {
					cq.where(cb.equal(ac.get(type), Integer.parseInt(val)));
				}catch (NumberFormatException e ) {
					System.out.println(e.getMessage());
					return null;
				}
			} else if (type.equals("namespaces")) {
				try {
					cq.having(cb.equal(cb.count(na), Integer.parseInt(val)));
				}catch (NumberFormatException e ) {
					System.out.println(e.getMessage());
					return null;
				}
			} else {
				throw new Exception("잘못된 검색 타입");
			}
		}
		/*********** SQL 실행 및 List 반환 ***********/
		TypedQuery<CloudDto> typeQuery = em.createQuery(cq);
		List<CloudDto> cloudDtoList = typeQuery.getResultList();
		return cloudDtoList;
	}
	

	@Override
	public List<CloudDto> page(PageDto pageDto) throws Exception {
		/*********** 기본 셋팅 ************/
		String type = pageDto.getType();
		String val = pageDto.getVal();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<CloudDto> cq = cb.createQuery(CloudDto.class);
		Root<AccessKey> ac = cq.from(AccessKey.class);
		Join<AccessKey, Namespace> na = ac.join("namespaces", JoinType.LEFT);
		cq.groupBy(ac.get("hostName"));
		cq.multiselect(ac.get("hostName"), ac.get("scanType"), ac.get("scanKey"), ac.get("accessKeyId")
				, ac.get("region"), cb.count(na)).distinct(true);
		cq.orderBy(cb.asc(ac.get("hostName")));
		/********* 동적 쿼리 **********/
		//val='';
		if(type!=null && val!= null && !val.equals("") ) {
			if (type.equals("hostName") || type.equals("scanKey") 
				|| type.equals("accessKeyId") || type.equals("region")) {
				cq.where(cb.like(ac.get(type), "%"+val+"%"));
			} else if (type.equals("scanType")) {
				try {
					cq.where(cb.equal(ac.get(type), Integer.parseInt(val)));
				}catch (NumberFormatException e ) {
					System.out.println(e.getMessage());
					return null;
				}
			} else if (type.equals("namespaces")) {
				try {
					cq.having(cb.equal(cb.count(na), Integer.parseInt(val)));
				}catch (NumberFormatException e ) {
					System.out.println(e.getMessage());
					return null;
				}
			} else {
				throw new Exception("잘못된 검색 타입");
			}
		}
		/*********** SQL 실행 및 List 반환 ***********/
		TypedQuery<CloudDto> typeQuery = em.createQuery(cq).setFirstResult(pageDto.getPage()*pageSize).setMaxResults(pageSize);
		List<CloudDto> cloudDtoList = typeQuery.getResultList();
		return cloudDtoList;
	}

	@Override
	public PropertyDto findCloud1(String host) throws Exception {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<AccessKey> cq = cb.createQuery(AccessKey.class);
		Root<AccessKey> ac = cq.from(AccessKey.class);
		cq.select(ac)
			.where(cb.equal(ac.get("hostName"), host));
		TypedQuery<AccessKey> typeQuery = em.createQuery(cq);
		List<AccessKey> t = typeQuery.getResultList();
		if(t.size()==0) {
			return null;
		}
//		if(t.size()>1) {
//			throw new Exception("DB 오류");
//		} pk로 검색하기 때문에 검색 불가능.. 혹은 오류 발생
		return new PropertyDto(t.get(0));
	}
	
	@Override
	public PropertyDto findCloud2(String host) throws Exception {
		AccessKey accessKey =em.find(AccessKey.class, host);
		if(accessKey==null) {
			return null;
		}
		return  new PropertyDto(accessKey);
	}
	
	@Override
	public void save(PropertyDto propertyDto) throws Exception {
		EntityManager em1 = emf.createEntityManager();
		EntityTransaction et = em1.getTransaction();
		try {
			et.begin();
			List<Namespace> namespaces = new ArrayList<Namespace>();
			for (String namespace : propertyDto.getNamespaces()) {
				NamespacePK pk = NamespacePK.builder().host(propertyDto.getHost_name()).namespace(namespace).build();
				Namespace na = Namespace.builder().pk(pk).build();
				namespaces.add(na);
			}
			AccessKey accessKey =em1.find(AccessKey.class, propertyDto.getHost_name());
			if(accessKey==null) {
				AccessKey newAccessKey = AccessKey.builder().hostName(propertyDto.getHost_name())
						.scanKey(propertyDto.getScan_key()).scanType(propertyDto.getScan_type())
						.accessKeyId(propertyDto.getAccess_key_id()).region(propertyDto.getRegion())
						.scanInterval(propertyDto.getScan_interval()).namespaces(namespaces).build();
				em1.persist(newAccessKey);
			}else {
				for(Namespace na: accessKey.getNamespaces()) {
					em1.remove(na);
				}
				/*******Namespace Delete 실행********/
				accessKey.initNamspaces();
				/*******내용 DB에 기록********/
				em1.flush();
				/*******Namespace 추가********/
				accessKey.changeNamesapces(namespaces);
				accessKey.changeAccessKey(propertyDto);
			}
			et.commit();
		}catch(Exception e) {
			if(et.isActive())
				et.rollback();
		}finally {
			if(em1!=null)
				em1.close();
		}
	}
	
	@Override
	public void save2(PropertyDto propertyDto) throws Exception {
		EntityManager em1 = emf.createEntityManager();
		EntityTransaction et = em1.getTransaction();
		ArrayList<String> newNamespaces =propertyDto.getNamespaces();
		try {
			et.begin();
			
			AccessKey accessKey =em1.find(AccessKey.class, propertyDto.getHost_name());
			if(accessKey==null) {
				List<Namespace> namespaces = new ArrayList<Namespace>();
				for (String namespace : propertyDto.getNamespaces()) {
					NamespacePK pk = NamespacePK.builder().host(propertyDto.getHost_name()).namespace(namespace).build();
					Namespace na = Namespace.builder().pk(pk).build();
					namespaces.add(na);
				}
				AccessKey newAccessKey = AccessKey.builder().hostName(propertyDto.getHost_name())
						.scanKey(propertyDto.getScan_key()).scanType(propertyDto.getScan_type())
						.accessKeyId(propertyDto.getAccess_key_id()).region(propertyDto.getRegion())
						.scanInterval(propertyDto.getScan_interval()).namespaces(namespaces).build();
				em1.persist(newAccessKey);
			}else {
				List<Namespace> deleteNamespaces = new ArrayList<>();
				for(Namespace n: accessKey.getNamespaces()) {
					System.out.println(n.getPk().getNamespace());
					if(newNamespaces.contains(n.getPk().getNamespace())) {
						newNamespaces.remove(n.getPk().getNamespace());
					}else {
						em1.remove(n);
						deleteNamespaces.add(n);
					}
				}
				for(Namespace n : deleteNamespaces) {
					accessKey.getNamespaces().remove(n);
				}
				for(String namespace : newNamespaces) {
					NamespacePK pk = NamespacePK.builder().host(propertyDto.getHost_name()).namespace(namespace).build();
					Namespace na = Namespace.builder().pk(pk).build();
					accessKey.getNamespaces().add(na);
				}
				accessKey.changeAccessKey(propertyDto);
			}
			et.commit();
		}catch(Exception e) {
			if(et.isActive())
				et.rollback();
		}finally {
			if(em1!=null)
				em1.close();
		}
	}
	/*************delete문 자체를 만들어주기 ***************/
	@Override
	public List<CloudDto> deleteCloud1(ArrayList<String> host_names) throws Exception {
		EntityManager em1 = emf.createEntityManager();
		EntityTransaction et = em1.getTransaction();
		try {
			et.begin();
			
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaDelete<AccessKey> delete1 = cb.createCriteriaDelete(AccessKey.class);
			CriteriaDelete<Namespace> delete2 = cb.createCriteriaDelete(Namespace.class);
			Root ac = delete1.from(AccessKey.class);
			Root na = delete2.from(Namespace.class);
			In<String> inClause1 = cb.in(ac.get("hostName"));
			In<String> inClause2 = cb.in(na.get("pk").get("host"));
			for(String host: host_names) {
				inClause1.value(host);
				inClause2.value(host);
			}
			delete1.where(inClause1);
			delete2.where(inClause2);
			em1.createQuery(delete1).executeUpdate();
			em1.createQuery(delete2).executeUpdate();
			
			et.commit();
		} catch(Exception e) {
			if(et.isActive())
				et.rollback();
			System.out.println(e.getMessage());
		} finally {
			if(em1!=null)
				em1.close();
		}
		
		return selectCloud3();
	}
	/********* find -> delete (기존 방식)**********/
	@Override
	@Transactional
	public List<CloudDto> deleteCloud2(ArrayList<String> host_names) throws Exception {
		EntityManager em1 = emf.createEntityManager();
		EntityTransaction et = em1.getTransaction();
		try {
			et.begin();
			
			CriteriaBuilder cb = em1.getCriteriaBuilder();
			CriteriaQuery<AccessKey> cq = cb.createQuery(AccessKey.class);
			Root<AccessKey> ac = cq.from(AccessKey.class);
			In<String> inClause1 = cb.in(ac.get("hostName"));
			for(String host: host_names) {
				inClause1.value(host);
			}
			cq.select(ac).where(inClause1);
			TypedQuery<AccessKey> typeQuery = em1.createQuery(cq);
			List<AccessKey> t = typeQuery.getResultList();
			if(t.size() > 0) {
				for(AccessKey accessKey : t) {
					em1.remove(accessKey);// 영속성 컨텍스트와 DB에서 삭제
				}
			} 
			
			et.commit();
		}catch(Exception e) {
			if(et.isActive())
				et.rollback();
			System.out.println(e.getMessage());
		} finally {
			if(em1!=null)
				em1.close();
		}	
		return selectCloud3();
	}


}
