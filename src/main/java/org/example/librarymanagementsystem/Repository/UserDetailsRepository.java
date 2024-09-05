package org.example.librarymanagementsystem.Repository;

import org.example.librarymanagementsystem.DAO.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {

    List<UserDetails> findUserDetailsByEmail(String email);

}
