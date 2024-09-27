package ServerData.Boss.ListBoss.NHIEMVU;

import Server.Data.Consts.ConstPlayer;
import ServerData.Boss.Boss;
import ServerData.Boss.BossID;
import ServerData.Boss.BossStatus;
import ServerData.Boss.BossesData;
import ServerData.Models.Map.ItemMap;
import ServerData.Models.Player.Player;
import ServerData.Services.PlayerService;
import ServerData.Services.Service;
import ServerData.Services.TaskService;
import ServerData.Utils.Util;


public class AndroidSaga_S15_NV extends Boss {

    public boolean callApk13;

    public AndroidSaga_S15_NV() throws Exception {
        super(BossID.ANDROID_15, BossesData.ANDROID_15);
    }
  @Override
    public void reward(Player plKill) {
        int[] itemRan = new int[]{1142, 382, 383, 384, 1142};
        int itemId = itemRan[2];
        if (Util.isTrue(15, 100)) {
            ItemMap it = new ItemMap(this.zone, itemId, 17, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            Service.gI().dropItemMap(this.zone, it);
        }
        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
    }

    @Override
    protected void resetBase() {
        super.resetBase();
        this.callApk13 = false;
    }

    @Override
    public void active() {
        this.attack();
    }

    @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (!this.callApk13 && damage >= this.nPoint.hp) {
            if (this.parentBoss != null) {
                ((AndroidSaga_S14_NV) this.parentBoss).callApk13();
            }
            return 0;
        }
        return super.injured(plAtt, damage, piercing, isMobAttack);
    }

    public void recoverHP() {
        PlayerService.gI().hoiPhuc(this, this.nPoint.hpMax, 0);
    }
}

/**
 * Vui lòng không sao chép mã nguồn này dưới mọi hình thức. Hãy tôn trọng tác
 * giả của mã nguồn này. Xin cảm ơn! - GirlBeo
 */
