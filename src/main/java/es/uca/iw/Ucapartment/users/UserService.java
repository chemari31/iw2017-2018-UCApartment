/**
 * 
 */
package es.uca.iw.Ucapartment.users;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author ruizrube
 *
 */
@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository repo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public User loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = repo.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}
		return user;
	}

	public User save(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword() != null ? user.getPassword() : "default"));
		return repo.save(user);
	}

	public List<User> findByLastNameStartsWithIgnoreCase(String lastName) {
		return repo.findByLastNameStartsWithIgnoreCase(lastName);
	}

	public User findOne(Long arg0) {
		return repo.findOne(arg0);
	}

	public void delete(User arg0) {
		repo.delete(arg0);
	}

	public List<User> findAll() {
		return repo.findAll();
	}

}
