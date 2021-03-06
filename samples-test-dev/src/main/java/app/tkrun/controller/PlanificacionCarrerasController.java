package app.tkrun.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;

import app.tkrun.entities.PlazosDeInscripcionEntity;
import app.tkrun.model.PlazosDeInscripcionModel;
import app.tkrun.view.PlanificacionCarrerasView;
import app.util.SwingUtil;

public class PlanificacionCarrerasController {

	PlanificacionCarrerasView pcv;
	InscripcionController ic = new InscripcionController();
	PlazosDeInscripcionModel pdiModel = new PlazosDeInscripcionModel();
	private String fechaCelebracion;
	private int id;

	public void init(String fechaCelebracion, int id) {
		this.fechaCelebracion = fechaCelebracion;
		this.id = id;
		pcv = new PlanificacionCarrerasView();
		PlazosDeInscripcionEntity entity = new PlazosDeInscripcionEntity();
		entity = pdiModel.getListaPlazosInscripcionesPorFechaFin(id);
		if (entity != null) {

			SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd");
			Date fechaInscrionFinal = null;
			try {
				fechaInscrionFinal = formatoDelTexto.parse(entity.getFechaFin());
			} catch (ParseException e1) {
				e1.printStackTrace();
			}

			Calendar c = Calendar.getInstance();
			c.setTime(fechaInscrionFinal);
			c.add(Calendar.DATE, 1);
			fechaInscrionFinal = c.getTime();

			pcv.getTextFieldInscripcionInicio().setText(formatoDelTexto.format(fechaInscrionFinal));
			pcv.getTextFieldInscripcionInicio().setEditable(false);
			
		}

		pcv.getBtnAdd().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (comprobarCampos() && comprobarFechas() && comprobarFecha() ) {
					if (pdiModel.findTodosLosPlazosParaUnaCarrera(id) < 4) {
						Random r = new Random();
						int idInscripcion = r.nextInt(100000000) + 1;
						PlazosDeInscripcionEntity plazoInscripcion = new PlazosDeInscripcionEntity();
						plazoInscripcion.setIdCarrera(id);
						plazoInscripcion.setIdPlazosDeInscripcion(idInscripcion);
						plazoInscripcion.setFechaInicio(pcv.getTextFieldInscripcionInicio().getText());
						plazoInscripcion.setFechaFin(pcv.getTextFieldInscripcionFin().getText());
						plazoInscripcion.setPrecio(Double.parseDouble(pcv.getTextFieldInscripcionPrecio().getText()));

						pdiModel.addInscripcion(plazoInscripcion);
						

						SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd");
						Date fechaInscrionFinal = null;
						try {
							fechaInscrionFinal = formatoDelTexto.parse(pcv.getTextFieldInscripcionFin().getText());
						} catch (ParseException e1) {
							e1.printStackTrace();
						}

						Calendar c = Calendar.getInstance();
						c.setTime(fechaInscrionFinal);
						c.add(Calendar.DATE, 1);
						fechaInscrionFinal = c.getTime();

						pcv.getTextFieldInscripcionInicio().setText(formatoDelTexto.format(fechaInscrionFinal));
						pcv.getTextFieldInscripcionInicio().setEditable(false);
						pcv.getTextFieldInscripcionFin().setText("");
						pcv.getTextFieldInscripcionPrecio().setText("");
					} else {
						JOptionPane.showMessageDialog(null, "No se pueden meter mas de 4 plazos");
					}

				} else {
					JOptionPane.showMessageDialog(null, "Valide sus campos");
				}
			}
		});

		pcv.getBtnCancelar().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pcv.dispose();
			}
		});
		pcv.getBtnRefrescar().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				 getPlazos();
			}
		});
		
		

		pcv.setModal(true);
		pcv.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		pcv.setLocationRelativeTo(null);
		pcv.setVisible(true);
	}

	private boolean comprobarCampos() {
		if (pcv.getTextFieldInscripcionFin().getText().isEmpty() || pcv.getTextFieldInscripcionInicio().getText().isEmpty()
				|| pcv.getTextFieldInscripcionPrecio().getText().isEmpty()) {
			return false;
		}
		return true;
	}

	private boolean comprobarFechas() {
		String fechaCelebracionCarrea = fechaCelebracion;
		String fechaInscrionIncio = pcv.getTextFieldInscripcionInicio().getText();
		String fechaInscrionFinal = pcv.getTextFieldInscripcionFin().getText();

		if (fechaInscrionIncio.compareTo(fechaCelebracionCarrea) < 0
				&& fechaInscrionFinal.compareTo(fechaCelebracionCarrea) < 0
				&& fechaInscrionIncio.compareTo(fechaInscrionFinal) < 0) {
			return true;
		}

		return false;
	}
	
	private void getPlazos() {

		List<PlazosDeInscripcionEntity> plazos = pdiModel.getPlazos(id);

		TableModel tmodel = SwingUtil.getTableModelFromPojos(plazos,
				new String[] { "fechaInicio", "fechaFin", "precio"});

		pcv.getTable().setModel(tmodel);
		SwingUtil.autoAdjustColumns(pcv.getTable());

	}
	
	private boolean comprobarFecha() {

		if ( pcv.getTextFieldInscripcionInicio().getText().isEmpty()) {
			return false;
		}
		Date fechaActual = new Date(System.currentTimeMillis());
		Calendar cal = Calendar.getInstance();
		cal.setTime(fechaActual);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date fechaActualTruncada = new Date(cal.getTimeInMillis());

		String fechaInicio =  pcv.getTextFieldInscripcionInicio().getText();
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
		Date fechaInicioDate = null;
		try {
			fechaInicioDate = date.parse(fechaInicio);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if (fechaInicioDate.after(fechaActualTruncada) || fechaInicioDate.equals(fechaActualTruncada)) {
			return true;
		} else {
			return false;
		}
	}


//	private boolean comprobarFechas() {
//		SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd");
//		Date fechaCelebracionCarrea = null;
//		Date fechaInscrionIncio = null;
//		Date fechaInscrionFinal = null;
//		try {
//
//			fechaCelebracionCarrea = formatoDelTexto.parse(fechaCelebracion);
//			fechaInscrionIncio = formatoDelTexto.parse((pcv.getTextFieldInscripcionInicio().getText()));
//			fechaInscrionFinal = formatoDelTexto.parse(pcv.getTextFieldInscripcionFin().getText());
//
//		} catch (ParseException ex) {
//
//			ex.printStackTrace();
//
//		}
//		if (fechaInscrionIncio.before(fechaCelebracionCarrea) && fechaInscrionFinal.before(fechaCelebracionCarrea)
//				&& fechaInscrionIncio.before(fechaInscrionFinal)) {
//			return true;
//		}
//
//		return false;
//	}

}
