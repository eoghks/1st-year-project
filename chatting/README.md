# 환경
JAVA8 이상

# 실행방법

1. TCPServer 프로그램 실행
     - 전역 변수 port에 사용할 port 설정(default: 9001)
     - 자바 Application 으로 실행
2. TCPClient 프로그램 실행
     - 전역 변수 ip에 TCPServer를 실행한 컴퓨터의 ip 설정(default: 211.241.150.169)
     - 전역 변수 port에 TCPServer에 설정한 port 설정(default: 9001)
     - 자바 Applicatio 으로 실행(새로운 사용자가 생길때 마다)
  
# 주요기능
1. 채팅방 접속
   - 채팅에 사용할 아이디 수신
   - 채팅방에 사용자가 없는 경우 제일 먼더 들어온 사용자를 방장으로 임명
3. 채팅 발송 및 수신
4. 접속 종료: 접속종료!!
     - 방장이 접속 종료 시 가장 최근에 들어온 유저에게 방장 권한 위임
5. 일반 명령어: "/"로 시작
     - 귓속말: /w {ID} {msg} ({}는 가변이란 의미)
     - 도움말: /help
     - 채팅창 멤버 보기: /member
6. 방장 명령어: "#"로 시작
     - 방장 도움말: #help
     - 방 최대 인원수 조회: #room
     - 방 최대 인원수 변경: #chMax n (n은 현재 채팅방 접속 유저 수 ~ 10명)
     - 강퇴: #out ID

# 에러 혹은 질문
https://www.notion.so/e71a2e709e31420e88effd16ee871cac?v=f64fd03f07a14ccf9924bb05a2324295&p=3c8ec275a52e4341b8c9ca53e9d8ca18&pm=c 에 댓글로 남겨주세요.
