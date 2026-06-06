// ==========================================
// CLASE: Empleado.java
// Representa la tabla: Empleados
// Si id_jefe es NULL → es Administrador
// ==========================================
package model;

/**
 * Representa un empleado del videoclub, incluyendo al administrador.
 * <p>
 * Mapea la tabla {@code Empleados} de la base de datos SQLite. El rol del
 * empleado se determina mediante el campo {@code idJefe}: si es {@code null},
 * el empleado es Administrador (no tiene jefe superior); si tiene valor, es un
 * empleado normal que reporta a dicho jefe. Esta lógica se implementa en el
 * método {@link #esAdministrador()}, evitando la necesidad de un campo rol
 * explícito en la base de datos.
 * </p>
 *
 * @author Gabriel Fernández Cañadas
 * @version 1.0
 */
public class Empleado {

	private int idEmpleado;
	private String nombreEmpleado;
	private String apellidoEmpleado;
	private String emailEmpleado;
	private String usuarioEmpleado;
	private String contraseniaEmpleado;
	private Integer idJefe; // NULL si es el administrador (jefe de todos)

	/**
	 * Constructor completo con todos los campos de la tabla Empleados.
	 *
	 * @param idEmpleado          identificador único del empleado
	 * @param nombreEmpleado      nombre del empleado
	 * @param apellidoEmpleado    apellido del empleado
	 * @param emailEmpleado       correo electrónico del empleado
	 * @param usuarioEmpleado     nombre de usuario para el login (único en la BD)
	 * @param contraseniaEmpleado contraseña del empleado
	 * @param idJefe              id del empleado supervisor, o {@code null} si es
	 *                            administrador
	 */
	public Empleado(int idEmpleado, String nombreEmpleado, String apellidoEmpleado, String emailEmpleado,
			String usuarioEmpleado, String contraseniaEmpleado, Integer idJefe) {
		this.idEmpleado = idEmpleado;
		this.nombreEmpleado = nombreEmpleado;
		this.apellidoEmpleado = apellidoEmpleado;
		this.emailEmpleado = emailEmpleado;
		this.usuarioEmpleado = usuarioEmpleado;
		this.contraseniaEmpleado = contraseniaEmpleado;
		this.idJefe = idJefe;
	}

	/**
	 * Devuelve el identificador único del empleado.
	 *
	 * @return id del empleado
	 */
	public int getIdEmpleado() {
		return idEmpleado;
	}

	/**
	 * Devuelve el nombre del empleado.
	 *
	 * @return nombre del empleado
	 */
	public String getNombreEmpleado() {
		return nombreEmpleado;
	}

	/**
	 * Establece el nombre del empleado.
	 *
	 * @param nombreEmpleado nombre del empleado
	 */
	public void setNombreEmpleado(String nombreEmpleado) {
		this.nombreEmpleado = nombreEmpleado;
	}

	/**
	 * Devuelve el apellido del empleado.
	 *
	 * @return apellido del empleado
	 */
	public String getApellidoEmpleado() {
		return apellidoEmpleado;
	}

	/**
	 * Establece el apellido del empleado.
	 *
	 * @param apellidoEmpleado apellido del empleado
	 */
	public void setApellidoEmpleado(String apellidoEmpleado) {
		this.apellidoEmpleado = apellidoEmpleado;
	}

	/**
	 * Devuelve el correo electrónico del empleado.
	 *
	 * @return email del empleado
	 */
	public String getEmailEmpleado() {
		return emailEmpleado;
	}

	/**
	 * Establece el correo electrónico del empleado.
	 *
	 * @param emailEmpleado email del empleado
	 */
	public void setEmailEmpleado(String emailEmpleado) {
		this.emailEmpleado = emailEmpleado;
	}

	/**
	 * Devuelve el nombre de usuario utilizado para iniciar sesión.
	 *
	 * @return nombre de usuario
	 */
	public String getUsuarioEmpleado() {
		return usuarioEmpleado;
	}

	/**
	 * Establece el nombre de usuario del empleado.
	 *
	 * @param usuarioEmpleado nombre de usuario (debe ser único en la BD)
	 */
	public void setUsuarioEmpleado(String usuarioEmpleado) {
		this.usuarioEmpleado = usuarioEmpleado;
	}

	/**
	 * Devuelve la contraseña del empleado.
	 *
	 * @return contraseña en texto plano
	 */
	public String getContraseniaEmpleado() {
		return contraseniaEmpleado;
	}

	/**
	 * Devuelve el identificador del empleado supervisor (jefe).
	 *
	 * @return id del jefe, o {@code null} si el empleado es administrador
	 */
	public Integer getIdJefe() {
		return idJefe;
	}

	/**
	 * Indica si este empleado tiene rol de administrador.
	 * <p>
	 * La lógica de roles reside en este método: si {@code idJefe} es {@code null},
	 * el empleado es Administrador/Jefe con acceso total al sistema.
	 * </p>
	 *
	 * @return {@code true} si es administrador, {@code false} si es empleado normal
	 */
	public boolean esAdministrador() {
		return this.idJefe == null;
	}

	/**
	 * Devuelve el nombre completo del empleado (nombre + apellido).
	 *
	 * @return nombre completo del empleado
	 */
	public String getNombreCompleto() {
		return nombreEmpleado + " " + apellidoEmpleado;
	}

}