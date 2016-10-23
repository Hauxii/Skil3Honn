package services;

/**
 * Created by Lenny on 23.10.2016.
 */
public class ServiceException extends Exception {
    public ServiceException()    {
        super();
    }

    public ServiceException(String s)    {
        super(s);
    }

    public ServiceException(String s, Throwable throwable)    {
        super(s, throwable);
    }
}
