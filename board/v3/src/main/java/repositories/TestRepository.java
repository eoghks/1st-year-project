package repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import entity.AccessKey;

@Repository
public class TestRepository implements TestR {
	@PersistenceContext
	EntityManager em;

	@Override
	@Transactional
	public List<AccessKey> findAll() {
		List<AccessKey> test = em.createQuery("SELECT a from AccessKey a", AccessKey.class).getResultList();
		return test;
	}
}
