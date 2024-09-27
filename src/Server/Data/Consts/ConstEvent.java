package Server.Data.Consts;

import ServerData.Models.NPC.NauBanh;
import ServerData.Utils.Logger;
import com.girlkun.database.GirlkunDB;
import com.girlkun.result.GirlkunResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

public class ConstEvent {
    String nameevent;
    int id;
    public final ArrayList<ConstEvent> listEvent = new ArrayList<>();
    private static ConstEvent ev;
    public static ConstEvent gI() {
        if (ev == null) {
            ev = new ConstEvent();
        }
        return ev;
    }
    public static byte EVENT = 8; // id event

    //constNPC
    public static final int MUAHE = 1;
    public static final int HUNGVUONG = 2;
    public static final int TRUNGTHU = 3;
    public static final int HLWEEN = 4;
    public static final int NHAGIAO = 5;
    public static final int NOEL = 6;
    public static final int TET = 7;
    public static final int PHUNU = 8;
    public static final int TRUNGTHU_HD = 9;
    public static final int TRUNGTHU_LAMBANH = 10;
    public static final int TRUNGTHU_DOIDIEM = 11;
    // chức năng
    public static int TONGSOBONG; // event 20/11
    public static long X2; // event 20/11
    public static int TONGSOBANH; // event tet
 
public String getNameEv () {
        switch (EVENT) {
            case 1:
                return "Lễ Hội Mùa Hè";
            case 2:
                return "Giỗ Tổ Hùng Vương";
            case 3:
                return "Tết Trung Thu";
            case 4:
                return "Lễ Hội Halloween";
            case 5:
                return "Ngày Nhà Giáo Việt Nam";
            case 6:
                return "Lễ Hội Giáng Sinh";
            case 7:
                return "Tết Nguyên Đán";
            case 8:
                return "Quốc Tế Phụ Nữ";
            case 9:
                return "Valentine";
            case 10:
                return "Tết Dương Lịch";
            case 11:
                return "30 Tháng 4 - 1 Tháng 5";
            case 12:
                return "Black Friday";
            case 13:
                return "Nông Dân Chăm Chỉ";
            case 14:
                return "Chuyển Sinh Đi Học";
        }
        return "Đã Đóng!";
    } 
    
    public void init() {
        Logger.log(Logger.RED,"init event!\n");
        try (Connection con = GirlkunDB.getConnection();) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM a_server_sukien");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ConstEvent Event = new ConstEvent();
                Event.id = rs.getInt("id_event");
                Event.nameevent = rs.getString("name_event");
                Logger.log(Logger.YELLOW,"Load Event ["+Event.nameevent+"] thành công!\n");
                listEvent.add(Event);
            }
            con.close();
            GirlkunResultSet dataPoint;
            dataPoint = GirlkunDB.executeQuery("select * from a_server_sukien where id_event = ?", 5);
                if (dataPoint.first()) {
                    JSONArray dataPointEvent = (JSONArray) JSONValue.parse(dataPoint.getString("Point"));
                    TONGSOBONG = Integer.parseInt(String.valueOf(dataPointEvent.get(0)));
                    dataPoint.dispose();
                }
            dataPoint = GirlkunDB.executeQuery("select * from a_server_sukien where id_event = ?", 7);
                if (dataPoint.first()) {
                    JSONArray dataPointEvent = (JSONArray) JSONValue.parse(dataPoint.getString("Point"));
                    TONGSOBANH = Integer.parseInt(String.valueOf(dataPointEvent.get(0)));
                    NauBanh.Count = Integer.parseInt(String.valueOf(dataPointEvent.get(1)));
                    NauBanh.Nuoc = Float.parseFloat(String.valueOf(dataPointEvent.get(2)));
                    NauBanh.Cui = Integer.parseInt(String.valueOf(dataPointEvent.get(3)));
                    X2 = Long.parseLong(String.valueOf(dataPointEvent.get(4)));
                    dataPoint.dispose();
                }
            con.close();
            Logger.log(Logger.RED,"Load event thanh cong!\n");
        }  catch (Exception erorlog) {
            erorlog.printStackTrace();
        }
    }
    
    public void close() {
        try {
            JSONArray dataArray = new JSONArray();
            dataArray.add(ConstEvent.TONGSOBANH);
            dataArray.add(NauBanh.Count);
            dataArray.add(NauBanh.Nuoc);
            dataArray.add(NauBanh.Cui);
            dataArray.add(ConstEvent.X2);
            String event_tet = dataArray.toJSONString();
            dataArray.clear();
            GirlkunDB.executeUpdate(" update a_server_sukien set Point = ? where id_event = ?", event_tet, 7);
        } catch (Exception e) {
        }
    }
    public static String removeCharAt(String s, int pos) {
        return s.substring(0, pos) + s.substring(pos + 1);
    }
}