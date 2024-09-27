/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerData.Utils;
import ServerData.Boss.Boss;
import ServerData.Boss.BossManager;
import ServerData.Models.Player.Player;
import ServerData.Server.Client;
import java.lang.instrument.Instrumentation;
/**
 *
 * @author Administrator
 */
public class GetSizeObject {
     private static Instrumentation instrumentation;

    public static void premain(String args, Instrumentation inst) {
        instrumentation = inst;
        System.out.println("Init getSize");
    }

    public static long getSizeObject(Object o) {
        if(instrumentation == null) return 0;
        return instrumentation.getObjectSize(o);
    }
    public static long sizeListPlayer(){
        long sum = 0;
        for(Player pl : Client.gI().getPlayers()){
            if(pl != null) sum += getSizeObject(pl);
        }
        return sum;
    }
    public static long sizeListBoss(){
        long sum = 0;
        for(Boss b : BossManager.gI().bosses){
            if(b != null) sum += getSizeObject(b);
        }
        return sum;
    }
}
