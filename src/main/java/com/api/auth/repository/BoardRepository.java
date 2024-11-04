package com.api.auth.repository;

import com.api.auth.DTO.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("SELECT b FROM Board b WHERE b.title LIKE %:query% ORDER BY b.writeDate DESC, b.writeTime DESC")
    Page<Board> findByTitleContaining(@Param("query") String query, Pageable pageable);

    @Query("SELECT b FROM Board b WHERE b.content LIKE %:query% ORDER BY b.writeDate DESC, b.writeTime DESC")
    Page<Board> findByContentContaining(@Param("query") String query, Pageable pageable);

    @Query("SELECT b FROM Board b WHERE b.title LIKE %:query% OR b.content LIKE %:query% ORDER BY b.writeDate DESC, b.writeTime DESC")
    Page<Board> findByTitleContainingOrContentContaining(@Param("query") String query, Pageable pageable);

    @Query("SELECT b FROM Board b JOIN User u ON b.userId = u.userId WHERE u.nickname LIKE %:query% ORDER BY b.writeDate DESC, b.writeTime DESC")
    Page<Board> findByNicknameContaining(@Param("query") String query, Pageable pageable);

}
