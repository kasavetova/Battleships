import java.io.Serializable;

/**
 *Represents all objects that are being sent through the server.
 *
 * @see ServerThread
 *
 * @author Team 1-O
 */
public class Request implements Serializable {
    String actionType;
    String origin;
    String destination;
    Object object;

    /**
     * Initialises the object wit an action type
     *
     * @param actionType the type of the action
     */
    public Request(String actionType) {
        this.actionType = actionType;
    }

    /**
     * Initialises the object with an action type and origin.
     * @param actionType the type of the action
     * @param origin the origin of the server request
     */
    public Request(String actionType, String origin) {
        this.actionType = actionType;
        this.origin = origin;
    }

    /**
     * Initialises the object with an action type, origin and destination.
     * @param actionType the type of the action
     * @param origin the origin of the server request
     * @param destination the destination of the server request
     */
    public Request(String actionType, String origin, String destination) {
        this.actionType = actionType;
        this.origin = origin;
        this.destination = destination;
    }

    /**
     * Initialises the object with an action type, origin, destination and object.
     * @param actionType the type of the action
     * @param origin the origin of the server request
     * @param destination the destination of the server request
     * @param object the object to be sent with server request
     */
    public Request(String actionType, String origin, String destination, Object object) {
        this.actionType = actionType;
        this.origin = origin;
        this.destination = destination;
        this.object = object;
    }

    /**
     * Returns the action type of the object.
     * @return the object action type
     */
    public String getActionType() {
        return actionType;
    }

    /**
     * Returns the origin of the server request.
     * @return the object origin
     */
    public String getOrigin() {
        return origin;
    }

    /**
     * Returns the destination of the server request.
     * @return the object destination
     */
    public String getDestination() {
        return destination;
    }

    /**
     * Returns the object that's being sent through the server request.
     * @return the object sent with the server request
     */
    public Object getObject() {
        return object;
    }

    /**
     * Returns a printable version of the object
     * @return printable version of the object
     */
    @Override
    public String toString() {
        return actionType + ":" + origin + "-" + destination + "=" + object;
    }
}
