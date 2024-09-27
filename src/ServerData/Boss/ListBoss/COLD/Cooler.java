package ServerData.Boss.ListBoss.COLD;

import ServerData.Boss.Boss;
import ServerData.Boss.BossID;
import ServerData.Boss.BossesData;
import ServerData.Models.Player.Player;
import ServerData.Services.Service;
import ServerData.Utils.Util;
import java.util.Random;


public class Cooler extends Boss {

    public Cooler() throws Exception {
        super(BossID.COOLER, BossesData.COOLER_1, BossesData.COOLER_2);
    }
    @Override
    
    public void reward(Player plKill) {
        int[] NRs = new int[]{16,17,18};
        int[] itemDoTL = new int[]{555,556,557,558,559,560,561,562,563,564,565};
        int randomDoTL = new Random().nextInt(itemDoTL.length);
        int randomNR = new Random().nextInt(NRs.length);
            if (Util.isTrue(30, 100)) {
                Service.gI().dropItemMap(this.zone, Util.ratiDTL(zone, itemDoTL[randomDoTL], 1, this.location.x, this.location.y, plKill.id));
            } else {
                Service.gI().dropItemMap(this.zone, Util.thoivang(zone, NRs[randomNR], 1, this.location.x, this.location.y, plKill.id));
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






















