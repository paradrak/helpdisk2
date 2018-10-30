package main.java.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class DBController {
    private Connection con = null;
    private Statement stmt = null;
    private ResultSet rs = null;

    /** Параметры подключения. Не локальные для метода, т.к. будут настраиваться приложением */
    private String connServer = "localhost";
    private String connNameBD = "helpdesk_study";
    private String connLogin = "root";
    private String connPassword = "SrpT_2f42dQN";  //SrpT_2f42dQN  //MySQL_Randir

    public void connectDB() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String url = "jdbc:mysql://" + connServer + ":3306/" + connNameBD + "?useUnicode=true&characterEncoding=utf-8" +
                    "&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC" +
                    "&user=" + connLogin + "&password="+connPassword;
            con = DriverManager.getConnection(url);
            stmt = con.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ObservableList<String> getTaskStatuses()
    {
        ObservableList<String> data = FXCollections.observableArrayList();
        String condition;
        condition = "SELECT " +
                "ts.t_status " +
                "FROM task_status ts;";
        try {
            rs = stmt.executeQuery(condition);
            while (rs.next()) {
                data.add(rs.getString(1));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public ObservableList<String> getUserNames()
    {
        ObservableList<String> data = FXCollections.observableArrayList();
        String condition;
        condition = "SELECT " +
                "u.name " +
                "FROM users u " +
                "WHERE u.status = 1;";
        try {
            rs = stmt.executeQuery(condition);
            while (rs.next()) {
                data.add(rs.getString(1));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public String[] loginUser(String login, String password)
    {
        String Condition;
        String[] user = new String[2];
        user[0] = "";
        user[1] = "";
        Condition = "SELECT " +
                    "u.login, u.name " +
                    "FROM users u " +
                    "WHERE `login` = " + '"' + login + '"' + "and `password` = " + '"' + password + '"' + ";";
        try {
            rs = stmt.executeQuery(Condition);
            while (rs.next()) {
                user[0] = rs.getString(1);
                user[1] = rs.getString(2);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public ObservableList<Task> getTasks(String userName, int userType)
    {
        ObservableList<Task> data = FXCollections.observableArrayList();
        String Condition, condWhere;
        switch (userType) {
            case 1:
                condWhere = "where uc.name = \"" + userName + "\"";
                break;
            case 2:
                condWhere = "where ua.name = \"" + userName + "\"";
                break;
            default:
                condWhere = "";
                break;
        }
        Condition = "SELECT " +
                "t.id, t.title, t.datetime , ua.name, uc.name, ts.t_status " +
                "FROM tasks t " +
                "inner join task_status ts on (t.t_status = ts.id) " +
                "inner join users ua on ((t.author = ua.id)) " +
                "inner join users uc on ((t.contractor = uc.id)) " +
                condWhere +
                "order by t.id" + ";";
        try {
            rs = stmt.executeQuery(Condition);
            while (rs.next()) {
                data.add(new Task(rs.getInt(1), rs.getString(2), rs.getString(3),
                                  rs.getString(4), rs.getString(5), rs.getString(6)));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public boolean createTask (Task task) {
        try {
            String table = "tasks";
            /** При вставке id создает MySQL автоматически (increment), дата-время полставляется текущая
             * Особенности (типичны для нашей старны): время - по времени сервера. При выводе неплохо бы учитывать часовые пояса  */
            String Condition = "INSERT INTO " + table +
                    " (`title`, `datetime`, `author`, `contractor`, `t_status`) " +
                    "VALUES ((\"" +
                    task.getTitle() + "\"), " +
                    "(SELECT now()), " +
                    "(SELECT u.id FROM users u WHERE u.name = \"" + task.getAuthor() + "\"), " +
                    "(SELECT u.id FROM users u WHERE u.name = \"" + task.getContractor() + "\"), " +
                    "(SELECT ts.id FROM task_status ts WHERE ts.t_status = \"" + task.getStatus() + "\"));";
            int delCount = stmt.executeUpdate(Condition);
            return delCount > 0;
        }
        catch (Exception e){
            return false;
        }
    }

    public boolean updateTask (Task task) {
        try {
            String table = "tasks";
            /**  При обновлении нельзя изменить id, автора, дату-время задачи  */
            String Condition = "UPDATE " + table + " SET " +
                    "`title` = \"" + task.getTitle() + "\", "+
                    "`contractor` = (SELECT u.id FROM users u WHERE u.name = \"" + task.getContractor() + "\"), " +
                    "`t_status` = (SELECT ts.id FROM task_status ts WHERE ts.t_status = \"" + task.getStatus() + "\") " +
                    "where `id` = " + task.getId() + ";";
            int delCount = stmt.executeUpdate(Condition);
            return delCount > 0;
        }
        catch (Exception e){
            return false;
        }
    }

    public boolean deleteTask (int taskID) {
        try {
            String Condition = "DELETE " +
                    "FROM tasks " +
                    "where tasks.id = " +
                    taskID + ";";
            int delCount = stmt.executeUpdate(Condition);
            return delCount > 0;
        }
        catch (Exception e){
            return false;
        }
    }

    public void disconnectDB()
    {
        try {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
