import java.io.Serializable;

/**
 * Created by Kristin on 26-10-14.
 */
public class Request implements Serializable {
    String actionType;
    String origin; //null for server requests only
    String destination; //can be null
    Object object; //can be null

    public Request(String actionType, String origin) {
        this.actionType = actionType;
        this.origin = origin;
    }

    public Request(String actionType, String origin, String destination) {
        this.actionType = actionType;
        this.origin = origin;
        this.destination = destination;
    }

    public Request(String actionType, String origin, String destination, Object object) {
        this.actionType = actionType;
        this.origin = origin;
        this.destination = destination;
        this.object = object;
    }

    public Request(String actionType) { //server request
        this.actionType = actionType;
    }

    public String getActionType() {
        return actionType;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public Object getObject() {
        return object;
    }

    @Override
    public String toString() {
        return actionType + ":" + origin + "-" + destination + "=" + object;
    }
}
