package org.example.librarymanagementsystem.Repository;

import org.example.librarymanagementsystem.DAO.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserDetails, Long> {

}
