package ServerData.Boss.ListBoss.NHIEMVU;

import ServerData.Boss.Boss;
import ServerData.Boss.BossID;
import ServerData.Boss.BossStatus;
import ServerData.Boss.BossesData;
import ServerData.Models.Map.ItemMap;
import ServerData.Models.Player.Player;
import ServerData.Services.EffectSkillService;
import ServerData.Services.Service;
import ServerData.Services.TaskService;
import ServerData.Utils.Util;
import java.util.Random;


public class DRMSaga_Chaien extends Boss {

    public DRMSaga_Chaien() throws Exception {
        super(BossID.CHAIEN, BossesData.CHAIEN);
    }
     @Override
    public void reward(Player plKill) {
        int[] itemDos = new int[]{261, 265, 269, 273, 277};
        int[] NRs = new int[]{19,17,18};
        int randomDo = new Random().nextInt(itemDos.length);
        int randomNR = new Random().nextInt(NRs.length);
        if (Util.isTrue(15, 100)) {
            Service.gI().dropItemMap(this.zone, Util.RaitiDoc12(zone, itemDos[randomDo], 1, this.location.x, this.location.y, plKill.id));
        } else  if (Util.isTrue(50, 100)){
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, NRs[randomNR], 1, this.location.x, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id));
        } else {
              Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 1504, Util.nextInt(1,2), this.location.x, this.location.y, plKill.id));
           
        } 
        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
    }

   @Override
    public void active() {
        super.active(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void joinMap() {
        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
        st= System.currentTimeMillis();
    }
    private long st;

    @Override
    public void wakeupAnotherBossWhenDisappear() {
        if (this.parentBoss == null) {
            return;
        }
        for (Boss boss : this.parentBoss.bossAppearTogether[this.parentBoss.currentLevel]) {
            if (boss.id == BossID.XEKO && boss.isDie()) {
                this.parentBoss.changeToTypePK();
                return;
            }
        }
        
    }
  
}

/**
 * Vui lòng không sao chép mã nguồn này dưới mọi hình thức. Hãy tôn trọng tác
 * giả của mã nguồn này. Xin cảm ơn! - GirlBeo
 */
