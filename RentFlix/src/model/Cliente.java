// ==========================================
// CLASE: Cliente.java
// Representa la tabla: Clientes
// ==========================================
package model;

/**
 * Representa un cliente registrado en el videoclub.
 * <p>
 * Mapea la tabla {@code Clientes} de la base de datos SQLite. El campo
 * {@code estado} controla el acceso del cliente al sistema: un cliente en
 * estado {@code bloqueado} no puede iniciar sesión.
 * </p>
 *
 * @author Gabriel Fernández Cañadas
 * @version 1.0
 */
public class Cliente {

	private int idCliente;
	private String nombreCliente;
	private String apellidoCliente;
	private String emailCliente;
	private String nombreUsuario;
	private String contraseniaCliente;
	private String estado; // "activo", "bloqueado"


	/**
	 * Constructor completo con todos los campos de la tabla Clientes.
	 *
	 * @param idCliente          identificador único del cliente
	 * @param nombreCliente      nombre del cliente
	 * @param apellidoCliente    apellido del cliente
	 * @param emailCliente       correo electrónico de contacto
	 * @param nombreUsuario      nombre de usuario para el login (único en la BD)
	 * @param contraseniaCliente contraseña del cliente
	 * @param estado             estado de la cuenta: {@code activo} o
	 *                           {@code bloqueado}
	 */
	public Cliente(int idCliente, String nombreCliente, String apellidoCliente, String emailCliente,
			String nombreUsuario, String contraseniaCliente, String estado) {
		this.idCliente = idCliente;
		this.nombreCliente = nombreCliente;
		this.apellidoCliente = apellidoCliente;
		this.emailCliente = emailCliente;
		this.nombreUsuario = nombreUsuario;
		this.contraseniaCliente = contraseniaCliente;
		this.estado = estado;
	}

	/**
	 * Devuelve el identificador único del cliente.
	 *
	 * @return id del cliente
	 */
	public int getIdCliente() {
		return idCliente;
	}

	/**
	 * Devuelve el nombre del cliente.
	 *
	 * @return nombre del cliente
	 */
	public String getNombreCliente() {
		return nombreCliente;
	}

	/**
	 * Establece el nombre del cliente.
	 *
	 * @param nombreCliente nombre del cliente
	 */
	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	/**
	 * Devuelve el apellido del cliente.
	 *
	 * @return apellido del cliente
	 */
	public String getApellidoCliente() {
		return apellidoCliente;
	}

	/**
	 * Establece el apellido del cliente.
	 *
	 * @param apellidoCliente apellido del cliente
	 */
	public void setApellidoCliente(String apellidoCliente) {
		this.apellidoCliente = apellidoCliente;
	}

	/**
	 * Devuelve el correo electrónico del cliente.
	 *
	 * @return email del cliente
	 */
	public String getEmailCliente() {
		return emailCliente;
	}

	/**
	 * Establece el correo electrónico del cliente.
	 *
	 * @param emailCliente email del cliente
	 */
	public void setEmailCliente(String emailCliente) {
		this.emailCliente = emailCliente;
	}

	/**
	 * Devuelve el nombre de usuario utilizado para iniciar sesión.
	 *
	 * @return nombre de usuario
	 */
	public String getNombreUsuario() {
		return nombreUsuario;
	}

	/**
	 * Establece el nombre de usuario.
	 *
	 * @param nombreUsuario nombre de usuario (debe ser único en la BD)
	 */
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	/**
	 * Devuelve la contraseña del cliente.
	 *
	 * @return contraseña en texto plano
	 */
	public String getContraseniaCliente() {
		return contraseniaCliente;
	}

	/**
	 * Establece la contraseña del cliente.
	 *
	 * @param contraseniaCliente contraseña en texto plano
	 */
	public void setContraseniaCliente(String contraseniaCliente) {
		this.contraseniaCliente = contraseniaCliente;
	}

	/**
	 * Devuelve el estado de la cuenta del cliente.
	 *
	 * @return {@code activo} o {@code bloqueado}
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * Establece el estado de la cuenta del cliente.
	 *
	 * @param estado {@code activo} o {@code bloqueado}
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * Devuelve el nombre completo del cliente (nombre + apellido). Se usa para
	 * mostrar el cliente en las vistas.
	 *
	 * @return nombre completo del cliente
	 */
	public String getNombreCompleto() {
		return nombreCliente + " " + apellidoCliente;
	}
}