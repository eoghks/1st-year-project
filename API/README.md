# 개요
공공 API 데이터 조회하는 프로그램

# 환경
* doc의 crtab을 이용하여  테이블 생성
* 유저 정보나 데이터 베이스는 정보는 src/config/mybatis-config.xml에서 입력
* Java 8이상
* PostgreSQL 10

# 기능
 - 공공 API 데이터 조회
 - 공공 API 정보 저장 (v2)<미완>
 - 공공 API 데이터 정보 저장 (v2)<미완>

# 참고 사항
* REST API로 제한(GET/POST)
* 확장자는 XML/JSON으로 제한
* GET Method를 사용할 경우 body 정보 필요
* Post Method를 사용할 경우 header과 body 정보 필요
* extension : 0 = xml, 1 = json
* method: 0 = GET, 1 = POST

# Test Url
* http://infuser.odcloud.kr/oas/docs

# 에러 혹은 질문은 아래 페이지 댓글로 부탁드립니다.
https://www.notion.so/e71a2e709e31420e88effd16ee871cac?v=f64fd03f07a14ccf9924bb05a2324295&p=c681fb16ec4e44459598657fe1ba5aaf&pm=c


* v1: 지정된 공공 API url에서 데이터를 읽어오는 버전
* v2: 공공 API url을 입력받아 데이터를 읽어온 후 지정된 테이블에 데이터를 저장하는 버전(미완) - db에 API 관련 정보 저장(매번 입력 받지 않기 위해)
