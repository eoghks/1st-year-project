package dao;

import java.util.List;

//import javax.inject.Inject;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import dto.HostDTO;
import dto.HostsDTO;
import dto.PropertyDTO;
import dto.SearchDTO;
import vo.CloudVO;
import vo.NamespaceVO;
import vo.PropertyVO;

@Repository
public class CloudDAOImpl implements CloudDAO {

	private static final String Namespace = "mapper.cloudMapper";

	@Override
	public List<CloudVO> selectCloudList(SqlSession sqlSession, SearchDTO searchDTO) throws Exception {
		return sqlSession.selectList(Namespace + ".selectCloudList", searchDTO);
	}

	@Override
	public void delete(SqlSession sqlSession, HostsDTO hostsDto) throws Exception {
		sqlSession.delete(Namespace + ".delete", hostsDto);
	}

	@Override
	public void insertAccount(SqlSession sqlSession, PropertyDTO propertyDto) throws Exception {
		sqlSession.insert(Namespace + ".insertAccount", propertyDto);
	}

	@Override
	public void insertNamespace(SqlSession sqlSession, PropertyDTO propertyDto) throws Exception {
		sqlSession.insert(Namespace + ".insertNamespace", propertyDto);
	}

	@Override
	public PropertyVO selectPropertyAccountCloud(SqlSession sqlSession, HostDTO hostDTO) throws Exception {
		return sqlSession.selectOne(Namespace + ".selectPropertyAccountCloud", hostDTO);
	}

	@Override
	public List<NamespaceVO> selectPropertyNamespaceCloudList(SqlSession sqlSession, HostDTO hostDTO) throws Exception {
		return sqlSession.selectList(Namespace + ".selectPropertyNamespaceCloudList", hostDTO);
	}

	@Override
	public void updateAccountCloud(SqlSession sqlSession, PropertyDTO propertyDto) throws Exception {
		sqlSession.update(Namespace + ".updateAccountCloud", propertyDto);
	}

}
