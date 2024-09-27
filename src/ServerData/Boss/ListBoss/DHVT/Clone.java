package ServerData.Boss.ListBoss.DHVT;

import ServerData.Boss.BossData;
import ServerData.Models.Player.Player;

/**
 * @author TTS
 */
public class Clone extends BossDHVT {
    public Clone(Player player,int id,BossData bossData) throws Exception {
        super(id, bossData);
        this.playerAtt = player;
    }
    
    
}
