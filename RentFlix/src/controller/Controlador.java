package controller;

import java.awt.JobAttributes.SidesType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import dao.ActorDAO;
import dao.AlquilerDAO;
import dao.ClienteDAO;
import dao.CopiaDAO;
import dao.EmpleadoDAO;
import dao.PagoDAO;
import dao.PeliculaDAO;
import model.Alquiler;
import view.CatalogoPanel;
import view.DashboardPanel;
import view.MainFrame;
import view.Sidebar;
import view.TopBar;

public class Controlador implements ActionListener {
	
	private MainFrame mf;
	private DashboardPanel dshp;
	private CatalogoPanel cp;
	private Sidebar sb;
	private TopBar tb;
	
	private ActorDAO actor;
	private AlquilerDAO alquiler;
	private ClienteDAO cliente;
	private CopiaDAO copia;
	private EmpleadoDAO empleado;
	private PagoDAO pago;
	private PeliculaDAO pelicula;
	
	public Controlador(MainFrame mf) {
		this.mf = mf;
		actor = new ActorDAO();
		alquiler = new AlquilerDAO();
		cliente = new ClienteDAO();
		copia = new CopiaDAO();
		empleado = new EmpleadoDAO();
		pago = new PagoDAO();
		pelicula = new PeliculaDAO();
	}
	
	public void setDshp(DashboardPanel dshp) {
		this.dshp = dshp;
	}
	
	public void setCp(CatalogoPanel cp) {
		this.cp = cp;
	}
	
	public void setSb(Sidebar sb) {
		this.sb = sb;
	}
	
	public void setTb(TopBar tb) {
		this.tb = tb;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() instanceof JMenuItem){
			
		}

	}

}
