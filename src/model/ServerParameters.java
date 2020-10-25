package model;

public class ServerParameters {
    public boolean hasData;
    public boolean hasFileList;
    public boolean postSuccess;

    public String filename;
    public String data;
    public String fileList;
    public String requestType;
    public String payload;
    public String response;

    public ServerParameters() {
        filename = "";
        data = "";
        requestType = "";
        payload = "";
        filename = "";
        response = "";
    }
}
