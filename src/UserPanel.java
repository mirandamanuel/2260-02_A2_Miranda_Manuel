import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class UserPanel implements Observer {
    JFrame window;
    User currentUser;
    JPanel followingPanel;
    JPanel followUserButtonsPanel;
    JPanel tweetPanel;
    JPanel feedPanel;
    JTextField userIdField;
    JTextArea tweetField;
    JTextArea feedTextArea;
    User userTarget;



    public UserPanel(Constituent currentUser){
        if(currentUser instanceof User) {
            this.currentUser = (User) currentUser;
            this.currentUser.addObserver(this);
            this.currentUser.getGlobalFeed().addObserver(this);
            this.followingPanel = createFollowingPanel();
            this.followUserButtonsPanel = createFollowPanel();
            this.tweetPanel = createTweetPanel();
            this.feedPanel = createFeedPanel();
            createAndOpenPanel((User) currentUser);
        }
        else if(currentUser instanceof UserGroup){
            JOptionPane.showMessageDialog(new JOptionPane(), "You have selected a User Group not a User");
        }
    }

    public void createAndOpenPanel(User currentUser){
        window = new JFrame(currentUser.getName()+"'s Mini Twitter");
        window.setMinimumSize(new Dimension(800, 600));
        window.setMaximumSize(new Dimension(800, 600));
        window.setLayout(null);
        window.setLocationRelativeTo(null);
        window.add(followUserButtonsPanel);
        window.add(followingPanel);
        window.add(tweetPanel);
        window.add(feedPanel);
        window.setVisible(true);
    }

    private JPanel createFollowingPanel(){
        followingPanel = new JPanel();
        followingPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),"Following"));
        followingPanel.setBounds(10,10, 200, 500);
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Following:");
        createUserTree(rootNode, currentUser.getUsersFollowed());
        JTree followingTree = new JTree(rootNode);
        followingPanel.add(followingTree);
        return followingPanel;
    }

    private void createUserTree(DefaultMutableTreeNode node, ArrayList<Constituent> group){
        DefaultMutableTreeNode userGroupNode = null;
        DefaultMutableTreeNode userNode = null;
        for(Constituent user : group){
            if(user instanceof UserGroup){
                userGroupNode = new DefaultMutableTreeNode(user);
                node.add(userGroupNode);
                createUserTree(node, user.getUsersFollowed());
            }
            else{
                userNode = new DefaultMutableTreeNode(user);
                node.add(userNode);
            }
        }
    }

    private JPanel createFollowPanel(){
        JPanel followPanel = new JPanel();
        followPanel.setLayout(new GridLayout(1,1));
        followPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        followPanel.add(followUserIdField());
        followPanel.add(followUserButton());
        followPanel.setBounds(220,20, 550, 50);
        return followPanel;
    }

    private JPanel createTweetPanel(){
        JPanel tweetPanel = new JPanel();
        tweetPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        tweetPanel.setLayout(new GridLayout(1,1));
        tweetPanel.setBounds(220,90, 550, 50);
        tweetPanel.add(addTweetField());
        tweetPanel.add(addTweetButton());
        return tweetPanel;
    }

    private JPanel createFeedPanel(){
        JPanel feedPanel = new JPanel();
        feedPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.blue),"Tweet Feed"));
        feedPanel.setBounds(220,160, 550, 350);
        feedPanel.setLayout(null);
        feedTextArea = addFeedTextArea();
        feedPanel.add(feedTextArea);

        return feedPanel;
    }

    private JTextArea addFeedTextArea(){
        feedTextArea = new JTextArea(25, 100);
        feedTextArea.setBounds(10,20, 530, 320);
        feedTextArea.setEditable(false);
        for(String message : currentUser.getGlobalFeed().messageFeed){
            feedTextArea.append(message+"\n");
        }
        return feedTextArea;
    }


    private JTextField followUserIdField(){
        userIdField = new JTextField();
        userIdField.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE),"Enter Username"));
        return userIdField;
    }

    private JButton followUserButton(){
        JButton followUserButton = new JButton("Follow User");
        followUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userTarget = (User) AdminControlPanel.getInstance().getRoot().findUser(userIdField.getText());
                currentUser.follow(userTarget);
            }
        });
        return followUserButton;
    }

    private JTextArea addTweetField(){
        tweetField = new JTextArea();
        tweetField.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE),"Tweet Field"));
        return tweetField;
    }

    private JButton addTweetButton(){
        JButton tweetButton = new JButton("Tweet it Out!");
        tweetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userMessage = currentUser.getName()+": " + tweetField.getText();
                currentUser.getUserFeed().addToFeed(userMessage);
                currentUser.getGlobalFeed().addToFeed(userMessage);
                //printFeed(currentUser.getGlobalFeed());
            }
        });
        return tweetButton;
    }


    @Override
    public void update(Observable o, Object arg) {
        this.followingPanel = createFollowingPanel();
        this.followUserButtonsPanel = createFollowPanel();
        this.tweetPanel = createTweetPanel();
        this.feedPanel = createFeedPanel();
        window.dispose();
        createAndOpenPanel(currentUser);
    }
}
