package org.example.librarymanagementsystem.Repository;

import org.example.librarymanagementsystem.DAO.BorrowedRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
@Transactional
public interface BorrowRecordRepository extends JpaRepository<BorrowedRecord, Long> {

    @Query("SELECT b FROM BorrowedRecord b WHERE b.book.isbnNo = :isbnNo AND b.isReturned = false")
    List<BorrowedRecord> findBorrowedRecord(String isbnNo);

    @Modifying
    @Transactional
    @Query("UPDATE BorrowedRecord b SET b.returnedDate = :returnedDate, b.isReturned = true WHERE b.book.isbnNo = :isbnNo")
    int updateBorrowedRecord(Date returnedDate, String isbnNo);

}
