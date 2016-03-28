import java.io.*;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeMap;

public class Main {

    public static TreeMap<Integer, Long> files = new TreeMap<>();
    public static void main(String[] args) throws IOException {
        File f = new File("filelist.txt");
        boolean isNew  = f.createNewFile();
        if(isNew){
            Random r = new Random(System.currentTimeMillis());
            for (int i = 0; i < 10; i++) {
                files.put(i, (long)(r.nextInt(40)+1));
            }
            writeFiles(f);
        }
        Scanner sc = new Scanner(f);

        while(!isNew && sc.hasNextInt()){
            files.put(sc.nextInt(), sc.nextLong());
        }
        sc.close();

        sc = new Scanner(System.in);
        while (true){
            System.out.println("Command?");
            String command = sc.nextLine();
            int fileNum;
            int newName;
            long size;
            switch (command){
                case "list":
                    System.out.println("Num" + " \tBlocks\tsize");
                    for(int file : files.keySet()){
                    System.out.println(file + "   \t" + files.get(file)+ "\t\t" +files.get(file)*512);
                }
                    System.out.println();
                    break;
                case "create":
                    newName = getFileNum(sc, "Name?");
                    if (newName == 0) break;
                    if (files.containsKey(newName) && !overwrite(sc)) break;
                    size = getFileNum(sc, "Size (in Blocks)?");
                    files.put(newName, size);
                    writeFiles(f);
                    System.out.println("File Created");
                    break;
                case "rename":
                    if(files.size()<1) {
                        System.out.println("No files to rename");
                        break;
                    }
                    fileNum = getFileNum(sc, "What file?");
                    if (fileNum == 0) break;
                    newName = getFileNum(sc, "New name?");
                    if (newName == 0) break;
                    if (files.containsKey(newName) && !overwrite(sc)) break;
                    size = files.remove(fileNum);
                    files.put(newName, size);
                    writeFiles(f);
                    System.out.println("File Renamed");
                    break;
                case "delete":
                    if(files.size()<1) {
                        System.out.println("No files to delete");
                        break;
                    }
                    fileNum = getFileNum(sc, "What file?");
                    if (fileNum == 0) break;
                    files.remove(fileNum);
                    writeFiles(f);
                    System.out.println("File Deleted");
                    break;
                case "exit": return;
                default:
                    System.out.println("Command not Recognized. Valid Commands:");
                    System.out.println("list, create, rename, delete, exit");
            }
        }
    }

    private static boolean overwrite(Scanner sc) {
        System.out.println("Overwrite? (y to accept)");
        return sc.nextLine().toLowerCase().equals("y");
    }

    private static void writeFiles(File f) throws IOException {
        PrintStream ps = new PrintStream(
                new FileOutputStream(f, false));
        for(int file : files.keySet()){
            ps.println(file + " " + files.get(file));
        }
        ps.close();
    }

    private static int getFileNum(Scanner sc, String prompt) {
        while(true){
            System.out.println(prompt + " (0 to return)");
            try{
                int n = Integer.parseInt(sc.nextLine());
                if (n>=0) return n;
            } catch (NumberFormatException e){
            }
            System.out.println("Not a valid number");
        }
    }
}
