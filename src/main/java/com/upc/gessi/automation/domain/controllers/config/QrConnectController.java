package com.upc.gessi.automation.domain.controllers.config;

import org.springframework.stereotype.Controller;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class QrConnectController {
    public Integer getLastTask(String subject,String type){
        String path = "home/connect/run/config/"+type+"_" + subject+"/"+type+".properties";

        Integer num_teams = 0;

        try (BufferedReader reader= new BufferedReader(new FileReader(path))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if(line.startsWith(type+".teams.num")){
                    String[] parts = line.split("=");
                    if(parts.length ==2){
                        num_teams = Integer.parseInt(parts[1].trim());

                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return num_teams;
    }

    public Boolean configure_script(String name,String subject,String type){
        String path = "home/connect/run/scripts/run."+type+"_"+subject+".sh";

        try {
            // Leer el archivo de script Bash
            BufferedReader reader = new BufferedReader(new FileReader(path));
            StringBuilder scriptContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("COMMITS_COLLECTION_NAME=(") && !line.contains(subject+"_"+name)) {
                    int endIndex = line.indexOf(')');
                    if (endIndex != -1) {
                        line = line.substring(0, endIndex) + " \"" + type+"_"+subject+"_"+name+".commits" + "\"" + line.substring(endIndex);
                    }
                }
                if(line.contains("ISSUES_COLLECTION_NAME=(") && !line.contains(subject+"_"+name)){
                    int endIndex = line.indexOf(')');
                    if (endIndex != -1) {
                        line = line.substring(0, endIndex) + " \"" + type+"_"+subject+"_"+name+".issues" + "\"" + line.substring(endIndex);
                    }
                }
                if (line.contains("EPICS_COLLECTION_NAME=(") && !line.contains(subject+"_"+name)) {
                    int endIndex = line.indexOf(')');
                    if (endIndex != -1) {
                        line = line.substring(0, endIndex) + " \"" + type+"_"+subject+"_"+name+".epics" + "\"" + line.substring(endIndex);
                    }
                }
                if(line.contains("USERSTORIES_COLLECTION_NAME=(") && !line.contains(subject+"_"+name)){
                    int endIndex = line.indexOf(')');
                    if (endIndex != -1) {
                        line = line.substring(0, endIndex) + " \"" + type+"_"+subject+"_"+name+".userstories" + "\"" + line.substring(endIndex);
                    }
                }
                if(line.contains("TASKS_COLLECTION_NAME=(") && !line.contains(subject+"_"+name)){
                    int endIndex = line.indexOf(')');
                    if (endIndex != -1) {
                        line = line.substring(0, endIndex) + " \"" + type+"_"+subject+"_"+name+".tasks" + "\"" + line.substring(endIndex);
                    }
                }
                if(line.contains("IMPUTATIONS_COLLECTION_NAME=(") && !line.contains(subject+"_"+name)){
                    int endIndex = line.indexOf(')');
                    if (endIndex != -1) {
                        line = line.substring(0, endIndex) + " \"" + type+"_"+subject+"_"+name+".imputations" + "\"" + line.substring(endIndex);
                    }
                }
                scriptContent.append(line).append("\n");
            }
            reader.close();

            // Escribir el archivo modificado
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(scriptContent.toString());
            writer.close();

            System.out.println("Archivo modificado exitosamente.");
            return true;
        } catch (IOException e) {
            System.err.println("Error al modificar el archivo: " + e.getMessage());
        }
        return true;
    }
    public Boolean config_mongo_properties(String name, String subject, String type){
        String path = "home/connect/run/config/"+type+"_" + subject+"/mongo.properties";

        StringBuilder contentAdd = new StringBuilder();
        String newLine = null;
        if(type == "github") {
            newLine = "github_" + subject + "_" + name + ".commits, github_" + subject + "_" + name + ".issues \n";
        }
        else if(type == "taiga") {
            newLine = "taiga_" + subject + "_" + name + ".issues, taiga_" + subject + "_" + name + ".epics, \\\n" + "taiga_" + subject + "_" + name + ".userstories, taiga_" + subject + "_" + name + ".tasks \n";
        }
        else if(type == "sheets") {
            newLine = "sheets_" + subject + "_" + name + ".imputations \n";
        }

        try {
            File file = new File(path);
            FileReader fileReader = new FileReader(file);
            BufferedReader reader = new BufferedReader(fileReader);

            String line;
            Boolean exist = false;
            String lastGithubLine = null;
            StringBuilder modifiedContent = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                if(line.contains(subject+"_"+name)){
                    exist = true;
                }
                // Agrega la línea al contenido modificado
                modifiedContent.append(line).append("\n");

                // Si la línea comienza con "github", actualiza lastGithubLine
                if (line.trim().startsWith(type)) {
                    lastGithubLine = line;
                }
            }

            reader.close();

            // Si encontramos al menos una línea que comienza con "github"
            if (lastGithubLine != null && !exist) {
                // Genera la nueva línea que quieres agregar
                // Encuentra la posición de la última línea de "github" en el contenido modificado

                // Encuentra la posición de la última línea de "github" en el contenido modificado
                int index = modifiedContent.lastIndexOf(lastGithubLine);
                // Inserta la nueva línea después de la última línea de "github"

                modifiedContent.insert(index + lastGithubLine.length() , ", \\");

                modifiedContent.insert(index + lastGithubLine.length() + 4, newLine);
            }

            // Imprime el contenido modificado
            System.out.println(modifiedContent.toString());

            // Si deseas guardar los cambios en el archivo, puedes hacerlo aquí
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            writer.write(modifiedContent.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    public Boolean config_GTS_properties(String name, String subject,String type,String id) {
        String path = "home/connect/run/config/"+type+"_" + subject+"/"+type+".properties";
        //System.out.print(path);

        Integer num_task = getLastTask(subject,type);
        //System.out.print(num_task);
        //String id;
        String newTask = null;
        if(type == "github"){
            //id=projectController.getIdGithub(name,subject);
            System.out.print(" github \n");

            newTask = "tasks."+num_task+".github.url="+id+"\n" +
                    "tasks."+num_task+".github.commit.topic=github_"+subject+"_"+name+".commits\n" +
                    "tasks."+num_task+".github.issue.topic=github_"+subject+"_"+name+".issues\n" +
                    "tasks."+num_task+".taiga.task.topic=taiga_"+subject+"_"+name+".tasks\n";
        }
        else if(type == "taiga"){

            //id = projectController.getIdTaiga(name,subject);
            //System.out.print("   "+ id+ "\n");
            newTask = "tasks."+num_task+".slug="+id+"\n" +
                    "tasks."+num_task+".taiga.issue.topic=taiga_"+subject+"_"+name+".issues\n" +
                    "tasks."+num_task+".taiga.epic.topic=taiga_"+subject+"_"+name+".epics\n" +
                    "tasks."+num_task+".taiga.userstory.topic=taiga_"+subject+"_"+name+".userstories\n" +
                    "tasks."+num_task+".taiga.task.topic=taiga_"+subject+"_"+name+".tasks\n";
        }
        else if(type == "sheets"){

            //id = projectController.getIdTaiga(name,subject);
            System.out.print("   "+ id+ "\n");
            newTask = "tasks."+num_task+".spreadsheet.id="+id+"\n" +
                    "tasks."+num_task+".team.name="+name+"\n" +
                    "tasks."+num_task+".imputations.topic=sheets_"+subject+"_"+name+".imputations\n";
        }
        //File sourceFile = file.toFile();
        StringBuilder contentAdd = new StringBuilder();
        Integer num_teams = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            StringBuilder content = new StringBuilder();
            Boolean exists = false;
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                if(line.startsWith(type+".teams.num")){

                    Pattern pattern = Pattern.compile(type+"\\.teams\\.num=(\\d+)");
                    Matcher matcher = pattern.matcher(line);
                    if(matcher.find()){
                        num_teams = Integer.parseInt(matcher.group(1));
                        System.out.print("aaaaaaa  "+num_teams);
                        num_teams++;
                        line=type+".teams.num=" + num_teams;
                        System.out.println(line);
                    }
                }
                content.append(line).append("\n");

            }
            content.append(newTask).append("\n");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
                writer.write(content.toString());
                System.out.println("Nueva tarea añadida al archivo con éxito.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }
}
