package ServerData.Boss.ListBoss.NHIEMVU;

import ServerData.Boss.Boss;
import ServerData.Boss.BossID;
import ServerData.Boss.BossesData;
import ServerData.Models.Map.ItemMap;
import ServerData.Models.Player.Player;
import ServerData.Services.Service;
import ServerData.Services.TaskService;
import ServerData.Utils.Util;
import java.util.Random;


public class FideSaga_Fide_NV extends Boss {

    public FideSaga_Fide_NV() throws Exception {
        super(BossID.FIDE, BossesData.FIDE_DAI_CA_1, BossesData.FIDE_DAI_CA_2, BossesData.FIDE_DAI_CA_3);
    }
    @Override
    public void reward(Player plKill) {
    if(Util.isTrue(100,100)){
        ItemMap it = new ItemMap(this.zone, Util.nextInt(17, 19), 1, this.location.x, this.location.y, plKill.id);
        Service.gI().dropItemMap(this.zone, it);
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

}