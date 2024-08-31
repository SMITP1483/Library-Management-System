package org.example.librarymanagementsystem.Repository;

import org.example.librarymanagementsystem.DAO.BorrowedRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowRecordRepository extends JpaRepository<BorrowedRecord, Long> {

}
