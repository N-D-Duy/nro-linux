package ServerData.Models.NPC;

import Server.Data.Consts.ConstEvent;
import ServerData.Models.Player.Player;
import ServerData.Services.Service;
import ServerData.Utils.Util;
import java.util.ArrayList;
import java.util.List;
/**
 * 
 * @author by Ts
 * 
 **/
public class NauBanh implements Runnable{
    public static int SoLuongBanhDangNau; public static float Nuoc; public static int Cui; public static int Count;
    public static long ThoiGianCho; public static long ThoiGianNau; public static long ThoiGianChoLayBanh;
    public boolean ChoXong = false; public boolean NauXong = false; public List<Player> ListPlNauBanh = new ArrayList<>(); 
    public boolean baotri = false; private static NauBanh instance;
    public static NauBanh gI() {if (instance == null) {instance = new NauBanh();}return instance;}
    public void addListPlNauBanh(Player pl){if(!ListPlNauBanh.equals(pl)){ListPlNauBanh.add(pl);}}
    public void removeListPlNauBanh(Player pl){ListPlNauBanh.remove(pl);}
    @Override
    public void run() { while (true) { try {
                if (Cui < 500 && ((ThoiGianCho - System.currentTimeMillis())/1000) <= 15 || Nuoc < 500 && ((ThoiGianCho - System.currentTimeMillis())/1000) <= 15) { int time = 15; while (time > 0) { time--; Service.gI().sendThongBaoAllPlayer("Thời gian chờ\nnấu bánh sắp hết\n|7|Hiện tại còn thiếu\nx" + (500-Cui) + " Que Củi\nvà " + Util.format(100-(Nuoc/500*100)) + "% Nước\n|-1|Hãy mau chóng hoàn thành!"); Thread.sleep(1000);}
                    ThoiGianCho = System.currentTimeMillis() + (1000 * 60 * 15);
                } else if(ChoXong == false && Cui > 499 && Nuoc > 499){ ChoXong = true; Service.gI().sendThongBaoAllPlayer("Nồi nấu bánh đã đủ nguyên liệu, đang bắt đầu nấu bánh!");
                    ThoiGianNau = System.currentTimeMillis() + (1000 * 60 * 30);
                } else if(NauXong == false && ChoXong == true && ((ThoiGianNau - System.currentTimeMillis())/1000) <= 0){ NauXong = true; Service.gI().sendThongBaoAllPlayer("Nồi nấu bánh đã nấu xong, tiếp tục lượt mới sau 5 phút\nMời bạn đến nhận bánh!");
                    ThoiGianChoLayBanh = System.currentTimeMillis() + (1000 * 60 * 5);
                } else if(NauXong == true && ((ThoiGianChoLayBanh - System.currentTimeMillis())/1000) <= 0) { ConstEvent.TONGSOBANH += SoLuongBanhDangNau; SoLuongBanhDangNau = 0; ListPlNauBanh.clear(); NauXong = false; ChoXong = false; Nuoc = 0; Cui = 0; Count++; ThoiGianChoLayBanh = 0; ThoiGianNau = 0;
                    if (ConstEvent.TONGSOBANH >= 500) {
                        ConstEvent.X2 = System.currentTimeMillis() + (1000 * 60 * 60);
                    }
                    ThoiGianCho = System.currentTimeMillis() + (1000 * 60 * 15); // return time
                } 
                Thread.sleep(1000);
            } catch (InterruptedException e) { System.out.print("Return Thread Fail");}
        }
    } 
}