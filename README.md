# 졸업작품 생물의 다양성
### 한라대학교 컴퓨터공학과 황성준

생물의 다양성을 주제로 three.js 와 java spring, mysql을 사용해 프로젝트를 진행

위 코드들은 그 중 java spring 부분 백엔드 파트 코드임


![image](https://github.com/doublecountjump/harmonyDBmanager/assets/122294767/92e2e155-8892-4ece-861d-cbd59fbb1de7)

대략적인 구조는 다음과 같다.
주로 프론트엔드쪽 요청을 받아서 데이터를 찾아서 전달해주는 역할을 진행함

프로젝트에 구체적인 단계가 정해지지 않았을 때는 h2 데이터베이스를 이용하여 DB의 구조를 만듬

DB 데이터는 Redlist api를 이용하여 멸종위기종에 대한 데이터들을 받아 온 후 

파파고api, 우리말샘api, 구글 custom search API 를 사용함 

각각 국가 이름번역, 학명 번역, 동물 사진 크롤링 에 사용됨.

