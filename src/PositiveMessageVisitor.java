import java.util.ArrayList;

public class PositiveMessageVisitor implements Visitor {
    private float posMessages = 0.0f;
    @Override
    public float visit(UserGroup group) {
        for(Constituent current : group.getSubUsers()){
            current.accept(this);
        }
        return posMessages;
    }

    @Override
    public float visit(User user) {
        String[] posExamples = {"Good","good", "Nice", "nice", "Happy","happy", "Favorite","favorite","Love", "love", "Wonderful", "wonderful", "Best", "best", "Amazing", "amazing"};
        for(String messageInFeed : user.getUserFeed().getMessageFeed()){
            for(String word : posExamples){
                if(messageInFeed.contains(word)) {
                    posMessages++;
                    break;
                }
            }
        }
        return posMessages;
    }
}
