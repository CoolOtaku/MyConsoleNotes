package otaku.development;

import otaku.development.Controller.Command;
import otaku.development.Model.ListCommands;
import otaku.development.View.ViewConsole;

import javax.swing.*;
import java.io.File;
import java.util.Scanner;

public class Main {

    public static String MyDocs = new JFileChooser().getFileSystemView().getDefaultDirectory().toString()+"\\MyConsoleNotes";
    private static boolean isExec = true;
    private static boolean start = true;

    public static void main(String[] args) {
        isDirectory();
        Scanner scanner = new Scanner(System.in);
        while(isExec){
            if(start){
                start = false;
                ViewConsole.view(ListCommands.get());
            }
            ViewConsole.view("Enter the command:");
            String command = scanner.nextLine();
            String res = Command.parse(command, scanner);
            if(res.isEmpty()){
                isExec = false;
            }
        }
    }
    private static void isDirectory(){
        File dir = new File(MyDocs);
        if(!dir.exists()){
            dir.mkdir();
        }
    }
}
