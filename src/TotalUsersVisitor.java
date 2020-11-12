public class TotalUsersVisitor implements Visitor {
    public float visit(UserGroup group){
        float total = 0;
        for(Constituent current: group.getSubUsers()){
            total += current.accept(this);
        }
        return total;
    }

    public float visit(User user){
        float total = 0;
        total++;
        return total;
    }
}
