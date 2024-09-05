package org.example.librarymanagementsystem.Repository;

import org.example.librarymanagementsystem.DAO.Books;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
@Transactional
public interface BookRepository extends JpaRepository<Books, String> {


    @Query("SELECT b FROM Books b WHERE LOWER(b.title) like LOWER(CONCAT('%',:title,'%')) AND b.isAvailable = true")
    List<Books> findAvailableBooksByTitle(String title);

    @Query("SELECT b FROM Books b WHERE b.isAvailable = true")
    List<Books> findAllAvailableBooks();

    boolean existsBooksByIsbnNo(String ISBN);

    boolean existsByIsbnNoAndIsAvailableTrue(String isbnNO);

    Books findByIsbnNo(String ISBN);

    @Transactional
    @Modifying
    @Query("UPDATE  Books b SET b.isAvailable = :isAvailable WHERE b.isbnNo = :ISBN")
    int updateBookAvailableStatus(String ISBN, boolean isAvailable);
}
