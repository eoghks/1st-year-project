package repositories;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.checkerframework.checker.units.qual.A;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import dto.CloudDto;
import dto.SearchDto;
import entity.AccessKey;
import entity.QAccessKey;
import entity.QNamespace;

@Repository
public class QueryDslRepository {
	/******* Entity 조회 ************/
	public List<AccessKey> findAllAccessKey(EntityManager em){
		JPAQuery<AccessKey> query = new JPAQuery<>(em);
		QAccessKey a = new QAccessKey("a");
		return query
				.select(a)
				.from(a)
				.orderBy(a.hostName.asc()).fetch();
	}
	/*********** Fetch Join **************/
	public List<AccessKey> fetchAccessKey(EntityManager em){
		JPAQuery<AccessKey> query = new JPAQuery<>(em);
		QAccessKey a = new QAccessKey("a");
		QNamespace n = new QNamespace("n");
		return query
				.select(a)
				.from(a)
				.leftJoin(a.namespaces, n)
				.fetchJoin()
				.orderBy(a.hostName.asc()).fetch();
	}
	/********** DTO 조회 ***********/
	public List<CloudDto> findAllCloudDto(EntityManager em) {
		JPAQuery<AccessKey> query = new JPAQuery<>(em);
		QAccessKey a = new QAccessKey("a");
		QNamespace n = new QNamespace("n");
		return query
				.select(Projections.bean(CloudDto.class,
								a.hostName.as("host_name"),
								a.scanType.as("scan_type"),
								a.scanKey.as("scan_key"),
								a.accessKeyId.as("access_key_id"),
								a.region,
								n.count().as("namespaces")))
				.from(a)
				.leftJoin(a.namespaces, n)
				.groupBy(a.hostName)
				.orderBy(a.hostName.asc()).fetch();
	}
	/********* deleteALL ************/
	public void deleteAll(EntityManager em, ArrayList<String> host_names) {
		JPAQueryFactory query = new JPAQueryFactory(em);
		EntityTransaction et = em.getTransaction();
		QAccessKey a = new QAccessKey("a");
		try {
			et.begin();
			long count =query.delete(a).where(a.hostName.in(host_names))
			.execute();
			et.commit();
		}catch(Exception e){
			et.rollback();
		}
	}
	/********* FindOne ************/
	public AccessKey findOnePropertyDto(EntityManager em, String host) {
		JPAQuery<AccessKey> query = new JPAQuery<>(em);
		QAccessKey a = new QAccessKey("a");
		QNamespace n = new QNamespace("n");
		return query
				.select(a)
				.from(a)
				.where(a.hostName.eq(host))
				.fetchOne();
	}
	/********* Search ************/
	public List<CloudDto> SearchCloudDto(EntityManager em, SearchDto searchDto) throws Exception{
		JPAQuery<AccessKey> query = new JPAQuery<>(em);
		QAccessKey a = new QAccessKey("a");
		QNamespace n = new QNamespace("n");
		String type = searchDto.getType();
		String val = searchDto.getVal();
		return query
				.select(Projections.bean(CloudDto.class,
								a.hostName.as("host_name"),
								a.scanType.as("scan_type"),
								a.scanKey.as("scan_key"),
								a.accessKeyId.as("access_key_id"),
								a.region,
								n.count().as("namespaces")))
				.from(a)
				.leftJoin(a.namespaces, n)
				.groupBy(a.hostName)
				.where(hostLike(type, val, a), scanTypeEq(type, val, a), scanKeyLike(type, val, a), 
						accessKeyLike(type, val, a), regionLike(type, val, a))
				.having(namespaceEq(type, val, n))
				.orderBy(a.hostName.asc()).fetch();
	}

	private Predicate namespaceEq(String type, String val, QNamespace n) {
		if(!type.equals("namespaces")) {
			return null;
		}
		return n.count().eq((long) Integer.parseInt(val));
	}

	private Predicate regionLike(String type, String val, QAccessKey a) {
		if(!type.equals("region")) {
			return null;
		}
		return a.region.contains(val);
	}

	private Predicate accessKeyLike(String type, String val, QAccessKey a) {
		if(!type.equals("accessKeyId")) {
			return null;
		}
		return a.accessKeyId.contains(val);
	}

	private Predicate scanKeyLike(String type, String val, QAccessKey a) {
		if(!type.equals("scanKey")) {
			return null;
		}
		return a.scanKey.contains(val);
	}

	private Predicate scanTypeEq(String type, String val, QAccessKey a) throws Exception {
		if(!type.equals("scanType")) {
			return null;
		}
		return a.scanType.eq(Integer.parseInt(val));
	}

	private Predicate hostLike(String type, String val, QAccessKey a) {
		if(!type.equals("hostName")) {
			return null;
		}
		return a.hostName.contains(val);
	}

}
