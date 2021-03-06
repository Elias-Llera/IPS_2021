package app.tkrun.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import app.tkrun.entities.AtletaEntity;
import app.tkrun.entities.CarreraEntity;
import app.tkrun.entities.CategoriaEntity;
import app.tkrun.entities.InscripcionEntity;
import app.tkrun.entities.LimiteDorsalesEntity;
import app.tkrun.entities.ParticipanteEntity;
import app.tkrun.entities.PlazosDeInscripcionEntity;
import app.tkrun.entities.TiempoEntity;
import app.tkrun.model.AtletaModel;
import app.tkrun.model.CarreraModel;
import app.tkrun.model.CategoriaModel;
import app.tkrun.model.InscripcionModel;
import app.tkrun.model.LimiteDorsalesModel;
import app.tkrun.model.PlazosDeInscripcionModel;
import app.tkrun.model.TiempoModel;
import app.tkrun.view.CarrerasView;
import app.tkrun.view.ClasificacionesCategoriaView;
import app.tkrun.view.DatosClasificacionView;
import app.tkrun.view.DatosView;
import app.util.SwingUtil;

/**
 * Controlador para la funcionalidad de visualizacion de carreras para la
 * inscripcion. Es el punto de entrada de esta pantalla que se invocará:
 * -instanciando el controlador con la vista y el modelo -ejecutando
 * initController que instalara los manejadores de eventos
 */
public class CarrerasController {
	private CarreraModel model;
	private CarrerasView view;
	private AtletaModel atletaModel = new AtletaModel();
	private InscripcionModel inscripcionModel = new InscripcionModel();
	private CategoriaModel categoriaModel = new CategoriaModel();
	private LimiteDorsalesModel dorsalesModel= new LimiteDorsalesModel();
	private CarreraModel carreraModel= new CarreraModel();
	private TiempoModel tiemposModel= new TiempoModel();
	private String lastSelectedKey = ""; // recuerda la ultima fila seleccionada para restaurarla cuando cambie la tabla
											// de carreras
	private List<Boolean> listaDorsalAsignado;
	private List<Integer> listaIds;
	
	

	public CarrerasController(CarreraModel m, CarrerasView v) {
		this.model = m;
		this.view = v;
		// no hay inicializacion especifica del modelo, solo de la vista
		this.initView();
	}

	/**
	 * Inicializacion del controlador: anyade los manejadores de eventos a los
	 * objetos del UI. Cada manejador de eventos se instancia de la misma forma,
	 * para que invoque un metodo privado de este controlador, encerrado en un
	 * manejador de excepciones generico para mostrar ventanas emergentes cuando
	 * ocurra algun problema o excepcion controlada.
	 */
	public void initController() {
		// ActionListener define solo un metodo actionPerformed(), es un interfaz
		// funcional que se puede invocar de la siguiente forma:
		// view.getBtnTablaCarreras().addActionListener(e -> getListaCarreras());
		// ademas invoco el metodo que responde al listener en el exceptionWrapper para
		// que se encargue de las excepciones
		view.getBtnTablaCarreras().addActionListener(e -> SwingUtil.exceptionWrapper(() -> getListaCarreras()));

		view.getBtnInscripciones().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mostrarVentanaDatos();
			}
		});
		
		view.getBtnClasificacionesCategoria().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			
				int index = listaIds.get(view.getTablaCarreras().getSelectedRow());;
				mostrarVentanaClasificacionCategoria(index);
			}
		});

		view.getBtnClasificaciones().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mostrarVentanaDatosClasificacion();
			}
		});
		
		
		view.getBtnDorsales().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int idCarrera = listaIds.get(view.getTablaCarreras().getSelectedRow());
				CarreraEntity carrera = carreraModel.findCarrera(idCarrera);
				if(carrera.getFecha().compareTo(LocalDate.now().toString()) < 0) {
					JOptionPane.showMessageDialog(view, "La carrera ya se ha celebrado");
					return;
				}
				generarDorsales(idCarrera);
				listaDorsalAsignado.set(view.getTablaCarreras().getSelectedRow(), true);
			}
		});
		// En el caso del mouse listener (para detectar seleccion de una fila) no es un
		// interfaz funcional puesto que tiene varios metodos
		// ver discusion:
		// https://stackoverflow.com/questions/21833537/java-8-lambda-expressions-what-about-multiple-methods-in-nested-class
		view.getTablaCarreras().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				// no usa mouseClicked porque al establecer seleccion simple en la tabla de
				// carreras
				// el usuario podria arrastrar el raton por varias filas e interesa solo la
				// ultima

				SwingUtil.exceptionWrapper(() -> updateDetail());
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				int idCarrera = listaIds.get(view.getTablaCarreras().getSelectedRow());
				getParticipantes(idCarrera);
			}

		});

		
		view.getBtnAceptar().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				SwingUtil.exceptionWrapper(() -> openInscriptionView());
			}

			private void openInscriptionView() {
				JTable tabla = view.getTablaCarreras();
				String nombreCarrera = (String) tabla.getValueAt(tabla.getSelectedRow(), 0);
				int idCarrera = listaIds.get(tabla.getSelectedRow());
				new InscripcionController().init(nombreCarrera, idCarrera);
				;
			}
		});

		view.getBtnCrearCarrera().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				SwingUtil.exceptionWrapper(() -> openCrearCarreraView());
			}

			private void openCrearCarreraView() {
				new CrearCarrerasController().init();

			}
		});

		view.getBtnDevoluciones().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new DevolucionesController().init();
			}
		});
		;

	}


	protected void generarDorsales(int idCarrera) {
		if(!listaDorsalAsignado.get(view.getTablaCarreras().getSelectedRow())) {
			List<InscripcionEntity> inscripciones = inscripcionModel.findInscripcionesByIdCarrera(idCarrera);
			if(inscripciones.isEmpty()){
				JOptionPane.showMessageDialog(this.view, "No hay participantes a los que asignar dorsales");
				return;
			}
			
			LimiteDorsalesEntity limite = dorsalesModel.findLimite(idCarrera);
			CarreraEntity carrera = model.findCarrera(idCarrera);
			
			if(limite.getSecuencial().equals("secuencial")) {
				generarSecuencial(idCarrera, limite.getNumero(), carrera.getPlazas());
			}else {
				generarAleatorio(idCarrera, limite.getNumero(), carrera.getPlazas());
				
			}
		}else {
			JOptionPane.showMessageDialog(this.view, "Los dorsales ya han sido asignados");
		}
	
		
	}

	private void generarAleatorio(int idCarrera, int numero, int plazas) {
		Random r = new Random();
		
		int contador = r.nextInt(plazas-numero) + numero;
		
		ArrayList<Integer> ocupados = new ArrayList<Integer>();
		
		List<InscripcionEntity> inscripciones = inscripcionModel.findInscripcionesByIdCarrera(idCarrera);
		
		for(InscripcionEntity inscripcion: inscripciones) {
			while(ocupados.contains(contador)) {
				contador = r.nextInt(plazas-numero) + numero;
			}
			
			inscripcionModel.actualizarDorsal(inscripcion.getEmailAtleta(), idCarrera, contador);
			
			ocupados.add(contador);
			
			System.out.println("actualizado" + contador);
		}
	}

	private void generarSecuencial(int idCarrera, int numero, int plazas) {
		int contador = numero ;
		
		List<InscripcionEntity> inscripciones = inscripcionModel.findInscripcionesByIdCarrera(idCarrera);
		
		for(InscripcionEntity inscripcion: inscripciones) {
			inscripcionModel.actualizarDorsal(inscripcion.getEmailAtleta(), idCarrera, contador);
			contador++; 
			
			System.out.println("actualizado" + contador);
		
		}
	}

	protected void mostrarVentanaClasificacionCategoria(int index) {
		
		List<CategoriaEntity> listaCategorias = categoriaModel.findCategoriasCarrera(index);
		CarreraEntity carrera = carreraModel.findCarrera(index);
		
		if(carrera.getFechaCarrera().compareTo(LocalDate.now().toString()) > 0) {
			JOptionPane.showMessageDialog(this.view, "La carrera aun no se ha celebrado");
			return ;
		}
		
		
		if(listaCategorias == null) {
			JOptionPane.showMessageDialog(this.view, "La carrera no tiene asignadas categorias");
			return ;
		}
		
		if(!categorias(index)) {
			return;
		}
		ClasificacionesCategoriaView vp = new ClasificacionesCategoriaView(index);

		vp.setLocationRelativeTo(null);
		vp.setVisible(true);
		
		

	}

	
	protected boolean categorias(int index) {
		List<CategoriaEntity> listaCategorias = categoriaModel.findCategoriasCarrera(index);
		List<TiempoEntity> clasificacion = new ArrayList<TiempoEntity>();
		
		for(CategoriaEntity categoria: listaCategorias) {
			List<InscripcionEntity> inscripciones = inscripcionModel.findByCarreraCategoria(index, categoria.getIdCategoria());
			if(inscripciones == null) {
				break;
			}
			for(InscripcionEntity inscripcion: inscripciones) {
				List<TiempoEntity> tiempos = tiemposModel.findClasificacionForCarreraCategoria(index, inscripcion.getEmailAtleta());
				
				for(TiempoEntity tiempo: tiempos) {
					clasificacion.add(tiempo);
				}
			}
		}
		
		if(clasificacion.isEmpty()) {
			JOptionPane.showMessageDialog(view, "Aun no se ha procesado la clasificacion para ninguna categoria");
			return false;
		}
		
		return true;
	}
	
	protected void mostrarVentanaDatosClasificacion() {
		DatosClasificacionView vp = new DatosClasificacionView();

		vp.setLocationRelativeTo(null);
		vp.setVisible(true);
	}

//	protected void mostrarVentanaParticipantes() {
//		JTable tabla = view.getTablaCarreras();
//		int idCarrera = (Integer) tabla.getValueAt(tabla.getSelectedRow(), 0);
//
//		ParticipantesView vp = new ParticipantesView(idCarrera);
//	}

	protected void mostrarVentanaDatos() {

		DatosView vp = new DatosView();

		vp.setLocationRelativeTo(null);
		vp.setVisible(true);
	}

	public void initView() {
		// Inicializa la fecha de hoy a un valor que permitira mostrar carreras en
		// diferentes fases
		// y actualiza los datos de la vista
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String date = LocalDate.now().format(formatter);
		view.setFechaHoy(date);
		this.getListaCarreras();

		if (view.getTablaCarreras().getSelectedRow() != -1) {
			int index = (Integer) view.getTablaCarreras().getValueAt(view.getTablaCarreras().getSelectedRow(), 0);
			this.getParticipantes(index);
		}

		// Abre la ventana (sustituye al main generado por WindowBuilder)
		view.getFrame().setVisible(true);
	}

	/**
	 * 
	 * @return
	 */
	private void getParticipantes(int idCarrera) {

		AtletaModel am = new AtletaModel();
		InscripcionModel im;
		CategoriaModel cm;

		List<AtletaEntity> atletas = am.findAtletasCarrera(idCarrera);
		List<ParticipanteEntity> participantes = new ArrayList<ParticipanteEntity>();

//		for (AtletaEntity a : atletas) {
//			im = new InscripcionModel();
//			InscripcionEntity inscripcion = im.findInscripcion(a.getEmailAtleta(), idCarrera);
//
//			cm = new CategoriaModel();
//			CategoriaEntity categoria = cm.findCategoriaCarrera(idCarrera);
//			ParticipanteEntity participante = new ParticipanteEntity();
//			participante.setEmailAtleta(a.getEmailAtleta());
//			participante.setNombreAtleta(a.getNombre());
//			participante.setApellidoAtleta(a.getApellido());
//			participante.setNombreCategoria(categoria.getNombre());
//			participante.setEstado(inscripcion.getEstado());
//			participante.setIdCarrera(inscripcion.getIdCarrera());
//			participante.setDorsal(inscripcion.getDorsal());
//
//			participantes.add(participante);
//		}
		
		
			im = new InscripcionModel();
			//InscripcionEntity inscripcion = im.findInscripcion(a.getEmailAtleta(), idCarrera);
			List<CategoriaEntity> categorias = categoriaModel.findCategoriasCarrera(idCarrera);
			if(categorias != null) {
				
			
			for(CategoriaEntity categoria: categorias) {
				
				List<InscripcionEntity> inscripciones = inscripcionModel.findByCarreraCategoria(idCarrera, categoria.getIdCategoria());
				if(inscripciones == null) {
					break;
				}
				for(InscripcionEntity inscripcion: inscripciones ) {
					
						AtletaEntity atleta = atletaModel.findAtleta(inscripcion.getEmailAtleta());
					
						ParticipanteEntity participante = new ParticipanteEntity();
						participante.setEmailAtleta(atleta.getEmailAtleta());
						participante.setNombreAtleta(atleta.getNombre());
						participante.setApellidoAtleta(atleta.getApellido());
						participante.setNombreCategoria(categoria.getNombre());
						participante.setEstado(inscripcion.getEstado());
						participante.setIdCarrera(inscripcion.getIdCarrera());
						participante.setDorsal(inscripcion.getDorsal());
			
						participantes.add(participante);
					
				}
			}
		}
		

		TableModel tmodel = SwingUtil.getTableModelFromPojos(participantes,
				new String[] { "emailAtleta", "nombreAtleta", "idCarrera", "nombreCategoria", "estado", "dorsal" });

		view.getTablaParticipantes().setModel(tmodel);

		SwingUtil.autoAdjustColumns(view.getTablaParticipantes());

	}

	/**
	 * La obtencion de la lista de carreras solo necesita obtener la lista de
	 * objetos del modelo y usar metodo de SwingUtil para crear un tablemodel que se
	 * asigna finalmente a la tabla.
	 */
	public void getListaCarreras() {
		PlazosDeInscripcionModel sacarPrecio = new PlazosDeInscripcionModel();
		listaIds = new ArrayList<Integer>();
		listaDorsalAsignado = new ArrayList<Boolean>();
		
		
		List<CarreraEntity> carreras = model.getListaCarreras(view.getFechaHoy());
		for (CarreraEntity carrera : carreras) {
			int plazasOcupadas = atletaModel.findAtletasParticipantesEnCarrera(carrera.getIdCarrera());
			PlazosDeInscripcionEntity precio = sacarPrecio.getListaPlazosInscripciones(carrera.getIdCarrera(),
					view.getFechaHoy());
			if (precio != null) {
				carrera.setPrecio(Double.toString(precio.getPrecio()));
			} else {
				carrera.setPrecio("No disponible");
			}

			if (plazasOcupadas > 0) {
				carrera.setPlazas(carrera.getPlazas() - plazasOcupadas);
			}
			listaIds.add(carrera.getIdCarrera());
			listaDorsalAsignado.add(false);
		}
		TableModel tmodel = SwingUtil.getTableModelFromPojos(carreras,
				new String[] { "nombre", "fecha", "tipo", "distancia", "plazas", "precio" });
		view.getTablaCarreras().setModel(tmodel);
		SwingUtil.autoAdjustColumns(view.getTablaCarreras());

		// Como se guarda la clave del ultimo elemento seleccionado, restaura la
		// seleccion de los detalles
		this.restoreDetail();
	}

	/**
	 * Restaura la informacion del detalle de la carrera para visualizar los valores
	 * correspondientes a la ultima clave almacenada.
	 */
	public void restoreDetail() {
		// Utiliza la ultimo valor de la clave (que se reiniciara si ya no existe en la
		// tabla)
		this.lastSelectedKey = SwingUtil.selectAndGetSelectedKey(view.getTablaCarreras(), this.lastSelectedKey);
		// Si hay clave para seleccionar en la tabla muestra el detalle, si no, lo
		// reinicia
	}

	/**
	 * Al seleccionar un item de la tabla muestra el detalle con el valor del
	 * porcentaje de descuento de la carrera seleccinada y los valores de esta
	 * entidad
	 */
	public void updateDetail() {
		// Obtiene la clave seleccinada y la guarda para recordar la seleccion en
		// futuras interacciones
//        this.lastSelectedKey = SwingUtil.getSelectedKey(view.getTablaCarreras());
		view.getBtnAceptar().setEnabled(true);
		view.getBtnClasificacionesCategoria().setEnabled(true);
	}

}
