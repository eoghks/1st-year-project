package dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import model.Body;
import model.Header;
import model.Mapping;
import model.Save;
import model.Url;

public class API_DAO {
	private static final String Facade = "Facade";

	public Url selectUrlInfo(SqlSession SqlSession, String url) throws Exception {
		return SqlSession.selectOne(Facade + ".selectUrl", url);
	}

	public List<Header> selectHeader(SqlSession SqlSession, int id) throws Exception {
		return SqlSession.selectList(Facade + ".selectHeader", id);
	}

	public List<Body> selectBody(SqlSession SqlSession, int id) throws Exception {
		return SqlSession.selectList(Facade + ".selectBody", id);
	}

	public String selectRoot(SqlSession sqlSession, int id) throws Exception {
		return sqlSession.selectOne(Facade + ".selectRoot", id);
	}

	public List<Mapping> selectMapping(SqlSession sqlSession, int id) {
		return sqlSession.selectList(Facade + ".selectMapping", id);
	}

	public int save(SqlSession sqlSession, Save save) {
		return sqlSession.insert(Facade + ".save", save);
	}
}
