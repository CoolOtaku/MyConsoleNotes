package otaku.development.Model;

import otaku.development.View.ViewConsole;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static otaku.development.Main.MyDocs;

public class Note {
    private String title;
    private String text;
    private LocalDateTime date;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    public Note(String title, String text) {
        this.title = title;
        this.text = text;
        this.date = LocalDateTime.now();
    }
    public Note(){}

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getDate() {
        return date.format(formatter);
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "---     "+title+"   ---     "+date.format(formatter)+"\n"+text
                +"---------------------------";
    }

    public void Save() throws IOException {
        File file = new File(MyDocs+"\\"+title+".txt");
        file.createNewFile();
        FileWriter wf = new FileWriter(file);
        wf.write(text);
        wf.flush();
        wf.close();

        if(file.exists()){
            ViewConsole.view("Successfully saved!");
        }else{
            ViewConsole.view("Error saved!");
        }
    }
    public static void Delete(String globalTitle){
        File file = new File(MyDocs+"\\"+globalTitle+".txt");
        boolean res = file.delete();
        if(res){
            ViewConsole.view("Successfully delete!");
        }else{
            ViewConsole.view("Error delete!");
        }
    }
}
