package ServerData.Boss.ListBoss.DHVT;

import ServerData.Boss.BossData;
import ServerData.Boss.BossID;
import ServerData.Boss.BossesData;
import ServerData.Models.Player.Player;

/**
 * @author TTS  
 */
public class ODo extends BossDHVT {

    public ODo(Player player) throws Exception {
        super(BossID.O_DO, BossesData.O_DO);
        this.playerAtt = player;
    }
}
