package app.tkrun.view;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;

public class PanelCategoria extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField textFieldNombre;
	private JComboBox<String> comboBoxSexo;
	private JSpinner spinnerEdadInicial;
	private JSpinner spinnerEdadFinal;
	private JButton btnDelete;

	/**
	 * Create the panel.
	 */
	public PanelCategoria() {
		
		JLabel lblName = new JLabel("Nombre: ");
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 14));
		add(lblName);
		
		textFieldNombre = new JTextField();
		textFieldNombre.setFont(new Font("Tahoma", Font.PLAIN, 14));
		add(textFieldNombre);
		textFieldNombre.setColumns(10);
		
		JLabel lblSex = new JLabel("Sexo: ");
		lblSex.setFont(new Font("Tahoma", Font.PLAIN, 14));
		add(lblSex);
		
		comboBoxSexo = new JComboBox<String>();
		comboBoxSexo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		comboBoxSexo.setModel(new DefaultComboBoxModel<String>(new String[] {"HOMBRE", "MUJER"}));
		add(comboBoxSexo);
		
		JLabel lblEdadInicial = new JLabel("Edad inicial:");
		lblEdadInicial.setFont(new Font("Tahoma", Font.PLAIN, 14));
		add(lblEdadInicial);
		
		spinnerEdadInicial = new JSpinner();
		spinnerEdadInicial.setFont(new Font("Tahoma", Font.PLAIN, 14));
		spinnerEdadInicial.setModel(new SpinnerNumberModel(18, 18, null, 1));
		add(spinnerEdadInicial);
		
		JLabel lblEdadFinal = new JLabel("Edad final: ");
		lblEdadFinal.setFont(new Font("Tahoma", Font.PLAIN, 14));
		add(lblEdadFinal);
		
		spinnerEdadFinal = new JSpinner();
		spinnerEdadFinal.setFont(new Font("Tahoma", Font.PLAIN, 14));
		spinnerEdadFinal.setModel(new SpinnerNumberModel(18, 18, 100, 1));
		add(spinnerEdadFinal);
		
		add(getBtnDelete());

	}
	
	public String getNombre() {
		return textFieldNombre.getText();
	}
	
	public String getSexo() {
		return comboBoxSexo.getSelectedItem().toString();
	}
	
	public int getEdadInicial() {
		return (Integer)spinnerEdadInicial.getValue();
	}
	
	public int getEdadFinal() {
		return (Integer)spinnerEdadFinal.getValue();
	}
	
	public JButton getBtnDelete() {
		if(btnDelete == null) {
			btnDelete = new JButton("X");
		}
		return btnDelete;
	}

}
