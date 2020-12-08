public class TotalGroupVisitor implements Visitor {
    public float visit(UserGroup group){
        float total = 0;
        total++;
        for(Constituent current: group.getSubUsers()){
            total += current.accept(this);
        }
        return total;
    }

    public float visit(User user){
        float total = 0;
        return total;
    }
}
