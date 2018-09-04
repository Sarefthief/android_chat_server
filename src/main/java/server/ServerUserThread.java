package server;

import java.io.*;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ServerUserThread extends Thread
{
    private String userName;
    private Socket user;
    private Server server;
    private ObjectInputStream ObjectIn;
    private ObjectOutputStream ObjectOut;
    private Message serverMessage;

    /**
     * Constructor with nickname initialization
     * @param user socket
     * @param server server socket
     */
    ServerUserThread (Socket user, Server server) throws IOException
    {
        this.user = user;
        this.server = server;

        InputStream input = user.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input,"UTF-8"));
        ObjectIn = new ObjectInputStream(user.getInputStream());
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(user.getOutputStream(),"UTF-8"),true);
        PrintStream ps = new PrintStream(System.out, true, "UTF-8");

        while(true){
            userName = reader.readLine();
            if ((server.getUserNames().contains(userName))||(userName.equals("Server"))){
                writer.println("nickname is occupied");
            } else {
                writer.println("nickname is free");
                break;
            }
        }
        try{
            TimeUnit.MILLISECONDS.sleep(10);
        } catch (InterruptedException ex){
            System.out.println("Interrupted exception");
        }
        ObjectOut = new ObjectOutputStream(user.getOutputStream());
        ObjectOut.flush();
        server.addUserName(userName);
        ps.println("User " + userName + " is connected.");
        server.printHistory(this);
        serverMessage = new Message("Server","New user connected: " + userName, new Date());
        server.broadcast(serverMessage, this);
    }

    /**
     * Method to receive client's messages
     */
    public void run()
    {
        try {
            String[] words;
            while(true){
                try{
                    serverMessage = (Message) ObjectIn.readObject();
                    serverMessage.setDate(new Date());
                    if (serverMessage.getMessage().equals("/exit")){
                        break;
                    }
                    if ((serverMessage.getMessage().charAt(0) == '/')&&(serverMessage.getMessage().charAt(1) == 'w')){
                        words = serverMessage.getMessage().split(" ");
                        if (server.getUserNames().contains(words[1])){
                            server.sendPrivate(serverMessage, words[1]);
                        } else {
                            ObjectOut.writeObject(new Message ("Server", "This user does not exists.", new Date()));
                        }
                    } else {
                        server.broadcast(serverMessage, this);
                    }
                } catch (ClassNotFoundException ex){
                    System.out.println("Class not found");
                }
            }
            server.removeUser(userName, this);
            user.close();
            serverMessage = new Message("Server", userName + " has quitted.", new Date());
            server.broadcast(serverMessage, this);

        } catch (IOException ex) {
            server.removeUser(userName, this);
            serverMessage = new Message("Server", userName + " has quitted.", new Date());
            server.broadcast(serverMessage, this);
        }
    }

    /**
     * @return string user name
     */
    String getUserName()
    {
        return userName;
    }

    /**
     * Sends a message to the client.w
     */
    public void sendMessage(Message message)
    {
        try{
            ObjectOut.writeObject(message);
        } catch (IOException ex){
            System.out.println("Send message IO exception");
        }
    }
}
