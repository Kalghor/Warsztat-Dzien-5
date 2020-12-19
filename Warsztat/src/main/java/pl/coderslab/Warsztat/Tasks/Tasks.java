package pl.coderslab.Warsztat.Tasks;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import pl.coderslab.ConsoleColors;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;


public class Tasks {
    //zmienne globalne
    public static Path path = Paths.get("/home/pawel/Pulpit/Repositories/Coderslab/Warsztat/tasks.csv");

    public static void main(String args[]){
        String[][] tasksData = new String[lengthOdFile()][3];
        loadData(tasksData);
        viewMenu(tasksData);
    }

    //Wyswietlanie dostepnych zadan
    public static void viewTaksk(String[][] tasksData){
        String[][] viewTable = tasksData;
        int counter = 0;
        for (String[] wiersz : tasksData){
            for(String element : wiersz){
                String tmp = element;
                System.out.print(counter + ": " + element);
                counter++;
                if(counter % 3 == 0){
                    System.out.println("");
                }
            }
        }
    }

    //Dodaj zadanie
    public static String[][] addTask(String[][] tasksData){
        File data = new File(String.valueOf(path));
        Scanner scan = new Scanner(System.in);
        String[] element = new String[3];
        System.out.println("Wprowadz nazwe zadania: ");
        element[0] = ConsoleColors.GREEN + scan.nextLine() + ConsoleColors.RESET;
        System.out.println("Wprowadz date zadania (rok-miesiac-dzien): ");
        element[1] = ConsoleColors.GREEN + scan.nextLine() + ConsoleColors.RESET;
        System.out.println("Wprowadz status zadania(true - wazne / false - niewazne): ");
        element[2] = ConsoleColors.GREEN + scan.nextLine() + ConsoleColors.RESET;
        String[][] addTab = Arrays.copyOf(tasksData,tasksData.length+1);
        addTab[addTab.length-1] = new String[3];
        addTab[addTab.length-1][0] = element[0];
        addTab[addTab.length-1][1] = element[1];
        addTab[addTab.length-1][2] = element[2];
        return addTab;
    }

    //Usun zadanie
    public static String[][] removeTask(String[][] tasksData){
        Scanner scan = new Scanner(System.in);
        String[][] tmp = new String[0][0];
        int index;
        System.out.println("Podaj index, ktory chcesz usunac z tablicy: ");
        String indexStr = ConsoleColors.GREEN + scan.next() + ConsoleColors.RESET;
        boolean check = NumberUtils.isParsable(indexStr);
       if(check == true){
           index = Integer.parseInt(indexStr);
           if(index >= 0 && index < tasksData.length) {
               tasksData = ArrayUtils.remove(tasksData, index);
               System.out.println("Pomyslnie usunieto zadanie.");
               return tasksData;
           } else {System.out.println("Niepoprawna wartosc.");}
       } else
           System.out.println("Nie udaulo sie usunac zadania.");
       return tmp;
    }
    //metoda obliczajaca dlugosc pliku
    public static int lengthOdFile(){
        int counter = 0;
        File data = new File(String.valueOf(path));
        try {
            Scanner scan = new Scanner(data);
            while (scan.hasNextLine()) {
                scan.nextLine();
                counter++;
            }
            }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        return counter;
    }

    //Wczytaj dane
    public static void loadData(String[][] tasksData){
        File data = new File(String.valueOf(path));
        String[] dataConvert;
        String dataFromFile = "";
        int lengthOfFile = lengthOdFile();
        if(!Files.exists(path)){
            System.out.println("Plik nie istnieje");
        } else
        try {
            Scanner scan = new Scanner(data);
            while (scan.hasNextLine()) {
                for(int i = 0; i < lengthOfFile; i++){
                    dataFromFile = scan.nextLine();
                    String[] dataConverted = dataFromFile.split(",");
                    for(int j = 0; j < 3; j++){
                        String[][] loadedData = new String[i+1][3];
                        loadedData[i][j] = dataConverted[j];
                        tasksData[i][j] = String.valueOf(loadedData[i][j]);
                    }
                }
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    //zapisz do pliku
    public static void write(String[][] tasksData){
        File file = new File(String.valueOf(path));
        String newline = "\n";
        try {
            FileWriter fileWriter = new FileWriter(String.valueOf(path),false);
            for (int i = 0; i < tasksData.length; i++){
                for(int j = 0; j < 3; j++){
                    String line = "";
                    String[] lineTab = new String[3];
                    lineTab[j] = tasksData[i][j];
                    if(j < 2) {
                        StringUtils.deleteWhitespace(line);
                        line += lineTab[j] + ",";
                    } else {
                        line += lineTab[j];
                    }
                    StringUtils.deleteWhitespace(line);
                    fileWriter.append(line);
                }
                fileWriter.append(newline);
            }

            fileWriter.close();
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public static void viewMenu(String[][] tasksData){
        String choice = "";
        Scanner scan = new Scanner(System.in);
        String[] operations = {"add", "remove", "list", "exit"};
        while(true) {
            System.out.println(ConsoleColors.BLUE + "What do you want to do now?" + ConsoleColors.RESET);
            for(int i = 0; i < operations.length; i++){
                System.out.println(operations[i]);
            }
            choice = scan.next();
            switch (choice) {
                case "add": {
                    tasksData = addTask(tasksData);
                    System.out.println("");
                    break;
                }
                case "remove": {
                    tasksData = removeTask(tasksData);
                    System.out.println("");

                    break;
                }
                case "list": {
                    viewTaksk(tasksData);
                    System.out.println("");
                    break;
                }
                case "exit": {
                    write(tasksData);
                    System.out.println(ConsoleColors.RED + "Bye, Bye.");
                    System.exit(0);
                    break;
                }
                default: {
                    System.out.println("Wrong choice");
                }
            }
        }
    }
}
