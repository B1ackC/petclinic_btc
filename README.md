# PetClinic 3-Tier

## 저장소 구조

| 경로 | 내용 |
|------|------|
| `pom.xml` | Maven 빌드 설정 (DB 프로파일 포함) |
| `src/main/java/` | 백엔드 코드 (web=REST 컨트롤러 · service · repository · model) |
| `src/main/resources/` | Spring 설정 · DB 스키마 (`spring/data-access.properties` 등) |
| `static/` | 프론트 화면(HTML/JS/CSS) |

## 호환성 (런타임)

| 항목 | 버전 | 비고 |
|------|------|------|
| Java 런타임 | JDK 8 / 11 | LTS 8·11 권장 |
| 서블릿 컨테이너 | Tomcat 9.x | Spring 5.3(javax) 기준, 9.x 권장 |
| DB | MySQL 8.0 | 드라이버 8.0.19 (`com.mysql.cj.jdbc.Driver`) |
| 패키징 | war | 외부 Tomcat, ROOT.war |

## 개발 스택 (참고)

| 항목 | 버전 |
|------|------|
| Spring Framework | 5.3.9 |
| Spring Data | 2021.0.1 |
| Hibernate (JPA) | 5.5.6.Final |
| Jackson | 2.12.4 |
| 빌드 | Maven 3.6+, compiler-plugin 3.8.1 (컴파일 타깃 Java 8) |

---

## WEB
파일 — `static/js/api.js` (라인 2)
- `API_BASE` : `/api`
- API 호스트/경로가 다르면 변경 (예: `http://<api-host>:8080/api`)

테스트
- 브라우저로 화면 접속 -> 목록 렌더링 확인

## WAS
설치, Maven 미설치 시 (택1):
```
choco install maven
scoop install maven
sudo dnf install -y maven
sudo apt install -y maven
brew install maven
```
빌드:
```
git clone https://github.com/B1ackC/petclinic_btc.git
cd petclinic_btc
mvn -P MySQL clean package
```
- 설치 명령 순서: Windows(Chocolatey) / Windows(Scoop) / Amazon Linux 2023 / Ubuntu·Debian / macOS
- 패키지 매니저가 없으면: maven.apache.org 에서 zip 다운로드 -> 압축 해제 -> `bin` 폴더를 PATH 에 추가 (JDK 8/11 선설치)
- Maven 3.6 이상 필요
- 결과물: `target/petclinic-api.war`

테스트 (`BASE` = 서버 또는 CloudFront 주소):
```
curl -s <BASE>/api/health
curl -s <BASE>/api/vets
curl -s <BASE>/api/owners
```
- `/api/health` -> `{"status":"UP"}`
- `/api/vets`·`/api/owners` -> 목록(JSON)
- JSON 응답: 헤더 `Accept: application/json` (브라우저 raw 접근 시 XML)
- Postman: 아래 엔드포인트로 컬렉션 구성

Endpoint:

| 메서드 | 경로 | 설명 |
|--------|------|------|
| GET | `/api/health` | 헬스체크 `{"status":"UP"}` |
| GET | `/api/vets` | 수의사 목록 |
| GET | `/api/pet-types` | 반려동물 종류 목록 |
| GET | `/api/owners` | 소유자 목록 (`?lastName=` 검색) |
| GET | `/api/owners/{id}` | 소유자 단건 |
| POST | `/api/owners` | 소유자 생성 |
| PUT | `/api/owners/{id}` | 소유자 수정 |
| POST | `/api/owners/{id}/pets` | 반려동물 추가 |
| PUT | `/api/owners/{id}/pets/{petId}` | 반려동물 수정 |
| POST | `/api/owners/{id}/pets/{petId}/visits` | 방문 기록 추가 |

Request Body (JSON)

소유자 — `POST /api/owners`, `PUT /api/owners/{id}`
```
{ "firstName": "길동", "lastName": "홍", "address": "서울시 강남구", "city": "서울", "telephone": "01012345678" }
```
반려동물 — `POST /api/owners/{id}/pets` (type.id 는 `/api/pet-types` 참고)
```
{ "name": "레오", "birthDate": "2024-01-01", "type": { "id": 1, "name": "cat" } }
```
방문 — `POST /api/owners/{id}/pets/{petId}/visits`
```
{ "date": "2026-07-06", "description": "정기 검진" }
```

## DB
빌드된 WAR 안의 설정 파일에서 수정

파일: `WEB-INF/classes/spring/data-access.properties`
- `jdbc.url` : localhost -> RDS 엔드포인트
- `jdbc.username` : root -> petclinic_app
- `jdbc.password` : petclinic -> 실제 비번
- `jdbc.initialize` : false(default, 데이터 보존) / true(상시 초기화)
