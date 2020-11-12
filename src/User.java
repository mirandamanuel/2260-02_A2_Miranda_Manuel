import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class User extends Observable implements Constituent, Observer, Visitable   {
    private ArrayList<Constituent> usersFollowing = new ArrayList<Constituent>();
    private ArrayList<Constituent> followedUsers = new ArrayList<Constituent>();
    private String name;
    Feed userFeed = new Feed();
    Feed globalFeed = new Feed(); //this feed contains all messages by this user and those they follow

    public User(String name){
        this.name = name;
    }

    @Override
    public void add(Constituent constituent) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ArrayList<Constituent> getUsersFollowingThisUser() {
        return usersFollowing;
    }

    @Override
    public ArrayList<Constituent> getUsersFollowed() {
        return followedUsers;
    }

    @Override
    public ArrayList<Constituent> getSubUsers() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Constituent findUser(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Feed getUserFeed() {
        return userFeed;
    }

    @Override
    public Feed getGlobalFeed() {
        return globalFeed;
    }

    @Override
    public void follow(Constituent followTarget) {
        followTarget.getUserFeed().addObserver(this);
        followedUsers.add(followTarget);
        setChanged();
        notifyObservers();
    }

    @Override
    public void update(Observable o, Object arg) {
        globalFeed.addToFeed((String)arg);
        setChanged();
        notifyObservers();
    }

    @Override
    public String toString(){
        return name;
    }

    @Override
    public float accept(Visitor visitor) {
        return visitor.visit(this);
    }
}