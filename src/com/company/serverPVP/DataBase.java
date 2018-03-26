package com.company.serverPVP;

import java.sql.*;
import java.util.Arrays;

public class DataBase {
    private static Connection con = null;
    private static String URL = "jdbc:sqlserver://127.0.0.1\\SQLEXPRESS:1433;database=SeaBattle;integratedSecurity=true";
    private static Statement st = null;
    public DataBase() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            //if (con == null)
            con = DriverManager.getConnection(URL);

            if (con != null) System.out.println("Connection Successful!\n");
            if (con == null) return;

            st = con.createStatement();
        } catch (ClassNotFoundException e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
        }
    }

    public void Learning() {
        try {
            for (int i = 0; i < 10; i++) {
                for (int y = 0; y < 10; y++) {
                    if (Server.placeOfBattleEnemy[i][y].isHere) {
                        String queryGetNumberOfTimes = "SELECT * FROM [dbo].[Steps] " +
                                "WHERE [dbo].[Steps].[PositionX]=" +
                                i + " AND [dbo].[Steps].[PositionY]=" +
                                y;
                        ResultSet rs = st.executeQuery(queryGetNumberOfTimes);
                        int line = 0;
                        int valueForUpdate = 0;
                        while (!rs.isClosed() && rs.next()) {
                            line = rs.getInt("Id");
                            valueForUpdate = rs.getInt(4);
                        }

                        if (line > 0) {
                            String querySetNewRow = "UPDATE [dbo].[Steps]" +
                                    "   SET [NumberOfTimes] =" + (valueForUpdate + 1) +
                                    " WHERE [dbo].[Steps].[PositionX]=" +
                                    i + " AND [dbo].[Steps].[PositionY]=" +
                                    y;
                            st.execute(querySetNewRow);
                        } else {
                            String querySetNewRow = "INSERT INTO [dbo].[Steps] ([PositionX],[PositionY]" +
                                    ",[NumberOfTimes]) VALUES (" + i + "," + y + ", 0)";
                            st.execute(querySetNewRow);
                        }
                        if (rs != null) rs.close();
                    }
                }
            }

            //
            //if (st != null) st.close();
            //if (con != null) con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int[][] GetStatistic() {
        int[][] result = new int[10][10];

        try {

            String queryGetNumberOfTimes = "SELECT * FROM [dbo].[Steps]";

            ResultSet rs = st.executeQuery(queryGetNumberOfTimes);


            while (rs.next()) {
                int x = rs.getInt(2);
                int y = rs.getInt(3);
                result[x][y] = rs.getInt(4);
            }
            //if (rs != null) rs.close();
            //if (st != null) st.close();
            //if (con != null) con.close();


        } catch (SQLException e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
        }

        return result;
    }
}
