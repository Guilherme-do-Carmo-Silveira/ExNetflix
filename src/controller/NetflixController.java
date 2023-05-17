package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import docarmo.FilaObject.Fila;
import docarmo.ListaObject.ListaEncadeadaObj;
import model.serie;

public class NetflixController implements INetflixController {

	ListaEncadeadaObj[] tabelahash;

	public NetflixController() {
		tabelahash = new ListaEncadeadaObj[7];
		IniciaTabHash();
	}

	public void MajorGenre() {

		try {
			GeraFila();
			SeparaRenovada();
			GeraLista();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void GeraFila() throws IOException {

		File arq = new File("C:\\Temp\\NetflixSeries.csv");
		if (arq.exists() && arq.isFile()) {
			FileInputStream fluxo = new FileInputStream(arq);
			InputStreamReader leitorfluxo = new InputStreamReader(fluxo);
			BufferedReader buffer = new BufferedReader(leitorfluxo);
			String linha = buffer.readLine();
			linha = buffer.readLine();
			while (linha != null) {
				String vtlinha[] = linha.split(";");
				String genero = vtlinha[0];
				Fila fila = new Fila();
				while (genero.contains(vtlinha[0]) && linha != null) {
					serie s = new serie();
					s.major_genre = vtlinha[0];
					s.title = vtlinha[1];
					s.subgenre = vtlinha[2];
					s.premiere_year = Integer.parseInt(vtlinha[4]);
					s.episodes = (vtlinha[10]);
					s.status = vtlinha[6];
					s.imdb_rating = Integer.parseInt(vtlinha[11]);
					fila.insert(s);
					linha = buffer.readLine();
					if (linha != null) {
						vtlinha = linha.split(";");
					}
				}
				GeraArquivo(fila, genero);
			}
		} else {
			throw new IOException("Arquivo invalido");
		}
	}

	private void GeraArquivo(Fila fila, String genero) throws IOException {

		File file = new File("C:\\Temp", genero + ".csv");
		StringBuffer buffer = new StringBuffer();
		int tam = fila.size();

		for (int i = 0; i < tam; i++) {
			serie s = new serie();
			try {
				if (i == 0) {
					buffer.append("Genero principal; Titulo; Subgenero; Ano; Episodios; Status; Nota" + "\n");
					FileWriter filewrite = new FileWriter(file);
					PrintWriter print = new PrintWriter(filewrite);
					print.write(buffer.toString());
					print.flush();
					print.close();
				}
				s = (serie) fila.remove();
				buffer.append(s);
				FileWriter filewrite = new FileWriter(file);
				PrintWriter print = new PrintWriter(filewrite);
				print.write(buffer.toString());
				print.flush();
				print.close();
				filewrite.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void SeparaRenovada() throws IOException {

		String status = "";
		int ano = 0;

		File arq = new File("C:\\Temp\\NetflixSeries.csv");
		if (arq.exists() && arq.isFile()) {
			FileInputStream fluxo = new FileInputStream(arq);
			InputStreamReader leitorfluxo = new InputStreamReader(fluxo);
			BufferedReader buffer = new BufferedReader(leitorfluxo);
			String linha = buffer.readLine();
			linha = buffer.readLine();
			ListaEncadeadaObj lista = new ListaEncadeadaObj();
			while (linha != null) {
				String vtlinha[] = linha.split(";");
				status = vtlinha[6];
				if (status.equals("Renewed")) {
					serie s = new serie();
					s.major_genre = vtlinha[0];
					s.title = vtlinha[1];
					s.subgenre = vtlinha[2];
					s.premiere_year = Integer.parseInt(vtlinha[4]);
					s.episodes = (vtlinha[10]);
					s.status = vtlinha[6];
					s.imdb_rating = Integer.parseInt(vtlinha[11]);
					if (lista.isEmpty()) {
						lista.addFirst(s);
					} else {
						try {
							lista.addLast(s);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				linha = buffer.readLine();
			}
			status = "Status";
			GeraArquivoRenovada(lista, status);
			organizaAno(lista);
		} else {
			throw new IOException("Arquivo invalido");
		}
	}

	private void GeraArquivoRenovada(ListaEncadeadaObj lista, String status) throws IOException {

		File file = new File("C:\\Temp", status + ".csv");
		StringBuffer buffer = new StringBuffer();
		int tam = lista.size();

		buffer.append("Genero principal; Titulo; Subgenero; Ano; Episodios; Status; Nota" + "\n");
		FileWriter filewrite = new FileWriter(file);
		PrintWriter print = new PrintWriter(filewrite);
		print.write(buffer.toString());
		print.flush();
		print.close();

		for (int i = 0; i < tam; i++) {
			serie s = new serie();
			try {
				s = (serie) lista.get(i);
				buffer.append(s);
				filewrite = new FileWriter(file);
				print = new PrintWriter(filewrite);
				print.write(buffer.toString());
				print.flush();
				print.close();
				filewrite.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void organizaAno(ListaEncadeadaObj lista) {

		ListaEncadeadaObj laux = new ListaEncadeadaObj();

		laux = lista;

		int tam = lista.size();
		int ano = 0;

		while (!laux.isEmpty()) {
			ListaEncadeadaObj lgrava = new ListaEncadeadaObj();
			serie s = new serie();
			try {
				s = (serie) laux.get(0);
				ano = s.premiere_year;
			} catch (Exception e) {
				e.printStackTrace();
			}

			for (int i = 0; i < tam; i++) {
				serie s1 = new serie();
				try {
					s1 = (serie) lista.get(i);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (ano == s1.premiere_year) {
					if (lgrava.isEmpty()) {
						lgrava.addFirst(s1);
						try {
							laux.remove(i);
							tam = laux.size();
							i--;
						} catch (Exception e) {
							e.printStackTrace();
						}

					} else {
						try {
							lgrava.addLast(s1);
							laux.remove(i);
							tam = laux.size();
							i--;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
			try {
				gravaano(lgrava, ano);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void gravaano(ListaEncadeadaObj lgrava, int ano) throws IOException {
		File file = new File("C:\\Temp", ano + ".csv");
		StringBuffer buffer = new StringBuffer();
		int tam = lgrava.size();

		buffer.append("Genero principal; Titulo; Subgenero; Ano; Episodios; Status; Nota" + "\n");
		FileWriter filewrite = new FileWriter(file);
		PrintWriter print = new PrintWriter(filewrite);
		print.write(buffer.toString());
		print.flush();
		print.close();

		for (int i = 0; i < tam; i++) {
			serie s = new serie();
			try {
				s = (serie) lgrava.get(i);
				buffer.append(s);
				filewrite = new FileWriter(file);
				print = new PrintWriter(filewrite);
				print.write(buffer.toString());
				print.flush();
				print.close();
				filewrite.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void GeraLista() throws IOException {

		File arq = new File("C:\\Temp\\NetflixSeries.csv");

		if (arq.exists() && arq.isFile()) {
			FileInputStream fluxo = new FileInputStream(arq);
			InputStreamReader leitorfluxo = new InputStreamReader(fluxo);
			BufferedReader buffer = new BufferedReader(leitorfluxo);
			String linha = buffer.readLine();
			linha = buffer.readLine();
			ListaEncadeadaObj lista = new ListaEncadeadaObj();
			while (linha != null) {
				String vtlinha[] = linha.split(";");
				serie s = new serie();
				s.major_genre = vtlinha[0];
				s.title = vtlinha[1];
				s.subgenre = vtlinha[2];
				s.premiere_year = Integer.parseInt(vtlinha[4]);
				s.episodes = (vtlinha[10]);
				s.status = vtlinha[6];
				s.imdb_rating = Integer.parseInt(vtlinha[11]);
				if (lista.isEmpty()) {
					lista.addFirst(s);
				} else {
					try {
						lista.addLast(s);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				linha = buffer.readLine();
			}
			SeparaNota(lista);
		} else {
			throw new IOException("Arquivo invalido");
		}
	}

	private void IniciaTabHash() {
		int tam = tabelahash.length;
		for (int i = 0; i < tam; i++) {
			tabelahash[i] = new ListaEncadeadaObj();
		}
	}

	private void SeparaNota(ListaEncadeadaObj lista) throws IOException {
		int posicao = 0, valor = 0;
		int tam = lista.size();
		for (int i = 0; i < tam; i++) {
			serie s = new serie();
			try {
				s = (serie) lista.get(i);
				posicao = s.imdb_rating;
				valor = HashCode(posicao);
				if (tabelahash[valor].isEmpty()) {
					tabelahash[valor].addFirst(s);
				} else {
					tabelahash[valor].addLast(s);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public int HashCode(int posicao) {
		return posicao = (int) (posicao / 15.1);
	}
	
	public void MostraNota(int valor) {
		int tam = tabelahash[valor].size();
		System.out.println("Filmes com Estrela " + valor + "\n");
		for(int i = 0; i < tam; i++) {
			serie s = new serie();
			try {
				s = (serie) tabelahash[valor].get(i);
				System.out.println(s);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("======================================================================================");
	}
}