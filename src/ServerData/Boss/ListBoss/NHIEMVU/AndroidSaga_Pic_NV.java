package ServerData.Boss.ListBoss.NHIEMVU;

import ServerData.Boss.Boss;
import ServerData.Boss.BossID;
import ServerData.Boss.BossStatus;
import ServerData.Boss.BossesData;
import ServerData.Models.Map.ItemMap;
import ServerData.Models.Player.Player;
import ServerData.Services.Service;
import ServerData.Services.TaskService;
import ServerData.Utils.Util;


public class AndroidSaga_Pic_NV extends Boss {

    public AndroidSaga_Pic_NV() throws Exception {
        super(BossID.PIC, BossesData.PIC);
    }

    @Override
    public void reward(Player plKill) {
        int[] itemRan = new int[]{381, 382, 383, 384, 385};
        int itemId = itemRan[2];
        if (Util.isTrue(15, 100)) {
            ItemMap it = new ItemMap(this.zone, itemId, 16, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            Service.gI().dropItemMap(this.zone, it);
        }
         TaskService.gI().checkDoneTaskKillBoss(plKill, this);
    }
    @Override
    public void doneChatE() {
        if (this.parentBoss == null || this.parentBoss.bossAppearTogether == null
                || this.parentBoss.bossAppearTogether[this.parentBoss.currentLevel] == null) {
            return;
        }
        for (Boss boss : this.parentBoss.bossAppearTogether[this.parentBoss.currentLevel]) {
            if (boss.id == BossID.POC && !boss.isDie()) {
                boss.changeToTypePK();
                break;
            }
        }
    }  @Override
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

/**
 * Vui lòng không sao chép mã nguồn này dưới mọi hình thức. Hãy tôn trọng tác
 * giả của mã nguồn này. Xin cảm ơn! - GirlBeo
 */
