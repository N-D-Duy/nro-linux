package ServerData.Services;

import Server.Data.Consts.ConstPlayer;
import ServerData.Models.Item.Template;
import ServerData.Models.Item.Item;
import ServerData.Models.Player.NewPet;
import ServerData.Models.Player.Pet;
import ServerData.Models.Player.Player;
import ServerData.Server.Manager;
import ServerData.Services.Func.UseItem;
import ServerData.Utils.SkillUtil;
import ServerData.Utils.Util;

public class PetService {

    private static PetService i;

    public static PetService gI() {
        if (i == null) {
            i = new PetService();
        }
        return i;
    }

    public void createNormalPet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, false, false,false,false,false, false,(byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Xin hãy thu nhận làm đệ tử");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createNormalPet(Player player, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, false, false,false,false,false,false);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Xin hãy thu nhận làm đệ tử");
            } catch (Exception e) {
            }
        }).start();
    }
    public void createMabuPet(Player player, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, true, false,false,false,false,false);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Oa oa oa...");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createMabuPet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, true, false,false,false,false, false,(byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Oa oa oa...");
            } catch (Exception e) {
            }
        }).start();
    }
    public void createBerusPet(Player player, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, false, true,false,false,false,false);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Thần hủy diệt hiện thân tất cả quỳ xuống...");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createBerusPet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, false, true,false,false,false, false,(byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Thần hủy diệt hiện thân tất cả quỳ xuống...");
            } catch (Exception e) {
            }
        }).start();
    } public void createPicPet(Player player, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, false, false,true,false,false,false);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Ọc Ọc");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createPicPet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, false,false,true,false,false,false, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Ọc Ọc");
            } catch (Exception e) {
            }
        }).start();
    }
    public void createCumberPet(Player player, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, false, false,false,true,false,false);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Nếu m chết anh em mày sẽ\nđụ vợ mày");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createCumberPet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, false, false,false,true,false,false, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Nếu m chết anh em sẽ đụ vợ m");
            } catch (Exception e) {
            }
        }).start();
    }
    
    public void createGokuPet(Player player, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, false, false,false,false,true,false);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Nếu m chết anh em mày sẽ\nđụ vợ mày");
            } catch (Exception e) {
            }
        }).start();
    }
    
    public void createGokuPet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, false, false,false,false,true,false, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Nếu m chết anh em sẽ đụ vợ m");
            } catch (Exception e) {
            }
        }).start();
    }
    public void createCadicPet(Player player, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, false, false,false,false,false,true);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Nếu m chết anh em mày sẽ\nđụ vợ mày");
            } catch (Exception e) {
            }
        }).start();
    }
    
    public void createCadicPet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, false, false,false,false,false,true, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Nếu m chết anh em sẽ đụ vợ m");
            } catch (Exception e) {
            }
        }).start();
    }
    
    public void changeNormalPet(Player player, int gender) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        ChangeMapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createNormalPet(player, gender, limitPower);
    }

    public void changeNormalPet(Player player) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        ChangeMapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createNormalPet(player, limitPower);
    }
    public void changeMabuPet(Player player) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        ChangeMapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createMabuPet(player, limitPower);
    }
    public void changeMabuPet(Player player, int gender) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        ChangeMapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createMabuPet(player, gender, limitPower);
    }
    public void changeBerusPet(Player player) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        ChangeMapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createBerusPet(player, limitPower);
    }

    public void changeBerusPet(Player player, int gender) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        ChangeMapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createBerusPet(player, gender, limitPower);
        
    } public void changePicPet(Player player) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        ChangeMapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createPicPet(player, limitPower);
    }

    public void changePicPet(Player player, int gender) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {  
            player.pet.unFusion();
        }
        ChangeMapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createPicPet(player, gender, limitPower);
    }
     public void changeCumberPet(Player player) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        ChangeMapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createCumberPet(player, limitPower);
    }
    
    public void changeCumberPet(Player player, int gender) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {  
            player.pet.unFusion();
        }
        ChangeMapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createCumberPet(player, gender, limitPower);
    }
    
     public void changeGokuPet(Player player) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        ChangeMapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createGokuPet(player, limitPower);
    }
     public void changeCadicPet(Player player, int gender) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        ChangeMapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createCadicPet(player, gender, limitPower);
    }
     
    public void changeGokuPet(Player player, int gender) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {  
            player.pet.unFusion();
        }
        ChangeMapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createGokuPet(player, gender, limitPower);
    }
    
    public void changeNamePet(Player player, String name) {
        try {
            if (!InventoryServiceNew.gI().isExistItemBag(player, 400)) {
                Service.getInstance().sendThongBao(player, "Bạn cần thẻ đặt tên đệ tử, mua tại Santa");
                return;
            } else if (Util.haveSpecialCharacter(name)) {
                Service.getInstance().sendThongBao(player, "Tên không được chứa ký tự đặc biệt");
                return;
            } else if (name.length() > 10) {
                Service.getInstance().sendThongBao(player, "Tên quá dài");
                return;
            }
            ChangeMapService.gI().exitMap(player.pet);
            player.pet.name = "$" + name.toLowerCase().trim();
            InventoryServiceNew.gI().subQuantityItemsBag(player, InventoryServiceNew.gI().findItemBag(player, 400), 1);
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    Service.getInstance().chatJustForMe(player, player.pet, "Cảm ơn sư phụ đã đặt cho con tên " + name);
                } catch (Exception e) {
                }
            }).start();
        } catch (Exception ex) {

        }
    }

    private int[] getDataPetNormal() {
        int[] hpmp = {1700, 1800, 1900, 2000, 2100, 2500};
        int[] petData = new int[5];
        petData[0] = Util.nextInt(40, 105) * 20; //hp
        petData[1] = Util.nextInt(40, 105) * 20; //mp
        petData[2] = Util.nextInt(20, 45); //dame
        petData[3] = Util.nextInt(9, 50); //def
        petData[4] = Util.nextInt(0, 2); //crit
        return petData;
    }

    private int[] getDataPetMabu() {
        int[] hpmp = {1700, 1800, 1900, 2000, 2100, 2500};
        int[] petData = new int[5];
        petData[0] = Util.nextInt(40, 105) * 20; //hp
        petData[1] = Util.nextInt(40, 105) * 20; //mp
        petData[2] = Util.nextInt(20, 45); //dame
        petData[3] = Util.nextInt(9, 50); //def
        petData[4] = Util.nextInt(0, 3); //crit
        return petData;
    }
     private int[] getDataPetPic() {
        int[] hpmp = {1700, 1800, 1900, 2000, 2100, 2500};
        int[] petData = new int[5];
        petData[0] = Util.nextInt(100, 150) * 20; //hp
        petData[1] = Util.nextInt(100, 150) * 20; //mp
        petData[2] = Util.nextInt(50, 500); //dame
        petData[3] = Util.nextInt(9, 100); //def
        petData[4] = Util.nextInt(0, 5); //crit
        return petData;
    }
     private int[] getDataPetBerus() {
        int[] hpmp = {1700, 1800, 1900, 2000, 2100, 2500};
        int[] petData = new int[5];
        petData[0] = Util.nextInt(40, 105) * 20; //hp
        petData[1] = Util.nextInt(40, 105) * 20; //mp
        petData[2] = Util.nextInt(20, 45); //dame
        petData[3] = Util.nextInt(9, 50); //def
        petData[4] = Util.nextInt(0, 4); //crit
        return petData;
    }
     private int[] getDataPetCumber() {
        int[] petData = new int[5];
        petData[0] = Util.nextInt(80, 200) * 20; //hp
        petData[1] = Util.nextInt(80, 200) * 20; //mp
        petData[2] = Util.nextInt(80, 500); //dame
        petData[3] = Util.nextInt(10, 200); //def
        petData[4] = Util.nextInt(0, 6); //crit
        return petData;
    }
      private int[] getDataPetGoku() {
        int[] petData = new int[5];
        petData[0] = Util.nextInt(100, 200) * 20; //hp
        petData[1] = Util.nextInt(100, 200) * 20; //mp
        petData[2] = Util.nextInt(100, 500); //dame
        petData[3] = Util.nextInt(10, 200); //def
        petData[4] = Util.nextInt(0, 6); //crit
        return petData;
    }
    private void createNewPet(Player player, boolean isMabu, boolean isBerus,boolean isPic,boolean isCumber, boolean Gokunecon,boolean Cadicnecon,byte... gender) {
        int[] data = isMabu ?  getDataPetMabu():
                isPic ? getDataPetPic():
                isBerus ? getDataPetBerus():
                isCumber ? getDataPetCumber():
                Gokunecon ? getDataPetGoku():
                Cadicnecon ? getDataPetGoku():
                 getDataPetNormal();
        Pet pet = new Pet(player);
        pet.name = "$" + (isMabu ? "MaBư" : isBerus ? "Gohan Buu" : isPic ? "Evil Buu" : isCumber ? "Cumber SSJ2" : Gokunecon ? "Songoku SSJ4" : Cadicnecon ? "Vegeta SSJ4"  : "Đệ tử");
        pet.gender = (gender != null && gender.length != 0) ? gender[0] : (byte) Util.nextInt(0, 2);
        pet.id = -player.id;
        pet.nPoint.power = isMabu || isBerus || isPic || isCumber ? 1500000 : Gokunecon || Cadicnecon ? 15000000 : 2000;
        pet.typePet = (byte) (isMabu ? 1 : isBerus ? 2 :isPic ? 3 :isCumber ? 4 : Gokunecon ? 5 : Cadicnecon ? 6 : 0);
        pet.thuctinh = (byte) 0;
        pet.nPoint.stamina = 1000;
        pet.nPoint.maxStamina = 1000;
        pet.nPoint.hpg = data[0];
        pet.nPoint.mpg = data[1];
        pet.nPoint.dameg = data[2];
        pet.nPoint.defg = data[3];
        pet.nPoint.critg = data[4];
        for (int i = 0; i < 8; i++) {
            pet.inventory.itemsBody.add(ItemService.gI().createItemNull());
        }
        pet.playerSkill.skills.add(SkillUtil.createSkill(Util.nextInt(0, 2) * 2, 1));
        for (int i = 0; i < 3; i++) {
            pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
        }
        pet.nPoint.setFullHpMp();
        player.pet = pet;
    }
    
    public static void Pet2(Player pl, int h, int b, int l) {
        if (pl.newpet != null) {
        ChangeMapService.gI().exitMap(pl.newpet);
        }
        pl.newpet = new NewPet(pl, (short) h, (short) b, (short) l);
        pl.newpet.name = "$"+pl.inventory.itemsBody.get(7).template.name;
        pl.newpet.nPoint.hpg = 5;
        pl.newpet.nPoint.tlNeDon = 1000000;
        pl.newpet.nPoint.setBasePoint();
        pl.newpet.nPoint.setFullHpMp();
    }
    /*public static void Petfl2(Player pl, int h, int b, int l) {
        if (pl.newpet != null) {
        ChangeMapService.gI().exitMap(pl.newpet);
          return;
        }
        pl.newpet = new NewPet(pl, (short) h, (short) b, (short) l);
        pl.newpet.name = "$";
        pl.newpet.nPoint.tiemNang = 1;
        pl.newpet.nPoint.power = 1;
        pl.newpet.nPoint.limitPower = 1;
        pl.newpet.nPoint.hpg = 5;
        pl.newpet.nPoint.mpg = 1;
        pl.newpet.nPoint.hp = 0;
        pl.newpet.nPoint.mp = 0;
        pl.newpet.nPoint.dameg = 1;
        pl.newpet.nPoint.defg = 500000000;
        pl.newpet.nPoint.critg = 1;
        pl.newpet.nPoint.tlNeDon = 1000;
        pl.newpet.nPoint.stamina = 1;
        pl.newpet.nPoint.setBasePoint();
        pl.newpet.nPoint.setFullHpMp();
    }*/
    //--------------------------------------------------------------------------
}