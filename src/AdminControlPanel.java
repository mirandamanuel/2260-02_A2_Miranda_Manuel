import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

public class AdminControlPanel implements Observer {
    private static UserGroup root = new UserGroup("Root");
    private static final AdminControlPanel instance = new AdminControlPanel(); //singleton instance
    JFrame window;
    JPanel treePanel;
    JPanel buttonsPanel;
    JPanel totalsPanel;
    Constituent selectedUser;
    User newUser;
    UserGroup newGroup;
    JTextField addUserField;
    JTextField addGroupField;

    //constructor private to allow for singleton pattern
    private AdminControlPanel(){
        root.addObserver(this);
        this.treePanel = createUserTree();
        this.buttonsPanel = createUserButtonsPanel();
        this.totalsPanel = createTotalsPanel();
        createAndOpenPanel();
    }

    //returns only instance of AdminControlPanel
    public static AdminControlPanel getInstance(){
        return instance;
    }

    public void createAndOpenPanel(){
        window = new JFrame("Mini Twitter by Manuel Miranda (Admin Control Panel)");
        window.setMinimumSize(new Dimension(800, 600));
        window.setMaximumSize(new Dimension(800, 600));
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(null);
        window.setLocationRelativeTo(null);
        window.add(treePanel);
        window.add(buttonsPanel);
        window.add(totalsPanel);

        window.setVisible(true);
    }

    private JPanel createUserTree(){
        treePanel = new JPanel();
        treePanel.setBounds(10,10, 200, 500);
        treePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),"Tree View"));
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(root);
        printUserTree(rootNode, root);
        JTree userTree = new JTree(rootNode);
        treePanel.add(userTree);

        userTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        userTree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode selected = (DefaultMutableTreeNode) userTree.getLastSelectedPathComponent();
                    selectedUser = (Constituent) selected.getUserObject();
            }
        });

        return treePanel;
    }

    private void printUserTree(DefaultMutableTreeNode node, UserGroup group){
        DefaultMutableTreeNode userGroupNode = null;
        DefaultMutableTreeNode userNode = null;
        for (Constituent user : group.getSubUsers()) {
            if (user instanceof UserGroup) {
                userGroupNode = new DefaultMutableTreeNode(user);
                node.add(userGroupNode);
                printUserTree(node, ((UserGroup) user));
            } else {
                userNode = new DefaultMutableTreeNode(user);
                node.add(userNode);
            }
        }
    }

    private JPanel createUserButtonsPanel(){
        JPanel buttonsPanel= new JPanel();
        buttonsPanel.setLayout(new GridLayout(3,2));
        buttonsPanel.setBounds(220,20, 550, 250);
        buttonsPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        buttonsPanel.add(addUserTextField());
        buttonsPanel.add(addUserButton());
        buttonsPanel.add(addGroupTextField());
        buttonsPanel.add(addGroupButton());
        buttonsPanel.add(new JPanel());
        buttonsPanel.add(addUserViewButton());
        return buttonsPanel;
    }

    private JPanel createTotalsPanel(){
        JPanel totalsPanel = new JPanel();
        totalsPanel.setLayout(new GridLayout(3,1));
        totalsPanel.setBounds(220,310, 550, 200);
        totalsPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        totalsPanel.add(addUserTotalButton());
        totalsPanel.add(addGroupTotalButton());
        totalsPanel.add(addMessagesTotalButton());
        totalsPanel.add(addPosMessageButton());
        totalsPanel.add(addValidateButton());
        totalsPanel.add(addLastUpdateButton());

        return totalsPanel;
    }

    private JTextField addUserTextField(){
        addUserField = new JTextField();
        addUserField.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE),"Enter Username"));
        return addUserField;
    }

    private JTextField addGroupTextField(){
        addGroupField = new JTextField();
        addGroupField.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE),"Enter Groupname"));
        return addGroupField;
    }

    private JButton addUserButton(){
        JButton addUserButton = new JButton("Add User");
        addUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newUser = new User(addUserField.getText());
                root.add(newUser);
            }
        });
        return addUserButton;
    }

    private JButton addGroupButton(){
        JButton addGroupButton = new JButton("Add Group");
        addGroupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newGroup = new UserGroup(addGroupField.getText());
                root.add(newGroup);
            }
        });
        return addGroupButton;
    }

    private JButton addUserViewButton(){
        JButton userViewButton = new JButton("Open User View");
        userViewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new UserPanel(selectedUser);
            }
        });
        return userViewButton;
    }

    private JButton addUserTotalButton(){
        JButton userTotalButton = new JButton("Show User Total");
        userTotalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TotalUsersVisitor visitor = new TotalUsersVisitor();
                double userTotal =  root.accept(visitor);
                JOptionPane.showMessageDialog(new JOptionPane(), "Total Number of Users: "+ ((int)userTotal));
            }
        });
        return userTotalButton;
    }

    private JButton addGroupTotalButton(){
        JButton userGroupButton = new JButton("Show Group Total");
        userGroupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TotalGroupVisitor visitor = new TotalGroupVisitor();
                double groupTotal = root.accept(visitor) - 1;
                JOptionPane.showMessageDialog(new JOptionPane(), "Total Number of Groups: "+ ((int)groupTotal));
            }
        });
        return userGroupButton;
    }

    private JButton addMessagesTotalButton(){
        JButton messagesTotalButton = new JButton("Show Messages Total");
        messagesTotalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TotalMessagesVisitor visitor = new TotalMessagesVisitor();
                double totalMessages = root.accept(visitor);
                JOptionPane.showMessageDialog(new JOptionPane(), "Total Number of Messages: "+ ((int)totalMessages));
            }
        });
        return messagesTotalButton;
    }

    private JButton addPosMessageButton(){
        JButton posMessageButton = new JButton("Show Positive Percentage");
        posMessageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TotalMessagesVisitor helperVisitor = new TotalMessagesVisitor();
                double totalMessages = root.accept(helperVisitor);
                PositiveMessageVisitor visitor = new PositiveMessageVisitor();
                double posMessages = root.accept(visitor);
                double posPercentage = (posMessages/totalMessages)*100;
                JOptionPane.showMessageDialog(new JOptionPane(), "Percentage of positive messages: "+ (posPercentage)+ "%");
            }
        });
        return posMessageButton;
    }

    private JButton addValidateButton(){
        JButton validateButton = new JButton("Validate IDs");
        validateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isValid = true;
                for(Constituent current : root.getSubUsers()){
                    if(current.getName().contains(" "))
                        isValid = false;
                }
                if(isValid)
                    JOptionPane.showMessageDialog(new JOptionPane(), "User IDs: "+ "Valid");
                else
                    JOptionPane.showMessageDialog(new JOptionPane(), "User IDs: "+ "Not Valid");
            }
        });
        return validateButton;
    }

    private JButton addLastUpdateButton(){
        JButton validateButton = new JButton("Last Update");
        validateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                User lastUpdate = (User) root.getSubUsers().get(0);
                for(Constituent current : root.getSubUsers()){
                    if(current instanceof User){
                        if(((User) current).getLastUpdateTime() > lastUpdate.getLastUpdateTime()){
                            lastUpdate = (User) current;
                        }
                    }
                }
                JOptionPane.showMessageDialog(new JOptionPane(), "Last User to Update: "+ lastUpdate.getName() +" at " +new Date(lastUpdate.getLastUpdateTime()));
            }
        });
        return validateButton;
    }

    public UserGroup getRoot(){
        return root;
    }

    @Override
    public void update(Observable o, Object arg) {
        this.treePanel = createUserTree();
        window.dispose();
        createAndOpenPanel();
        System.out.println(root.getSubUsers().toString());
    }
}
