public class TotalMessagesVisitor implements Visitor {
    private float messages = 0;
    @Override
    public float visit(UserGroup group) {
        for(Constituent current : group.getSubUsers()){
            current.accept(this);
        }
        return messages;
    }

    @Override
    public float visit(User user) {
        for(String message : user.getUserFeed().getMessageFeed()){
            messages++;
        }
        return messages;
    }
}
