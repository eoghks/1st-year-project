# 개요
Mybatis를 이용한 게시판 프로젝트

# 환경
createdDB 프로젝트를 이용하여 데이터베이스 사용자, 테이블 생성(default => db: board, user: board, password: board)
<만약 유저 정보나 데이터 베이스 이름이 다르면 WebContent/WEB-INF/spring/root-context.xml에서 변경)
gradle 7.5.1
Java 8이상
Apache 8.5
PostgreSQL 10
Serever path: /

# 기능
 - 호스트 검색(호스트 이름, Agent 설치 여부, 계정키, 암호키, 지역, 네임스페이스 수)
 - 호스트 추가 및 수정
 - 호스트 추가 및 수정에 대한 유효성 체크 기능
 - 호스트 삭제
 - 다국어
