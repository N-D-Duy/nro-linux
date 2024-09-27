package ServerData.Models.PVP.Matches;

import ServerData.Models.Map.Zone;
import ServerData.Models.Player.Player;
import com.girlkun.network.io.Message;
import ServerData.Services.MapService;
import ServerData.Services.Service;
import ServerData.Services.ChangeMapService;
import ServerData.Utils.Util;

/**
 * @author by Ts iu iu
 */
public class MartialCongressService {

    private static MartialCongressService i;

    public static MartialCongressService gI() {
        if (i == null) {
            i = new MartialCongressService();
        }
        return i;
    }

    public void startChallenge(Player player) {
        Zone zone = getMapChalllenge(129);
        if (zone != null) {
            ChangeMapService.gI().changeMap(player, zone, player.location.x, 360);
            Util.setTimeout(() -> {
                MartialCongress mc = new MartialCongress();
                mc.setPlayer(player);
                mc.setNpc(zone.getReferee());
                mc.toTheNextRound();
                MartialCongressManager.gI().add(mc);
                Service.getInstance().sendThongBao(player, "Số thứ tự của ngươi là 1\n chuẩn bị thi đấu nhé");
            }, 500);
        } else {

        }
    }

    public void moveFast(Player pl, int x, int y) {
        Message msg;
        try {
            msg = new Message(58);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeShort(x);
            msg.writer().writeShort(y);
            msg.writer().writeInt((int) pl.id);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendTypePK(Player player, Player boss) {
        Message msg;
        try {
            msg = Service.getInstance().messageSubCommand((byte) 35);
            msg.writer().writeInt((int) boss.id);
            msg.writer().writeByte(3);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public Zone getMapChalllenge(int mapId) {
        Zone map = MapService.gI().getMapWithRandZone(mapId);
        if (map.getNumOfBosses() < 1) {
            return map;
        }
        return null;
    }
}
