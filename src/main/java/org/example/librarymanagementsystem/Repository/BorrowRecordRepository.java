package org.example.librarymanagementsystem.Repository;

import org.example.librarymanagementsystem.DAO.BorrowedRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowRecordRepository extends JpaRepository<BorrowedRecord, Long> {

}
