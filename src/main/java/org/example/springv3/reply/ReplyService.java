package org.example.springv3.reply;

import lombok.RequiredArgsConstructor;
import org.example.springv3.board.Board;
import org.example.springv3.board.BoardRepository;
import org.example.springv3.core.error.ex.Exception404;
import org.example.springv3.core.error.ex.ExceptionApi403;
import org.example.springv3.core.error.ex.ExceptionApi404;
import org.example.springv3.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service // Ioc 등록
public class ReplyService {
    private final ReplyRepository replyRepository; // Ioc 에서 가져와서
    private final BoardRepository boardRepository;

    @Transactional // DB는 변경하는거니까 일의 최소단위를 트랜잭션으로 묶어준다.
    public void 댓글삭제(int id, User sessionUser) {
        Reply replyPS = replyRepository.findById(id)
                .orElseThrow(() -> new ExceptionApi404("댓글을 찾을 수 없습니다.")); // 그냥 Exception 터트리면 안됨 주의

        // DB 에 reply 를 조회했을 때, board_id 랑 user_id 가 있단말이지
        // 이미 조회해왔기 때문에 select 두번 치지 않는다.
        // 근데 getUsername 은 없기 때문에 Lazy 로딩으로 두번 치겠지
        if(replyPS.getUser().getId() != sessionUser.getId()) { // 이미 조회가 된 상태
            throw new ExceptionApi403("댓글 삭제 권한이 없습니다.");
        }

        replyRepository.deleteById(id); // 이미 만들어져있어서 쓰면 됨^^
    }

    @Transactional
    public ReplyResponse.DTO 댓글쓰기(ReplyRequest.SaveDTO saveDTO, User sessionUser) {
        System.out.println(3);
        // 1. 게시글 존재 유무 확인
        Board boardPS = boardRepository.findById(saveDTO.getBoardId())
                .orElseThrow(() -> new ExceptionApi404("게시글을 찾을 수 없습니다."));

        System.out.println(4);
        // 2. 비영속 댓글 객체 만들기
        Reply reply = saveDTO.toEntity(sessionUser, boardPS);

        System.out.println(5);
        // 3. 댓글 저장 (reply가 영속화됨)
//        Reply replyPS = replyRepository.save(reply); // 담궈지는 순간, pk 가 만들어진다.
        replyRepository.save(reply); // 동기화되어서 pk 가 들어옴
        System.out.println(6);
        return new ReplyResponse.DTO(reply);
        // DTO 응답하는 이유:
        // 1. 화면에 필요한 데이터만 응답하려고
        // 2. 양방향 매핑이던, lazy loading이건 안터진다.
    }
}
