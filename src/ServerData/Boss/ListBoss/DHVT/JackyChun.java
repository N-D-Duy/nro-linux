package ServerData.Boss.ListBoss.DHVT;

import ServerData.Boss.BossData;
import ServerData.Boss.BossID;
import ServerData.Boss.BossesData;
import ServerData.Models.Player.Player;

/**
 * @author TTS  
 */
public class JackyChun extends BossDHVT {

    public JackyChun(Player player) throws Exception {
        super(BossID.JACKY_CHUN, BossesData.JACKY_CHUN);
        this.playerAtt = player;
    }
}