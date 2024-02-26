# 환경
* JAVA8 이상
* Apach Tomacat Version 8.5
* PostgesSQL Version 10
* gradle 7.5.1

# 목적
호스트를 추가, 삭제, 수정 할 수 있는 프로그램(호스트는 AWS 서비스를 의미.)

# 각 프로젝트별 개요
 - createdDB: board 프로젝트에 필요한 DB 사용자, 테이블을 생성해주는 프로그램
 - v1: MyBatis를 이용하여 board 프로젝트 구현(호스트 검색, 추가, 삭제, 수정)
 - v2: Spring JPA에서 제공하는 쿼리 메소드를 이용하여 board 프로젝트 구현(호스트 검색, 삭제) 
 - v3: Sptring JPA의 QueryDSL를 이용하여 board 프로젝트 구현
# v1,v2,v3의 UI는 동일하나 백엔드 코드가 다름
