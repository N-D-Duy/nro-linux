package ServerData.Services;

import com.girlkun.database.GirlkunDB;
import ServerData.Server.Manager;
import ServerData.Utils.Logger;
import ServerData.Utils.Util;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class TopService implements Runnable{
    private static TopService i;

    public static TopService gI() {
        if (i == null) {
            i = new TopService();
        }
        return i;
    }
    public static String getTopNap() {
        StringBuffer sb = new StringBuffer("");

        String SELECT_TOP_VND = "SELECT name, vnd FROM player ORDER BY vnd DESC LIMIT 15;";
        PreparedStatement ps;
        ResultSet rs;
        try {
            Connection conn = GirlkunDB.getConnection();
            ps = conn.prepareStatement(SELECT_TOP_VND);
            conn.setAutoCommit(false);

            rs = ps.executeQuery();
            byte i = 1;
            while(rs.next()) {
                sb.append(i).append(".").append(rs.getString("name")).append(": ").append(Util.powerToString(rs.getLong("vnd"))).append(" VNĐ\b");
                i++;
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }
    
    public static String getTopSM() {
        StringBuffer sb = new StringBuffer("");

        String SELECT_TOP_SM = "SELECT name, CAST( split_str(data_point,',',2) AS UNSIGNED) AS sm FROM player INNER JOIN account ON account.id = player.account_id WHERE account.is_admin = 0 AND account.ban = 0 ORDER BY CAST( split_str(data_point,',',2)  AS UNSIGNED) DESC LIMIT 10;";//"SELECT name, CAST( split_str( data_point,',',2)  AS UNSIGNED) AS sm FROM player ORDER BY CAST( split_str( data_point,',',2)  AS UNSIGNED) DESC LIMIT 20;";
        PreparedStatement ps;
        ResultSet rs;
        try {
            Connection conn = GirlkunDB.getConnection();
            ps = conn.prepareStatement(SELECT_TOP_SM);
            conn.setAutoCommit(false);

            rs = ps.executeQuery();
            byte i = 1;
            while(rs.next()) {
                sb.append(i).append(".").append(rs.getString("name")).append(": ").append(rs.getString("sm")).append("\b");
                i++;
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }
      public static String gethotong() {
        StringBuffer sb = new StringBuffer("");

        String SELECT_TOP_POWER = "SELECT name, diemhotong FROM player ORDER BY diemhotong DESC LIMIT 10;";
        PreparedStatement ps;
        ResultSet rs;
        try {
            Connection conn = GirlkunDB.getConnection();
            ps = conn.prepareStatement(SELECT_TOP_POWER);
            conn.setAutoCommit(false);

            rs = ps.executeQuery();
            byte i = 1;
            while(rs.next()) {
                sb.append(i).append(".").append(rs.getString("name")).append(": ").append(rs.getString("diemhotong")).append("\b");
                i++;
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }
    public static String getTopSD() {
        StringBuffer sb = new StringBuffer("");

        String SELECT_TOP_SD = "SELECT name, CAST( split_str(data_point,',',8) AS UNSIGNED) AS sd FROM player INNER JOIN account ON account.id = player.account_id WHERE account.is_admin = 0 AND account.ban = 0 ORDER BY CAST( split_str(data_point,',',8)  AS UNSIGNED) DESC LIMIT 10;";
        PreparedStatement ps;
        ResultSet rs;
        try {
            Connection conn = GirlkunDB.getConnection();
            ps = conn.prepareStatement(SELECT_TOP_SD);
            conn.setAutoCommit(false);

            rs = ps.executeQuery();
            byte i = 1;
            while(rs.next()) {
                sb.append(i).append(".").append(rs.getString("name")).append(": ").append(rs.getString("sd")).append("\b");
                i++;
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }
    
    public static String getTopHP() {
        StringBuffer sb = new StringBuffer("");

        String SELECT_TOP_HP = "SELECT name, CAST( split_str(data_point,',',6) AS UNSIGNED) AS hp FROM player INNER JOIN account ON account.id = player.account_id WHERE account.is_admin = 0 AND account.ban = 0 ORDER BY CAST( split_str(data_point,',',6)  AS UNSIGNED) DESC LIMIT 20;";
        PreparedStatement ps;
        ResultSet rs;
        try {
            Connection conn = GirlkunDB.getConnection();
            ps = conn.prepareStatement(SELECT_TOP_HP);
            conn.setAutoCommit(false);

            rs = ps.executeQuery();
            byte i = 1;
            while(rs.next()) {
                sb.append(i).append(".").append(rs.getString("name")).append(": ").append(rs.getString("hp")).append("\b");
                i++;
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }
    
    public static String getTopKI() {
        StringBuffer sb = new StringBuffer("");

        String SELECT_TOP_KI = "SELECT name, CAST( split_str(data_point,',',7) AS UNSIGNED) AS ki FROM player INNER JOIN account ON account.id = player.account_id WHERE account.is_admin = 0 AND account.ban = 0 ORDER BY CAST( split_str(data_point,',',7)  AS UNSIGNED) DESC LIMIT 20;";
        PreparedStatement ps;
        ResultSet rs;
        try {
            Connection conn = GirlkunDB.getConnection();
            ps = conn.prepareStatement(SELECT_TOP_KI);
            conn.setAutoCommit(false);

            rs = ps.executeQuery();
            byte i = 1;
            while(rs.next()) {
                sb.append(i).append(".").append(rs.getString("name")).append(": ").append(rs.getString("ki")).append("\b");
                i++;
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }
    
    public static String getTopHONGNGOC() {
        StringBuffer sb = new StringBuffer("");

        String SELECT_TOP_HONGNGOC = "SELECT name, CAST( split_str( data_inventory,',',3)  AS UNSIGNED) AS HONGNGOC FROM player INNER JOIN account ON account.id = player.account_id WHERE account.is_admin = 0 AND account.ban = 0 ORDER BY CAST( split_str( data_inventory,',',3)  AS UNSIGNED) DESC LIMIT 15;";
        PreparedStatement ps;
        ResultSet rs;
        try {
            Connection conn = GirlkunDB.getConnection();
            ps = conn.prepareStatement(SELECT_TOP_HONGNGOC);
            conn.setAutoCommit(false);

            rs = ps.executeQuery();
            byte i = 1;
            while(rs.next()) {
                sb.append(i).append(".").append(rs.getString("name")).append(": ").append(rs.getString("HONGNGOC")).append("\b");
                i++;
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }
    
    public static String getTopNV() {
        StringBuffer sb = new StringBuffer("");

        String SELECT_TOP_NV = "SELECT name, CAST( split_str(split_str(data_task,',',1),'[',2)  AS UNSIGNED) AS nv FROM player INNER JOIN account ON account.id = player.account_id WHERE account.is_admin = 0 AND account.ban = 0 ORDER BY CAST( split_str(split_str(data_task,',',1),'[',2)  AS UNSIGNED) DESC, CAST(split_str(data_task,',',2)  AS UNSIGNED) DESC, CAST( split_str(data_point,',',2) AS UNSIGNED) DESC LIMIT 10;";
        PreparedStatement ps;
        ResultSet rs;
        try {
            Connection conn = GirlkunDB.getConnection();
            ps = conn.prepareStatement(SELECT_TOP_NV);
            conn.setAutoCommit(false);

            rs = ps.executeQuery();
            byte i = 1;
            while(rs.next()) {
                sb.append(i).append(".").append(rs.getString("name")).append(": ").append(rs.getString("nv")).append("\b");
                i++;
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }
    
    public static String getTopPvP() {
        StringBuffer sb = new StringBuffer("");

        String SELECT_TOP_PVP = "SELECT name, pointPvP FROM player ORDER BY pointPvP DESC LIMIT 10;";
        PreparedStatement ps;
        ResultSet rs;
        try {
            Connection conn = GirlkunDB.getConnection();
            ps = conn.prepareStatement(SELECT_TOP_PVP);
            conn.setAutoCommit(false);

            rs = ps.executeQuery();
            byte i = 1;
            while(rs.next()) {
                sb.append(i).append(".").append(rs.getString("name")).append(": ").append(rs.getString("pointPvP")).append(" Điểm\b");
                i++;
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }
    
    public static String getTopNHS() {
        StringBuffer sb = new StringBuffer("");

        String SELECT_TOP_SM = "SELECT name, NguHanhSonPoint FROM player ORDER BY pointPvP DESC LIMIT 10;";
        PreparedStatement ps;
        ResultSet rs;
        try {
            Connection conn = GirlkunDB.getConnection();
            ps = conn.prepareStatement(SELECT_TOP_SM);
            conn.setAutoCommit(false);

            rs = ps.executeQuery();
            byte i = 1;
            while(rs.next()) {
                sb.append(i).append(".").append(rs.getString("name")).append(": ").append(rs.getString("NguHanhSonPoint")).append("\b");
                i++;
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    @Override
    public void run() {
        while(true){
            try{
                if (Manager.timeRealTop + (30 * 60 * 1000) < System.currentTimeMillis()) {
                    Manager.timeRealTop = System.currentTimeMillis();
                    try (Connection con = GirlkunDB.getConnection()) {
                        Manager.topSieuHang = Manager.realTopSieuHang2(con);
                        Manager.topNV = Manager.realTop(Manager.queryTopNV, con);
                        Manager.topSM = Manager.realTop(Manager.queryTopSM, con);
                        Manager.topSK = Manager.realTop(Manager.queryTopSK, con);
                        Manager.topPVP = Manager.realTop(Manager.queryTopPVP, con);
                        Manager.topSD = Manager.realTop(Manager.queryTopSD, con);
                        Manager.topHN = Manager.realTop(Manager.queryTopHN, con);
                        Manager.topVND = Manager.realTop(Manager.queryTopVND, con);
                    } catch (Exception ignored) {
                        Logger.error("Lỗi đọc top");
                    }
                }
                Thread.sleep(1000);
            }catch (Exception ignored) {
            }
        }
    }

}