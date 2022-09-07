import java.io.*;
import java.lang.StringBuilder;
import java.util.HashMap;

public class Normalizer{

    private HashMap<Character, Character> normalizerMap;


    public Normalizer(){
        normalizerMap = new HashMap<Character, Character>();
        normalizerMap.put('á', 'a');
        normalizerMap.put('é', 'e');
        normalizerMap.put('í', 'i');
        normalizerMap.put('ó', 'o');
        normalizerMap.put('ú', 'u');
        normalizerMap.put('ñ', 'n');
        normalizerMap.put('Á', 'A');
        normalizerMap.put('É', 'E');
        normalizerMap.put('Í', 'I');
        normalizerMap.put('Ó', 'O');
        normalizerMap.put('Ú', 'U');
        normalizerMap.put('Ñ', 'N');
    }

    public String normalizeString(String uglyText){
        String beautyText = "";
        StringBuilder beautySB = null;
        if(uglyText != null && uglyText != ""){ 
            beautySB = new StringBuilder(uglyText);

            for(int i=0; i< uglyText.length(); i++){
                if(normalizerMap.get(uglyText.charAt(i)) != null) beautySB.setCharAt(i, normalizerMap.get(uglyText.charAt(i)));
            }
            beautyText = beautySB.toString();
        }
        return beautyText;
    }

    public boolean normalizeDir(String dir){
        boolean success = false;
        File folder = null;
        File[] folderContent = null;
        try{
            folder = new File(dir);
            if(folder.isDirectory()){
                folderContent = folder.listFiles();
                for(File file : folderContent){
                    if(file.isFile()){
                        String normalizado = normalizeString(file.getName());
                        success = renameFile(dir, file.getName(), (normalizado != "") ? normalizado : file.getName());
                    }
                }
            }
            
        }catch(Exception e){
            System.out.println("Error: " + e.toString());
        }

        return success;
    }

    public boolean renameFile(String routeDir, String oldName, String newName){
        boolean success = false;
        try{
            File oldFile = new File(routeDir + oldName);
            File newFile = new File(routeDir + newName);

            success = oldFile.renameTo(newFile);

        }catch(Exception e){
            System.out.println("Error: " + e.toString());
        }
        return success;
    }
}