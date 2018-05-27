package es.uca.iw.Ucapartment.Usuario;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService implements UserDetailsService {

	@Autowired
	private UsuarioRepository repo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public Usuario loadUserByUsername(String nombreUsuario) throws UsernameNotFoundException {

		Usuario usuario = repo.findByNombreUsuario(nombreUsuario);
		if (usuario == null) {
			throw new UsernameNotFoundException(nombreUsuario);
		}
		return usuario;
	}
	
	public Usuario findById(long id)
	{
		return repo.findById(id);
	}
	
	public Usuario save(Usuario usuario) {
		usuario.setPassword(passwordEncoder.encode(usuario.getPassword() != null ? usuario.getPassword() : "default"));
		return repo.save(usuario);
	}

	public List<Usuario> findByApellidosStartsWithIgnoreCase(String Apellidos) {
		return repo.findByApellidosStartsWithIgnoreCase(Apellidos);
	}

	public Usuario findOne(Long arg0) {
		return repo.findOne(arg0);
	}

	public void delete(Usuario arg0) {
		repo.delete(arg0);
	}

	public List<Usuario> findAll() {
		return repo.findAll();
	}
	
	public boolean nombreUsuarioExistente(String nombreUsuario) {
		Usuario usuario = repo.findByNombreUsuario(nombreUsuario);
		boolean existe = false;
		if(usuario != null) existe = true;
		
		return existe;
	}
	
	public boolean emailExistente(String email) {
		Usuario usuario = repo.findByEmail(email);
		boolean existe = false;
		if(usuario != null) existe = true;
		
		return existe;
	}
	
	public boolean dniExistente(String dni) {
		Usuario usuario = repo.findByDni(dni);
		boolean existe = false;
		if(usuario != null) existe = true;
		
		return existe;
	}
	
	public List<Usuario> findByRol(Rol rol) {
		return repo.findByRol(rol);
	}

}