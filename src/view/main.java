package view;

import javax.swing.JOptionPane;

import controller.NetflixController;

public class main {

	public static void main(String[] args) {
		NetflixController n = new NetflixController();
		n.MajorGenre();

		int opc = 0;
		while (opc != 9) {
			opc = Integer.parseInt(JOptionPane.showInputDialog("1 - Nota IMBD \n9 - Finaliza"));
			switch (opc) {
			case 1:
				int valor = Integer.parseInt(JOptionPane.showInputDialog("Digite um valor de 0 a 6"));
				while (valor < 0 || valor > 6) {
					valor = Integer.parseInt(JOptionPane.showInputDialog("Valor invalido, Por Favor Digite um valor de 0 a 6"));
				}
				n.MostraNota(valor); // CHAMADA FUNÇÃO
				break;
			case 9:
				JOptionPane.showMessageDialog(null, "Programa finalizado");
				break;
			default:
				JOptionPane.showMessageDialog(null, "Opção Inválida");
			} // FECHA SWITCH
		}
	}

}