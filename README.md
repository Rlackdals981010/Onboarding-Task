# Onboarding-Task

# 요구사항

- Junit을 통한 테스트 코드를 작성할 수 있는가?
- Spring Security를 이용해서 Filter를 다룰 수 있는가?
- JWT와 구체적인 알고리즘을 이해하고 있는가?
- PR을 할 수 있는가?
- 코드 개선이 가능한가?
- EC2에 배포가 가능한가?

# 시나리오

## 주제

회원가입, 로그인 서비스를 구현하고 배포하세요.

## 세부 요구사항

1. 인증 인가 관리
    - Spring Security 적용
2. 기능 구현
    - 회원 가입
    - 로그인 : JWT를 통한 무상태
3. 테스트
    - JWT Unit 테스트 코드 작성
4. 검증
    - Swagger UI 이용
5. 배포
    - AWS EC2 환경에 배포
    - 코드 리뷰를 통해 코드 리팩토링 후 재배포

## 구현 시나리오

1. 기능 구현
   - 회원 가입 : `정보 저장을 위해 MySQL 사용`
2. 배포
   - `Github Action 사용`

# API 명세

| 기능    | Method | URL       | request                                                               | response                                                                                                    |
|-------|--------|-----------|-----------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------|
| 회원 가입 | `POST` | `/signup` | {	"username": "JIN HO",	"password": "12341234",	"nickname": "Mentos"} | {	"username": "JIN HO",	"nickname": "Mentos",	"authorities": [			{					"authorityName": "ROLE_USER"			}	]	} |
| 로그인   | `POST` | `/sign`   | {	"username": "JIN HO",	"password": "12341234"}                       | {	"token": "eKDIkdfjoakIdkfjpekdkcjdkoIOdjOKJDFOlLDKFJKL",}                                                 |

