package ServerData.Boss.ListBoss.DHVT;

import ServerData.Boss.BossData;
import ServerData.Boss.BossID;
import ServerData.Boss.BossesData;
import ServerData.Models.Player.Player;

/**
 * @author TTS  
 */
public class SoiHecQuyn extends BossDHVT {
    public SoiHecQuyn(Player player) throws Exception {
        super(BossID.SOI_HEC_QUYN, BossesData.SOI_HEC_QUYN);
        this.playerAtt = player;
    }
}
