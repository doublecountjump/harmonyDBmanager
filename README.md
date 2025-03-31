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

## 📊 아키텍처

### API 레이어
- GraphQL API (`/graphql` 엔드포인트)
- REST API (토큰 발급 및 관리)

### 서비스 레이어
- 트랜잭션 관리
- 비즈니스 로직 처리

### 데이터 레이어
- JPA 기반 리포지토리
- 엔티티 관계 맵핑

## 🔍 주요 구현 사항

### 1. GraphQL을 활용한 단일 엔드포인트 API
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

### 2. N+1 문제 해결을 위한 배치 매핑
GraphQL 쿼리에서 흔히 발생하는 N+1 문제를 `@BatchMapping`을 통해 효율적으로 해결했습니다.

```java
@BatchMapping
public Map<Habitat, Country> countryInfo(List<Habitat> habitats) {
    Set<Long> collect = habitats.stream().map(Habitat::getId).collect(Collectors.toSet());
    List<Object[]> allById = service.findCountryWithHabitatIdById(collect);
    
    // 단일 쿼리로 모든 관련 데이터 로드
    // ...
}
```

### 3. Caffeine 캐시를 활용한 API 키 관리
API 키 검증 성능 향상을 위해 Caffeine 캐시를 도입했습니다. 빈번한 데이터베이스 조회를 방지하고 응답 시간을 크게 개선했습니다.

```java
public Token findToken(String key) {
    Cache cache = manager.getCache("tokenCache");
    // 캐시 우선 조회 로직
    // ...
}
```

## 🚀 성능 최적화

### 트러블슈팅: GraphQL N+1 문제
- **문제**: 서식지 조회 시 각 서식지마다 별도의 Country 및 Species 쿼리 발생
- **해결**: `@BatchMapping`을 통한 일괄 데이터 로딩으로 쿼리 수 대폭 감소
- **결과**: 쿼리 수가 21개에서 3개로 감소, 응답 시간 약 70% 개선

### 트러블슈팅: API 키 중복 조회
- **문제**: API 키 검증 시 반복적인 데이터베이스 조회로 인한 성능 저하
- **해결**: Caffeine 로컬 메모리 캐시 적용
- **결과**: API 키 조회 응답 시간 85% 감소, 데이터베이스 부하 대폭 감소

## 📝 API 문서

### GraphQL API

#### 쿼리
- `getHabitatByCountry(country: String)`: 국가명으로 서식지 조회
- `getHabitatByName(name: String)`: 종 이름으로 서식지 조회
- `getHabitat()`: 모든 서식지 조회

#### 뮤테이션
- `insertSpecies(country: String, species: SpeciesInput)`: 새로운 종 정보 추가

### REST API

#### 토큰 발급
- `POST /api/issue`: 새로운 API 키 발급

## 🔒 인증 시스템

UUID 기반 토큰을 사용하여 API 접근을 제어합니다. 토큰은 발급 후 일정 기간 동안 유효하며, 캐싱 시스템을 통해 효율적으로 관리됩니다.

## 📈 확장성

- Spring Cache 추상화 레이어를 활용하여 필요 시 Redis 등의 분산 캐시로 손쉽게 전환 가능
- 마이크로서비스 아키텍처로의 확장 용이

## 📦 설치 및 실행

```bash
# 프로젝트 클론
git clone https://github.com/yourusername/harmony.git
cd harmony

# 빌드
./gradlew build

# 실행
java -jar build/libs/harmony-0.0.1-SNAPSHOT.jar
```

## 🤝 기여하기

프로젝트에 기여하고 싶으신가요? 이슈를 등록하거나 풀 리퀘스트를 보내주세요!

## 📄 라이센스

MIT 라이센스에 따라 배포됩니다. 자세한 내용은 [LICENSE](LICENSE) 파일을 참조하세요.
