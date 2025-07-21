# Approved_SpringBack
서울시 토지이용, 커뮤니티, 지도, 회원 관리 등 다양한 기능을 제공하는 Spring Boot 기반 비즈니스 Insight 백엔드 서비스입니다.
## 🔍 프로젝트 개요
Approved_SpringBack는 서울시의 다양한 공간 및 커뮤니티 데이터를 효과적으로 관리하고, 사용자와 관리자가 인사이트를 얻을 수 있도록 설계된 백엔드 서비스입니다. 지도 기반 정보 제공, 커뮤니티 피드백 분석, 관리자 통계 등 다양한 비즈니스 의사결정을 지원합니다.
## 🎯 주요 기능
* 지도 기반 토지이용 정보 API 제공
* 커뮤니티 게시글 및 피드백 관리
* 관리자 통계(카테고리/제공자별 데이터)
* 회원가입/로그인/마이페이지 등 사용자 관리
* 통합 검색 및 데이터 필터링
## 🛠 기술 스택
* **Backend:** Spring Boot, Java, Spring Security, JPA
* **Database:** MySQL (설정에 따라 변경 가능)
* **빌드/관리:** Maven
* **API 문서화:** Swagger (옵션)
* **파일 업로드:** 이미지 및 기타 파일 관리
## 📌 적용 대상
Approved_SpringBack는 다음과 같은 기업/기관에 적합합니다:
* ✅ 공간 데이터 기반 의사결정이 필요한 기관
* ✅ 커뮤니티 피드백을 빠르게 분석하고 싶은 기업
* ✅ 관리자 통계 및 트렌드 분석이 필요한 조직
## 📂 프로젝트 구조
```
Approved_SpringBack
│── src
│   ├── main
│   │   ├── java/com/example/backend
│   │   │   ├── controller      # API 엔드포인트
│   │   │   ├── service         # 비즈니스 로직
│   │   │   ├── repository      # 데이터 접근
│   │   │   ├── model           # 엔티티 클래스
│   │   │   ├── DTO             # 데이터 전송 객체
│   │   │   ├── config, util, security
│   │   ├── resources
│   │   │   ├── application.properties
│   │   │   ├── templates       # HTML 템플릿
│── uploads                     # 업로드 파일 저장소
│── test                        # 테스트 코드
│── pom.xml                     # Maven 설정
```
## 🚀 실행 방법
### 저장소 클론
```powershell
git clone https://github.com/whitepanguin/Approved_SpringBack.git
```
### 프로젝트 폴더 이동
```powershell
cd Approved_SpringBack
```
### 환경설정
`src/main/resources/application.properties`에서 DB 등 환경을 설정하세요.
### 빌드 및 실행
```powershell
.\mvnw spring-boot:run
```
### 브라우저 접속
`http://localhost:8080`
## 💡 트러블슈팅 & 해결 방법
* DB 연결 오류 → application.properties 설정 확인
* 파일 업로드 문제 → uploads 폴더 권한 및 경로 확인
* API 인증 문제 → SecurityConfig 및 토큰 설정 확인
## 👭 팀 & 기여자
Approved_SpringBack 팀
* 팀장 : 이재용
* 팀원 : 김동원, 김우주, 손단하, 엄동렬, 이훤
---
문의 및 기여는 [whitepanguin](https://github.com/whitepanguin) 또는 이슈 게시판을 통해 연락 바랍니다.
