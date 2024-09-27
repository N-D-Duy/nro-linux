//package ServerData.Boss.ListBoss;
//
//import ServerData.Boss.Boss;
//import ServerData.Boss.BossID;
//import ServerData.Boss.BossStatus;
//import ServerData.Boss.BossesData;
//import ServerData.Boss.ListBoss.DHVT.BossDHVT;
//import ServerData.Models.Map.Zone;
//import ServerData.Models.Player.Player;
//import ServerData.Models.Player.Referee;
//import ServerData.Models.Player.Referee1;
//import ServerData.Server.Client;
//import ServerData.Services.InventoryServiceNew;
//import ServerData.Services.MapService;
//import ServerData.Services.PlayerService;
//import ServerData.Services.Service;
//import ServerData.Services.ChangeMapService;
//import ServerData.Utils.Util;
//
///**
// *
// * @author by Ts cụt cụt
// */
//public class AnTrom extends Boss {
//
//    private long antrom;
//    private long time;
//    private static final long timenhay = 1000;
//    private long lastTimeJoinMap;
//
//    public AnTrom() throws Exception {
//        super(BossID.AN_TROM, BossesData.ANTROM);
//    }
//
//    @Override
//    public Zone getMapJoin() {
//        int mapId = this.data[this.currentLevel].getMapJoin()[Util.nextInt(0, this.data[this.currentLevel].getMapJoin().length - 1)];
//        return MapService.gI().getMapById(mapId).zones.get(0);
//    }
//    
//    @Override
//    public void active() {
//        this.antrom();
//        this.cFlag = 8;
//        Service.gI().changeFlag(this, this.cFlag);
//        this.nPoint.tlNeDon = 80;
//        if (Util.canDoWithTime(st, 600000)){
//            this.changeStatus(BossStatus.LEAVE_MAP);
//        }
//    }
//    @Override
//    public Player getPlayerAttack() {
//        return super.getPlayerAttack();
//    }
//    
//    @Override
//    public void joinMap() {
//        super.joinMap();
//        st= System.currentTimeMillis();
//        lastTimeJoinMap = System.currentTimeMillis() + timenhay;
//
//    }
//    private long st;
//    
//    @Override
//    public void moveTo(int x, int y) {
//        byte dir = (byte) (this.location.x - x < 0 ? 1 : -1);
//        byte move = (byte) Util.nextInt(20, 30);
//        PlayerService.gI().playerMove(this, this.location.x + (dir == 1 ? move : -move), y);
//    }
//
//    private void antrom() {
//        if (Util.canDoWithTime(st, 30000)){
//        Player ramdonPlayer = Client.gI().getPlayers().get(Util.nextInt(Client.gI().getPlayers().size()));
//            if (ramdonPlayer != null && ramdonPlayer.isPl() && !ramdonPlayer.isPet && !ramdonPlayer.isNewPet && !(ramdonPlayer instanceof BossDHVT) && !(ramdonPlayer instanceof Referee) && !(ramdonPlayer instanceof Referee1)) {
//                if (ramdonPlayer.zone.map.mapId != 51 && ramdonPlayer.zone.map.mapId != 5 && ramdonPlayer.zone.map.mapId != 21 && ramdonPlayer.zone.map.mapId != 22 && ramdonPlayer.zone.map.mapId != 23 && ramdonPlayer.zone.map.mapId != 113 && ramdonPlayer.zone.map.mapId != 129 && ramdonPlayer.zone.map.mapId != 52) {
//                        ChangeMapService.gI().spaceShipArrive(this, (byte) 2, ChangeMapService.DEFAULT_SPACE_SHIP);
//                        ChangeMapService.gI().exitMap(this);
//                        this.zoneFinal = null;
//                        this.lastZone = null;
//                        this.zone = ramdonPlayer.zone;
//                        this.location.x = Util.nextInt(100, zone.map.mapWidth - 100);
//                        this.location.y = zone.map.yPhysicInTop(this.location.x, 100);
//                        this.joinMap();
//        }}}
//        // Kiểm tra thời gian cho phép ăn trộm
//        if (!Util.canDoWithTime(this.time, this.antrom)) {
//            return;
//        }
//        // Lấy một người chơi ngẫu nhiên trong khu vực đang hoạt động của Boss
//        Player player = this.zone.getRandomPlayerInMapNotPet();
//        if (player == null || player.isDie() || player.isPet || player.isNewPet) {
//            return;
//        }
//        // Kiểm tra số vàng và hoạt động của người chơi
//        if (player.inventory.gold <= 0) {
//            this.chat("Khứa này nghèo thế!");
//            return;
//        }
//        this.moveToPlayer(player);
//        if (this.location.x+Util.nextInt(0, 10) >= player.location.x) {
//        if (!(player instanceof BossDHVT) && !(player instanceof Referee) && !(player instanceof Referee1)) {
//        // ăn trộm vàng của người chơi
//        int stolenGold = Util.nextInt(5000, 10000000);
//        player.inventory.gold -= stolenGold;
//        this.inventory.gold += stolenGold;
//        // Thông báo ăn trộm trên kênh chat
//        
//        this.chat("Haha, vừa trôm được " + Util.numberToMoney(stolenGold) + " vàng rồi!!");
//        // Cập nhật thời gian ăn trộm lần cuối và thời gian cho phép ăn trộm tiếp theo
//        this.time = System.currentTimeMillis();
//        this.antrom = 1000;
//        // Gửi thông tin vàng mới của người chơi và cập nhật trên máy chủ
//        Service.gI().sendMoney(player);
//        InventoryServiceNew.gI().sendItemBags(player);
//        }
//    }
//}
//
//
//    @Override
//    public void reward(Player plKill) {
//        if (Util.isTrue(100,100)) {
//            int goldReward = (int) (this.inventory.gold/2);
//            Service.getInstance().dropItemMap(
//                    this.zone,
//                    Util.manhTS(zone, 76, goldReward, this.location.x, this.location.y, plKill.id)
//            );
//            Service.gI().sendThongBaoAllPlayer(plKill.name + " Vừa Tiêu Diệt Ăn Trộm Và Nhận Được " + Util.numberToMoney(goldReward) + " Vàng");
//            plKill.inventory.event += 1;
//        }
//    }
//    @Override
//    public void update() {
//        super.update();
//        if (Client.gI().getPlayers().isEmpty() || this.isDie()) {
//            return;
//        }
//        try {
//            Player ramdonPlayer = Client.gI().getPlayers().get(Util.nextInt(Client.gI().getPlayers().size()));
//                if (ramdonPlayer != null && ramdonPlayer.isPl() && !ramdonPlayer.isPet && !ramdonPlayer.isNewPet && !(ramdonPlayer instanceof BossDHVT) && !(ramdonPlayer instanceof Referee) && !(ramdonPlayer instanceof Referee1)) {
//                    if (ramdonPlayer.zone.map.mapId != 51 && ramdonPlayer.zone.map.mapId != 5 && ramdonPlayer.zone.map.mapId != 21 && ramdonPlayer.zone.map.mapId != 22 && ramdonPlayer.zone.map.mapId != 23 && ramdonPlayer.zone.map.mapId != 113 && ramdonPlayer.zone.map.mapId != 129 && ramdonPlayer.zone.map.mapId != 52 && this.zone.getPlayers().size() <= 0 && System.currentTimeMillis() > this.lastTimeJoinMap) {
//                        lastTimeJoinMap = System.currentTimeMillis() + timenhay;
//                        ChangeMapService.gI().spaceShipArrive(this, (byte) 2, ChangeMapService.DEFAULT_SPACE_SHIP);
//                        ChangeMapService.gI().exitMap(this);
//                        this.zoneFinal = null;
//                        this.lastZone = null;
//                        this.zone = ramdonPlayer.zone;
//                        this.location.x = Util.nextInt(100, zone.map.mapWidth - 100);
//                        this.location.y = zone.map.yPhysicInTop(this.location.x, 100);
//                        this.joinMap();
//                    }
//                }
//            } catch (Exception e) {
//        }
//    }
//}