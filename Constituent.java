import java.util.ArrayList;

public interface Constituent extends Visitable {
    void add(Constituent constituent);

    String getName();

    long getCreationTime();

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
