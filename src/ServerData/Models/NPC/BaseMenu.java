package ServerData.Models.NPC;

import ServerData.Models.Player.Player;
import com.girlkun.network.io.Message;


public class BaseMenu {

    public int npcId;

    public String npcSay;

    public String[] menuSelect;

    public void openMenu(Player player) {
        Message msg;
        try {
            msg = new Message(32);
            msg.writer().writeShort(npcId);
            msg.writer().writeUTF(npcSay);
            msg.writer().writeByte(menuSelect.length);
            for (String menu : menuSelect) {
                msg.writer().writeUTF(menu);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }
}
