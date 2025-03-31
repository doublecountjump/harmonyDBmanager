  # 🌿 Harmony - 생물종 정보 관리 시스템

생물종과 서식지 정보를 효율적으로 관리하고 GraphQL API를 통해 제공하는 백엔드 시스템입니다.

## 📋 프로젝트 소개

Harmony는 다양한 생물종과 그들의 서식지, 국가 정보를 관리하는 백엔드 시스템입니다. GraphQL API를 통해 데이터를 유연하게 쿼리할 수 있으며, API 키 기반 인증 시스템을 갖추고 있습니다.

## ✨ 주요 기능

- 국가별 서식지 및 생물종 정보 조회
- 종 이름 기반 서식지 정보 조회
- 생물종 데이터 추가
- API 키 발급 및 인증

## 🛠️ 기술 스택

### 백엔드
- **언어**: Java
- **프레임워크**: Spring Boot, Spring MVC
- **API**: GraphQL (Spring GraphQL)
- **데이터베이스 접근**: JPA, Spring Data JPA
- **캐싱**: Caffeine, Spring Cache
- **유틸리티**: Lombok, Stream API

## 📊 DB아키텍처

![image](https://github.com/user-attachments/assets/b3b9ace7-b39d-4967-93e1-9173dcf46e8d)


## 🔍 주요 구현 사항

### API 사용을 위한 토큰 발급
API 키가 포함된 토큰을 발급받기 위해 '/api/issue' 엔드포인트에 먼저 접속합니다. 발급받은 토큰의 key를 api_key 라는 이름의 헤더에 넣어 요청을 보내야 합니다.
```json
{
    "id": 11,
    "key": "19297a30-3a7d-422e-8a79-e13e5f9124a9",
    "expiredDate": "2025-03-31T17:58:49.0909698"
}
```

### GraphQL을 활용한 단일 엔드포인트 API
모든 데이터 쿼리 및 뮤테이션을 단일 `/graphql` 엔드포인트를 통해 처리합니다. 클라이언트는 필요한 데이터만 정확히 요청할 수 있어 오버페칭/언더페칭 문제를 해결합니다.

```graphql
# 예시 쿼리
query {
  getHabitatByCountry(country: "Korea") {
    id
    countryInfo {
      country
      continent
    }
    speciesInfo {
      scientific_name
      common_name
    }
  }
}
```

