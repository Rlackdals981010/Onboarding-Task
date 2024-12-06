# Onboarding-Task

# 요구사항

- JUnit을 통한 테스트 코드를 작성할 수 있는가?
- Spring Security를 이용해 Filter를 다룰 수 있는가?
- JWT 및 관련 알고리즘을 이해하고 있는가?
- PR을 작성하고 리뷰 과정을 거칠 수 있는가?
- 코드 개선이 가능한가?
- AWS EC2에 배포가 가능한가?

# 주제

회원가입 및 로그인 서비스 구현 및 배포

## 세부 요구사항

1. 인증/인가 관리
   - Spring Security 적용
2. 기능 구현
   - 회원가입
   - 로그인 (JWT를 이용한 무상태 인증)
3. 테스트
   - JWT Unit 테스트 코드 작성
4. API 검증
   - Swagger UI 사용
5. 배포
   - AWS EC2에 배포
   - 코드 리뷰 후 개선 및 재배포

## 구현 시나리오

1. 설계
   1. .gitignore, 환경 변수 설정
   2. MySQL 연동
   3. Spring Security 설정
   4. JWT 설정
2. 기능 구현
   - 회원가입
     - Request: 사용자 정보 저장
     - Response: 사용자 정보 및 권한 반환
   - 로그인
     - Request: 사용자 인증 정보 입력
     - Response: JWT 토큰 발행
     - Swagger UI를 통해 API 검증
3. 테스트
   - JUnit을 사용해 JWT 토큰 발행 및 검증 테스트
4. 배포
   - AWS EC2를 이용해 Spring Boot 애플리케이션 배포
   - Swagger UI를 통해 배포 환경에서 API 검증
5. 개선
   - 코드 리뷰를 통해 피드백 반영
   - 리팩토링 후 AWS EC2에 재배포


# API 명세

| 기능    | Method | URL       | request                                                               | response                                                                                                    |
|-------|--------|-----------|-----------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------|
| 회원 가입 | `POST` | `/signup` | {	"username": "JIN HO",	"password": "12341234",	"nickname": "Mentos"} | {	"username": "JIN HO",	"nickname": "Mentos",	"authorities": [			{					"authorityName": "ROLE_USER"			}	]	} |
| 로그인   | `POST` | `/sign`   | {	"username": "JIN HO",	"password": "12341234"}                       | {	"token": "eKDIkdfjoakIdkfjpekdkcjdkoIOdjOKJDFOlLDKFJKL",}                                                 |

# 작업 진행 상황
- [x] JUnit을 이용한 JWT 테스트 코드 작성
- [x] Spring Security 설정 및 Filter 처리
- [x] JWT 및 관련 알고리즘 이해
- [x] 회원가입 기능 구현 및 테스트
      
![회원가입](https://github.com/user-attachments/assets/e36cea00-ea9e-4982-adc3-a762a3c84b33)

- [x] 로그인 기능 구현 및 테스트
      
![로그인](https://github.com/user-attachments/assets/27001714-2c93-4078-8251-8fce569e7e58)

- [x] Swagger UI 설정 및 API 검증
      
![스웨거 회원가입](https://github.com/user-attachments/assets/813ff74e-52a0-4255-ae9b-34f28cc69571)
![스웨거 로그인](https://github.com/user-attachments/assets/baff7bf2-1625-411f-b008-e3db93449cef)     

- [x] PR 작성 및 제출
- [x] 코드 리뷰를 통한 개선
- [x] AWS EC2 환경에 배포 [헬스체크링크](http://3.39.236.40:8080/health)
      
![스크린샷 2024-12-06 오후 6 37 09](https://github.com/user-attachments/assets/5149bf02-dd01-434c-b23c-caf52e986ce1)

