package SotonHKPASS.Website_testing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SotonHKPASS.Website_testing.entity.Login_info;

@Repository
public interface Stock_repository extends JpaRepository<Login_info, Long> {

}
