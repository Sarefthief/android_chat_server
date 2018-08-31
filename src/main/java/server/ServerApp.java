package server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;

public class ServerApp
{
    public static void main(String[] args) throws IOException
    {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        Server server = mapper.readValue(new File("serverConfig.yml"), Server.class);
        ExitThread exit = new ExitThread();
        exit.start();
        server.start();
    }
}
