import model.ServerParameters;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HttpRequest {

    public void processRequest(StringBuilder sb, ServerParameters serverParameters) {
        String incoming = sb.toString();
        String file = "", payload = "", requestType = "";
        System.out.println("HEELOOO WORLD");
        System.out.println(sb.toString());
        String[] lines = incoming.split("\r\n");

        for (int i = 0; i < lines.length; i++) {
            String[] words = lines[i].split(" ");

            if (i == 0) {
                serverParameters.requestType = words[0];
                if (words.length == 3) {
                    serverParameters.filename = serverParameters.filename + words[1];
                } else if (words.length == 2) {
                    serverParameters.filename = serverParameters.filename + "/";
                }
            }

            if (lines[i].equalsIgnoreCase("")) {
                serverParameters.payload = lines[i + 1];
            }
        }
        System.out.println(file);
        System.out.println(requestType);
        System.out.println(payload);

        //if (FileOperations.validatePath(serverParameters)) {
            HttpResponse httpResponse = new HttpResponse();
            httpResponse.processResponse(serverParameters);
//        } else
 //           errorResponse(serverParameters);
    }

    public void errorResponse(ServerParameters serverParameters) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        String response = "";

        response = response.concat("HTTP/1.0 " + ErrorCodes.BAD_REQUEST + "\r\n");
        response = response.concat("Date: " + dtf.format(now) + "\r\n");
        response = response.concat("Content-Length: " + serverParameters.data.length() + "\r\n");
        response = response.concat("Connection: " + "close" + "\r\n");
        response = response.concat("Server: httpfs/v1.0" + "\r\n");
        response = response.concat("\r\n");

        serverParameters.response = response;
    }
}