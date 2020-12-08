import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class Feed extends Observable implements Observer {
    ArrayList<String> messageFeed = new ArrayList<String>();

    public ArrayList<String> getMessageFeed() {
        return messageFeed;
    }

    public void addToFeed(String message){
        messageFeed.add(message);
        setChanged();
        notifyObservers(message);
    }

    @Override
    public String toString(){
        return messageFeed.toString();
    }

    @Override
    public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers();
    }
}
