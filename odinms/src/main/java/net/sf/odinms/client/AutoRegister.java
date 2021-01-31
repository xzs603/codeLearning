
package net.sf.odinms.client;

import java.io.FileReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

import java.util.Properties;
import net.sf.odinms.database.DatabaseConnection;

public class AutoRegister {
        private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MapleClient.class);
        private static final int ACCOUNTS_PER_IP = 1; //change the value to the amount of accounts you want allowed for each ip
        private static Properties props = new Properties();
        public static  boolean autoRegister = getautoRegister(); //enable = true or disable = false
        public static boolean iplimit=getiplimit();

        public void setautoRegister(boolean ar)
        {
            autoRegister=ar;
        }
        public static boolean getautoRegister()
        {
        InputStreamReader is;
        try {
            is = new FileReader("config/world.properties");
                        props.load(is);
			is.close();
        } catch (Exception ex) {
            ex.getStackTrace();
        }
          String  autostate=props.getProperty("net.sf.odinms.net.autoreg");
//          System.out.println(autostate);
          if(autostate.equals("true")){
          return autoRegister=true;
          }
          else
            return autoRegister=false;
        }
                public static boolean getiplimit()
        {
        InputStreamReader is;
        try {
            is = new FileReader("config/world.properties");
                        props.load(is);
			is.close();
        } catch (Exception ex) {
            ex.getStackTrace();
        }
          String  iplimitstate=props.getProperty("net.sf.odinms.net.iplimit");
//          System.out.println(autostate);
          if(iplimitstate.equals("true")){
          return iplimit=true;
          }
          else
            return iplimit=false;
        }
        public static boolean success = false; // DONT CHANGE

        public static boolean getAccountExists(String login) {
                boolean accountExists = false;
                Connection con = DatabaseConnection.getConnection();
                try {
                        PreparedStatement ps = con.prepareStatement("SELECT name FROM accounts WHERE name = ?");
                        ps.setString(1, login);
                        ResultSet rs = ps.executeQuery();
                        if(rs.next())
                         accountExists = true;
                         rs.close();
                } catch (Exception ex) {
                        log.error("ERROR", ex);
                }
            return accountExists;
        }

        public static void createAccount(String login, String pwd, String eip) {
                String sockAddr = eip;
                Connection con;

                //connect to database or halt
                try {
                        con = DatabaseConnection.getConnection();
                } catch (Exception ex) {
                        log.error("ERROR", ex);
                        return;
                }

                try {
                        PreparedStatement localstillau=con.prepareStatement("update  accounts set lastknownip = 0 where lastknownip='127.0.0.1'");
                        localstillau.executeUpdate();
                        PreparedStatement ipc = con.prepareStatement("SELECT lastknownip FROM accounts WHERE lastknownip = ?");
                        ipc.setString(1, sockAddr.substring(1, sockAddr.lastIndexOf(':')));
                        ResultSet rs = ipc.executeQuery();
                        if (iplimit==false||rs.first() == false || rs.last() == true && rs.getRow() < ACCOUNTS_PER_IP) {
                                try {
                                        PreparedStatement ps = con.prepareStatement("INSERT INTO accounts (name, password, email, birthday, macs, greason, banreason, md5pass, qq, lastknownip) VALUES (?, ?, ?, ?, ?, ?,?,?,?,?)");
                                        ps.setString(1, login);
                                        ps.setString(2, LoginCrypto.hexSha1(pwd));
                                        ps.setString(3, "307586193@qq.com");
                                        ps.setString(4, "2009-05-13");
                                        ps.setString(5, "00-00-00-00-00-00");
                                        ps.setInt(6, 0);
                                        ps.setString(7,"0");
                                        ps.setString(8, "0");
                                        ps.setString(9, "0");
                                        ps.setString(10, sockAddr.substring(1, sockAddr.lastIndexOf(':')));

                                        ps.executeUpdate();

                                        ps.close();


                                } catch (SQLException ex) {
                                        log.error("ERROR", ex);
                                        return;
                                }
                        }
//                        else{
                            success = true;
//                        }

                        rs.close();
                        ipc.close();
                } catch (SQLException ex) {
                        log.error("ERROR", ex);
                        return;
                }
        }
}