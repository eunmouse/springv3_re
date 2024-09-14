package org.example.springv3.board;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DataJpaTest // JPARepository 상속하면 import 안해도 된다. QueryRepository 는 import 해야 됨!
public class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void mFindAllV2_test() throws JsonProcessingException {
        // given
        String title = "제목";

        // when
        Pageable pageable = PageRequest.of(0, 3);
        Page<Board> boardPG = boardRepository.mFindAll(title, pageable);

        // eye
        System.out.println(boardPG.getContent());
    }

    @Test
    public void mFindAll_test() throws JsonProcessingException {
        // given
        String title = "제목";

        // when
        Pageable pageable = PageRequest.of(0, 3);
        Page<Board> boardPG = boardRepository.mFindAll(title, pageable);

        // eye
        ObjectMapper om = new ObjectMapper();
        String responseBody = om.writeValueAsString(boardPG);
        //om.readValue(responseBody, Board.class);

        System.out.println(responseBody);
    }

//    @Test
//    public void mFindAll_test(){
//        // given
//        String title = "제";
//        // when
//        List<Board> boardList = boardRepository.mFindAll(title);
//
//        // eye
//        System.out.println(boardList.size());
//        System.out.println(boardList.get(0).getTitle());
//    }

    @Test
    public void mFindByIdwithReply_test(){
        Board board = boardRepository.mFindByIdWithReply(5).get();
        System.out.println(board.getReplies().get(0).getComment());
    }

}
