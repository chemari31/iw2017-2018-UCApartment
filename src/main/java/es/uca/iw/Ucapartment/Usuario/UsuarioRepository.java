package es.uca.iw.Ucapartment.Usuario;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	public List<Usuario> findByApellidosStartsWithIgnoreCase(String Apellidos);
	
	public Usuario findByNombreUsuario(String nombreUsuario);
	
	public Usuario findByEmail(String email);
	
	public Usuario findById(long id);
	
	public Usuario findByDni(String dni);
	
	public List<Usuario> findByRol (Rol rol);
}
