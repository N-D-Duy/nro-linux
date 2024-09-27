package ServerData.Boss.ListBoss.NHIEMVU;
import Server.Data.Consts.ConstPlayer;
import ServerData.Boss.*;
import ServerData.Models.Map.ItemMap;
import ServerData.Models.Player.Player;
import ServerData.Server.Manager;
import ServerData.Services.EffectSkillService;
import ServerData.Services.Service;
import ServerData.Services.TaskService;
import ServerData.Utils.Util;
import ServerData.Services.PlayerService;
import java.util.Random;


public class CellSaga_SieuBoHung_NV extends Boss {
    private long lastTimeHapThu;
    private int timeHapThu;
    public CellSaga_SieuBoHung_NV() throws Exception {
        super(BossID.SIEU_BO_HUNG, BossesData.SIEU_BO_HUNG_1, BossesData.SIEU_BO_HUNG_2, BossesData.SIEU_BO_HUNG_3);
    }
    @Override
    public void joinMap() {
        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
        st = System.currentTimeMillis();
    }
    private long st;

   @Override
    public void reward(Player plKill) {
        int[] itemDos = new int[]{555, 557, 559, 556, 558, 560, 562, 564, 566, 563, 565, 567};
        int[] NRs = new int[]{18,16,17};
        int randomDo = new Random().nextInt(itemDos.length);
        int randomNR = new Random().nextInt(NRs.length);
        int randomDo12 = new Random().nextInt(itemDos.length);
        int[] itemDos12 = new int[]{233, 237, 241, 245, 249, 253, 257, 261, 265, 269, 273, 277};
            if (Util.isTrue(10, 100)) {
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, itemDos12[randomDo12], 1, this.location.x, this.location.y, plKill.id));
            }
            else if (Util.isTrue(80, 100)) {
            Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, itemDos[randomDo], 1, this.location.x, this.location.y, plKill.id));
            } 
            else if (Util.isTrue(50, 100)) {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, NRs[randomNR], 1, this.location.x, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id));
        }
        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
    }
     @Override
    public void active() {
        if (this.typePk == ConstPlayer.NON_PK) {
            this.changeToTypePK();
        }
        this.hapThu();
        this.attack();
         super.active(); //To change body of generated methods, choose Tools | Templates.
    }

    private void hapThu() {
        if (!Util.canDoWithTime(this.lastTimeHapThu, this.timeHapThu) || !Util.isTrue(1, 100)) {
            return;
        }

        Player pl = this.zone.getRandomPlayerInMap();
        if (pl == null || pl.isDie()) {
            return;
        }
//        ChangeMapService.gI().changeMapYardrat(this, this.zone, pl.location.x, pl.location.y);
        this.nPoint.dameg += (pl.nPoint.dame * 5 / 100);
        this.nPoint.hpg += (pl.nPoint.hp * 2 / 100);
        this.nPoint.critg++;
        this.nPoint.calPoint();
        PlayerService.gI().hoiPhuc(this, pl.nPoint.hp, 0);
        pl.injured(null, pl.nPoint.hpMax, true, false);
        Service.gI().sendThongBao(pl, "Bạn vừa bị " + this.name + " hấp thu!");
        this.chat(2, "Ui cha cha, kinh dị quá. " + pl.name + " vừa bị tên " + this.name + " nuốt chửng kìa!!!");
        this.chat("Haha, ngọt lắm đấy " + pl.name + "..");
        this.lastTimeHapThu = System.currentTimeMillis();
        this.timeHapThu = Util.nextInt(15000, 20000);
    }
 
//    @Override
//    public void moveTo(int x, int y) {
//        if(this.currentLevel == 1){
//            return;
//        }
//        super.moveTo(x, y);
//    }
//
//    @Override
//    public void reward(Player plKill) {
//        if(this.currentLevel == 1){
//            return;
//        }
//        super.reward(plKill);
//    }
//    
//    @Override
//    protected void notifyJoinMap() {
//        if(this.currentLevel == 1){
//            return;
//        }
//        super.notifyJoinMap();
//    }
}






















