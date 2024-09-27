package ServerData.Boss.ListBoss;

import ServerData.Boss.Boss;
import ServerData.Boss.BossID;
import ServerData.Boss.BossStatus;
import ServerData.Boss.BossesData;
import ServerData.Boss.ListBoss.DHVT.BossDHVT;
import ServerData.Models.Map.Zone;
import ServerData.Models.Player.Player;
import ServerData.Models.Player.Referee;
import ServerData.Models.Player.Referee1;
import ServerData.Server.Client;
import ServerData.Services.MapService;
import ServerData.Services.PlayerService;
import ServerData.Services.Service;
import ServerData.Services.ChangeMapService;
import ServerData.Utils.Util;
import java.util.Random;

/**
 *
 * @author ADMIN
 */
public class Noel extends Boss {

    private long phatquane;
    private long timedephatquane;
    private final long timenhay = 1000;
    private long lastTimeJoinMap;
    
    public Noel() throws Exception {
        super(BossID.NOEL_NE, BossesData.NOELNE);
    }
    @Override
    public Zone getMapJoin() {
        int mapId = this.data[this.currentLevel].getMapJoin()[Util.nextInt(0, this.data[this.currentLevel].getMapJoin().length - 1)];
        return MapService.gI().getMapById(mapId).zones.get(0);
    }
    
    @Override
    public void active() {
        this.phatquane();
        this.cFlag = 8;
        Service.gI().changeFlag(this, this.cFlag);
        this.nPoint.tlNeDon = 10000000;
        if (Util.canDoWithTime(st, 600000)){
            this.changeStatus(BossStatus.LEAVE_MAP);
        }
    }
    @Override
    public Player getPlayerAttack() {
        return super.getPlayerAttack();
    }
    
    @Override
    public void joinMap() {
        super.joinMap();
        st= System.currentTimeMillis();
        lastTimeJoinMap = System.currentTimeMillis() + timenhay;
    }
    private long st;
    
    @Override
    public void moveTo(int x, int y) {
        byte dir = (byte) (this.location.x - x < 0 ? 1 : -1);
        byte move = (byte) Util.nextInt(20, 30);
        PlayerService.gI().playerMove(this, this.location.x + (dir == 1 ? move : -move), y);
    }
        
    public void phatquane() {
    if (Util.canDoWithTime(st, 60000)){
        Player ramdonPlayer = Client.gI().getPlayers().get(Util.nextInt(Client.gI().getPlayers().size()));
            if (ramdonPlayer != null && ramdonPlayer.isPl() && !ramdonPlayer.isPet && !ramdonPlayer.isNewPet && !(ramdonPlayer instanceof BossDHVT) && !(ramdonPlayer instanceof Referee) && !(ramdonPlayer instanceof Referee1)) {
                if (ramdonPlayer.zone.map.mapId != 51 && ramdonPlayer.zone.map.mapId != 5 && ramdonPlayer.zone.map.mapId != 21 && ramdonPlayer.zone.map.mapId != 22 && ramdonPlayer.zone.map.mapId != 23 && ramdonPlayer.zone.map.mapId != 113 && ramdonPlayer.zone.map.mapId != 129 && ramdonPlayer.zone.map.mapId != 52) {
                        ChangeMapService.gI().spaceShipArrive(this, (byte) 2, ChangeMapService.DEFAULT_SPACE_SHIP);
                        ChangeMapService.gI().exitMap(this);
                        this.zoneFinal = null;
                        this.lastZone = null;
                        this.zone = ramdonPlayer.zone;
                        this.location.x = Util.nextInt(100, zone.map.mapWidth - 100);
                        this.location.y = zone.map.yPhysicInTop(this.location.x, 100);
                        this.joinMap();
    }}}    
    if (Util.canDoWithTime(st, 1000)) {
    Player pl = this.zone.getRandomPlayerInMapNotPet();
    if (pl == null || pl.isDie() || pl.isPet || pl.isNewPet) {
            return;
    }
    this.moveToPlayer(pl);
    }
    if (!Util.canDoWithTime(this.phatquane, this.timedephatquane)) {
            return;
    }
    if (Util.canDoWithTime(this.phatquane, this.timedephatquane)) {
    Player player = getPlayerAttack();  
    this.moveTo(player.location.x,player.location.y);
    if (Util.isTrue(40, 100)) {
    Service.getInstance().dropItemMap(this.zone,Util.ratiDTL(zone, Util.nextInt(555, 567), 1, this.location.x, this.location.y, player.id));
    this.chat("ohohohoho ta đã ném ra vài cục shit");
    } else {
    int[] listdoC12 = new int[]{233, 237, 241,245, 249, 253,257, 261, 265,269, 273, 277};
    int randomlist = new Random().nextInt(listdoC12.length);
    Service.getInstance().dropItemMap(this.zone,Util.RaitiDoc12(zone, listdoC12[randomlist], 1, this.location.x, this.location.y, player.id));
    this.chat("ohohohoho ta đã ném ra vài cục shit");    
    }
    this.timedephatquane = 5000;
    this.phatquane = System.currentTimeMillis();
    }}
    
    @Override
    public void update() {
        super.update();
        if (Client.gI().getPlayers().isEmpty() || this.isDie()) {
            return;
        }
        try {
            Player ramdonPlayer = Client.gI().getPlayers().get(Util.nextInt(Client.gI().getPlayers().size()));
                if (ramdonPlayer != null && ramdonPlayer.isPl() && !ramdonPlayer.isPet && !ramdonPlayer.isNewPet && !(ramdonPlayer instanceof BossDHVT) && !(ramdonPlayer instanceof Referee) && !(ramdonPlayer instanceof Referee1)) {
                    if (ramdonPlayer.zone.map.mapId != 51 && ramdonPlayer.zone.map.mapId != 5 && ramdonPlayer.zone.map.mapId != 21 && ramdonPlayer.zone.map.mapId != 22 && ramdonPlayer.zone.map.mapId != 23 && ramdonPlayer.zone.map.mapId != 113 && ramdonPlayer.zone.map.mapId != 129 && ramdonPlayer.zone.map.mapId != 52 && this.zone.getPlayers().size() <= 0 && System.currentTimeMillis() > this.lastTimeJoinMap) {
                        lastTimeJoinMap = System.currentTimeMillis() + timenhay;
                        ChangeMapService.gI().spaceShipArrive(this, (byte) 2, ChangeMapService.DEFAULT_SPACE_SHIP);
                        ChangeMapService.gI().exitMap(this);
                        this.zoneFinal = null;
                        this.lastZone = null;
                        this.zone = ramdonPlayer.zone;
                        this.location.x = Util.nextInt(100, zone.map.mapWidth - 100);
                        this.location.y = zone.map.yPhysicInTop(this.location.x, 100);
                        this.joinMap();
                    }
                }
            } catch (Exception e) {
        }
    }
}
