package app.tkrun.view;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

/**
 * Vista de la pantalla que muestra las carreras activas y permite interactuar con ellas.
 * <br/>Se ha generado con WindowBulder y modificado para ser conforme a MVC teniendo en cuenta:
 * - Se elimina main (es invocada desde CarrerasMain) y se incluye Title en el frame
 * - No se incluye ningun handler de eventos pues estos van en el controlador
 * - Las tablas se encierran en JOptionPane para que se puedan visualizar las cabeceras
 * - Se asinga nombre a las tablas si se van a automatizar la ejecucion de pruebas
 * - Incluye al final los metodos adicionales necesarios para acceder al UI desde el controlador
 */
public class CarrerasView extends JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private JTextField txtFechaHoy;
	private JButton btnTabCarreras;
	private JTable tabCarreras;
	private JTable tabParticipantes;
	private JButton btnAceptar;
	private JScrollPane tablePanel;

	private JScrollPane tablePanelParticipantes;
	private JButton btnInscripciones;

	private JButton btnCrearCarrera;
	private JButton btnClasificaciones;
	private JButton btnDevoluciones;

	private JButton btnDorsales;

	private JButton btnClasificacionesCategoria;

	
	/**
	 * Create the application.
	 */
	public CarrerasView() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Carreras");
		frame.setName("Carreras");


		frame.setBounds(0, 0, 952, 403);

		frame.setLocationRelativeTo(frame);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new MigLayout("", "[grow]", "[][][grow][][][][][][][][][][][]"));
		final JLabel lblFechaHoy;
		
		lblFechaHoy = new JLabel("Fecha de hoy:");
		frame.getContentPane().add(lblFechaHoy, "flowx,cell 0 3");
		
		txtFechaHoy = new JTextField();
		txtFechaHoy.setName("txtFechaHoy");
		frame.getContentPane().add(txtFechaHoy, "cell 0 3,growx");
		txtFechaHoy.setColumns(10);
		
		btnTabCarreras = new JButton("Ver carreras en esta tabla");
		lblFechaHoy.setLabelFor(btnTabCarreras);
		frame.getContentPane().add(btnTabCarreras, "cell 0 3");
		
		JLabel lblLbltable = new JLabel("Proximas carreras:");
		frame.getContentPane().add(lblLbltable, "cell 0 4");
		
		//Incluyo la tabla en un JScrollPane y anyado este en vez de la tabla para poder ver los headers de la tabla
		tabCarreras = new JTable();
		tabCarreras.setName("tabCarreras");
		tabCarreras.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tabCarreras.setDefaultEditor(Object.class, null); //readonly
		tablePanel = new JScrollPane(tabCarreras);
		frame.getContentPane().add(tablePanel, "cell 0 5,grow");
		
		JLabel lblLbltableParticipantes = new JLabel("Participantes:");
		frame.getContentPane().add(lblLbltableParticipantes, "cell 0 6");
		//Incluimos la tabla de particopantaews
		tabParticipantes = new JTable();
		tabParticipantes.setName("tabParticipantes");
		tabParticipantes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tabParticipantes.setDefaultEditor(Object.class, null); //readonly
		tablePanelParticipantes = new JScrollPane(tabParticipantes);
		frame.getContentPane().add(tablePanelParticipantes, "cell 0 7,grow");
		
		btnClasificaciones = new JButton("Generar clasificacion");
		frame.getContentPane().add(btnClasificaciones, "flowx,cell 0 8");
		
		btnInscripciones = new JButton("Inscripciones");
		
		frame.getContentPane().add(btnInscripciones, "cell 0 8,alignx center");
		
		
		
		btnAceptar = new JButton("Inscribirse");
		btnAceptar.setEnabled(false);
		btnAceptar.setMnemonic('I');
		btnAceptar.setHorizontalAlignment(SwingConstants.RIGHT);

		frame.getContentPane().add(btnAceptar, "cell 0 8,alignx right");

		
		
		btnCrearCarrera = new JButton("Crear carrera");
		frame.getContentPane().add(btnCrearCarrera, "cell 0 8");
		
		btnDevoluciones = new JButton("Devoluciones");
		frame.getContentPane().add(btnDevoluciones, "cell 0 8");
		

		btnDorsales = new JButton("Genera Dorsales");
		frame.getContentPane().add(btnDorsales, "cell 0 8");

		btnClasificacionesCategoria = new JButton("ClasificacionesCategoria");
		btnClasificacionesCategoria.setEnabled(false);
		frame.getContentPane().add(btnClasificacionesCategoria, "cell 0 8");


	}

	

	//Getters y Setters anyadidos para acceso desde el controlador (repersentacion compacta)
	public JFrame getFrame() { return this.frame; }
	public String getFechaHoy()  { return this.txtFechaHoy.getText(); }
	public void setFechaHoy(String fechaIso)  { this.txtFechaHoy.setText(fechaIso); }
	public JButton getBtnTablaCarreras() { return this.btnTabCarreras; }
	public JTable getTablaCarreras() { return this.tabCarreras; }
	public JTable getTablaParticipantes() { return this.tabParticipantes; }
	public JButton getBtnAceptar() { return this.btnAceptar; }
	public JButton getBtnInscripciones() { return this.btnInscripciones; }
	public JScrollPane getTablePanel() {return this.tablePanel; };
	public JButton getBtnClasificaciones() { return this.btnClasificaciones; }
	public  JButton getBtnDevoluciones() {return this.btnDevoluciones;}
	public  JButton getBtnCrearCarrera() {return this.btnCrearCarrera;}

	public  JButton getBtnDorsales() {return this.btnDorsales;}

	public  JButton getBtnClasificacionesCategoria() {return this.btnClasificacionesCategoria;}



	
}
