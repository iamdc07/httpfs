import java.io.*;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileOperations {
    public void processFileOperation(ServerParameters serverParameters) {

        if (serverParameters.requestType.equalsIgnoreCase("GET")) {
            if (serverParameters.filename.equalsIgnoreCase("") || serverParameters.filename.equalsIgnoreCase("/")) {
                System.out.println("process");
                listFiles(serverParameters);
            } else {
                readFile(serverParameters);
            }
        } else {
            writeFile(serverParameters);
        }
    }

    public void listFiles(ServerParameters serverParameters) {
        try {
            System.out.println("Listfiles");
            StringBuilder sb = new StringBuilder();
            String filePath = Config.path.concat(serverParameters.filename);
            Path path = Paths.get(filePath).normalize().toAbsolutePath();
            Path resolvedPath = path.resolve(path).normalize().toAbsolutePath();
            File file = new File(String.valueOf(resolvedPath));
            String[] fileList = file.list();

            if (fileList != null) {
                for (String each : fileList) {
                    sb.append(each.concat("\n"));
                }
            }
            serverParameters.fileList = sb.toString();
            serverParameters.hasFileList = true;

//            System.out.println(sb.toString());
        } catch (Exception ex) {
            System.out.println("Something went wrong. Could not list the files.");
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("DuplicatedCode")
    public void readFile(ServerParameters serverParameters) {
        try {
            System.out.println("Readfile");
            String filePath = Config.path.concat(serverParameters.filename);
            Path path = Paths.get(filePath).normalize().toAbsolutePath();
            Path resolvedPath = path.resolve(path).normalize().toAbsolutePath();

            String data = new String(Files.readAllBytes(resolvedPath));

            if (data.length() != 0) {
                serverParameters.hasData = true;
                serverParameters.data = data;
            }
//            System.out.println(sb.toString());
        } catch (FileSystemException ex) {
            serverParameters.hasData = false;
            System.out.println("Invalid Directory");
            ex.printStackTrace();
        }
        catch (IOException ex) {
            System.out.println("Something went wrong. Could not read the file.");
            ex.printStackTrace();
        }
    }

    public void writeFile(ServerParameters serverParameters) {
        try {
            System.out.println("Writefile");
            String filePath = Config.path.concat(serverParameters.filename);
            Path path = Paths.get(filePath).normalize().toAbsolutePath();
            Path resolvedPath = path.resolve(path).normalize().toAbsolutePath();

            BufferedWriter out = new BufferedWriter(new FileWriter(resolvedPath.toString().concat(".txt")));
            out.write(serverParameters.payload);
            out.flush();
            out.close();

            serverParameters.postSuccess = true;
        } catch (FileSystemException ex) {
            System.out.println("Invalid Directory");
            ex.printStackTrace();
        }
        catch (IOException ex) {
            System.out.println("Something went wrong. Could not write on to the file.");
            ex.printStackTrace();
        }
    }

    public static boolean validatePath(ServerParameters serverParameters) {
        String path = Config.path.concat(serverParameters.filename);
        Path absPath = Paths.get(Config.path).normalize().toAbsolutePath();
        Path accessPath = absPath.resolve(path).normalize().toAbsolutePath();
        Path serverPath = Paths.get(Config.path).normalize().toAbsolutePath().resolve(Config.path).normalize().toAbsolutePath();

        System.out.println("Client:" + accessPath);
        System.out.println("Server:" + serverPath);

        return (accessPath.startsWith(serverPath));
    }
}