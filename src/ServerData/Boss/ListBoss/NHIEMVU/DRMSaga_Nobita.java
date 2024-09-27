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

public class DRMSaga_Nobita extends Boss {

    public DRMSaga_Nobita() throws Exception {
        super(BossID.NOBITA, BossesData.NOBITA);
    }


 @Override
    public void reward(Player plKill) {
        int[] itemDos = new int[]{16,19};
        int[] NRs = new int[]{17,18};
        int randomDo = new Random().nextInt(itemDos.length);
        int randomNR = new Random().nextInt(NRs.length);
        if (Util.isTrue(15, 100)) {
            if (Util.isTrue(1, 5)) {
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 561, 1, this.location.x, this.location.y, plKill.id));
           
            }
            Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, itemDos[randomDo], 1, this.location.x, this.location.y, plKill.id));
        }  else {
              Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 1504, Util.nextInt(1,2), this.location.x, this.location.y, plKill.id));

        } 
        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
    }
    
    @Override
    public void wakeupAnotherBossWhenDisappear() {
        if (this.parentBoss == null) {
            return;
        }
        for (Boss boss : this.parentBoss.bossAppearTogether[this.parentBoss.currentLevel]) {
            if (boss.id == BossID.XEKO || boss.id == BossID.CHAIEN) {
                boss.changeToTypePK();
            }
        }
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
     
}






















