
package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

@Service

@Transactional(readOnly = true)
public class CloudServiceImpl implements CloudService {

	@Autowired
	private AccessKeyRepository accessKeyRepository;

	@Autowired
	private NamespaceRepository namespaceRepository; // @Autowired //
	// private TestR testRepository;

	public List<CloudDto> selectCloud(SearchDto searchDto) throws Exception {
		List<AccessKey> accessKeys = new ArrayList<AccessKey>();
		if (searchDto.getType() == null || searchDto.getVal() == "") {
			accessKeys = accessKeyRepository.findAllAccessKey();
		} else {
			accessKeys = searchCloud(searchDto);
		} // DTO변환 
		if(accessKeys==null)
			return null;
		return accessKeys.stream().map(a -> new CloudDto(a)).collect(Collectors.toList());
	}

	private List<AccessKey> searchCloud(SearchDto searchDto) throws Exception {
		String type = searchDto.getType();
		String val = searchDto.getVal();

		if (type.equals("host_name")) {
			return accessKeyRepository.findByHostNameContaining("%" + val + "%");
		} else if (type.equals("scan_type")) {
			return accessKeyRepository.findByScanType(Integer.parseInt(val));
		} else if (type.equals("scan_key")) {
			return accessKeyRepository.findByScanKeyContaining("%" + val + "%");
		} else if (type.equals("access_key_id")) {
			return accessKeyRepository.findByAccessKeyIdContaining("%" + val + "%");
		} else if (type.equals("region")) {
			return accessKeyRepository.findByRegionContaining("%" + val + "%");
		} else if (type.equals("namespaces")) {
			List<AccessKey> accessKeys = accessKeyRepository.findAllAccessKey();
			List<AccessKey> result = new ArrayList<>();
			for (AccessKey a : accessKeys) {
				if (a.getNamespaces().size() == Integer.parseInt(val)) {
					result.add(a);
				}
			}
			return result;
		} else {
			throw new Exception("잘못된 검색 타입");
		}
	}

	@Override
	public PropertyDto findCloud(String host) throws Exception {
		Optional<AccessKey> ac = accessKeyRepository.findById(host); 
		if(ac.isPresent() == true) { 
			AccessKey accessKey = ac.get();
			PropertyDto cloud = new PropertyDto(accessKey);
			return cloud;
		}else {
			return null;
		}
		
	}

	@Override

	@Transactional
	public void save(PropertyDto propertyDto) throws Exception {
		Optional<AccessKey> ac = accessKeyRepository.findById(propertyDto.getHost_name());
		if(ac.isPresent() == true) {
			AccessKey accessKey = ac.get();
			for (Namespace na : accessKey.getNamespaces()) {
				if (!propertyDto.getNamespaces().contains(na.getPk().getNamespace())) {
					namespaceRepository.delete(na);
				}
			}
		}
		List<Namespace> namespaces = new ArrayList<Namespace>();
		for (String namespace : propertyDto.getNamespaces()) {
			NamespacePK pk = NamespacePK.builder().host(propertyDto.getHost_name()).namespace(namespace).build();
			Namespace na = Namespace.builder().pk(pk).build();
			namespaces.add(na);
		}
		AccessKey accessKey = AccessKey.builder().hostName(propertyDto.getHost_name())
				.scanKey(propertyDto.getScan_key()).scanType(propertyDto.getScan_type())
				.accessKeyId(propertyDto.getAccess_key_id()).region(propertyDto.getRegion())
				.scanInterval(propertyDto.getScan_interval()).namespaces(namespaces).build();

		accessKeyRepository.save(accessKey);
	}

	@Override

	@Transactional
	public List<CloudDto> deleteCloud(ArrayList<String> host_names) throws Exception {
		accessKeyRepository.deleteByHostNameIn(host_names);
		return selectCloud(new SearchDto());
	} 
  }
