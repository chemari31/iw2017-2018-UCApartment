package es.uca.iw.Ucapartment.Estado;

public enum Valor{
	PENDIENTE("Pendiente", 0),
	ACEPTADA("Aceptada", 1),
	CANCELADA("Cancelada", 2),
	REALIZADA("Realizada", 3);
	
	//Atributos
	private final String eValor;
	private final int id;
	
	//Constructor
	Valor(String eValor, int id) {
		this.eValor = eValor;
		this.id = id;
	}

	//Getters
	public String geteValor() {
		return eValor;
	}

	public int getId() {
		return id;
	}
}
