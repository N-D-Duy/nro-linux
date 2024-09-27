package ServerData.Models.Map.ListMap;

import ServerData.Models.Item.ItemTime;
import ServerData.Models.Player.Player;
import ServerData.Services.MapService;
import ServerData.Services.Service;
import ServerData.Services.ChangeMapService;
import ServerData.Utils.TimeUtil;
import ServerData.Utils.Util;

import java.util.List;

public class MapMaBu {


    public static final byte HOUR_OPEN_MAP_MABU = 11;
    public static final byte MIN_OPEN_MAP_MABU = 59;
    public static final byte SECOND_OPEN_MAP_MABU = 59;


    public static final byte HOUR_CLOSE_MAP_MABU = 12;
    public static final byte MIN_CLOSE_MAP_MABU = 59;
    public static final byte SECOND_CLOSE_MAP_MABU = 59;
    
    public static final byte HOUR_OPEN_MAP_TN = 20;
    public static final byte MIN_OPEN_MAP_TN = 59;
    public static final byte SECOND_OPEN_MAP_TN = 59;
    public static final byte HOUR_CLOSE_MAP_TN = 23;
    public static final byte MIN_CLOSE_MAP_TN = 59;
    public static final byte SECOND_CLOSE_MAP_TN = 59;
    
    public static final byte HOUR_OPEN_MAP_PVP = 20;
    public static final byte MIN_OPEN_MAP_PVP = 59;
    public static final byte SECOND_OPEN_MAP_PVP = 59;
    public static final byte HOUR_CLOSE_MAP_PVP = 21;
    public static final byte MIN_CLOSE_MAP_PVP = 59;
    public static final byte SECOND_CLOSE_MAP_PVP = 59;
    

    public static final byte HOUR_OPEN_MAP_TN2 = 8;
    public static final byte MIN_OPEN_MAP_TN2 = 59;
    public static final byte SECOND_OPEN_MAP_TN2 = 59;
    public static final byte HOUR_CLOSE_MAP_TN2 = 11;
    public static final byte MIN_CLOSE_MAP_TN2 = 59;
    public static final byte SECOND_CLOSE_MAP_TN2 = 59;
    
    public static final byte HOUR_OPEN_MAP_TDST = 12;
    public static final byte MIN_OPEN_MAP_TDST = 59;
    public static final byte SECOND_OPEN_MAP_TDST = 59;
    public static final byte HOUR_CLOSE_MAP_TDST = 13;
    public static final byte MIN_CLOSE_MAP_TDST = 59;
    public static final byte SECOND_CLOSE_MAP_TDST = 59;
    public static final int AVAILABLE = 7;

    private static MapMaBu i;

    public static long TIME_OPEN_MABU;
    public static long TIME_CLOSE_MABU;
    
    public static long TIME_OPEN_TN;
    public static long TIME_CLOSE_TN;
    public static long TIME_OPEN_TN2;
    public static long TIME_CLOSE_TN2;
    
    public static long TIME_OPEN_PVP;
    public static long TIME_CLOSE_PVP;
    
    public static long TIME_OPEN_TDST;
    public static long TIME_CLOSE_TDST;


    private int day = -1;

    public static MapMaBu gI() {
        if (i == null) {
            i = new MapMaBu();
        }
        i.setTimeJoinMapMaBu();
        return i;
    }

    public void setTimeJoinMapMaBu() {
        if (i.day == -1 || i.day != TimeUtil.getCurrDay()) {
            i.day = TimeUtil.getCurrDay();
            try {
                TIME_OPEN_MABU = TimeUtil.getTime(TimeUtil.getTimeNow("dd/MM/yyyy") + " " + HOUR_OPEN_MAP_MABU + ":" + MIN_OPEN_MAP_MABU + ":" + SECOND_OPEN_MAP_MABU, "dd/MM/yyyy HH:mm:ss");
                TIME_CLOSE_MABU = TimeUtil.getTime(TimeUtil.getTimeNow("dd/MM/yyyy") + " " + HOUR_CLOSE_MAP_MABU + ":" + MIN_CLOSE_MAP_MABU + ":" + SECOND_CLOSE_MAP_MABU, "dd/MM/yyyy HH:mm:ss");
                TIME_OPEN_TN = TimeUtil.getTime(TimeUtil.getTimeNow("dd/MM/yyyy") + " " + HOUR_OPEN_MAP_TN + ":" + MIN_OPEN_MAP_TN + ":" + SECOND_OPEN_MAP_TN, "dd/MM/yyyy HH:mm:ss");
                TIME_CLOSE_TN = TimeUtil.getTime(TimeUtil.getTimeNow("dd/MM/yyyy") + " " + HOUR_CLOSE_MAP_TN + ":" + MIN_CLOSE_MAP_TN + ":" + SECOND_CLOSE_MAP_TN, "dd/MM/yyyy HH:mm:ss");
                TIME_OPEN_TN2 = TimeUtil.getTime(TimeUtil.getTimeNow("dd/MM/yyyy") + " " + HOUR_OPEN_MAP_TN2 + ":" + MIN_OPEN_MAP_TN2 + ":" + SECOND_OPEN_MAP_TN2, "dd/MM/yyyy HH:mm:ss");
                TIME_CLOSE_TN2 = TimeUtil.getTime(TimeUtil.getTimeNow("dd/MM/yyyy") + " " + HOUR_CLOSE_MAP_TN2 + ":" + MIN_CLOSE_MAP_TN2 + ":" + SECOND_CLOSE_MAP_TN2, "dd/MM/yyyy HH:mm:ss");
                TIME_OPEN_PVP = TimeUtil.getTime(TimeUtil.getTimeNow("dd/MM/yyyy") + " " + HOUR_OPEN_MAP_PVP + ":" + MIN_OPEN_MAP_PVP + ":" + SECOND_OPEN_MAP_PVP, "dd/MM/yyyy HH:mm:ss");
                TIME_CLOSE_PVP = TimeUtil.getTime(TimeUtil.getTimeNow("dd/MM/yyyy") + " " + HOUR_CLOSE_MAP_PVP + ":" + MIN_CLOSE_MAP_PVP + ":" + SECOND_CLOSE_MAP_PVP, "dd/MM/yyyy HH:mm:ss");
            } catch (Exception ignored) {
            }
        }
    }


    private void kickOutOfMapMabu(Player player) {
        if (MapService.gI().isMapMaBu(player.zone.map.mapId)) {
            Service.gI().sendThongBao(player, "Trận đại chiến đã kết thúc, tàu vận chuyển sẽ đưa bạn về nhà");
            ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 250);
        }
    }
    private void kickOutOfMapTN(Player player) {
        if (Util.canDoWithTime(player.itemTime.LTNGOC, ItemTime.TIMENGOC)&&MapService.gI().isMapUpTN(player.zone.map.mapId)) {
            Service.gI().sendThongBao(player, "Thời gian up đã kết thúc, tàu vận chuyển sẽ đưa bạn về nhà");
            ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 250);
        }
    }
    private void kickOutOfMapPVP(Player player) {
        if (player.zone.map.mapId == 196) {
            Service.gI().sendThongBao(player, "Thời gian giải đã kết thúc, tàu vận chuyển sẽ đưa bạn về nhà");
            ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 250);
        }
    }
    private void kickOutOfMapTN2(Player player) {
        if (Util.canDoWithTime(player.itemTime.LTNGOC, ItemTime.TIMENGOC)&&MapService.gI().isMapUpTN2(player.zone.map.mapId)) {
            Service.gI().sendThongBao(player, "Thời gian up đã kết thúc, tàu vận chuyển sẽ đưa bạn về nhà");
            ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 250);
        }
    }
    
    private void ketthucPVP(Player player) {
        player.zone.finishMapPVP = true;
        List<Player> playersMap = player.zone.getPlayers();
        for (int i = playersMap.size() - 1; i >= 0; i--) {
            Player pl = playersMap.get(i);
            kickOutOfMapPVP(pl);
        }
    }    
    private void ketthucTN2(Player player) {
        player.zone.finishMapTN = true;
        List<Player> playersMap = player.zone.getPlayers();
        for (int i = playersMap.size() - 1; i >= 0; i--) {
            Player pl = playersMap.get(i);
            kickOutOfMapTN2(pl);
        }
    }
    private void ketthucmabu(Player player) {
        player.zone.finishMapMaBu = true;
        List<Player> playersMap = player.zone.getPlayers();
        for (int i = playersMap.size() - 1; i >= 0; i--) {
            Player pl = playersMap.get(i);
            kickOutOfMapMabu(pl);
        }
    }

    private void ketthucTN(Player player) {
        player.zone.finishMapTN = true;
        List<Player> playersMap = player.zone.getPlayers();
        for (int i = playersMap.size() - 1; i >= 0; i--) {
            Player pl = playersMap.get(i);
            kickOutOfMapTN(pl);
        }
    }
    
    public void joinMapMabu(Player player) {
        boolean changed = false;
        if (player.clan != null) {
            List<Player> players = player.zone.getPlayers();
            for (Player pl : players) {
                if (pl.clan != null && !player.equals(pl) && player.clan.equals(pl.clan) && !player.isBoss) {
                    Service.gI().changeFlag(player, Util.nextInt(9, 10));
                    changed = true;
                    break;
                }
            }
        }
        if (!changed && !player.isBoss) {
            Service.gI().changeFlag(player, Util.nextInt(9, 10));
        }
    }
    
    public void update(Player player) {
        if (player.zone == null || !MapService.gI().isMapBlackBallWar(player.zone.map.mapId)) {
            try {
                long now = System.currentTimeMillis();
                if (now < TIME_OPEN_MABU || now > TIME_CLOSE_MABU) {
                    ketthucmabu(player);
                }
                if (now < TIME_OPEN_TN || now > TIME_CLOSE_TN) {
                    ketthucTN(player);
                }
                if (now < TIME_OPEN_TN2 || now > TIME_CLOSE_TN2) {
                    ketthucTN2(player);
                }
                if (now < TIME_OPEN_PVP || now > TIME_CLOSE_PVP) {
                    ketthucPVP(player);
                }
            } catch (Exception ignored) {
            }
        }

    }
}
