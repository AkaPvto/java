import java.util.ArrayList;
import java.io.*;
import java.awt.datatransfer.*;
import java.awt.Toolkit;

public class CSVObject {
    private int nColumns;
    private String[] columnHead;
    private String[] typeColumns;
    private ArrayList<String[]> content;
    private String columnSeparator;
    private String tuplaContainer;


    
    public CSVObject(String route, String[] types, String cSeparator, String tConainer){
        content = new ArrayList<String[]>();

        if(cSeparator != null && cSeparator != "") columnSeparator = cSeparator;
        else columnSeparator = ";";

        if(tConainer != null) tuplaContainer = tConainer;
        else tuplaContainer = "\"";

        if(types != null && types.length > 0) typeColumns = types;
        

        if(!readCSV(route)) nColumns = 0;
            
        
    }

    public int getNumColumns(){
        return nColumns;
    }

    public String getColumnSeparator(){
        return columnSeparator;
    }

    public String getTuplaContainer(){
        return tuplaContainer;
    }

    public void setColumnsSeparator(String columnSeparator){
        if(columnSeparator != null && columnSeparator != "") this.columnSeparator = columnSeparator;
    }

    public void setTuplaContainer(String tuplaContainer){
        if(tuplaContainer != null) this.tuplaContainer = tuplaContainer;
    }

    public String parseSQL(boolean createDB, int primaryKeyPos){
        String sqlInsert = "";

        // En caso de especificarlo, se creara una nueva base de datos
        if(createDB){
            sqlInsert = "DROP DATABASE 'Auxiliar';\nCREATE DATABASE 'Auxiliar';\n";
        }
        
        // ##################### CREAR TABLA #########################
        sqlInsert +="CREATE TABLE 'auxiliar'(";
        for(int i=0; i<nColumns;i++){
            // Sentencia de la base de datos con las columnas del contenido del csv
            // Si no se han definido tipos todo seran VARCHAR(255)
            
            sqlInsert += columnHead[i]+" ";
            if(typeColumns.length < 1){
                sqlInsert += "varchar(255)";
            }
            else{
                switch (typeColumns[i]){
                    case "I":
                        sqlInsert += "int";
                        break;
                    
                    case "B":
                        sqlInsert += "boolean";
                        break;
    
                    case "S":
                        sqlInsert += "varchar(255)";
                        break;
    
                    case "L":
                        sqlInsert += "varchar(1000)";
                        break;
    
                    case "SL":
                        sqlInsert += "varchar(2000)";
                        break;
    
                    default:
                        sqlInsert += "varchar(255)";
                        break;
                }
            }
            
            // Se coloca la clave primaria indicada en el metodo
            // Si no se ha indicado correctamente, el primer parametro sera la clave primaria
            if(primaryKeyPos >= 0 && primaryKeyPos < nColumns){
                if(i == primaryKeyPos) sqlInsert += " PRIMARY KEY";
            }
            else{
                if(i == 0) sqlInsert += " PRIMARY KEY";
            }

            // Separadores
            if(i != nColumns - 1) sqlInsert += ", ";
        }
        sqlInsert +=");\n";
        // ##################### FIN DE CREAR TABLA #########################


        // ##################### INSERTAR VALORES #########################

        // Inicio de la sentencia indicando la tabla y columnas en la que vamos a insertar
        sqlInsert += "INSERT INTO 'auxiliar'(";
        for(int i = 0; i<nColumns; i++){
            sqlInsert += columnHead[i];
            if(i!= nColumns - 1) sqlInsert += ", ";
        }
        sqlInsert += ") ";


        // Inisio de la sentencia de valores
        sqlInsert += "VALUES ";
        // Se van a ir generando Strings con estructura: "(column1, column2, ...)" y concatenando por cada tupla
        String tupla = "";
        for(int i=0; i<content.size(); i++){
            tupla = "(";
            for(int j=0; j<nColumns; j++){
                // En caso de que el contenido NO venga contemplado entre ningun caracter especial (como \" o \')
                if(tuplaContainer.equals("")){
                    // Para los enteros y booleanos no es necesario meterlo entre ''
                    if(typeColumns[j].equals("I") || typeColumns[j].equals("B")){
                        tupla+=content.get(i)[j];
                    }
                    // Para las cadenas de texto es necesario meterlas entre ''
                    else{
                        tupla+="'"+content.get(i)[j]+"'";
                    }
                }
                // En caso de que el contenido venga contemplado entre ningun caracter especial (como \" o \')
                else{
                    // Para los enteros y booleanos no es necesario meterlo entre ''
                    if(typeColumns[j].equals("I") || typeColumns[j].equals("B")){
                        tupla+=content.get(i)[j].split(tuplaContainer)[1];
                    }
                    // Para las cadenas de texto es necesario meterlas entre ''
                    else{
                        tupla+="'"+content.get(i)[j].split(tuplaContainer)[1]+"'";
                    }
                }
                // Separadores
                if(j != nColumns - 1 ) tupla += ", ";
            }
            tupla += ")";
            //Separadores
            if(i != content.size() - 1) tupla += ", ";

            // Incluimos la tupla (column1, column2, ...) en la sentencia general
            sqlInsert += tupla;
        }
        // ##################### INSERTAR VALORES #########################

        return sqlInsert;
    }

    // Se le pasa por parametro la ruta de un CSV y rellena el objeto correspondiente con el contenido del csv
    private boolean readCSV(String route){
        boolean succes = false;
        File file = null;
        FileReader fr = null;
        BufferedReader br = null;
        try {
            file = new File(route);
            fr = new FileReader(file);
            br = new BufferedReader(fr);

            String line;

            columnHead = br.readLine().split(columnSeparator);

            nColumns = columnHead.length;
            
            // Mientras hayan lineas que seguir leyendo
            while((line = br.readLine())!=null){
                content.add(line.split(columnSeparator));
                
            }

            if(content.size()>0) succes = true;

        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }finally{
			try{
				if(file != null)
					br.close();
			}catch(Exception e){
				System.out.println("Error: " + e.toString());
			}
		}

        return succes;
    }

    // Utiliza el contenido del objeto y los parametros que se pasan para escribir un csv en la ruta definida
    public boolean writeCSV(String route, String tContainer, String cSeparator){
        boolean succes = false;
		FileWriter file = null;
        PrintWriter pw = null;
		try{
			file = new FileWriter(route);
			pw = new PrintWriter(file);


            String fila = "";

            // Se guardan en fila los nombres de las columnas concatenados con el formato correspondiente
            for(int i = 0; i<nColumns; i++){
                if(tContainer != null) fila=tContainer+columnHead[i]+tContainer;
                else fila=tuplaContainer+columnHead[i]+tuplaContainer;

                // Separadores
                if(i!= nColumns - 1){
                    if(cSeparator != null && cSeparator != "") fila += cSeparator;
                    else fila += columnSeparator;
                }
            }
            // Se incluyen en el archivo la fila preparada
            pw.println(fila);

            // Recorre el contenido del csv y crea filas con el formato correspondiente que va insertando una a una en el archivo
			for(int i=0; i<content.size(); i++){
                for(int j=0; j<nColumns; j++){
                    // Se prepara la fila
                    if(tContainer != null) fila=tContainer+content.get(i)[j]+tContainer;
                    else fila=tuplaContainer+content.get(i)[j]+tuplaContainer;
                    
                    // Separadores
                    if(j != nColumns - 1 ){
                        if(cSeparator != null && cSeparator != "") fila += cSeparator;
                        else fila += columnSeparator;
                    }
                }
                // Se incluyen en el archivo la fila preparada
                pw.println(fila);
            }

            succes = true;
		}catch(Exception e){
			System.out.println("Error: " + e.toString());
		}finally{
			try{
				if(file != null)
					file.close();

                if(pw != null)
                    pw.close();
			}catch(Exception e){
				System.out.println("Error: " + e.toString());
			}
		}
        return succes;
	}
    
    public void copy(String copyText){
		StringSelection stringSelection = new StringSelection(copyText);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, stringSelection);
	}

    public void cmd(String cmdLine){
        try {
	        String cmd = "cat archivo_prueba.txt | clip.exe"; //Comando de apagado en linux
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            System.out.println (e);
        }
    }
}
