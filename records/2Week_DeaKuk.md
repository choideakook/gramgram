# 2 Week mission

## 필수 미션 []
- 호감표시 할 때 예외처리 케이스 3가지 이상을 추가로 처리 []
  1. 로그인을 하지않은경우 호감표시를 할 수 없다. []
  2. 현재 본인의 인스타 id 를 등록하지 않은 회원은 호감표시를 할 수 없다. []
  3. 현재 본인이 본인의 인스타 id 를 호감 상대로 등록할 수 없다. []
  4. 같은 인스타 회원에게 중복으로 호감표시를 할 수 없다. []
    - 같은 회원이더라도 다른 항목으로 호감표시하는 것은 가능하다. []
    - 이 때 새로운 객체가 생성되는 것이 아닌 기존 객체에 호감표시 사유만 변경이되어야 한다. []
  5. 호감표시 할 수 있는 횟수는 최대 10번이다. []
  

<br>

## 추가 미션 []
- naver 로그인 연동 []
  - Spring OAuth2 클라이언트를 사용한 소셜 로그인 구현 []
  - username 이 to string 형태가 아닌 id 값만 따로 추출된 형태여야 한다. []