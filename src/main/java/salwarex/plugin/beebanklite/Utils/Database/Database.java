package salwarex.plugin.beebanklite.Utils.Database;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Database {

    private final Connection connection;

    public Database(String path) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:" + path);
    } //SQLite

    public Database(String url, String user, String password) throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://" + url, user, password);
    } //MySQL

    public void createTable(String tableName, String data_info){
        try{
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS " + tableName + " (" + data_info + ");");
            statement.close();
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
    }
    public void addColumn(String tableName, String column){
        if(tableExists(tableName) && !columnExists(tableName, column)){
            try{
                Statement statement = connection.createStatement();
                statement.execute("ALTER TABLE " + tableName + " ADD " + column + ";");
                statement.close();
            }
            catch (SQLException ex){
                ex.printStackTrace();
            }
        }
    }

    public void addData(String INSERT_INTO, String attributes, Object ... values){
        if(attributes.split(", ").length == values.length){
            String valuesPaster = "";
            for(int i = 0; i < attributes.split(", ").length; i++){ valuesPaster += "?, "; }
            valuesPaster = valuesPaster.substring(0, valuesPaster.length() - 2);
            try(PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO " + INSERT_INTO + " (" + attributes + ") VALUES (" + valuesPaster + ");")){
                int i = 1;
                for(Object obj : values){
                    preparedStatement.setObject(i, obj);
                    i++;
                }
                preparedStatement.executeUpdate();
            }
            catch (SQLException ex){
                ex.printStackTrace();
            }
        }

    }

    public void addDataFast(String INSERT_INTO, Object ... values){
        String valuesPaster = "";
        for(int i = 0; i < values.length; i++){ valuesPaster += "?, "; }
        valuesPaster = valuesPaster.substring(0, valuesPaster.length() - 2);
        try(PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO " + INSERT_INTO + " VALUES (" + valuesPaster + ");")){
            int i = 1;
            for(Object obj : values){
                preparedStatement.setObject(i, obj);
                i++;
            }
            preparedStatement.executeUpdate();
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }

    }

    public boolean dataExists(String FROM, String WHERE, Object check){
        if(check.toString().equalsIgnoreCase("CURRENT_DATE")){
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
            check = timeStamp;
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM " + FROM + " WHERE " + WHERE + " = ?;")){
            preparedStatement.setObject(1, check);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return false;
    }

    public boolean dataExists(String FROM, String WHERE1, String WHERE2, Object check1, Object check2){
        if(check2.toString().equalsIgnoreCase("CURRENT_DATE")){
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
            check2 = timeStamp;
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM " + FROM + " WHERE " + WHERE1 + " = ? AND " + WHERE2 + " = ?;")){
            preparedStatement.setObject(1, check1);
            preparedStatement.setObject(2, check2);
            //Reports.send(preparedStatement.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return false;
    }

    public boolean tableExists(String tableName){
        try{
            DatabaseMetaData md = connection.getMetaData();
            ResultSet rs = md.getTables(null, null, tableName, null);
            if (rs.next()) {
                return true;
            }
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return false;
    }
    public boolean columnExists(String table, String columnName){
        try{
            if(tableExists(table)){
                DatabaseMetaData md = connection.getMetaData();
                ResultSet rs = md.getColumns(null, null, table, columnName);
                if (rs.next()) {
                    return true;
                }
            }
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return false;
    }


    public void updateData(String UPDATE, String SET, String WHERE, String check, Object data){
        if(check.toString().equalsIgnoreCase("CURRENT_DATE")){
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
            check = timeStamp;
        }
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE " + UPDATE + " SET " + SET + " = ? WHERE " + WHERE + " = ?;");
            preparedStatement.setObject(1, data);
            preparedStatement.setObject(2, check);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    public void updateData(String UPDATE, String SET, String WHERE1, String WHERE2, String check1, String check2, Object data){
        if(check2.toString().equalsIgnoreCase("CURRENT_DATE")){
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
            check2 = timeStamp;
        }
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE " + UPDATE + " SET " + SET + " = ? WHERE " + WHERE1 + " = ? AND " + WHERE2 + " = ?;");
            preparedStatement.setObject(1, data);
            preparedStatement.setObject(2, check1);
            preparedStatement.setObject(3, check2);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    public Object getData(String SELECT, String FROM, String WHERE, String check){
        if(check.toString().equalsIgnoreCase("CURRENT_DATE")){
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
            check = timeStamp;
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT " + SELECT + " FROM " + FROM + " WHERE " + WHERE + " = ?;")){
            preparedStatement.setObject(1, check);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getObject(SELECT);
            }
            else{
                return null;
            }
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return null;
    }

    public Object getData(String SELECT, String FROM, String WHERE1, String WHERE2, String check1, String check2){
        if(check2.toString().equalsIgnoreCase("CURRENT_DATE")){
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
            check2 = timeStamp;
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT " + SELECT + " FROM " + FROM + " WHERE " + WHERE1 + " = ? AND " + WHERE2 + " = ?;")){
            preparedStatement.setObject(1, check1);
            preparedStatement.setObject(2, check2);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getObject(SELECT);
            }
            else{
                return null;
            }
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return null;
    }

    public ArrayList<ArrayList<Object>> getDataRS(String FROM, String WHERE, Object check, String... providedColumns) {
        if (((String) check).equalsIgnoreCase("CURRENT_DATE")) {
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
            check = timeStamp;
        }

        ArrayList<ArrayList<Object>> resultList = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM " + FROM + " WHERE " + WHERE + " = ?")) {
            preparedStatement.setObject(1, check);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ArrayList<Object> row = new ArrayList<>();
                for (String column : providedColumns) {
                    row.add(resultSet.getObject(column));
                }
                resultList.add(row);
            }

            return resultList.isEmpty() ? null : resultList;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public ArrayList<ArrayList<Object>> getDataRS_OR(String FROM, String WHERE1, String WHERE2, Object check1, Object check2, String... providedColumns) {
        if (((String) check1).equalsIgnoreCase("CURRENT_DATE")) {
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
            check1 = timeStamp;
        }

        ArrayList<ArrayList<Object>> resultList = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM " + FROM + " WHERE " + WHERE1 + " = ? OR " + WHERE2 + " = ?")) {
            preparedStatement.setObject(1, check1);
            preparedStatement.setObject(2, check2);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ArrayList<Object> row = new ArrayList<>();
                for (String column : providedColumns) {
                    row.add(resultSet.getObject(column));
                }
                resultList.add(row);
            }

            return resultList.isEmpty() ? null : resultList;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }




    public void closeConnection() throws SQLException{
        if(connection != null && !connection.isClosed()){connection.close();}
    }
}
