/*
 * KhanhDTK
*/
package ServerData.Services.Giftcode;

import com.girlkun.database.GirlkunDB;
import ServerData.Models.Item.Item.ItemOption;
import ServerData.Models.Player.Player;
import com.girlkun.result.GirlkunResultSet;
import ServerData.Services.Service;
import ServerData.Utils.Logger;
import ServerData.Utils.TimeUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author Administrator
 */
public class GiftcodeManager {
    public String name;
    public final ArrayList<Giftcode> listGiftCode = new ArrayList<>();

    private static GiftcodeManager instance;

    public static GiftcodeManager gI() {
        if (instance == null) {
            instance = new GiftcodeManager();
        }
        return instance;
    }

    public void init() {
        Logger.log(Logger.RED,"Load giftcode!\n");
        try (Connection con = GirlkunDB.getConnection();) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM giftcode");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Giftcode giftcode = new Giftcode();
                ArrayList<Integer> tempListIdPlayer = new ArrayList<>();
                String tempDBListIdPlayers = null;
                giftcode.code = rs.getString("code");
                giftcode.countLeft = rs.getInt("count_left");
                giftcode.datecreate = rs.getTimestamp("datecreate");
                giftcode.dateexpired = rs.getTimestamp("expired");
                String dbListIdPlayer = rs.getString("listIdPlayers");
                JSONArray jar = (JSONArray) JSONValue.parse(rs.getString("item"));
                Logger.log(Logger.CYAN,"Load code ["+giftcode.code+"] thành công!\n");
                if (jar != null) {
                    for (int i = 0; i < jar.size(); ++i) {
                        JSONObject jsonObj = (JSONObject) jar.get(i);
                        giftcode.detail.put(Integer.valueOf(jsonObj.get("id").toString()),
                                Integer.valueOf(jsonObj.get("quantity").toString()));
                        jsonObj.clear();
                    }
                }
                JSONArray option = (JSONArray) JSONValue.parse(rs.getString("option"));
                if (option != null) {
                    for (int u = 0; u < option.size(); u++) {
                        JSONObject jsonobject = (JSONObject) option.get(u);
                        giftcode.option.add(new ItemOption(Integer.parseInt(jsonobject.get("id").toString()),
                                Integer.parseInt(jsonobject.get("param").toString())));
                        jsonobject.clear();
                    }
                }
                if (!dbListIdPlayer.isEmpty()) {
                    tempDBListIdPlayers = dbListIdPlayer = removeCharAt(dbListIdPlayer, 0);
                    tempDBListIdPlayers = dbListIdPlayer = removeCharAt(dbListIdPlayer, dbListIdPlayer.length() - 1);
                    String[] resultTempDBListPlayer = tempDBListIdPlayers.split(",");
                    for (String item : resultTempDBListPlayer) {
                        if (!item.isEmpty())
                            tempListIdPlayer.add(Integer.parseInt(item));
                    }
                    giftcode.listIdPlayer = tempListIdPlayer;
                }
                listGiftCode.add(giftcode);
            }
            con.close();
            ps.close();
            rs.close();
        Logger.log(Logger.RED,"Load giftcode thanh cong!\n");
        } catch (Exception erorlog) {
            erorlog.printStackTrace();
        }
    }

    public void updateGiftCodeListIdPlayer(ArrayList<Integer> listIdPlayers, String code) {
        try {
            String sql = "UPDATE giftcode set listIdPlayers=? where code=?";
            ArrayList<Integer> deDupStriList = new ArrayList<>(new HashSet<>(listIdPlayers));
            GirlkunDB.executeUpdate(sql, JSONValue.toJSONString(deDupStriList), code);
        } catch (Exception e) {
        }
    }
    
    public void sizeList(Player pl) {
        Service.gI().sendThongBao(pl, "" + Giftcode.class);
    }

    public Giftcode checkUseGiftCode(int idPlayer, String code) {
        for (Giftcode giftCode : listGiftCode) {
            if (giftCode.code.equals(code) && giftCode.countLeft > 0 && !giftCode.isUsedGiftCode(idPlayer)) {
                giftCode.countLeft -= 1;
                giftCode.addPlayerUsed(idPlayer);
                updateGiftCodeListIdPlayer(giftCode.listIdPlayer, code);
                return giftCode;
            }
        }
        return null;
    }
    
    public Giftcode CheckCode(int idPlayer, String code) {
        for (Giftcode giftCode : listGiftCode) {
            if (giftCode.code.equals(code) && giftCode.countLeft > 0 && !giftCode.isUsedGiftCode(idPlayer)) {
                return giftCode;
            }
        }
        return null;
    }

    public void checkInfomationGiftCode(Player p) throws Exception {
        GirlkunResultSet rs = GirlkunDB.executeQuery("SELECT * FROM Giftcode WHERE id > 0");
        String textGift = "|7|[ - •⊹٭LIST GIFTCODE٭⊹• - ]\n";
         while (rs.next()) {
            String code = rs.getString("code");
            int Luot = rs.getInt("count_left");
           String hsd = TimeUtil.getTimeNow(rs.getString("datecreate"));
           String hhsd = TimeUtil.getTimeNow(rs.getString("expired"));
            textGift += "|6|Giftcode : " +code +"\nSố lượng còn : " + (Luot==0?"(HẾT)\n":+Luot+"\n")+"|3|(Thời hạn: "+hsd+" - "+hhsd+")\n";
        }
        rs.dispose();
        Service.gI().sendThongBaoFromAdmin(p, textGift);
    }

    public static String removeCharAt(String s, int pos) {
        return s.substring(0, pos) + s.substring(pos + 1);
    }
}
