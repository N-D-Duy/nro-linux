package ServerData.Models.Map.ListMap;

import ServerData.Boss.BossData;
import ServerData.Boss.ListBoss.DHVT.BossDHVT;
import ServerData.Models.Player.Player;
import ServerData.Utils.Util;

/**
 *
 * @author Béo Mập :3
 */
public class ClonePlayer extends BossDHVT{
    
    public  ClonePlayer(Player player, BossData data, int id) throws Exception {
        super(Util.randomBossId(), data,5000);
        this.playerAtt = player;
        this.idPlayer = id;
    }
}
