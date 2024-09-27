package ServerData.Boss.ListBoss;

import ServerData.Boss.*;
import ServerData.Models.Item.Item;
import ServerData.Models.Map.ItemMap;
import ServerData.Models.Map.Zone;
import ServerData.Models.Player.Player;
import ServerData.Services.Service;
import ServerData.Utils.Util;
import java.util.Random;

public class NhanBan extends Boss {

    public NhanBan(int bossID, BossData bossData, Zone zone) throws Exception {
        super(bossID, bossData);
        this.zone = zone;
        
    }

    @Override
    public void reward(Player plKill) {
        //vật phẩm rơi khi diệt boss nhân bản
         int[] itemDos = new int[]{1099,1100,1101,1102,1103,381,382,383,384,385};
        int randomDo = new Random().nextInt(itemDos.length);
        ItemMap it = new ItemMap(this.zone, 861,Util.nextInt(100,1000), this.location.x, this.zone.map.yPhysicInTop(this.location.x+5,
                this.location.y - 24), plKill.id) ;
        Service.gI().dropItemMap(plKill.zone, it);
        ItemMap it2 = new ItemMap(this.zone, itemDos[randomDo],1, this.location.x, this.zone.map.yPhysicInTop(this.location.x+10,
                this.location.y - 24), plKill.id) ;
        Service.gI().dropItemMap(plKill.zone, it2);
        ItemMap it3 = new ItemMap(this.zone, 1318,Util.nextInt(1,5), this.location.x, this.zone.map.yPhysicInTop(this.location.x+15,
                this.location.y - 24), plKill.id) ;
        Service.gI().dropItemMap(plKill.zone, it3);
    }
    
    @Override
    public void active() {
        super.active();
    }

    @Override
    public void joinMap() {
        super.joinMap();
    }

    @Override
    public void leaveMap() {
        super.leaveMap();
        BossManager.gI().removeBoss(this);
        this.dispose();
    }
}
