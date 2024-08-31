package org.example.librarymanagementsystem.Repository;

import org.example.librarymanagementsystem.DAO.Books;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BookRepository extends JpaRepository<Books, String> {
      boolean existsBooksByIsbnNo(String ISBN);

}
