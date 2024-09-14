package org.example.springv3.board;

import lombok.Data;
import org.example.springv3.reply.Reply;
import org.example.springv3.user.User;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public class BoardResponse {

    @Data // getter, setter, toString
    public static class ListDTO {
        private Integer id;
        private String title;
        private Long count;

        public ListDTO(Integer id, String title, Long count) {
            this.id = id;
            this.title = title;
            this.count = count;
        }
    }

    @Data
    public static class PageDTO {
        private Integer number; // 현재페이지
        private Integer totalPage; // 전체페이지 개수
        private Integer size; // 한페이지에 아이템 개수
        private Boolean first;
        private Boolean last;
        private Integer prev; // 현재페이지 -1
        private Integer next; // 현재페이지 +1
        private String keyword; // 검색 제목

        // [0,1,2, -> 0number]
        // [3,4,5, -> 3number]
        // [6,7,9, -> 6number]
        // number = 0 (0,1,2)
        // number = 1 (0,1,2)
        // number = 2 (0,1,2)
        // number = 3 (3,4,5)
        // number = 4 (3,4,5)
        // number = 5 (3,4,5)
        // number = 6 (6,7,8)
        private List<Integer> numbers = new ArrayList<>();
        private List<Content> contents = new ArrayList<>();


        public PageDTO(Page<Board> boardPG, String title) {
            this.keyword = title;

            this.number = boardPG.getNumber();
            this.totalPage = boardPG.getTotalPages();
            this.size = boardPG.getSize();
            this.first = boardPG.isFirst();
            this.last = boardPG.isLast();
            this.prev = boardPG.getNumber()-1;
            this.next = boardPG.getNumber()+1;
            int temp = (number / 3)*3; // 0 -> 0, 3 -> 3, 6 -> 6

            for(int i=temp; i<temp+3; i++){ // 0
                this.numbers.add(i);
            }
            for(Board board : boardPG.getContent()) {
                contents.add(new Content(board));
            }

        }

        @Data
        class Content {
            private Integer id;
            private String title;

            public Content (Board board) {
                this.id = board.getId();
                this.title = board.getTitle();
            }
        }
    }

    @Data
    public static class DetailDTO {

        private Integer id;
        private String title;
        private String content;
        private Boolean isOwner;
//        private Integer userId;
        private String username;

        // 댓글들
        private List<ReplyDTO> replies = new ArrayList<>();

        // DTO 로 뽑을 때는 순수한 객체 여야 한다.
        // 핵심은 엔티티를 객체로 바꿔치기 하는 것. (엔티티에 쓸데 없는 데이터가 많기 때문에, 엔티티 그대로 리턴하지 않음)
        // 내가 화면에 필요한 객체를 하나 만들어서 옮기는 과정.
        // 1. 화면에 필요한 데이터만 뽑을 수 있고,
        // 2. 지금 양방향매핑(Board<>Reply)되어있어서 DTO 로 만들지 않고 Board 만 때리면(?) 서로 연결이 되어서 양방향으로 다 터진다. (엔티티를 DTO 로 끌고오지 않으면 된다)
        // 패키징 하는 것. 이걸 연습해야 한다. 그래야 내가 원하는 데이터를 전달 할 수 있다.
        public DetailDTO(Board board, User sessionUser) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.isOwner = false;

            if (sessionUser != null) {
                if (board.getUser().getId() == sessionUser.getId()) {
                    isOwner = true; // 권한체크

                }
            }

//            this.userId = board.getUser().getId();
            this.username = board.getUser().getUsername();

            // for 문으로 돌리면서 집어넣음
            for(Reply reply : board.getReplies()) { // Board 엔티티에 속해있는 모든 Reply 객체들의 리스트를 가지고와서 각각의 Reply 객체를 reply 변수에 담고,
                replies.add(new ReplyDTO(reply, sessionUser)); // 그 reply 객체를 ReplyDTO 생성자의 파라미터로 넘긴다.
            }
        }

        @Data
        class ReplyDTO {
            private Integer id;
            private String comment;
            private String username;
            private Boolean isOwner;

            public ReplyDTO(Reply reply, User sessionUser) {
                this.id = reply.getId();
                this.comment = reply.getComment();
                this.username = reply.getUser().getUsername();
                this.isOwner = false;

                if (sessionUser != null) {
                    if (reply.getUser().getId() == sessionUser.getId()) {
                        isOwner = true;// 권한체크
                    }
                }
            }
        }
    }
    @Data
    public static class DTO {
        private Integer id;
        private String title;
        private String content;

        public DTO(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
        }
    }

}