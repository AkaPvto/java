import java.util.ArrayList;
import java.io.*;

public class Repes{	
	public static void main(String[] args){
		if(args.length < 1){
			//System.out.println("Tienes que pasarle parametros al asunto tio");
		}
		else{
			for(int i = 0;i<args.length;i++){
				System.out.println("Parametro " + i + ": " + args[i]);
			}
		}
		if(args.length > 0){
			switch(args[0]){
				case "1":
					// Los valores que quieres meter en el arrayLiss
					ArrayList<Integer> ids_web = readCSV("../Farma10/data/reference_all_prods_fixed.csv");
					ArrayList<Integer> ids_alm = readCSV("../Farma10/data/prod_almacen.csv");
					ArrayList<Integer> ids_notin = new ArrayList<Integer>();
					System.out.println(ids_web.size());
					System.out.println(ids_alm.size());
					Boolean isIn = false;
					int cont = 0;
					for(int j = 0; j < ids_alm.size();j++){
						for(int i = 0; i<ids_web.size();i++){
							cont++;
							if(ids_web.get(i).equals(ids_alm.get(j))){
								isIn = true;
							}
						}
						if(!isIn){
							ids_notin.add(ids_alm.get(j));
						}
						else{
							isIn = false;
						}
					}


					System.out.println("\nTotal productos que no estan en el almacen: " + ids_notin.size());
					System.out.println("\nTotal iteraciones: " + cont);
					writeCSV("../Farma10/data/prods_not_alm.csv", ids_notin);
					break;
				case "2":
					if(args.length != 2){ 
						System.out.println("Asegurate de pasar los parámetros correctos");
						break;
					}
					else{

					}
					break;
				case "3":
					System.out.println("El modo 3 todavia no esta implementado");
					break;
				case "4":
					System.out.println("El modo 4 todavia no esta implementado");
					break;
				default:
					System.out.println("\n##############################################################################################");
					System.out.println("Este programa tiene distintas funciones, \ntienes que elegir que quieres que haga pasandole como primer argumento (1, 2, 3, 4)");
					System.out.println("##############################################################################################");
					System.out.println("Menu del programa:");
					System.out.println("  - 1: Todavia no implementado");
					System.out.println("  - 2: Pase por parametro la ruta total o parcial del csv y un string con la parametrizacion (ej: primera columna Integer, segunda columna String, tercera columna Booleano ==> (I,S,B)");
					System.out.println("  - 3: Todavia no implementado");
					System.out.println("  - 4: Todavia no implementado");

					break;	
			}

		}
		else{
			System.out.println("\n##############################################################################################");
			System.out.println("Este programa tiene distintas funciones, \ntienes que elegir que quieres que haga pasandole como primer argumento (1, 2, 3, 4)");
			System.out.println("##############################################################################################");
			System.out.println("Menu del programa:");
			System.out.println("  - 1: Todavia no implementado");
			System.out.println("  - 2: Pase por parametro la ruta total o parcial del csv y un string con la parametrizacion (ej: primera columna Integer, segunda columna String, tercera columna Booleano ==> (I,S,B)");
			System.out.println("  - 3: Todavia no implementado");
			System.out.println("  - 4: Todavia no implementado");
		}
		
	}

	public static ArrayList<Integer> readCSV(String route){
		ArrayList<Integer> arrayName = new ArrayList<Integer>();
		try{

			File file = new File(route);
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);

			String line;
			while((line=br.readLine())!=null){
				arrayName.add(Integer.parseInt(line));
			}
		}catch(Exception e){
			System.out.println("Exception: " + e.toString());
		}
		return arrayName;
	}

	public static ArrayList<String[]> readFullCSV(String route){
		ArrayList<String[]> content = new ArrayList<String[]>();
		try{

			File file = new File(route);
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);

			String line;
			while((line=br.readLine())!=null){
				content.add(line.split(";"));
			}
		}catch(Exception e){
			System.out.println("Exception: " + e.toString());
		}
		return content;
	}

	public static void writeCSV(String route, ArrayList<Integer> content){
		FileWriter file = null;
		try{
			file = new FileWriter(route);
			PrintWriter pw = new PrintWriter(file);

			pw.println("\"Id_producto\"");

			for(int i = 0; i< content.size(); i++){
				pw.println("\"" + content.get(i) + "\"");
			}
		}catch(Exception e){
			System.out.println("Exception: " + e.toString());
		}finally{
			try{
				if(file != null)
					file.close();
			}catch(Exception e){
				System.out.println("Exception: " + e.toString());
			}
		}
	}
}
