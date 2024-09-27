package ServerData.Services;

import ServerData.Models.Player.NPoint;
import ServerData.Models.Player.Pet;
import ServerData.Models.Player.Player;
import ServerData.Server.Client;
import ServerData.Utils.Util;
import java.awt.Point;


public class OpenPowerService {

    public static final int COST_SPEED_OPEN_LIMIT_POWER = 500000000;

    private static OpenPowerService i;

    private OpenPowerService() {

    }

    public static OpenPowerService gI() {
        if (i == null) {
            i = new OpenPowerService();
        }
        return i;
    }
  public boolean chuyenSinh(Player player) {
                            if(player.getSession().player.nPoint.power < 300000000000L ){
                                Service.gI().sendThongBaoOK(player, "Cần 300 Tỉ Sức Mạnh Để Chuyển Sinh 1!");}
                                else if(player.getSession().player.nPoint.power < 900000000000L &&  player.getSession().player.nPoint.dameg == 36000 ){
                                Service.gI().sendThongBaoOK(player, "Cần 900 Tỉ Sức Mạnh Để Chuyển Sinh 2!");} 
                                else if(player.getSession().player.nPoint.power < 1500000000000L &&  player.getSession().player.nPoint.dameg == 37000 ){
                                Service.gI().sendThongBaoOK(player, "Cần 1.500 Tỉ Sức Mạnh Để Chuyển Sinh 3!");}
                                else if(player.getSession().player.nPoint.power < 4000000000000L &&  player.getSession().player.nPoint.dameg == 38000 ){
                                Service.gI().sendThongBaoOK(player, "Cần 4.000 Tỉ Sức Mạnh Để Chuyển Sinh 4!");}
                                else if(player.getSession().player.nPoint.power < 5000000000000L &&  player.getSession().player.nPoint.dameg == 39000 ){
                                Service.gI().sendThongBaoOK(player, "Cần 5.000 Tỉ Sức Mạnh Để Chuyển Sinh 5!");}
                                else if(player.getSession().player.nPoint.power < 6000000000000L &&  player.getSession().player.nPoint.dameg == 40000 ){
                                Service.gI().sendThongBaoOK(player, "Cần 6.000 Tỉ Sức Mạnh Để Chuyển Sinh 6!");} 
                                else if(player.getSession().player.nPoint.power < 7000000000000L &&  player.getSession().player.nPoint.dameg == 41000 ){
                                Service.gI().sendThongBaoOK(player, "Cần 7.000 Tỉ Sức Mạnh Để Chuyển Sinh 7!");}
                                else if(player.getSession().player.nPoint.power < 8000000000000L &&  player.getSession().player.nPoint.dameg == 42000 ){
                                Service.gI().sendThongBaoOK(player, "Cần 8.000 Tỉ Sức Mạnh Để Chuyển Sinh 8!");} 
                                else if(player.getSession().player.nPoint.power < 9000000000000L &&  player.getSession().player.nPoint.dameg == 43000 ){
                                Service.gI().sendThongBaoOK(player, "Cần 9.000 Tỉ Sức Mạnh Để Chuyển Sinh 9!");}
                                else if(player.getSession().player.nPoint.power < 10000000000000L &&  player.getSession().player.nPoint.dameg == 44000 ){
                                Service.gI().sendThongBaoOK(player, "Cần 10.000 Tỉ Sức Mạnh Để Chuyển Sinh 10!");}
                                
                                else if(player.getSession().player.nPoint.hpg < 1000000){
                                Service.gI().sendThongBaoOK(player, "Còn Thiếu "+(1000000-player.nPoint.hpg)+" HP Nữa");
                            }   else if(player.getSession().player.nPoint.mpg < 1000000){
                                Service.gI().sendThongBaoOK(player, "Còn Thiếu "+(1000000-player.nPoint.mpg)+" KI Nữa");
                            }   else if(player.getSession().player.nPoint.dameg < 35000){
                                Service.gI().sendThongBaoOK(player, "Còn Thiếu "+(35000-player.nPoint.dameg)+" SD Nữa");
                            }   
                            else if(player.getSession().player.inventory.ruby < 50000){
                                Service.gI().sendThongBaoOK(player, "Còn Thiếu "+(50000-player.inventory.ruby)+" Hồng Ngọc");
                            }
                 
                            else if (player.getSession().player.nPoint.power >= 300000000000L  && player.getSession().player.nPoint.dameg == 35000 && player.getSession().player.inventory.ruby >= 50000) //Chuyển sinh 1
                            {
                                    
                                int num = Util.nextInt(0,1);
                                
                                if (player.inventory.ruby >= 50000
                                            ) {
                                        if (player.capChuyenSinh < 10) {
                                
                                    player.nPoint.power = 2000;
                                    player.inventory.ruby -= 50000;
                                    player.nPoint.dameg += 1000;
                                    player.nPoint.hpg += 25000;
                                    player.nPoint.mpg += 25000;
                                    Service.gI().sendMoney(player);
                                    player.capChuyenSinh++;
                                   
                                    Service.gI().sendThongBaoOK(player,
                                        "Cấp chuyển sinh của bạn hiện tại là " + player.capChuyenSinh);

                                    Client.gI().getPlayers().remove(player);
                                    Client.gI().kickSession(player.getSession());
                                    Service.gI().sendThongBaoOK(player, "Bạn đã chuyển sinh lần 1 thành công");
                                                                
                              
                                } else {
                                    Service.gI().sendThongBaoOK(player, "Bạn đã chuyển sinh ở cấp tối đa!");
                                        }

                                } else {
                                    Service.gI().sendThongBaoOK(player,
                                                 "Cần đủ 50k hồng ngọc");
                                    }
                                }
                            
                            else if (player.getSession().player.nPoint.power >= 900000000000L && player.getSession().player.nPoint.dameg == 36000 && player.getSession().player.inventory.ruby >= 50000) //Chuyển sinh 2
                            {
                                    
                                int num = Util.nextInt(0,1);
                                
                                if (player.inventory.ruby >= 50000
                                            ) {
                                        if (player.capChuyenSinh < 10) {
                              
                                    player.nPoint.power = 2000;
                                    player.inventory.ruby -= 50000;
                                    player.nPoint.dameg += 1000;
                                    player.nPoint.hpg += 25000;
                                    player.nPoint.mpg += 25000;
                                    Service.gI().sendMoney(player);
                                    player.capChuyenSinh++;
                                   
                                    Service.gI().sendThongBaoOK(player,
                                        "Cấp chuyển sinh của bạn hiện tại là " + player.capChuyenSinh);

                                    Client.gI().getPlayers().remove(player);
                                    Client.gI().kickSession(player.getSession());
                                    Service.gI().sendThongBaoOK(player, "Bạn đã chuyển sinh lần 2 thành công");
                                                                
                              
                                } else {
                                    Service.gI().sendThongBaoOK(player, "Bạn đã chuyển sinh ở cấp tối đa!");
                                        }

                                } else {
                                    Service.gI().sendThongBaoOK(player,
                                                "Cần đủ 50k hồng ngọc");
                                    }
                                }
                            
                            
                            
                            else if (player.getSession().player.nPoint.power >= 1500000000000L && player.getSession().player.nPoint.dameg == 37000 && player.getSession().player.inventory.ruby >= 50000) //Chuyển sinh 1
                            {
                                int num = Util.nextInt(0,1);
                                
                                if (player.inventory.ruby >= 50000
                                            ) {
                                        if (player.capChuyenSinh < 10) {
                                  
                                    player.nPoint.power = 2000;
                                    player.inventory.ruby -= 50000;
                                    player.nPoint.dameg += 1000;
                                    player.nPoint.hpg += 25000;
                                    player.nPoint.mpg += 25000;
                                    Service.gI().sendMoney(player);
                                    player.capChuyenSinh++;
                                    
                                    Service.gI().sendThongBaoOK(player,
                                        "Cấp chuyển sinh của bạn hiện tại là " + player.capChuyenSinh);

                                    Client.gI().getPlayers().remove(player);
                                    Client.gI().kickSession(player.getSession());
                                    Service.gI().sendThongBaoOK(player, "Bạn đã chuyển sinh lần 3 thành công");
                              
                                } else {
                                    Service.gI().sendThongBaoOK(player, "Bạn đã chuyển sinh ở cấp tối đa!");
                                        }

                                } else {
                                    Service.gI().sendThongBaoOK(player,
                                               "Cần đủ 20k hồng ngọc");
                                    }
                                }
                            
                            
                            else if (player.getSession().player.nPoint.power >= 4000000000000L && player.getSession().player.nPoint.dameg == 38000 && player.getSession().player.inventory.ruby >= 50000) //Chuyển sinh 1
                            {
                                   
                                    
                                int num = Util.nextInt(0,1);
                                
                                if (player.inventory.ruby >= 50000
                                            ) {
                                        if (player.capChuyenSinh < 10) {
                                 
                                    player.nPoint.power = 2000;
                                    player.inventory.ruby -= 50000;
                                    player.nPoint.dameg += 1000;
                                    player.nPoint.hpg += 25000;
                                    player.nPoint.mpg += 25000;
                                    Service.gI().sendMoney(player);
                                    player.capChuyenSinh++;
                                  
                                    Service.gI().sendThongBaoOK(player,
                                        "Cấp chuyển sinh của bạn hiện tại là " + player.capChuyenSinh);

                                    Client.gI().getPlayers().remove(player);
                                    Client.gI().kickSession(player.getSession());
                                    Service.gI().sendThongBaoOK(player, "Bạn đã chuyển sinh lần 4 thành công");
                                                                
                            
                                } else {
                                    Service.gI().sendThongBaoOK(player, "Bạn đã chuyển sinh ở cấp tối đa!");
                                        }

                                } else {
                                    Service.gI().sendThongBaoOK(player,
                                                "Cần đủ 50k hồng ngọc");
                                    }
                                }
                            
                            
                            else if (player.getSession().player.nPoint.power >= 5000000000000L && player.getSession().player.nPoint.dameg == 39000 && player.getSession().player.inventory.ruby >= 50000) //Chuyển sinh 1
                            {
                                  
                                int num = Util.nextInt(0,1);
                                
                                if (player.inventory.ruby >= 50000
                                            ) {
                                        if (player.capChuyenSinh < 10) {
                                 
                                    player.nPoint.power = 2000;
                                    player.inventory.ruby -= 50000;
                                    player.nPoint.dameg += 1000;
                                    player.nPoint.hpg += 25000;
                                    player.nPoint.mpg += 25000;
                                    Service.gI().sendMoney(player);
                                    player.capChuyenSinh++;
                                   
                                    Service.gI().sendThongBaoOK(player,
                                        "Cấp chuyển sinh của bạn hiện tại là " + player.capChuyenSinh);

                                    Client.gI().getPlayers().remove(player);
                                    Client.gI().kickSession(player.getSession());
                                    Service.gI().sendThongBaoOK(player, "Bạn đã chuyển sinh lần 5 thành công");
                                                                
                            
                                } else {
                                    Service.gI().sendThongBaoOK(player, "Bạn đã chuyển sinh ở cấp tối đa!");
                                        }

                                } else {
                                    Service.gI().sendThongBaoOK(player,
                                             "Cần đủ 20k hồng ngọc");
                                    }
                                }
                            
                            
                              else if (player.getSession().player.nPoint.power >= 6000000000000L && player.getSession().player.nPoint.dameg == 40000 && player.getSession().player.inventory.ruby >= 50000) //Chuyển sinh 1
                            {
                                   
                                int num = Util.nextInt(0,1);
                                
                                if (player.inventory.ruby >= 50000
                                           ) {
                                        if (player.capChuyenSinh < 10) {
                                 
                                    player.nPoint.power = 2000;
                                    player.inventory.ruby -= 50000;
                                    player.nPoint.dameg += 1000;
                                    player.nPoint.hpg += 25000;
                                    player.nPoint.mpg += 25000;
                                    Service.gI().sendMoney(player);
                                    player.capChuyenSinh++;
                                  
                                    Service.gI().sendThongBaoOK(player,
                                        "Cấp chuyển sinh của bạn hiện tại là " + player.capChuyenSinh);

                                    Client.gI().getPlayers().remove(player);
                                    Client.gI().kickSession(player.getSession());
                                    Service.gI().sendThongBaoOK(player, "Bạn đã chuyển sinh lần 6 thành công");
                                                                
                               
                                } else {
                                    Service.gI().sendThongBaoOK(player, "Bạn đã chuyển sinh ở cấp tối đa!");
                                        }

                                } else {
                                    Service.gI().sendThongBaoOK(player,
                                             "Cần đủ 20k hồng ngọc");
                                    }
                                }
                              
                              
                              else if (player.getSession().player.nPoint.power >= 7000000000000L && player.getSession().player.nPoint.dameg == 41000 && player.getSession().player.inventory.ruby >= 50000) //Chuyển sinh 1
                            {
                                  
                                int num = Util.nextInt(0,1);
                                
                                if (player.inventory.ruby >= 50000
                                           ) {
                                        if (player.capChuyenSinh < 10) {
                                  
                                    player.nPoint.power = 2000;
                                    player.inventory.ruby -= 50000;
                                    player.nPoint.dameg += 1000;
                                    player.nPoint.hpg += 25000;
                                    player.nPoint.mpg += 25000;
                                    Service.gI().sendMoney(player);
                                    player.capChuyenSinh++;
                                
                                    Service.gI().sendThongBaoOK(player,
                                        "Cấp chuyển sinh của bạn hiện tại là " + player.capChuyenSinh);

                                    Client.gI().getPlayers().remove(player);
                                    Client.gI().kickSession(player.getSession());
                                    Service.gI().sendThongBaoOK(player, "Bạn đã chuyển sinh lần 7 thành công");
                                                                
                               
                                } else {
                                    Service.gI().sendThongBaoOK(player, "Bạn đã chuyển sinh ở cấp tối đa!");
                                        }

                                } else {
                                    Service.gI().sendThongBaoOK(player,
                                               "Cần đủ 50k hồng ngọc");
                                    }
                                }
                              
                              else if (player.getSession().player.nPoint.power >= 8000000000000L && player.getSession().player.nPoint.dameg == 42000 && player.getSession().player.inventory.ruby >= 50000) //Chuyển sinh 1
                            {
                                  
                                    
                                int num = Util.nextInt(0,1);
                                
                                if (player.inventory.ruby >= 50000
                                            ) {
                                        if (player.capChuyenSinh < 10) {
                                 
                                    player.nPoint.power = 2000;
                                    player.inventory.ruby -= 50000;
                                    player.nPoint.dameg += 1000;
                                    player.nPoint.hpg += 25000;
                                    player.nPoint.mpg += 25000;
                                    Service.gI().sendMoney(player);
                                    player.capChuyenSinh++;
                                    
                                    Service.gI().sendThongBaoOK(player,
                                        "Cấp chuyển sinh của bạn hiện tại là " + player.capChuyenSinh);

                                    Client.gI().getPlayers().remove(player);
                                    Client.gI().kickSession(player.getSession());
                                    Service.gI().sendThongBaoOK(player, "Bạn đã chuyển sinh lần 8 thành công");
                                                                
                              
                                } else {
                                    Service.gI().sendThongBaoOK(player, "Bạn đã chuyển sinh ở cấp tối đa!");
                                        }

                                } else {
                                    Service.gI().sendThongBaoOK(player,
                                                "Cần đủ 50k hồng ngọc");
                                    }
                                }
                              
                              else if (player.getSession().player.nPoint.power >= 9000000000000L && player.getSession().player.nPoint.dameg == 43000 && player.getSession().player.inventory.ruby >= 50000) //Chuyển sinh 1
                            {
                                  
                                int num = Util.nextInt(0,1);
                                
                                if (player.inventory.ruby >= 50000
                                            ) {
                                        if (player.capChuyenSinh < 10) {
                                   
                                    player.nPoint.power = 2000;
                                    player.inventory.ruby -= 50000;
                                    player.nPoint.dameg += 1000;
                                    player.nPoint.hpg += 25000;
                                    player.nPoint.mpg += 25000;
                                    Service.gI().sendMoney(player);
                                    player.capChuyenSinh++;
                                   
                                    Service.gI().sendThongBaoOK(player,
                                        "Cấp chuyển sinh của bạn hiện tại là " + player.capChuyenSinh);

                                    Client.gI().getPlayers().remove(player);
                                    Client.gI().kickSession(player.getSession());
                                    Service.gI().sendThongBaoOK(player, "Bạn đã chuyển sinh lần 9 thành công");
                                                                
                            
                                } else {
                                    Service.gI().sendThongBaoOK(player, "Bạn đã chuyển sinh ở cấp tối đa!");
                                        }

                                } else {
                                    Service.gI().sendThongBaoOK(player,
                                                "Cần đủ 20k hồng ngọc");
                                    }
                                }
                              
                              
                              else if (player.getSession().player.nPoint.power >= 10000000000000L && player.getSession().player.nPoint.dameg == 44000 && player.getSession().player.inventory.ruby >= 50000) //Chuyển sinh 1
                            {
                                    
                                int num = Util.nextInt(0,1);
                                
                                if ( player.inventory.ruby >= 50000
                                           ) {
                                        if (player.capChuyenSinh < 10) {
                                
                                    player.nPoint.power = 2000;
                                    player.inventory.ruby -= 50000;
                                    player.nPoint.dameg += 1000;
                                    player.nPoint.hpg += 25000;
                                    player.nPoint.mpg += 25000;
                                    Service.gI().sendMoney(player);
                                    player.capChuyenSinh++;
                                    
                                    Service.gI().sendThongBaoOK(player,
                                        "Cấp chuyển sinh của bạn hiện tại là " + player.capChuyenSinh);

                                    Client.gI().getPlayers().remove(player);
                                    Client.gI().kickSession(player.getSession());
                                    Service.gI().sendThongBaoOK(player, "Bạn đã chuyển sinh lần 10 thành công");
                                                                
                              
                                } else {
                                    Service.gI().sendThongBaoOK(player, "Bạn đã chuyển sinh ở cấp tối đa!");
                                        }

                                } else {
                                    Service.gI().sendThongBaoOK(player,
                                                 "Cần đủ 50k hồng ngọc");
                                    }
                                }
                              
                              
                                else if(player.capChuyenSinh <= 10){
                                Service.gI().sendThongBaoOK(player, "Bạn đã chuyển sinh ở cấp tối đa!");} 
        return true;
    }
    public boolean openPowerBasic(Player player) {
        byte curLimit = player.nPoint.limitPower;
        if (curLimit < NPoint.MAX_LIMIT) {
            if (!player.itemTime.isOpenPower && player.nPoint.canOpenPower()) {
                player.itemTime.isOpenPower = true;
                player.itemTime.lastTimeOpenPower = System.currentTimeMillis();
                ItemTimeService.gI().sendAllItemTime(player);
                return true;
            } else {
                Service.gI().sendThongBao(player, "Sức mạnh của bạn không đủ để thực hiện");
                return false;
            }
        } else {
            Service.gI().sendThongBao(player, "Sức mạnh của bạn đã đạt tới mức tối đa");
            return false;
        }
    }

    public boolean openPowerSpeed(Player player) {
        if (player.nPoint.limitPower < NPoint.MAX_LIMIT) {
            if (player.nPoint.power >= 17999999999L && player.nPoint.limitPower < 1) {
            player.nPoint.limitPower += 1;
            if (player.nPoint.limitPower > NPoint.MAX_LIMIT) {
                player.nPoint.limitPower = NPoint.MAX_LIMIT;
            }
            if (!player.isPet) {
                Service.getInstance().sendThongBao(player, "Giới hạn sức mạnh của bạn đã được tăng lên 1 bậc");
            } else {
                Service.getInstance().sendThongBao(((Pet) player).master, "Giới hạn sức mạnh của đệ tử đã được tăng lên 1 bậc");
            }
            return true;
            }
            if (player.nPoint.power >= 17999999999L && player.nPoint.limitPower < 2) {
            player.nPoint.limitPower += 1;
            if (player.nPoint.limitPower > NPoint.MAX_LIMIT) {
                player.nPoint.limitPower = NPoint.MAX_LIMIT;
            }
            if (!player.isPet) {
                Service.getInstance().sendThongBao(player, "Giới hạn sức mạnh của bạn đã được tăng lên 1 bậc");
            } else {
                Service.getInstance().sendThongBao(((Pet) player).master, "Giới hạn sức mạnh của đệ tử đã được tăng lên 1 bậc");
            }
            return true;
            }
            if (player.nPoint.power >= 17999999999L && player.nPoint.limitPower < 3) {
            player.nPoint.limitPower += 1;
            if (player.nPoint.limitPower > NPoint.MAX_LIMIT) {
                player.nPoint.limitPower = NPoint.MAX_LIMIT;
            }
            if (!player.isPet) {
                Service.getInstance().sendThongBao(player, "Giới hạn sức mạnh của bạn đã được tăng lên 1 bậc");
            } else {
                Service.getInstance().sendThongBao(((Pet) player).master, "Giới hạn sức mạnh của đệ tử đã được tăng lên 1 bậc");
            }
            return true;
            }
            if (player.nPoint.power >= 17999999999L && player.nPoint.limitPower < 4) {
            player.nPoint.limitPower += 1;
            if (player.nPoint.limitPower > NPoint.MAX_LIMIT) {
                player.nPoint.limitPower = NPoint.MAX_LIMIT;
            }
            if (!player.isPet) {
                Service.getInstance().sendThongBao(player, "Giới hạn sức mạnh của bạn đã được tăng lên 1 bậc");
            } else {
                Service.getInstance().sendThongBao(((Pet) player).master, "Giới hạn sức mạnh của đệ tử đã được tăng lên 1 bậc");
            }
            return true;
            }
            if (player.nPoint.power >= 17999999999L && player.nPoint.limitPower < 5) {
            player.nPoint.limitPower += 1;
            if (player.nPoint.limitPower > NPoint.MAX_LIMIT) {
                player.nPoint.limitPower = NPoint.MAX_LIMIT;
            }
            if (!player.isPet) {
                Service.getInstance().sendThongBao(player, "Giới hạn sức mạnh của bạn đã được tăng lên 1 bậc");
            } else {
                Service.getInstance().sendThongBao(((Pet) player).master, "Giới hạn sức mạnh của đệ tử đã được tăng lên 1 bậc");
            }
            return true;
            }
            if (player.nPoint.power >= 17999999999L && player.nPoint.limitPower < 6) {
            player.nPoint.limitPower += 1;
            if (player.nPoint.limitPower > NPoint.MAX_LIMIT) {
                player.nPoint.limitPower = NPoint.MAX_LIMIT;
            }
            if (!player.isPet) {
                Service.getInstance().sendThongBao(player, "Giới hạn sức mạnh của bạn đã được tăng lên 1 bậc");
            } else {
                Service.getInstance().sendThongBao(((Pet) player).master, "Giới hạn sức mạnh của đệ tử đã được tăng lên 1 bậc");
            }
            return true;
            }
            if (player.nPoint.power >= 17999999999L && player.nPoint.limitPower < 7) {
            player.nPoint.limitPower += 1;
            if (player.nPoint.limitPower > NPoint.MAX_LIMIT) {
                player.nPoint.limitPower = NPoint.MAX_LIMIT;
            }
            if (!player.isPet) {
                Service.getInstance().sendThongBao(player, "Giới hạn sức mạnh của bạn đã được tăng lên 1 bậc");
            } else {
                Service.getInstance().sendThongBao(((Pet) player).master, "Giới hạn sức mạnh của đệ tử đã được tăng lên 1 bậc");
            }
            return true;
            }
            if (player.nPoint.power >= 17999999999L && player.nPoint.limitPower < 8) {
            player.nPoint.limitPower += 1;
            if (player.nPoint.limitPower > NPoint.MAX_LIMIT) {
                player.nPoint.limitPower = NPoint.MAX_LIMIT;
            }
            if (!player.isPet) {
                Service.getInstance().sendThongBao(player, "Giới hạn sức mạnh của bạn đã được tăng lên 1 bậc");
            } else {
                Service.getInstance().sendThongBao(((Pet) player).master, "Giới hạn sức mạnh của đệ tử đã được tăng lên 1 bậc");
            }
            return true;
            }
            if (player.nPoint.power >= 17999999999L && player.nPoint.limitPower < 9) {
            player.nPoint.limitPower += 1;
            if (player.nPoint.limitPower > NPoint.MAX_LIMIT) {
                player.nPoint.limitPower = NPoint.MAX_LIMIT;
            }
            if (!player.isPet) {
                Service.getInstance().sendThongBao(player, "Giới hạn sức mạnh của bạn đã được tăng lên 1 bậc");
            } else {
                Service.getInstance().sendThongBao(((Pet) player).master, "Giới hạn sức mạnh của đệ tử đã được tăng lên 1 bậc");
            }
            return true;
            } 
         /*   if (player.nPoint.power >= 150000000000L && player.nPoint.limitPower < 10) {
            player.nPoint.limitPower += 1;
            if (player.nPoint.limitPower > NPoint.MAX_LIMIT) {
                player.nPoint.limitPower = NPoint.MAX_LIMIT;
            } 
            if (!player.isPet) {
                Service.getInstance().sendThongBao(player, "Giới hạn sức mạnh của bạn đã được tăng lên 1 bậc");
            } else {
                Service.getInstance().sendThongBao(((Pet) player).master, "Giới hạn sức mạnh của đệ tử đã được tăng lên 1 bậc");
            }
            return true;
            }
        /*    if (player.nPoint.power >= 240000000000L && player.nPoint.limitPower < 11) {
            player.nPoint.limitPower += 1;
            if (player.nPoint.limitPower > NPoint.MAX_LIMIT) {
                player.nPoint.limitPower = NPoint.MAX_LIMIT;
            }
            if (!player.isPet) {
                Service.getInstance().sendThongBao(player, "Giới hạn sức mạnh của bạn đã được tăng lên 1 bậc");
            } else {
                Service.getInstance().sendThongBao(((Pet) player).master, "Giới hạn sức mạnh của đệ tử đã được tăng lên 1 bậc");
            }
            return true;
            }
            if (player.nPoint.power >= 270000000000L && player.nPoint.limitPower < 12) {
            player.nPoint.limitPower += 1;
            if (player.nPoint.limitPower > NPoint.MAX_LIMIT) {
                player.nPoint.limitPower = NPoint.MAX_LIMIT;
            }
            if (!player.isPet) {
                Service.getInstance().sendThongBao(player, "Giới hạn sức mạnh của bạn đã được tăng lên 1 bậc");
            } else {
                Service.getInstance().sendThongBao(((Pet) player).master, "Giới hạn sức mạnh của đệ tử đã được tăng lên 1 bậc");
            }
            return true;
            }
            if (player.nPoint.power >= 320000000000L && player.nPoint.limitPower < 13) {
            player.nPoint.limitPower += 1;
            if (player.nPoint.limitPower > NPoint.MAX_LIMIT) {
                player.nPoint.limitPower = NPoint.MAX_LIMIT;
            }
            if (!player.isPet) {
                Service.getInstance().sendThongBao(player, "Giới hạn sức mạnh của bạn đã được tăng lên 1 bậc");
            } else {
                Service.getInstance().sendThongBao(((Pet) player).master, "Giới hạn sức mạnh của đệ tử đã được tăng lên 1 bậc");
            }
            return true;
            }
            if (player.nPoint.power >= 360000000000L && player.nPoint.limitPower < 14) {
            player.nPoint.limitPower += 1;
            if (player.nPoint.limitPower > NPoint.MAX_LIMIT) {
                player.nPoint.limitPower = NPoint.MAX_LIMIT;
            }
            if (!player.isPet) {
                Service.getInstance().sendThongBao(player, "Giới hạn sức mạnh của bạn đã được tăng lên 1 bậc");
            } else {
                Service.getInstance().sendThongBao(((Pet) player).master, "Giới hạn sức mạnh của đệ tử đã được tăng lên 1 bậc");
            }
            return true;
            }
            if (player.nPoint.power >= 500000000000L && player.nPoint.limitPower < 15) {
            player.nPoint.limitPower += 1;
            if (player.nPoint.limitPower > NPoint.MAX_LIMIT) {
                player.nPoint.limitPower = NPoint.MAX_LIMIT;
            }
            if (!player.isPet) {
                Service.getInstance().sendThongBao(player, "Giới hạn sức mạnh của bạn đã được tăng lên 1 bậc");
            } else {
                Service.getInstance().sendThongBao(((Pet) player).master, "Giới hạn sức mạnh của đệ tử đã được tăng lên 1 bậc");
            } 
            return true;
            } */ else {
                if (!player.isPet) {
                    Service.getInstance().sendThongBao(player, "Sức mạnh của bạn không đủ để thực hiện");
                } else {
                    Service.getInstance().sendThongBao(((Pet) player).master, "Sức mạnh của đệ tử không đủ để thực hiện");
                }
                return false;
            }
        } else {
            if (!player.isPet) {
                Service.getInstance().sendThongBao(player, "Sức mạnh của bạn đã đạt tới mức tối đa");
            } else {
                Service.getInstance().sendThongBao(((Pet) player).master, "Sức mạnh của đệ tử đã đạt tới mức tối đa");
            }
            return false;
        } 
    } 

}
