package org.example.springv3.board;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.springv3.core.error.ex.Exception404;
import org.example.springv3.core.error.ex.ExceptionApi404;
import org.example.springv3.core.util.Resp;
import org.example.springv3.user.User;
import org.example.springv3.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.Errors;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class BoardController {

    private final HttpSession session;
    private final BoardService boardService;

    @GetMapping("/test/v6")
    public ResponseEntity<?> testV6(){ // 1. ResponseBody 생략, 상태코드를 넣을 수 있다.
        throw new ExceptionApi404("페이지를 찾을 수 없습니다.");
    }

    // 데이터를 응답하면서 + 상태코드 전달하고 싶을 때, ResponseEntity 사용
    @GetMapping("/test/v5")
    public ResponseEntity<?> testV5(){ // 1. ResponseBody 생략, 상태코드를 넣을 수 있다.
        return new ResponseEntity<>(Resp.fail(404, "찾을 수 없습니다"), HttpStatus.NOT_FOUND);
    }

    @GetMapping("/test/v4")
    public @ResponseBody Resp testV4(HttpServletResponse response){
        response.setStatus(404);
        return Resp.fail(404, "유저를 찾을 수 없습니다");
    }

    @GetMapping("/test/v3")
    public @ResponseBody Resp testV3() {
        return Resp.fail(404, "유저를 찾을 수 없습니다.");
    }

    @GetMapping("/test/v1")
    public @ResponseBody Resp testV1() { // @ 이거 안붙이면 데이터로 응답못함.
        User u = new User();
        u.setId(1);
        u.setUsername("ssar");
        u.setPassword("1234");
        u.setEmail("ssar@nate.com");
        return Resp.ok(u); // 이러면 유저객체에 담기고, 코드도 깔끔.
    }

    @GetMapping("/test/v2")
    public @ResponseBody Resp testV2() { // 만약 Resp 가 없다면, 타입이 List<User> 될거고..
        User u1 = new User();
        u1.setId(1);
        u1.setUsername("ssar");
        u1.setPassword("1234");
        u1.setEmail("ssar@nate.com");
        User u2 = new User();
        u2.setId(2);
        u2.setUsername("cos");
        u2.setPassword("1234");
        u2.setEmail("cos@nate.com");
        List<User> users = Arrays.asList(u1, u2); // new ArrayList 와 같은 것.
        return Resp.ok(users);
    }


    @GetMapping("/")
    public String list(@RequestParam(name = "title", required = false) String title,
                       @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                       HttpServletRequest request) {
        BoardResponse.PageDTO pageDTO = boardService.게시글목록보기(title, page);
        request.setAttribute("model", pageDTO);
        return "board/list";
    }

    // localhost:8080?title=제목
    // get 요청은 쿼리스트링 밖에 없음
//    @GetMapping("/")
//    public String list(@RequestParam(name = "title", required = false) String title,
//                       @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
//                       HttpServletRequest request) {
//
//        Page<Board> boardPG = boardService.게시글목록보기(title, page);
//        request.setAttribute("model", boardPG);
//        request.setAttribute("prev", boardPG.getNumber()-1);
//        request.setAttribute("next", boardPG.getNumber()+1);
//        return "board/list";
//    }


    @PostMapping("/api/board/{id}/delete")
    public String removeBoard(@PathVariable("id") Integer id, HttpServletRequest request) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        boardService.게시글삭제하기(id, sessionUser);
        return "redirect:/";
    }


    @GetMapping("/api/board/save-form")
    public String saveForm() {
        return "board/save-form";
    }


    @PostMapping("/api/board/save")
    public String save(@Valid BoardRequest.SaveDTO saveDTO, Errors errors) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        boardService.게시글쓰기(saveDTO, sessionUser);

        return "redirect:/";
    }


    @GetMapping("/api/board/{id}/update-form")
    public String updateForm(@PathVariable("id") int id, HttpServletRequest request) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        BoardResponse.DTO model = boardService.게시글수정화면(id, sessionUser);
        request.setAttribute("model", model);
//        Board board = boardService.게시글수정화면(id, sessionUser);
//        request.setAttribute("model", board);
        return "board/update-form";
    }

    @GetMapping("/v2/api/board/{id}/update-form")
    public @ResponseBody BoardResponse.DTO updateForm(@PathVariable("id") int id) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        BoardResponse.DTO model = boardService.게시글수정화면V2(id, sessionUser);
        return model;
    }


    @PostMapping("/api/board/{id}/update")
    public String update(@PathVariable("id") int id, @Valid BoardRequest.UpdateDTO updateDTO, Errors errors) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        boardService.게시글수정(id, updateDTO, sessionUser);
        return "redirect:/board/" + id;
    }

    @GetMapping("/board/{id}")
    public String detail(@PathVariable("id") Integer id, HttpServletRequest request) {
        User sessionUser = (User) session.getAttribute("sessionUser");

//        Board model = boardService.게시글상세보기(sessionUser, id);
        BoardResponse.DetailDTO model = boardService.게시글상세보기(sessionUser, id);
        request.setAttribute("model", model);

        return "board/detail";


    }

    // 1. BoardResponse.DetailDTO 로 필요한 것만 걸러낸거 확인하려면
    // 2. BoardService 가서 게시글상세보기 리턴타입 BoardResponse.DetailDTO 로 바꾸고 실행
    @GetMapping("/v2/board/{id}") // @ResponseBody 붙이면 데이터 그대로 리턴해줌
    public @ResponseBody BoardResponse.DetailDTO detailV2(@PathVariable("id") Integer id) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        BoardResponse.DetailDTO model = boardService.게시글상세보기(sessionUser, id);

        return model; // 데이터명
    }

    @GetMapping("/v3/board/{id}")
    public @ResponseBody Board detailV3(@PathVariable("id") Integer id) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        Board model = boardService.게시글상세보기V3(sessionUser, id);

        return model;
    }

}
