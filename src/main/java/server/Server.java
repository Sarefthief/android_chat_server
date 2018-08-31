package server;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Server
{
    private int port;
    private Set<String> userNames = new HashSet<>();
    private Set<ServerUserThread> userThreads = new HashSet<>();
    private ArrayList<Message> messages = new ArrayList<>();

    /**
     * server.server.Server main method
     * @throws IOException
     */
    public void start() throws IOException
    {
        try{
            ServerSocket server = new ServerSocket(port);
            System.out.println("Port number: " + port);
            System.out.println("To shutdown the server type: \"/exit\"");
            while(true) {
                Socket client = server.accept();
                ServerUserThread user = new ServerUserThread(client, this);
                userThreads.add(user);
                user.start();
            }
        } catch(BindException e){
            System.out.println("Port: " + port + " is unavailable. Please change port number in serverConfig.yml file.");
        }
    }

    /**
     * @param message string message to broadcast
     * @param excludeUser user who send the message
     */
    void broadcast(Message message, ServerUserThread excludeUser)
    {
        messages.add(message);
        for (ServerUserThread user : userThreads) {
            if ((user != excludeUser)&&(!user.getUserName().equals(""))) {
                user.sendMessage(message);
            }
        }
    }

    /**
     * @param message string message
     * @param receiver string name of receiver
     */
    void sendPrivate(Message message, String receiver)
    {
        for (ServerUserThread user : userThreads) {
            if (user.getUserName().equals(receiver)) {
                user.sendMessage(message);
            }
        }
    }

    /**
     * @param user user to send a history
     */
    void messageHistory(ServerUserThread user)
    {
        int num;
        if(messages.size() > 10) {
            num = messages.size() - 10;
        } else {
            num = 0;
        }
        if (!messages.isEmpty()) {
            for (int i = num; i <= messages.size()-1; i++) {
                user.sendMessage(messages.get(i));
            }
        }
    }

    /**
     * @param userName string user name
     */
    void addUserName(String userName)
    {
        userNames.add(userName);
    }

    /**
     * @param userName string name of user to remove
     * @param user user thread to remove
     */
    void removeUser(String userName, ServerUserThread user) {
        boolean removed = userNames.remove(userName);
        if (removed) {
            userThreads.remove(user);
            System.out.println("User " + userName + " has quitted.");
        }
    }

    /**
     * @return user names array
     */
    Set<String> getUserNames() {
        return this.userNames;
    }

    /**
     * @return int port number
     */
    public int getPort()
    {
        return port;
    }

    /**
     * @param port int port number
     */
    public void setPort(int port)
    {
        this.port = port;
    }
}
