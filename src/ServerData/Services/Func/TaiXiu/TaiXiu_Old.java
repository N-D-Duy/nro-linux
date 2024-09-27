//package ServerData.Services.Func.TaiXiu;
//
//import ServerData.Models.Player.Player;
//import ServerData.Server.Client;
//import ServerData.Services.ChatGlobalService;
//import ServerData.Services.InventoryServiceNew;
//import ServerData.Services.Service;
//import ServerData.Utils.Util;
//import java.util.ArrayList;
//import java.util.Comparator;
//import java.util.List;
//
///**
// *
// * @author DEV Ăn Trộm
// */
//public class TaiXiu_Old  implements Runnable{
//    public int goldTai;
//    public int goldXiu;
//    public boolean ketquaTai = false;
//    public boolean ketquaXiu = false;
//    public boolean ketquaTamhoa = false;
//    
//    public boolean baotri = false;
//    public long lastTimeEnd;
//    public List<Player> PlayersTai = new ArrayList<>();
//    public List<Player> PlayersXiu = new ArrayList<>();
//    private static TaiXiu_Old instance;
//    public int x,y,z;
//    
// public static TaiXiu_Old gI() {
//        if (instance == null) {
//            instance = new TaiXiu_Old();
//        }
//        return instance;
//    }
//    
//    public void addPlayerXiu(Player pl){
//        if(!PlayersXiu.equals(pl)){
//            PlayersXiu.add(pl);
//        }
//    }
//    
//    public void addPlayerTai(Player pl){
//        if(!PlayersTai.equals(pl)){
//            PlayersTai.add(pl);
//        }
//    }
//    
//    public void removePlayerXiu(Player pl){
//        if(PlayersXiu.equals(pl)){
//            PlayersXiu.remove(pl);
//        }
//    }
//    
//    public void removePlayerTai(Player pl){
//        if(PlayersTai.equals(pl)){
//            PlayersTai.remove(pl);
//        }
//    }
//    
//    @Override
//    public void run() {
//        while (true) {
//            try{
//                if(((TaiXiu_Old.gI().lastTimeEnd - System.currentTimeMillis())/1000) <= 0){
//                    int x,y,z;
//                    // Thực hiện các hành động sau khi chờ 10 giây
//                    if (TaiXiu_Old.gI().goldTai >= TaiXiu_Old.gI().goldXiu){
//                        if (Util.isTrue(60, 100)){
//                            x = Util.nextInt(1, 2);
//                            y = Util.nextInt(1, 3);
//                            z = Util.nextInt(1, 3);
//                            TaiXiu_Old.gI().x =x ;
//                            TaiXiu_Old.gI().y =y ;
//                            TaiXiu_Old.gI().z =z ;
//                        } else {
//                            x = Util.nextInt(3, 6);
//                            y = Util.nextInt(4, 6);
//                            z = Util.nextInt(4, 6);
//                            TaiXiu_Old.gI().x =x ;
//                            TaiXiu_Old.gI().y =y ;
//                            TaiXiu_Old.gI().z =z ;
//                        }
//                    } else {
//                        if (Util.isTrue(60, 100)){
//                            x = Util.nextInt(3, 6);
//                            y = Util.nextInt(4, 6);
//                            z = Util.nextInt(4, 6);
//                            TaiXiu_Old.gI().x =x ;
//                            TaiXiu_Old.gI().y =y ;
//                            TaiXiu_Old.gI().z =z ;
//                        } else {
//                            x = Util.nextInt(1, 2);
//                            y = Util.nextInt(1, 3);
//                            z = Util.nextInt(1, 3);
//                            TaiXiu_Old.gI().x =x ;
//                            TaiXiu_Old.gI().y =y ;
//                            TaiXiu_Old.gI().z =z ;
//                        }
//                    }
//                    int tong = (x + y + z);
//                    
//                    if (tong > 3 && tong <11) {
//                        ketquaTamhoa = false;
//                        ketquaXiu = true;
//                        ketquaTai = false;
//                    }
//                    if (tong > 10){
//                        ketquaTamhoa = false;
//                        ketquaXiu = false;
//                        ketquaTai = true;
//                    }
//                    if (x==y && y==z && z==x){
//                        ketquaTamhoa = true;
//                        ketquaXiu = false;
//                        ketquaTai = false;
//                    }
//                   
//                    if (ketquaTai == true){
//                        if( !TaiXiu_Old.gI().PlayersTai.isEmpty()){
//                                for (int i=0; i< PlayersTai.size();i++){
//                                    Player pl = this.PlayersTai.get(i);
//                                    if(pl != null && Client.gI().getPlayer(pl.name) != null){
//                                        int goldC = pl.goldTai + pl.goldTai * 80 / 100;
//                                        Service.getInstance().sendThongBao(pl, "Số hệ thống quay ra\n" + x + " : "
//                                                        + y + " : " + z + "\n|5|Tổng là : " + tong + "\n(TÀI)\n\n|1|Bạn đã chiến thắng!!");
//                                        Service.getInstance().sendThongBao(pl, "Chúc mừng bạn đã dành chiến thắng và nhận được " + Util.format(goldC) +" Hồng ngọc");
//                                        ChatGlobalService.gI().chat(pl, pl.name + " đã chiến thắng và nhận được " + Util.format(goldC) +" Hồng ngọc");
//                                        pl.inventory.ruby += goldC;
//                                        Service.getInstance().sendMoney(pl);
//                                        InventoryServiceNew.gI().sendItemBags(pl);
//                                    }
//                                }
//                        }for (int i=0; i< PlayersXiu.size();i++){
//                                    Player pl = this.PlayersXiu.get(i);
//                                    if(pl != null && Client.gI().getPlayer(pl.name) != null){
//                                        Service.getInstance().sendThongBao(pl, "Số hệ thống quay ra\n" + x + " : "
//                                                        + y + " : " + z + "\n|5|Tổng là : " + tong + "\n(TÀI)\n\n|7|Trắng tay gòi, chơi lại đi!!!");
//                                    }
//                                }
//                    } else if (ketquaXiu == true){
//                        if (!TaiXiu_Old.gI().PlayersXiu.isEmpty()){
//                                for (int i=0; i< PlayersXiu.size();i++){
//                                    Player pl = this.PlayersXiu.get(i);
//                                if(pl != null && Client.gI().getPlayer(pl.name) != null){
//                                        int goldC = pl.goldXiu + pl.goldXiu * 80 / 100;
//                                        Service.getInstance().sendThongBao(pl,"Số hệ thống quay ra\n" + x + " : "
//                                                        + y + " : " + z + "\n|5|Tổng là : " + tong + "\n(XỈU)\n\n|1|Bạn đã chiến thắng!!");
//                                        Service.getInstance().sendThongBao(pl, "Chúc mừng bạn đã dành chiến thắng và nhận được " + Util.format(goldC) +" Hồng ngọc");
//                                        ChatGlobalService.gI().chat(pl, pl.name + " đã chiến thắng và nhận được " + Util.format(goldC) +" Hồng ngọc");
//                                                pl.inventory.ruby += goldC;
//                                                Service.getInstance().sendMoney(pl);
//                                        InventoryServiceNew.gI().sendItemBags(pl);
//                                    }
//                                }
//                        }for (int i=0; i< PlayersTai.size();i++){
//                                    Player pl = this.PlayersTai.get(i);
//                                    if(pl != null && Client.gI().getPlayer(pl.name) != null){
//                                        Service.getInstance().sendThongBao(pl, "Số hệ thống quay ra\n" + x + " : "
//                                                        + y + " : " + z + "\n|5|Tổng là : " + tong + "\n(XỈU)\n\n|7|Trắng tay gòi, chơi lại đi!!!");
//                                    }
//                                }
//                    } else {
//                        for (int i=0; i< PlayersTai.size();i++){
//                                    Player pl = this.PlayersTai.get(i);
//                                    if(pl != null && Client.gI().getPlayer(pl.name) != null){
//                                        Service.getInstance().sendThongBao(pl, "Số hệ thống quay ra\n" + x + " : "
//                                                        + y + " : " + z + "\n|5|Tổng là : " + tong + "\n(TAM HOA)\n\n|7|Hahaha Nhà cái lụm hết nha!!!");
//                                    }
//                                }
//                        for (int i=0; i< PlayersXiu.size();i++){
//                                    Player pl = this.PlayersXiu.get(i);
//                                    if(pl != null && Client.gI().getPlayer(pl.name) != null){
//                                        Service.getInstance().sendThongBao(pl, "Số hệ thống quay ra\n" + x + " : "
//                                                        + y + " : " + z + "\n|5|Tổng là : " + tong + "\n(TAM HOA)\n\n|7|Hahaha Nhà cái lụm hết nha!!!");
//                                    }
//                                }
//                    }
//                    for(int i = 0 ; i < TaiXiu_Old.gI().PlayersTai.size();i++){
//                        Player pl = TaiXiu_Old.gI().PlayersTai.get(i);
//                        if(pl != null){
//                            pl.goldTai = 0;
//                        }
//                    }
//                    for(int i = 0 ; i < TaiXiu_Old.gI().PlayersXiu.size();i++){
//                        Player pl = TaiXiu_Old.gI().PlayersXiu.get(i);
//                        if(pl != null){
//                            pl.goldXiu = 0;
//                        }
//                    }
//                    ketquaXiu = false;
//                    ketquaTai = false;
//                    ketquaTamhoa = false;
//                    TaiXiu_Old.gI().goldTai = 0;
//                    TaiXiu_Old.gI().goldXiu = 0;
//                    TaiXiu_Old.gI().PlayersTai.clear();
//                    TaiXiu_Old.gI().PlayersXiu.clear();
//                    TaiXiu_Old.gI().lastTimeEnd = System.currentTimeMillis() + 100000;
////                    GirlkunDB.executeUpdate("UPDATE player SET Tai_xiu = JSON_REPLACE(JSON_REPLACE(Tai_xiu, '$[0]', 0), '$[1]', 0)");
//                }
//                Thread.sleep(500);
//            } catch (Exception e) {
//            }
//        }
//    }
//}