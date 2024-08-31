package org.example.librarymanagementsystem.Repository;

import org.example.librarymanagementsystem.DAO.Books;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface BookRepository extends JpaRepository<Books, String> {
    boolean existsBooksByIsbnNo(String ISBN);

    boolean existsByIsbnNoAndIsAvailableTrue(String isbnNO);

    Books findByIsbnNo(String ISBN);

    @Transactional
    @Modifying
    @Query("UPDATE  Books b SET b.isAvailable = :isAvailable WHERE b.isbnNo = :ISBN")
    int updateBookAvailableStatus(String ISBN, boolean isAvailable);
}
