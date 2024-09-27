package ServerData.Models.Map.ListMap;

import ServerData.Models.Clan.Clan;
import ServerData.Models.Map.TrapMap;
import ServerData.Models.Map.Zone;
import ServerData.Models.Map.Mob.Mob;
import ServerData.Models.Player.Player;
import ServerData.Services.ItemTimeService;
import ServerData.Services.MapService;
import ServerData.Services.Service;
import ServerData.Services.ChangeMapService;
import ServerData.Utils.Util;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author TTS
 */
public class KhiGasHuyDiet implements Runnable {

    public static final long POWER_CAN_GO_TO_KGHD = 2000000000;

    public static final List<KhiGasHuyDiet> KHI_GA_HUY_DIETS;
    public static final int MAX_AVAILABLE = 50;
    public static final int TIME_KHI_GA_HUY_DIET = 1800000;

    public static Object gI() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private Player player;

    static {
        KHI_GA_HUY_DIETS = new ArrayList<>();
        for (int i = 0; i < MAX_AVAILABLE; i++) {
            KHI_GA_HUY_DIETS.add(new KhiGasHuyDiet(i));
        }
    }

    public int id;
    public byte level;
    public final List<Zone> zones;

    public Clan clan;
    public boolean isOpened;
    private long lastTimeOpen;
    private boolean running;
    private long lastTimeUpdate;

    public KhiGasHuyDiet(int id) {
        this.id = id;
        this.zones = new ArrayList<>();
        running = true;
        //new Thread(this).start();
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(10000);
                if (Util.canDoWithTime(lastTimeUpdate, 10000)) {
                    update();
                    lastTimeUpdate = System.currentTimeMillis();
                }
            } catch (Exception ignored) {
            }

        }
    }

    public void update() {
        for (KhiGasHuyDiet khiga : KHI_GA_HUY_DIETS) {
            if (khiga.isOpened) {
                if (Util.canDoWithTime(lastTimeOpen, TIME_KHI_GA_HUY_DIET)) {
                    this.finish();
                }
            }
        }
    }

    public void openKhiGaHuyDiet(Player plOpen, Clan clan, byte level) {
        this.level = level;
        this.lastTimeOpen = System.currentTimeMillis();
        this.isOpened = true;
        this.clan = clan;
        this.clan.timeOpenKhiGaHuyDiet = this.lastTimeOpen;
        this.clan.playerOpenKhiGaHuyDiet = plOpen;
        this.clan.KhiGaHuyDiet = this;

        resetKhiGa();
        ChangeMapService.gI().goToKhiGas(plOpen);
        sendTextKhiGaHuyDiet();
    }

    private void resetKhiGa() {
        for (Zone zone : zones) {
            for (TrapMap trap : zone.trapMaps) {
                trap.dame = this.level * 10000;
            }
        }
        for (Zone zone : zones) {
            for (Mob m : zone.mobs) {
                m.initMobKhiGaHuyDiet(m, this.level);
                m.hoiSinhMob(m);
                m.hoiSinh();
                m.sendMobHoiSinh();
            }
        }
    }

    //kết thúc Khí Ga Hủy Diệt
    public void finish() {
        List<Player> plOutKG = new ArrayList();
        for (Zone zone : zones) {
            List<Player> players = zone.getPlayers();
            for (Player pl : players) {
                plOutKG.add(pl);
                kickOutOfKGHD(pl);
            }

        }
        for (Player pl : plOutKG) {
            ChangeMapService.gI().changeMapBySpaceShip(pl, 0, -1, 64);
        }


        this.clan.KhiGaHuyDiet = null;
        this.clan = null;
        this.isOpened = false;
    }

    private void kickOutOfKGHD(Player player) {
        if (MapService.gI().isMapKhiGaHuyDiet(player.zone.map.mapId)) {
            Service.getInstance().sendThongBao(player, "Khí Ga Hủy Diệt Đã Kết Thúc!");
            ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 1038);
            running = false;
            this.clan.KhiGaHuyDiet = null;
        }
    }

    public Zone getMapById(int mapId) {
        for (Zone zone : zones) {
            if (zone.map.mapId == mapId) {
                return zone;
            }
        }
        return null;
    }

    public static void addZone(int idKhiGa, Zone zone) {
        KHI_GA_HUY_DIETS.get(idKhiGa).zones.add(zone);
    }

    private void sendTextKhiGaHuyDiet() {
        for (Player pl : this.clan.membersInGame) {
            ItemTimeService.gI().sendTextKhiGaHuyDiet(pl);
        }
    }
}
