import java.sql.*;
import java.util.ArrayList;

public class Users {

    private ArrayList<User> userList;
    private ArrayList<User> usersFromDB;

    public Users(){
        userList = new ArrayList<User>();
        usersFromDB = new ArrayList<User>();

    }

    public Users(ArrayList<User> userList) {
        this.userList = userList;
        usersFromDB = new ArrayList<User>();
    }

    public boolean addUser(User u){
        if(!userList.contains(u)){
            userList.add(u);
            return true;
        }
        else{
            return false;
        }
    }

    public void syncDB(Connection conn) throws SQLException {
        for(User u:userList){
            if(!(usersFromDB.contains(u))){
                u.addToDB(conn);
            }
        }
        loadFromDB(conn);
    }

    public void removeUser(User u){
        userList.remove(u);
    }

    public boolean containsUser(User u){
        if(userList.contains(u)){
            return true;
        }
        else{
            return false;
        }
    }

    public void loadFromDB(Connection conn) throws SQLException {
        Statement st = conn.createStatement();
        String getUsersFromDB = "select * from quiz.users;";
        ResultSet rset = st.executeQuery(getUsersFromDB);
        User actualUser = new User();
        while(rset.next()){
            actualUser = new User(rset.getString("username"),rset.getString("passWord"));
            userList.add(actualUser);
            usersFromDB.add(actualUser);
        }
    }

    public class User{
        private String name;
        private String passWord;

        public User(){
            name = null;
            passWord = null;
        }

        public User(String name, String passWord) {
            this.name = name;
            this.passWord = passWord;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPassWord() {
            return passWord;
        }

        public void setPassWord(String passWord) {
            this.passWord = passWord;
        }

        public void addToDB(Connection conn) throws SQLException {

            String insertUserIntoDB = "insert into quiz.users (username, passWord)" +
                    " values (?,?);";
            PreparedStatement pst = conn.prepareStatement(insertUserIntoDB);

            pst.setString(1, this.name);
            pst.setString(2, this.passWord);

            pst.execute();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }

            if (!User.class.isAssignableFrom(obj.getClass())) {
                return false;
            }

            final User other = (User) obj;
            if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
                return false;
            }

            if ((this.passWord == null) ? (other.passWord != null) : !this.passWord.equals(other.passWord)) {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 53 * hash + (this.name != null ? this.name.hashCode() : 0);
            hash = 53 * hash + (this.passWord != null ? this.passWord.hashCode() : 0);
            return hash;
        }

    }

}
