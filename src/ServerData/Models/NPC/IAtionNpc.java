package ServerData.Models.NPC;

import ServerData.Models.Player.Player;

public interface IAtionNpc {
    
    void openBaseMenu(Player player);

    void confirmMenu(Player player, int select);


}
