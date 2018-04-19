package es.uca.iw.Ucapartment.users;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

	public List<User> findByLastNameStartsWithIgnoreCase(String lastName);
	
	public User findByUsername(String username);
}

