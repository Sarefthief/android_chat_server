package server;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable{

    private static final long serialVersionUID = 1L;
    private String username;
    private String message;
    private Date date;

    /**
     * @param username string user name
     * @param message string message text
     */
    public Message(String username, String message){
        this.message = message;
        this.username = username;
    }

    /**
     * @param username string user name
     * @param message string message text
     * @param date time message send
     */
    public Message(String username, String message, Date date){
        this.message = message;
        this.username = username;
        this.date = date;
    }

    /**
     * @return string username
     */
    public String getUsername(){
        return username;
    }

    /**
     * @return string message text
     */
    public String getMessage(){
        return message;
    }

    /**
     * @return date
     */
    public Date getDate(){
        return date;
    }

    /**
     * @param message string message text
     */
    public void setMessage(String message)
    {
        this.message = message;
    }

    /**
     * @param username string username
     */
    public void setUsername(String username)
    {
        this.username = username;
    }

    /**
     *
     * @param date time message send
     */
    public void setDate(Date date)
    {
        this.date = date;
    }
}
