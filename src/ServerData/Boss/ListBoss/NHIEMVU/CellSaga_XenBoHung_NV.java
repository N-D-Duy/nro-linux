package ServerData.Boss.ListBoss.NHIEMVU;

import Server.Data.Consts.ConstPlayer;
import ServerData.Boss.Boss;
import ServerData.Boss.BossStatus;
import ServerData.Boss.BossesData;
import ServerData.Boss.BossID;
import ServerData.Models.Map.ItemMap;
import ServerData.Models.Player.Player;
import ServerData.Services.EffectSkillService;
import ServerData.Services.PlayerService;
import ServerData.Services.Service;
import ServerData.Services.TaskService;
import ServerData.Utils.Util;

import java.util.Random;


public class CellSaga_XenBoHung_NV extends Boss {

    private long lastTimeHapThu;
    private int timeHapThu;

    public CellSaga_XenBoHung_NV() throws Exception {
        super(BossID.XEN_BO_HUNG, BossesData.XEN_BO_HUNG_1, BossesData.XEN_BO_HUNG_2, BossesData.XEN_BO_HUNG_3);
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
        int randomDo = new Random().nextInt(itemDos.length);
        if(Util.isTrue(80,100)){
        ItemMap it = new ItemMap(this.zone, Util.nextInt(17, 19), 1, this.location.x, this.location.y, plKill.id);
        Service.gI().dropItemMap(this.zone, it);
            Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, itemDos[randomDo], 1, this.location.x, this.location.y, plKill.id));
    }else  if(Util.isTrue(10,100)){
         Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, itemDos[randomDo], 1, this.location.x, this.location.y, plKill.id));
        }
     else if(Util.isTrue(10,100)){
         ItemMap it2 = new ItemMap(this.zone, 16, 1, this.location.x, this.location.y, plKill.id);
        Service.gI().dropItemMap(this.zone, it2);
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
        this.timeHapThu = Util.nextInt(30000, 50000);
    }
    

}

/**
 * Vui lòng không sao chép mã nguồn này dưới mọi hình thức. Hãy tôn trọng tác
 * giả của mã nguồn này. Xin cảm ơn! - GirlBeo
 */
