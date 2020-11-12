import java.util.ArrayList;
import java.util.Observable;

public interface Constituent extends Visitable {
    void add(Constituent constituent);

    String getName();

    ArrayList<Constituent> getUsersFollowingThisUser();

    ArrayList<Constituent> getUsersFollowed();

    ArrayList<Constituent> getSubUsers();

    public Constituent findUser(String name);

    public Feed getUserFeed();

    public Feed getGlobalFeed();

    public void follow(Constituent requestingUser);

    @Override
    public String toString();

}
