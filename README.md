# 📝 포트폴리오 프로젝트

## 설명

* 포트폴리오 프로젝트는 가장 기본 기능을 기반으로 본인만의 포트폴리오 프로젝트를 만들어보세요!
* 프로젝트 명칭은 변경해주시길 바랍니다!


* CRUD(Create, Read, Update, Delete) 데이터 조작 작업을 포함합니다.
* TEST코드 작성 및 단위 테스트를 진행합니다.
* 스프링 시큐리티를 활용합니다.


아래의 작업들은 필수로 구현해야하는 프로젝트 기능이며,

**본인만의 프로젝트 기능을 아래의 내용에 추가해서 작성해주세요!**

## 로그인

* [x]  스프링 시큐리티를 활용한 로그인 기능 구현
* [x]  테스트 코드 작성 및 통과

## 회원가입

* [x]  이메일 - 이메일 형식에 맞는지 검증
* [x]  휴대폰 번호 - 숫자와 하이폰으로 구성된 형식 검즘
* [x]  작성자 - 아이디 대소문자 및 한글 이름 검즘
* [x]  비밀번호 - 대소문자, 숫자 5개 이상, 특수문자 포함 2개 이상 검즘
* [x]  테스트 코드 작성 및 통과

## 게시글 등록

* [x]  제목 - 200글자 이하 제한
* [x]  내용 - 1000글자 이하 제한
* [x]  생성및 수정 시간 자동관리
* [x]  테스트 코드 작성 및 통과

## 게시글 수정

* [x]  생성일 기준 10일 이후 수정불가
* [x]  생성일 9일째 경고 알림(하루 후 수정 불가 알람)
* [x]  테스트 코드 작성 및 통과

## 게시글 목록조회

* [ ]  생성일 기준 내림차순 오름차순 정렬
* [ ]  title 기준 부분 검색 가능
* [ ]  title 이 없을 경우 cratedAt 정렬 기준으로 표시
* [ ]  deletedAt 기준 삭제된 게시글 제외
* [ ]  테스트 코드 작성 및 통과

## 게시글 상세보기

* [ ]  수정 가능일 현재 날짜 기준 계산 및 표시
* [ ]  테스트 코드 작성 및 통과

## 게시글 삭제

* [x]  Soft Delete 적용 deletedAt 사용하여 삭제처리
* [x]  Hard Delete 적용
* [x]  테스트 코드 작성 및 통과

## 📌 추가 기능구현

* [x] Swagger 적용

### 사용자 인증 및 권한 관리

* [x]  내 정보 조회하기(HandlerMethodArgumentResolver)
* [x]  JWT를 이용한 사용자 인증
* [x]  게시글 작성자만 수정 및 삭제 가능
* [x]  관리자는 모든 게시글 수정 및 삭제 가능

### 파일 업로드 기능

* [ ]  게시글에 이미지 첨부 기능 추가
* [ ]  이미지 파일 형식 크기 제한
* [ ]  이미지 업로드 시 S3와 같은 외부 스토리지 연동

### 댓글 기능

게시글에 댓글 추가 기능

* [ ]  댓글 작성, 수정, 삭제 (Soft Delete)
* [ ]  댓글 작성자는 본인의 댓글만 수정 및 삭제 가능

### 좋아요 및 조회수 기능

* [ ]  게시글 조회수 증가 기능
* [ ]  동일 사용자가 여러 번 조회 시 조회수 증가 방지

### 알림 기능

* [ ]  댓글 및 좋아요 시 알림 기능
* [ ]  수정 제한 경고 알림

# 🌟 더욱 더 추가하기

## Backend
### 코드 품질 향상

* [ ] SonarQube 적용 - 정적 코드 분석
* [ ] Design Patten, Architecture Patten 적용하기
* [ ] Multi Module 적용

### 테스트 - 부하/성능/스트레스 테스트

* [ ] Artillery/nGrinder/jMeter 중 택 1 적용 for TPS(Transaction Per Second) (Throughput 증가/Response Time 감소)

### Dev+Human 운영

* [ ] 슬랙 알림 적용(로그인 시, 에러 발생 시)

### 로깅

* [ ] 일반 로깅(println, 로그 < Warn)과 유의 로깅(로그 >= WARN) 2개 파일로 구분 관리

### 악의 이용 방지

* [ ] 어뷰징 방지 - 짧은 시간 내 같은 IP/API 요청이 있을 경우 해당 IP 블록
* [ ] 욕설이 포함된 글 제목/내용/댓글은 다른 텍스트로 대체되거나 해당 유저의 이용 차단 기능
* [ ] 댓글 막기 - 악성 댓글이 많이 달릴 경우 게시글 못 보게 임시 상태 처리 (Spring Batch+Quartz 통해 매일 오후 3시에 처리) -> 관리자가 직접 확인

### Caching Layer

* [ ] Internal/External Cache 적용 - Refresh Token 저장
* [ ] Refresh Token 재발급 api 추가

### APM - Application Performance Monitoring

* [ ] 프로메테우스 - 그라파나 대시보드 적용
* [ ] CPU, JVM, DB hikari Pool 모니터링
* [ ] 게시글, 댓글 수 모니터링 - Gauge/Counter/Timer

### Ops

* [ ] 도커로 말아서 배포
* [ ] 쿠버네티스로 말아서 배포
* [ ] Ingress Controller 추가
* [ ] GitOps + ArgoCD 적용

### CI/CD

* [ ] Github Actions/Jenkins/TeamCity 사용해서 CI/CD pipeline 구축

### 실시간 급상승 좋아요 글 띄우기

* [ ] 이벤트를 통해 알림

### 쪽지 기능

* [ ] 글 작성자에게 쪽지 보내는 기능 추가
* [ ] 답장 기능 추가

### 채팅 기능

* [ ] 채팅방 접속
* [ ] 채팅하기
* [ ] 차단하기
* [ ] 채팅방 나가기

### MyPage

* [ ] 내 게시글/댓글 모아보기
* [ ] AI 분석 - 게시글/댓글 좋아요/내용을 토대로 선호도 분석. 서빙레이어(Torch)에서 결과값을 받아오기

### BackOffice

* [ ] 관리자 통계 쿼리 구현

## Frontend
### 상태관리 라이브러리

* [ ] Client Side State - Zustand
* [ ] Server Side State - React-Query/Redux

### 디자인 시스템(UI)

* [ ] 직접만들기(scss) or UI 프레임워크<라이브러리> 적용(Material UI or PrimeReact) + TailwindCSS

### 모니터링 시스템

* [ ] UI/UX 모니터링 시스템 구축 - Sentry/Datadog/Rollbar
