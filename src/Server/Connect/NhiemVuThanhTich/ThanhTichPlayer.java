/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.Connect.NhiemVuThanhTich;

import com.girlkun.database.GirlkunDB;
import ServerData.Models.Player.Player;
import com.girlkun.network.io.Message;
import com.girlkun.result.GirlkunResultSet;
import ServerData.Server.Manager;
import ServerData.Services.Service;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

/**
 *
 * @author Administrator
 */
public class ThanhTichPlayer {

    private static boolean CheckRecieve(Player pl, int idThanhTich) {
        for (int i = 0; i < pl.Archivement.size(); i++) {
            if (pl.Archivement.get(i).Template.id == idThanhTich) {
                return pl.Archivement.get(i).isRecieve;
            }
        }
        return false;
    }

    public static boolean GetFinish(Player pl, int idThanhTich) {
        if (CheckRecieve(pl, idThanhTich)) {
            return true;
        }
        switch (idThanhTich) {
            case 0:
                return CheckTaskThanhTich.CheckSKH(pl, 1);
            case 1:
                return CheckTaskThanhTich.CheckSKH(pl, 2);
            case 2:
                return CheckTaskThanhTich.CheckSKH(pl, 3);
            case 3:
                return CheckTaskThanhTich.CheckKillBoss(pl, 1);
            case 4:
                return CheckTaskThanhTich.CheckKillBoss(pl, 2);
            case 5:
                return CheckTaskThanhTich.CheckKillBoss(pl, 3);
            case 6:
                return CheckTaskThanhTich.CheckTask(pl, 1);
            case 7:
                return CheckTaskThanhTich.CheckTask(pl, 2);
            case 8:
                return CheckTaskThanhTich.CheckTongNap(pl, 1);
            case 9:
                return CheckTaskThanhTich.CheckTongNap(pl, 2);
            case 10:
                return CheckTaskThanhTich.CheckTongNap(pl, 3);
            case 11:
                return CheckTaskThanhTich.CheckSMTN(pl, 1);
            case 12:
                return CheckTaskThanhTich.CheckSMTN(pl, 2);
            case 13:
                return CheckTaskThanhTich.CheckSMTN(pl, 3);
        }
        return false;
    }

    public static void GetArchivemnt(Player pl, int index) {    
        boolean flag = false;
        int hongngoc = 0;
        for (int i = 0; i < pl.Archivement.size(); i++) {
            if (pl.Archivement.get(i).Template.id == index) {
                flag = pl.Archivement.get(i).isRecieve;
                hongngoc = pl.Archivement.get(i).Template.money;
            }
        }
        if (GetFinish(pl, index) && !flag && hongngoc != 0) {
            pl.inventory.ruby += hongngoc;
            Service.gI().sendMoney(pl);
            pl.Archivement.get(index).isRecieve = true;
            SendGetArchivemnt(pl, index);
            Service.gI().sendThongBao(pl, "Bạn Nhận Được " + hongngoc + " Hồng Ngọc");
        } else {
            Service.gI().sendThongBao(pl, "Có lỗi xảy ra");
            
        }
    }

    public static void SendGetArchivemnt(Player pl, int index) {
        Message msg = null;
        try {
            msg = new Message(-76);
            msg.writer().writeByte(1);
            msg.writer().writeByte(index);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public static void GetRecieve(Player pl) {
        GirlkunResultSet rs = null;
        try {
            rs = GirlkunDB.executeQuery("select * from player where id = ? limit 1", pl.id);
            JSONValue jv = new JSONValue();
            JSONArray dataArray = null;
            if (rs.first()) {
                dataArray = (JSONArray) jv.parse(rs.getString("dataArchiverment"));
                if (dataArray != null) {
                    for (int i = 0; i < dataArray.size(); i++) {
                        JSONArray dataThanhTich = (JSONArray) jv.parse(String.valueOf(dataArray.get(i)));
                        int id = Integer.parseInt(String.valueOf(dataThanhTich.get(0)));
                        int flag = Integer.parseInt(String.valueOf(dataThanhTich.get(1)));
                        SetRecieve(pl, id, flag);
                    }
                }
                dataArray.clear();
            }
        } catch (Exception e) {
        }
    }

    public static void SetRecieve(Player pl, int id, int flag) {
        if (flag != 0 && flag != 1) {
            return;
        }
        for (int i = 0; i < pl.Archivement.size(); i++) {
            if (pl.Archivement.get(i).Template.id == id) {
                pl.Archivement.get(i).isRecieve = flag == 1;
            }
        }
    }

    public static String getStringInfo2(Player pl, int idThanhTich) {
        switch (idThanhTich) {
            case 3:
                return CheckTaskThanhTich.SCheckKillBoss(pl, 1);
            case 4:
                return CheckTaskThanhTich.SCheckKillBoss(pl, 2);
            case 5:
                return CheckTaskThanhTich.SCheckKillBoss(pl, 3);
            case 6:
                return CheckTaskThanhTich.SCheckTask(pl, 1);
            case 7:
                return CheckTaskThanhTich.SCheckTask(pl, 2);
            case 8:
                return CheckTaskThanhTich.SCheckTongNap(pl, 1);
            case 9:
                return CheckTaskThanhTich.SCheckTongNap(pl, 2);
            case 10:
                return CheckTaskThanhTich.SCheckTongNap(pl, 3);
        }
        return "";
    }

    public static void SendThanhTich(Player pl) {
        if (pl == null) {
            return;
        }
        Message msg = null;
        try {
            msg = new Message(-76);
            msg.writer().writeByte(0);
            msg.writer().writeByte(Manager.A_TEMPLATES.size());
            for (ThanhTich a : pl.Archivement) {
                msg.writer().writeUTF(a.Template.info1);
                msg.writer().writeUTF(a.Template.info2 + (a.isRecieve ? "" : getStringInfo2(pl, a.Template.id)));
                msg.writer().writeShort(a.Template.money);
                msg.writer().writeBoolean(GetFinish(pl, a.Template.id));
                msg.writer().writeBoolean(a.isRecieve);
            }
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
                msg = null;
            }
        }
    }
}
