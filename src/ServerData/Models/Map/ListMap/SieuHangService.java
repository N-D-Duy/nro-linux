package ServerData.Models.Map.ListMap;

import Server.Connect.GodGK;
import ServerData.Boss.BossData;
import ServerData.Models.Map.Zone;
import ServerData.Models.Player.Player;
import ServerData.Models.Player.PlayerSkill.Skill;
import com.girlkun.network.io.Message;
import ServerData.Services.MapService;
import ServerData.Services.PlayerService;
import ServerData.Services.Service;
import ServerData.Services.ChangeMapService;
import ServerData.Utils.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Béo Mập :3
 */
public class SieuHangService {

    private static SieuHangService i;

    public static SieuHangService gI() {
        if (i == null) {
            i = new SieuHangService();
        }
        return i;
    }

    public boolean checkDangThiDau(Player player1, Player player2) {
        for (SieuHang sieuHang : SieuHangManager.gI().list) {
            if (player1.name.equals(sieuHang.getPlayer().name) || player1.name.equals(sieuHang.getBoss().name) || player2.name.equals(sieuHang.getPlayer().name) || player2.name.equals(sieuHang.getBoss().name)) {
                return true;
            }
        }
        return false;
    }

    public void startChallenge(Player player, int id) throws Exception {
        if (player.zone.map.mapId != 113) {
            return;
        }
        Player pl = GodGK.loadById(id);
        if (pl != null) {
            pl.nPoint.calPoint();
            if (player.rankSieuHang > 11 && pl.rankSieuHang <= 10) {
                Service.gI().sendThongBao(player, "Chưa thể khiêu chiến đối thử trong top 100");
                return;
            }
            if (player.rankSieuHang < 10 && (Math.abs(player.rankSieuHang - pl.rankSieuHang) > 2)) {
                Service.gI().sendThongBao(player, "Không thể khiêu chiến đối thủ quá 2 hạng");
                return;
            }
            if (checkDangThiDau(player, pl)) {
                Service.gI().sendThongBao(player, "Đối thủ đang thi đấu");
                return;
            }

//            int[][] skillTemp = new int[pl.playerSkill.skills.size()][3];
//            for (byte i = 0; i < pl.playerSkill.skills.size(); i++) {
//                Skill skill = pl.playerSkill.skills.get(i);
//                if (skill.point > 0) {
//                    skillTemp[i][0] = skill.template.id;
//                    skillTemp[i][1] = skill.point;
//                    skillTemp[i][2] = skill.IsSkillDam(skill.template.id) ? 300 : skill.coolDown;
//                }
//            }
//            BossData data = BossData.builder()
//                    .name(pl.name)
//                    .gender(pl.gender)
//                    .dame(pl.nPoint.dame)
//                    .hp(new int[]{pl.nPoint.hpMax})
//                    .outfit(new short[]{pl.getHead(), pl.getBody(), pl.getLeg(), -1, -1, -1})
//                    .mapJoin(new int[]{113})
//                    .skillTemp(skillTemp)
//                    .secondsRest(5)
//                    .build();
            List<Skill> skillList = new ArrayList<>();
            for (byte i = 0; i < pl.playerSkill.skills.size(); i++) {
                Skill skill = pl.playerSkill.skills.get(i);
                if (skill.point > 0) {
                    skillList.add(skill);
                }
            }
            int[][] skillTemp = new int[skillList.size()][5];
            for (byte i = 0; i < skillList.size(); i++) {
                Skill skill = skillList.get(i);
                if (skill.point > 0) {
                    skillTemp[i][0] = skill.template.id;
                    skillTemp[i][1] = skill.point;
                    skillTemp[i][2] = skill.coolDown;
                }
            }

            BossData data = new BossData(
                    pl.name,
                    pl.gender,
                    new short[]{pl.getHead(), pl.getBody(), pl.getLeg(), pl.getFlagBag(), pl.getAura(), pl.getEffFront()},
                    pl.nPoint.dame,
                    new long[]{pl.nPoint.hpMax},
                    new int[]{140},
                    skillTemp,
                    new String[]{}, //text chat 1
                    new String[]{}, //text chat 2
                    new String[]{}, //text chat 3
                    5
            );

            ClonePlayer boss = new ClonePlayer(player, data, (int) pl.id);
            boss.rankSieuHang = pl.rankSieuHang;
            Zone zone = getMapChalllenge(113);
            if (zone != null) {
                ChangeMapService.gI().changeMap(player, zone, player.location.x, 360);
                Util.setTimeout(() -> {
                    SieuHang mc = new SieuHang();
                    mc.setPlayer(player);
                    mc.toTheNextRound(boss);
                    SieuHangManager.gI().add(mc);
                }, 500);
            }
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
