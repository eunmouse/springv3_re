package org.example.springv3.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Integer> {

    @Query("select b from Board b where b.title like %:title% order by b.id desc")
    Page<Board> mFindAll(@Param("title") String title, Pageable pageable);

    // 이제 상세보기는 이게 필요
    // 게시글, 게시글 작성자, 댓글들                                 + r 은 별칭 / Board 는 이미 뽑았으니까, r안의 user 만 뽑음
    @Query("select b from Board b join fetch b.user left join fetch b.replies r left join fetch r.user where b.id=:id")
    Optional<Board> mFindByIdWithReply(@Param("id") int id);

    //@Query(value = "select * from board_tb bt inner join user_tb ut on bt.user_id = ut.id where bt.id=?", nativeQuery = true)
    @Query("select b from Board b join fetch b.user u where b.id=:id")
    Optional<Board> mFindById(@Param("id") int id);

//    @Query("select b from Board b order by b.id desc") // JPQL, 이렇게 하면 Sort 안해도 됨
//    List<Board> mFindAll(); // 이건 팀 컨벤션, ex. 내가 만든 거 m 붙이기


}

