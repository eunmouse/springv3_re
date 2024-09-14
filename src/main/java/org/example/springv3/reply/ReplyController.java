package org.example.springv3.reply;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.springv3.core.util.Resp;
import org.example.springv3.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@Controller
public class ReplyController {
    private final HttpSession session;
    private final ReplyService replyService;

    @PostMapping("/api/reply") // post 요청밖에 없으면 save, delete 이런거 붙일 텐데 postmapping 쓰면 안써도 됨
    public ResponseEntity<?> save(@RequestBody ReplyRequest.SaveDTO saveDTO) {
        System.out.println(1);
        User sessionUser = (User) session.getAttribute("sessionUser");
        System.out.println(2);
        ReplyResponse.DTO replyDTO = replyService.댓글쓰기(saveDTO, sessionUser); // session 을 넣는 이유 : save 할 때, 정보가 필요하다.
        System.out.println(7);
        return ResponseEntity.ok(Resp.ok(replyDTO));
    }


    @DeleteMapping("/api/reply/{id}") // postMapping 이면 reply/{id}/delete 해줘야 했지만 이제 안해줘도됨
    public ResponseEntity<?> delete (@PathVariable("id") Integer id) {
        // 1. 인증 체크 -> 주소 처리 (클리어)
        // 2. 서비스 호출 - 댓글삭제
        User sessionUser = (User) session.getAttribute("sessionUser");
        replyService.댓글삭제(id, sessionUser);

        // 3. 응답
        return ResponseEntity.ok(Resp.ok(null)); // 무조건 Resp.ok();// 돌려줄 데이터가 없다.
                // 상태코드 감싸려고
//        throw new ExceptionApi403("권한이 없습니다."); // 이것도 테스트해볼 때 콘솔에 뜨는거 찍어보기
    }
}
