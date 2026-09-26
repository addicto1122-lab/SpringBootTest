# Spring Boot JPA 게시판 과제 정리

## 1. 전체 구조

이번 과제에서는 Spring Boot + Spring Data JPA + Thymeleaf를 이용해 게시판 기능을 구현했다.

기본 흐름은 다음과 같다.

Controller
→ Service
→ Repository
→ JPA
→ MySQL

Controller
- 사용자의 HTTP 요청을 받음
- Service 호출
- 조회한 데이터를 Model에 담아 Thymeleaf에 전달

Service
- 게시판의 실제 기능 처리
- 조회, 저장, 수정, 삭제, 조회수 증가, 페이징 등의 로직 처리

Repository
- DB 접근 담당
- JpaRepository를 상속하면 기본 CRUD 기능 사용 가능
- 메서드 이름을 규칙에 맞게 작성하면 JPA가 자동으로 쿼리를 생성함

Thymeleaf
- Controller에서 전달받은 데이터를 HTML에 출력
- th:text, th:each, th:href 등의 문법 사용


==================================================

## 2. JpaRepository

BoardRepository에서 JpaRepository를 상속했다.

public interface BoardRepository extends JpaRepository<Board, Long> {
}

JpaRepository<Board, Long>

Board
→ 관리할 Entity 클래스

Long
→ Board의 PK(bno) 타입

JpaRepository를 사용하면 아래와 같은 기본 기능을 사용할 수 있다.

findAll()
→ 전체 조회

findById(id)
→ PK로 한 건 조회

save(entity)
→ INSERT 또는 UPDATE

deleteById(id)
→ PK로 삭제

count()
→ 전체 개수 조회


==================================================

## 3. 조회수 증가

게시글 상세 페이지를 조회할 때 조회수를 1 증가시켰다.

public Board read(Long bno){
    Board board = boardRepository.findById(bno).orElse(null);

    if(board == null){
        return null;
    }

    board.setViewCnt(board.getViewCnt() + 1);

    return boardRepository.save(board);
}

동작 흐름

게시글 상세조회
→ DB에서 기존 Board 조회
→ 현재 조회수 + 1
→ save()
→ DB UPDATE

예)

기존 조회수
viewCnt = 5

상세 페이지 접속

viewCnt = 6


==================================================

## 4. 게시글 수정

클라이언트에서 전달받은 Board 객체를 그대로 save()하면 안 되는 경우가 있다.

수정 폼에서는 title, content 정도만 전달하기 때문에
조회수, 작성일 등의 값이 null 또는 초기값이 될 수 있다.

따라서 기존 게시글을 먼저 조회한 뒤
수정할 필드만 변경한다.

public Board modify(Board newBoard){

    Board board =
        boardRepository.findById(newBoard.getBno())
                       .orElse(null);

    if(board == null){
        return null;
    }

    board.setTitle(newBoard.getTitle());
    board.setContent(newBoard.getContent());

    return boardRepository.save(board);
}

흐름

DB에서 기존 게시글 조회
→ title 변경
→ content 변경
→ save()
→ UPDATE


==================================================

## 5. 페이징

게시글 전체를 한 번에 가져오는 대신
한 페이지에 일정 개수만 가져오도록 구현했다.

핵심 클래스

Pageable
PageRequest
Page
Sort


==================================================

## 6. Pageable

Pageable은

"몇 번째 페이지를,
한 페이지에 몇 개씩,
어떤 순서로 가져올 것인지"

정보를 담는 객체이다.

예)

Pageable pageable = PageRequest.of(
        page,
        10,
        Sort.by("bno").descending()
);

page
→ 현재 페이지 번호

10
→ 한 페이지에 10개

Sort.by("bno").descending()
→ bno 기준 내림차순
→ 최신 게시글부터 출력


페이지 번호는 0부터 시작한다.

page = 0
→ 첫 번째 페이지

page = 1
→ 두 번째 페이지

page = 2
→ 세 번째 페이지


SQL로 생각하면 비슷한 개념은 다음과 같다.

첫 페이지

LIMIT 10 OFFSET 0

두 번째 페이지

LIMIT 10 OFFSET 10


==================================================

## 7. PageRequest

PageRequest는 Pageable 객체를 실제로 생성하는 클래스이다.

PageRequest.of(page, size)

예)

PageRequest.of(0, 10)

→ 첫 번째 페이지
→ 10개 조회


정렬까지 포함하면

PageRequest.of(
    page,
    10,
    Sort.by("bno").descending()
)


==================================================

## 8. Page<T>

Pageable은
"어떻게 가져올지"

Page<T>는
"가져온 결과"

라고 생각하면 된다.

예)

Page<Board> list =
    boardRepository.findAll(pageable);


Page 객체에는 게시글뿐 아니라
페이징에 필요한 정보도 포함되어 있다.

list.getContent()
→ 현재 페이지 게시글

list.getTotalPages()
→ 전체 페이지 수

list.getTotalElements()
→ 전체 게시글 수

list.getNumber()
→ 현재 페이지 번호

list.isFirst()
→ 첫 페이지 여부

list.isLast()
→ 마지막 페이지 여부

list.hasNext()
→ 다음 페이지 존재 여부

list.hasPrevious()
→ 이전 페이지 존재 여부


정리

Pageable
→ 어떻게 조회할지

Page<Board>
→ 조회 결과 + 페이징 정보


==================================================

## 9. Service 페이징

public Page<Board> getList(int page){

    Pageable pageable = PageRequest.of(
            page,
            10,
            Sort.by("bno").descending()
    );

    return boardRepository.findAll(pageable);
}


==================================================

## 10. Controller 페이징

@GetMapping("/list")
public void getList(
        @RequestParam(defaultValue = "0") int page,
        Model model
){

    Page<Board> list =
        boardService.getList(page);

    model.addAttribute("list", list);
}

@RequestParam

URL의 Query Parameter를 Controller에서 받는다.

예)

http://localhost:8080/board/list?page=2

page 변수에는

2

가 들어간다.

defaultValue = "0"

page 값이 없으면 기본값으로 0을 사용한다.


==================================================

## 11. Thymeleaf 페이징

현재 Page 객체를 그대로 반복할 수 있다.

<tr th:each="board : ${list}">

페이지 번호 생성

<a th:each="i : ${#numbers.sequence(0, list.totalPages - 1)}"
   th:href="@{/board/list(page=${i})}"
   th:text="${i + 1}">
</a>

list.totalPages
→ 전체 페이지 수

list.number
→ 현재 페이지 번호

화면에는 사용자가 보기 편하도록

i + 1

을 출력한다.


현재 페이지 표시

th:classappend="${i == list.number} ? ' paging-active' : ''"

현재 페이지라면

paging-active

CSS 클래스를 추가한다.


이전 페이지

<a th:if="${!list.first}"
   th:href="@{/board/list(page=${list.number - 1})}">
    이전
</a>


다음 페이지

<a th:if="${!list.last}"
   th:href="@{/board/list(page=${list.number + 1})}">
    다음
</a>


==================================================

## 12. 검색 기능

검색 조건

제목
내용
제목 + 내용

을 선택할 수 있도록 구현했다.


Repository

제목 검색

Page<Board> findByTitleContaining(
        String keyword,
        Pageable pageable
);

내용 검색

Page<Board> findByContentContaining(
        String keyword,
        Pageable pageable
);

제목 또는 내용 검색

Page<Board> findByTitleContainingOrContentContaining(
        String title,
        String content,
        Pageable pageable
);


==================================================

## 13. Containing

Spring Data JPA에서

Containing

을 사용하면 SQL의 LIKE 검색과 비슷하게 동작한다.

findByTitleContaining("테스트")

SQL 개념

WHERE title LIKE '%테스트%'


==================================================

## 14. 검색 Service

public Page<Board> getList(
        int page,
        String type,
        String keyword
){

    Pageable pageable = PageRequest.of(
            page,
            10,
            Sort.by("bno").descending()
    );

    if(keyword == null || keyword.isBlank()){
        return boardRepository.findAll(pageable);
    }

    if(type.equals("title")){
        return boardRepository
                .findByTitleContaining(
                        keyword,
                        pageable
                );
    }

    if(type.equals("content")){
        return boardRepository
                .findByContentContaining(
                        keyword,
                        pageable
                );
    }

    return boardRepository
            .findByTitleContainingOrContentContaining(
                    keyword,
                    keyword,
                    pageable
            );
}


==================================================

## 15. isBlank()

keyword.isBlank()

문자열이 비어 있거나
공백만 있는지 확인한다.

예)

""
→ true

"   "
→ true

"test"
→ false


==================================================

## 16. 검색 Controller

@GetMapping("/list")
public void getList(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "title") String type,
        @RequestParam(defaultValue = "") String keyword,
        Model model
){

    Page<Board> list =
            boardService.getList(
                    page,
                    type,
                    keyword
            );

    model.addAttribute("list", list);
    model.addAttribute("type", type);
    model.addAttribute("keyword", keyword);
}


검색 요청 예

/board/list?type=title&keyword=테스트


==================================================

## 17. 검색 form

<form method="get"
      action="/board/list">

<select name="type">
    <option value="title">제목</option>
    <option value="content">내용</option>
    <option value="titleContent">제목+내용</option>
</select>

<input type="text"
       name="keyword">

<button type="submit">
    검색
</button>

</form>


GET 방식이므로 검색 조건이 URL에 들어간다.

예)

/board/list?type=title&keyword=Spring


==================================================

## 18. 검색 + 페이징 유지

검색 후 페이지를 이동할 때

keyword와 type도 같이 전달해야 한다.

그렇지 않으면 2페이지를 누르는 순간
검색 조건이 사라진다.

예)

th:href="@{/board/list(
    page=${i},
    type=${type},
    keyword=${keyword}
)}"


검색 결과 2페이지 요청

/board/list?page=1&type=title&keyword=테스트


==================================================

## 19. 작성자별 게시글 조회

Board와 User가 연관관계로 연결되어 있기 때문에
작성자의 id를 이용해 게시글을 조회할 수 있다.

Repository

Page<Board> findByUser_Id(
        Long userId,
        Pageable pageable
);


findByUser_Id

의 의미

Board.user.id

를 기준으로 검색한다.

SQL 개념

SELECT *
FROM board
WHERE user_id = ?;


==================================================

## 20. 작성자별 조회 Service

public Page<Board> getListByUser(
        Long userId,
        int page
){

    Pageable pageable = PageRequest.of(
            page,
            10,
            Sort.by("bno").descending()
    );

    return boardRepository
            .findByUser_Id(
                    userId,
                    pageable
            );
}


==================================================

## 21. 작성자 페이지 Controller

@GetMapping("/writer")
public String writer(
        long userId,
        @RequestParam(defaultValue = "0") int page,
        Model model
){

    Page<Board> list =
        boardService.getListByUser(
                userId,
                page
        );

    model.addAttribute("list", list);
    model.addAttribute("userId", userId);

    return "board/writer";
}


요청 예

/board/writer?userId=2

→ 2번 사용자가 작성한 게시글만 조회


==================================================

## 22. 작성자 링크

게시판 목록의 작성자 번호를 링크로 변경했다.

<td class="writer">

    <a th:href="@{/board/writer(
            userId=${board.user.id}
       )}"
       th:text="${board.user.id}">
    </a>

</td>

작성자 번호 클릭

→ 작성자별 게시글 페이지로 이동


==================================================

## 23. 작성자별 통계

작성자별로

게시글 수
총 조회수
평균 조회수

를 계산했다.

결과 예

작성자 | 게시글 수 | 총 조회수 | 평균 조회수
1     | 35       | 595       | 17.0
2     | 5        | 58        | 11.6
3     | 5        | 74        | 14.8


==================================================

## 24. Projection

통계처럼 Entity 전체가 아니라
일부 계산 결과만 받고 싶을 때
Projection 인터페이스를 사용할 수 있다.

public interface WriterStats {

    Long getUserId();

    Long getPostCount();

    Long getTotalViews();

    Double getAverageViews();
}

Repository의 SELECT 결과 이름과
getter 이름을 맞춰준다.


예)

AS userId
→ getUserId()

AS postCount
→ getPostCount()

AS totalViews
→ getTotalViews()

AS averageViews
→ getAverageViews()


==================================================

## 25. JPQL 통계 Query

@Query("""
    SELECT b.user.id AS userId,
           COUNT(b) AS postCount,
           SUM(b.viewCnt) AS totalViews,
           AVG(b.viewCnt) AS averageViews
    FROM Board b
    GROUP BY b.user.id
    ORDER BY b.user.id
""")
List<WriterStats> findWriterStats();


JPQL은 DB 테이블명을 직접 사용하는 것이 아니라
Entity와 Entity의 필드명을 사용한다.

SQL

FROM board

JPQL

FROM Board b


==================================================

## 26. GROUP BY

GROUP BY는 같은 값을 가진 데이터끼리 묶어서
통계를 계산할 때 사용한다.

이번 과제에서는

GROUP BY b.user.id

을 사용했다.

즉 작성자별로 게시글을 묶는다.


==================================================

## 27. COUNT

COUNT()

데이터의 개수를 계산한다.

COUNT(b)

→ 작성자가 작성한 게시글 개수


==================================================

## 28. SUM

SUM()

숫자값의 합계를 계산한다.

SUM(b.viewCnt)

→ 작성자의 모든 게시글 조회수 합계


==================================================

## 29. AVG

AVG()

숫자의 평균을 계산한다.

AVG(b.viewCnt)

→ 작성자 게시글의 평균 조회수


==================================================

## 30. 통계 Service

public List<WriterStats> getWriterStats(){
    return boardRepository.findWriterStats();
}


==================================================

## 31. 통계 Controller

@GetMapping("/stats")
public void stats(Model model){

    List<WriterStats> stats =
        boardService.getWriterStats();

    model.addAttribute(
            "stats",
            stats
    );
}


요청

/board/stats

Thymeleaf

templates/board/stats.html


==================================================

## 32. Thymeleaf th:each

리스트 또는 Page를 반복 출력할 때 사용한다.

<tr th:each="board : ${list}">

의미

list에서 Board를 하나씩 꺼내서
board 변수에 저장


통계에서는

<tr th:each="stat : ${stats}">

사용


==================================================

## 33. th:text

HTML 요소 내부의 텍스트를 출력한다.

예)

<td th:text="${board.title}"></td>

board.title 값을 화면에 출력


==================================================

## 34. th:href

동적으로 링크를 생성한다.

예)

<a th:href="@{/board/read(
        bno=${board.bno}
   )}">

게시글 번호를 Query Parameter로 전달


결과 예

/board/read?bno=3


==================================================

## 35. th:selected

select의 특정 option을 선택 상태로 유지한다.

<option value="title"
        th:selected="${type == 'title'}">

검색 후에도 사용자가 선택했던 검색 조건이 유지된다.


==================================================

## 36. th:if

조건이 true일 때만 HTML을 출력한다.

예)

th:if="${!list.first}"

첫 페이지가 아닐 때만
'이전' 버튼 출력


==================================================

## 37. th:classappend

기존 CSS class에 조건에 따라
클래스를 추가한다.

th:classappend="
    ${i == list.number}
    ? ' paging-active'
    : ''
"

현재 페이지 번호라면

paging-active

클래스 추가


==================================================

## 38. #numbers.sequence

Thymeleaf에서 연속된 숫자를 생성한다.

#numbers.sequence(
    0,
    list.totalPages - 1
)

예)

전체 페이지가 4개라면

0, 1, 2, 3

생성

화면에는

i + 1

을 사용하여

1, 2, 3, 4

로 출력한다.


==================================================

## 39. #numbers.formatDecimal

숫자의 소수점 자릿수를 정리할 때 사용한다.

예)

${#numbers.formatDecimal(
    stat.averageViews,
    1,
    1
)}

평균 조회수

11.66666...

→

11.7


==================================================

## 40. <i> 태그와 Font Awesome

<i> 태그는 원래 기울임 글자를 표현하는 HTML 태그이다.

하지만 이번 프로젝트에서는
Font Awesome 아이콘을 표시하는 용도로 사용했다.

예)

<i class="fa fa-search"></i>

→ 검색 아이콘

<i class="fa fa-pencil"></i>

→ 연필 아이콘

<i class="fa fa-bar-chart"></i>

→ 통계 막대그래프 아이콘

실제로 아이콘을 만드는 것은

class="fa ..."

부분이다.


==================================================

## 41. 이번 과제에서 구현한 기능

1. 게시글 전체 조회

2. 게시글 작성

3. 게시글 상세 조회

4. 게시글 상세 조회 시 조회수 증가

5. 게시글 수정

6. 게시글 삭제

7. 게시판 페이징

8. 최신 게시글 순 정렬

9. 제목 검색

10. 내용 검색

11. 제목 + 내용 검색

12. 검색 결과 페이징 유지

13. 작성자별 게시글 조회

14. 작성자별 게시글 페이징

15. 작성자별 통계

16. 작성자별 게시글 수 계산

17. 작성자별 총 조회수 계산

18. 작성자별 평균 조회수 계산

19. 작성자 페이지 연결

20. 통계 페이지 연결


==================================================

## 42. 이번 과제 핵심 개념 요약

JpaRepository
→ JPA Repository 기본 CRUD 제공

Pageable
→ 페이지 번호, 개수, 정렬 조건 저장

PageRequest
→ Pageable 생성

Page<T>
→ 현재 페이지 데이터 + 전체 페이지 정보

Sort
→ 정렬 조건

@RequestParam
→ URL Query Parameter 받기

Containing
→ LIKE %keyword% 검색

Spring Data JPA Query Method
→ 메서드 이름으로 쿼리 자동 생성

findByUser_Id
→ 연관 Entity의 필드를 따라 조건 검색

@Query
→ JPQL 직접 작성

JPQL
→ 테이블이 아닌 Entity 기준으로 쿼리 작성

GROUP BY
→ 작성자별 데이터 그룹화

COUNT
→ 게시글 수

SUM
→ 총 조회수

AVG
→ 평균 조회수

Projection
→ Entity 전체가 아닌 원하는 결과만 조회

Thymeleaf
→ 서버에서 받은 데이터를 HTML에 출력

th:each
→ 반복문

th:text
→ 값 출력

th:href
→ 동적 링크

th:if
→ 조건문

th:selected
→ select 선택값 유지

th:classappend
→ 조건에 따라 CSS class 추가


==================================================

## 43. 가장 중요하게 기억할 흐름

게시판 목록

Browser
→ GET /board/list?page=0
→ BoardController
→ BoardService
→ BoardRepository
→ DB
→ Page<Board>
→ Model
→ Thymeleaf
→ Browser


검색

Browser
→ type + keyword 전달
→ Controller
→ Service에서 type 판단
→ Repository Query Method
→ 검색 결과 Page<Board>
→ Thymeleaf 출력


작성자 조회

작성자 클릭
→ userId 전달
→ findByUser_Id()
→ 해당 사용자 게시글 조회


통계

Controller
→ Service
→ Repository의 JPQL
→ GROUP BY
→ COUNT / SUM / AVG
→ WriterStats Projection
→ stats.html 출력
