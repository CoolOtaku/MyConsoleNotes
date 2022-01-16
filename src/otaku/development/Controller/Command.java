package otaku.development.Controller;

import otaku.development.Main;
import otaku.development.Model.ListCommands;
import otaku.development.Model.Note;
import otaku.development.View.ViewConsole;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Command {

    private static String title = null;
    private static boolean exist;

    public static String parse(String command, Scanner scanner){
        StringTokenizer tokenizer = new StringTokenizer(command, " ");
        String mainCommand;
        try {
            mainCommand = tokenizer.nextToken().toLowerCase().trim();
        }catch (Exception e){
            ViewConsole.view("Error!");
            return " ";
        }
        switch (mainCommand) {
            case "add":
                try {
                    title = tokenizer.nextToken();
                }catch (Exception e){
                    ViewConsole.view("Error!");
                    return " ";
                }
                exist = Exists(title);
                if (exist) {
                    ViewConsole.view("This note is exist, please rename note!");
                } else {
                    ViewConsole.view("Write text (save the command \"!save\"):");
                    StringBuilder text = new StringBuilder();
                    String line;
                    do{
                        line = scanner.nextLine();
                        if(line.equals("!save")){
                            break;
                        }
                        text.append(line).append("\n");
                    }while(true);
                    Note note = new Note(title,text.toString());
                    try {
                        note.Save();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case "delete":
                try {
                    title = tokenizer.nextToken();
                }catch (Exception e){
                    ViewConsole.view("Error!");
                    return " ";
                }
                exist = Exists(title);
                if (exist) {
                    Note.Delete(title);
                }else{
                    ViewConsole.view("Not found note!");
                }
                break;
            case "list":
                ViewConsole.view("---   Note list   ---");
                File files = new File(Main.MyDocs);
                String[] paths = files.list();
                for (int i = 0; i < paths.length; i++){
                    files = new File(paths[i]);
                    StringBuilder text = new StringBuilder(files.getName());
                    text.delete(text.length()-4,text.length());
                    Note note = new Note(text.toString(),null);
                    note.setDate(getDateInNote(text.toString()));
                    ViewConsole.view("- "+note.getTitle()+"     "+note.getDate());
                }
                ViewConsole.view("---------------------");
                break;
            case "view":
                try {
                    title = tokenizer.nextToken();
                }catch (Exception e){
                    ViewConsole.view("Error!");
                    return " ";
                }
                exist = Exists(title);
                if (exist) {
                    File file = new File(Main.MyDocs+"\\"+title+".txt");
                    Note note = new Note(title, ReadFile(file));
                    note.setDate(getDateInNote(title));
                    ViewConsole.view(note.toString());
                }else{
                    ViewConsole.view("Not found note!");
                }
                break;
            case "edit":
                try {
                    title = tokenizer.nextToken();
                }catch (Exception e){
                    ViewConsole.view("Error!");
                    return " ";
                }
                exist = Exists(title);
                if (exist) {
                    File file = new File(Main.MyDocs + "\\" + title + ".txt");
                    String data = ReadFile(file);
                    ViewConsole.view("---   Text:   ---");
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    Robot robot = null;
                    try {
                        robot = new Robot();
                    } catch (AWTException e) {
                        e.printStackTrace();
                    }
                    StringBuilder saveData = new StringBuilder();
                    String line;
                    Scanner dataScanner = new Scanner(data);
                    while (dataScanner.hasNextLine()) {
                        Robot finalRobot = robot;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String line1 = dataScanner.nextLine();
                                StringSelection stringSelection = new StringSelection(line1);
                                clipboard.setContents(stringSelection, null);
                                finalRobot.keyPress(KeyEvent.VK_CONTROL);
                                finalRobot.keyPress(KeyEvent.VK_V);
                                finalRobot.keyRelease(KeyEvent.VK_V);
                                finalRobot.keyRelease(KeyEvent.VK_CONTROL);
                            }
                        }).start();
                        line = scanner.nextLine();
                        if(line.equals("!save")){
                            Note note = new Note(title,saveData.toString());
                            try {
                                note.Save();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return " ";
                        }
                        saveData.append(line).append("\n");
                    }
                    dataScanner.close();
                    do{
                        line = scanner.nextLine();
                        if(line.equals("!save")){
                            break;
                        }
                        saveData.append(line).append("\n");
                    }while(true);
                    Note note = new Note(title,saveData.toString());
                    try {
                        note.Save();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    ViewConsole.view("Not found note!");
                }
                break;
            case "command":
                ViewConsole.view(ListCommands.get());
                break;
            case "exit":
                ViewConsole.view("---   Exit    ---");
                return "";
        }
        return " ";
    }
    private static boolean Exists(String title){
        title += ".txt";
        File f = new File(Main.MyDocs + "\\" + title);
        return f.exists();
    }
    private static LocalDateTime getDateInNote(String title){
        BasicFileAttributes attr = null;
        try {
            attr = Files.readAttributes(Paths.get(Main.MyDocs+"\\"+title+".txt"), BasicFileAttributes.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        LocalDateTime date =  LocalDateTime.ofInstant(attr.creationTime().toInstant(), ZoneId.systemDefault());
        return date;
    }
    private static String ReadFile(File file){
        StringBuilder text = new StringBuilder();
        try {
            FileReader fr = new FileReader(file);
            int c;
            while ((c = fr.read()) != -1) {
                text.append((char) c);
            }
            fr.close();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return text.toString();
    }
}
