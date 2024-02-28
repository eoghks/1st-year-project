package service;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import dao.API_DAO;
import model.Body;
import model.Header;
import model.Mapping;
import model.Save;
import model.Url;
import mybatis.MyBatisConnectionFactory;

public class MybatisService {
	private SqlSessionFactory sqlSessionFactory = MyBatisConnectionFactory.getSqlSessionFactory();
	API_DAO dao = new API_DAO();

	public Url getUrlInfo(String url) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		Url urlInfo = new Url();
		try {
			urlInfo = dao.selectUrlInfo(sqlSession, url);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqlSession.close();
		}
		return urlInfo;
	}

	List<Header> getHeader(int id) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		List<Header> header = new ArrayList<Header>();
		try {
			header = dao.selectHeader(sqlSession, id);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqlSession.close();
		}
		return header;
	}

	List<Body> getBody(int id) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		List<Body> body = new ArrayList<Body>();
		try {
			body = dao.selectBody(sqlSession, id);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqlSession.close();
		}
		return body;
	}

	public String getParent(Url urlInfo) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		String parent = "";
		try {
			parent = dao.selectRoot(sqlSession, urlInfo.getUrlId());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqlSession.close();
		}
		return parent;
	}

	public List<Mapping> getMaping(Url urlInfo) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		List<Mapping> mappingInfo = new ArrayList<Mapping>();
		try {
			mappingInfo = dao.selectMapping(sqlSession, urlInfo.getUrlId());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqlSession.close();
		}
		return mappingInfo;
	}
	
	public void save(Url urlInfo, ArrayList<Object> insertList, List<Mapping> mappingInfo) {
		ArrayList<String> colList = new ArrayList<String>();
		for(Mapping m : mappingInfo) {
			colList.add(m.getColName());
		}
		Save save = new Save();
		save.setId(urlInfo.getUrlId());
		save.setData(insertList);
		save.setCol(colList);
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			dao.save(sqlSession, save);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqlSession.commit();
			sqlSession.close();
		}
		return;
	}
}
