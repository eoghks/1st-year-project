# 개요
* JPA를 이용한 게시판 프로젝트(default: JPA의 QueryDsl를 이용)
* service pakage의 CloudService는 JPA의 Query Method를 이용(주석 처리되어 있음)
* service pakage의 CloudService2는 JPA의 CriteriaQuery를 이용(주석 처리되어 있음)
* service pakage의 CloudService3는 JPA의 QueryDsl을 이용

# 환경
* createdDB 프로젝트를 이용하여 데이터베이스 사용자, 테이블 생성(default => db: board, user: board, password: board)
* <만약 유저 정보나 데이터 베이스 이름이 다르면 WebContent/WEB-INF/spring/root-context.xml에서 변경)
* gradle 7.5.1
* Java 8이상
* Apache 8.5
* PostgreSQL 10
* Serever path: /test

# 기능
 - 호스트 검색(호스트 이름, Agent 설치 여부, 계정키, 암호키, 지역, 네임스페이스 수)
 - 호스트 삭제
 - 호스트 추가 및 수정
 - 호스트 추가 및 수정 유효성 체크
 - 다국어

# 접속 URL
localhost:{port}/test

# 주의 사항
* lombok으로 getter, setter등의 메소드가 만들어지지 않는다면 아래와 같이 해주세요.
 1. Project and External Dependendcies 펼치기
 2. lombok-1.18.24.jar 우클릭 -> Run as -> Java Application
 3. 설치 진행
 4. eclipse 재실행

* quesyDsl의 Q Class를 찾지못하는경우
 1. 프로젝트에 마우스 우클릭을 해서 build Path -> Configure Build Path 창을 연다.
 2. 팝업창이 열리면, Java Build Path > Source 탭에 들어간 후 Add Folder를 클릭한다.
 3. Source Folter를 선택하는 창에서 아까 build를 해서 생성한 Q class가 있는 폴더를 선택한다. src/main/generated/queryDsl에 설정되어 있다. 만약 다른 경로를 설정했다면 설정된 경로로 설정해야 한다.
 4. buildPath 설정을 모두 마치면 아래와 깉으 프로젝트에 Q Class가 있는 'src/main/generated/queryDsl' 폴더가 import된 것을 볼 수 있다.
