package dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import dto.HostDTO;
import dto.HostsDTO;
import dto.PropertyDTO;
import dto.SearchDTO;
import vo.CloudVO;
import vo.NamespaceVO;
import vo.PropertyVO;;

public interface CloudDAO {
	public List<CloudVO> selectCloudList(SqlSession sqlSession, SearchDTO serachDTO) throws Exception;

	public void delete(SqlSession sqlSession, HostsDTO hostsDto) throws Exception;

	public PropertyVO selectPropertyAccountCloud(SqlSession sqlSession, HostDTO hostDTO) throws Exception;

	public List<NamespaceVO> selectPropertyNamespaceCloudList(SqlSession sqlSession, HostDTO hostDTO) throws Exception;

	public void updateAccountCloud(SqlSession sqlSession, PropertyDTO propertyDto) throws Exception;

	void insertAccount(SqlSession sqlSession, PropertyDTO propertyDto) throws Exception;

	void insertNamespace(SqlSession sqlSession, PropertyDTO propertyDto) throws Exception;
}
