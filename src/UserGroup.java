import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class UserGroup extends Observable implements Constituent, Observer, Visitable {
    private ArrayList<Constituent> usersFollowing = new ArrayList<Constituent>();
    private ArrayList<Constituent> followedUsers = new ArrayList<>();
    private ArrayList<Constituent> subUsers = new ArrayList<Constituent>();
    private String name;

    public UserGroup(String name){
        this.name = name;
    }

    @Override
    public void add(Constituent constituent) {
        subUsers.add(constituent);
        setChanged();
        notifyObservers();
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
        return subUsers;
    }

    @Override
    public Constituent findUser(String username) {
        for (Constituent constituent : subUsers) {
            if (constituent.getName().equalsIgnoreCase(username)) {
                return constituent;
            }
//            else {
//                for (Constituent constituent2 : constituent.getSubUsers()) {
//                    if (constituent.getName().equalsIgnoreCase(username)) {
//                        return constituent;
//                    }
//                }
//            }
        }
        throw new RuntimeException("Could not find user");
    }

    @Override
    public Feed getUserFeed() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Feed getGlobalFeed() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void follow(Constituent requestingUser) {
        usersFollowing.add(requestingUser);
    }

    @Override
    public void update(Observable o, Object arg) {

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