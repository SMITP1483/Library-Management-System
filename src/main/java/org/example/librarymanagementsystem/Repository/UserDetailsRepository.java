package org.example.librarymanagementsystem.Repository;

import org.example.librarymanagementsystem.DAO.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {

}
