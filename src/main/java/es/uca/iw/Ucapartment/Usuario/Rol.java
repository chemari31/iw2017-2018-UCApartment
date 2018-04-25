package es.uca.iw.Ucapartment.Usuario;

//Enumerado Rol
public enum Rol {
	ADMINISTRADOR("Administrador", 0),
	GERENTE("Gerente", 1),
	ANFITRION("Anfitrion", 2);
	
	//Atributos
	private final String funcion;
	private final int id;
	
	//Constructor
	Rol(String funcion, int id){
		this.funcion = funcion;
		this.id = id;
	}

	//Getters
	public String getFuncion() {
		return funcion;
	}

	public int getId() {
		return id;
	}
}
