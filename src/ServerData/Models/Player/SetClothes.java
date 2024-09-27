package ServerData.Models.Player;

import ServerData.Models.Item.Item;
import ServerData.Services.Service;


public class SetClothes {

    private Player player;
    public boolean huydietClothers;

    public SetClothes(Player player) {
        this.player = player;
    }

    public byte songoku;
    public byte thienXinHang;
    public byte kirin;

    public byte ocTieu;
    public byte pikkoroDaimao;
    public byte picolo;

    public byte kakarot;
    public byte cadic;
    public byte nappa;
    
    // SETKH V2
    public byte songoku2;
    public byte thienXinHang2;
    public byte kirin2;
    public byte ocTieu2;
    public byte pikkoroDaimao2;
    public byte picolo2;
    public byte kakarot2;
    public byte cadic2;
    public byte nappa2;
      
    // SETKH V3
    public byte songoku3;
    public byte thienXinHang3;
    public byte kirin3;
    public byte ocTieu3;
    public byte pikkoroDaimao3;
    public byte picolo3;
    public byte kakarot3;
    public byte cadic3;
    public byte nappa3;

    public byte worldcup;
    public byte setDHD;
    public byte setDTL;
    
    public byte tiemnang;

    public boolean godClothes;
    public int ctHaiTac = -1;

    public void setup() {
        setDefault();
        setupSKTT();
        setupSKT();
        setupSKTTL();
        this.godClothes = true;
        for (int i = 0; i < 5; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                if (item.template.id > 567 || item.template.id < 555) {
                    this.godClothes = false;
                    break;
                }
            } else {
                this.godClothes = false;
                break;
            }
        }
        Item ct = this.player.inventory.itemsBody.get(5);
        if (ct.isNotNullItem()) {
            switch (ct.template.id) {
                case 618:
                case 619:
                case 620:
                case 621:
                case 622:
                case 623:
                case 624:
                case 626:
                case 627:
                    this.ctHaiTac = ct.template.id;
                    break;

            }
        }
    }
  private void setupSKTT() {
        for (int i = 0; i < 5; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                boolean isActSet = false;
                for (Item.ItemOption io : item.itemOptions) {
                    switch (io.optionTemplate.id) {
                      case 21:
                            if (io.param > 39 && io.param < 80) {
                                setDHD++;
                            }
                            if (io.param > 14 && io.param < 21) {
                                setDTL++;
                            }
                            break;
                    }
                    if (isActSet) {
                        break;
                    }
                }
            } else {
                break;
            }
        }
    }
    private void setupSKT() {
        for (int i = 0; i < 5; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()&&item.template.id >= 0 && item.template.id <= 40) {
                boolean isActSet = false;
                for (Item.ItemOption io : item.itemOptions) {
                    switch (io.optionTemplate.id) {
                        case 129:
                        case 141:
                            isActSet = true;
                            songoku++;
                               if (songoku ==5){
                            Service.gI().addEffectChar(player, 2200, 1, -1, -1, 0);} else 
                            {
                                Service.gI().removeEff(player, 2200);
                            }
                             
                            break;
                        case 127:
                        case 139:
                            isActSet = true;
                            thienXinHang++;
                              if (thienXinHang ==5){
                            Service.gI().addEffectChar(player, 2200, 1, -1, -1, 0);} else 
                            {
                                Service.gI().removeEff(player, 2200);
                            }
                            break;
                        case 128:
                        case 140:
                            isActSet = true;
                           
                            kirin++;
                               if (kirin ==5){
                            Service.gI().addEffectChar(player, 2200, 1, -1, -1, 0);} else 
                            {
                                Service.gI().removeEff(player, 2200);
                            }
                            break;
                        case 131:
                        case 143:
                            isActSet = true;
                            ocTieu++;
                               if (ocTieu ==5){
                            Service.gI().addEffectChar(player, 2201, 1, -1, -1, 0);} else 
                            {
                                Service.gI().removeEff(player, 2201);
                            }
                            break;
                        case 132:
                        case 144:
                            isActSet = true;
                            pikkoroDaimao++;
                                 if (pikkoroDaimao ==5){
                            Service.gI().addEffectChar(player, 2201, 1, -1, -1, 0);} else 
                            {
                                Service.gI().removeEff(player, 2201);
                            }
                            break;
                        case 130:
                        case 142:
                            isActSet = true;
                            picolo++;
                               if (picolo ==5){
                            Service.gI().addEffectChar(player, 2201, 1, -1, -1, 0);} else 
                            {
                                Service.gI().removeEff(player, 2201);
                            }
                            break;
                        case 135:
                        case 138:
                            isActSet = true;
                            nappa++;
                              if (nappa ==5){
                            Service.gI().addEffectChar(player, 2202, 1, -1, -1, 0);} else 
                            {
                                Service.gI().removeEff(player, 2202);
                            }
                            break;
                        case 133:
                        case 136:
                            isActSet = true;
                            kakarot++;
                             if (kakarot ==5){
                            Service.gI().addEffectChar(player, 2202, 1, -1, -1, 0);} else 
                            {
                                Service.gI().removeEff(player, 2202);
                            }
                            break;
                        case 134:
                        case 137:
                            isActSet = true;
                            cadic++;
                             if (cadic ==5){
                            Service.gI().addEffectChar(player, 2202, 1, -1, -1, 0);} else 
                            {
                                Service.gI().removeEff(player, 2202);
                            }
                            break;
                        case 188:
                        case 213:
                            isActSet = true;
                            tiemnang++;
                            break;
                        case 220:
                        case 226:
                            isActSet = true;
                             
                            songoku2++;
                             if (songoku2 ==5){
                            Service.gI().addEffectChar(player, 2200, 1, -1, -1, 0);} else 
                            {
                                Service.gI().removeEff(player, 2200);
                            }
                            break;
                        case 218:
                        case 224:
                            isActSet = true;
                            thienXinHang2++;
                              if (thienXinHang2 ==5){
                            Service.gI().addEffectChar(player, 2200, 1, -1, -1, 0);} else 
                            {
                                Service.gI().removeEff(player, 2200);
                            }
                            break;
                        case 219:
                        case 225:
                            isActSet = true;
                            kirin2++;
                              if (kirin2 ==5){
                            Service.gI().addEffectChar(player, 2200, 1, -1, -1, 0);} else 
                            {
                                Service.gI().removeEff(player, 2200);
                            }
                            break;
                        case 222:
                        case 228:
                            isActSet = true;
                            ocTieu2++;
                              if (ocTieu2 ==5){
                            Service.gI().addEffectChar(player, 2201, 1, -1, -1, 0);} else 
                            {
                                Service.gI().removeEff(player, 2201);
                            }
                            break;
                        case 223:
                        case 229:
                            isActSet = true;
                            pikkoroDaimao2++;
                              if (pikkoroDaimao2 ==5){
                            Service.gI().addEffectChar(player, 2201, 1, -1, -1, 0);} else 
                            {
                                Service.gI().removeEff(player, 2201);
                            }
                            break;
                        case 221:
                        case 227:
                            isActSet = true;
                            picolo2++;
                              if (picolo2 ==5){
                            Service.gI().addEffectChar(player, 2201, 1, -1, -1, 0);} else 
                            {
                                Service.gI().removeEff(player, 2201);
                            }
                            break;
                        case 217:
                        case 232:
                            isActSet = true;
                            nappa2++;
                             if (nappa2 ==5){
                            Service.gI().addEffectChar(player, 2202, 1, -1, -1, 0);} else 
                            {
                                Service.gI().removeEff(player, 2202);
                            }
                            break;
                        case 215:
                        case 230:
                            isActSet = true;
                            kakarot2++;
                             if (kakarot2 ==5){
                            Service.gI().addEffectChar(player, 2202, 1, -1, -1, 0);} else 
                            {
                                Service.gI().removeEff(player, 2202);
                            }
                            break;
                        case 216:
                        case 231:
                            isActSet = true;
                            cadic2++;
                             if (cadic2 ==5){
                            Service.gI().addEffectChar(player, 2202, 1, -1, -1, 0);} else 
                            {
                                Service.gI().removeEff(player, 2202);
                            }
                            break;
                             case 199:
                        case 46:
                            isActSet = true;
                            songoku3++;
                              if (songoku3 ==5){
                            Service.gI().addEffectChar(player, 2200, 1, -1, -1, 0);} else 
                            {
                                Service.gI().removeEff(player, 2200);
                            }
                            break;
                        case 197:
                        case 44:
                            isActSet = true;
                            thienXinHang3++;
                              if (thienXinHang3 ==5){
                            Service.gI().addEffectChar(player, 2200, 1, -1, -1, 0);} else 
                            {
                                Service.gI().removeEff(player, 2200);
                            }
                            break;
                        case 198:
                        case 45:
                            isActSet = true;
                            kirin3++;
                              if (kirin3 ==5){
                            Service.gI().addEffectChar(player, 2200, 1, -1, -1, 0);} else 
                            {
                                Service.gI().removeEff(player, 2200);
                            }
                            break;
                        case 201:
                        case 190:
                            isActSet = true;
                            ocTieu3++;
                              if (ocTieu3 ==5){
                            Service.gI().addEffectChar(player, 2201, 1, -1, -1, 0);} else 
                            {
                                Service.gI().removeEff(player, 2201);
                            }
                            break;
                        case 202:
                        case 191:
                            isActSet = true;
                            pikkoroDaimao3++;
                              if (pikkoroDaimao3 ==5){
                            Service.gI().addEffectChar(player, 2201, 1, -1, -1, 0);} else 
                            {
                                Service.gI().removeEff(player, 2201);
                            }
                            break;
                        case 200:
                        case 189:
                            isActSet = true;
                            picolo3++;
                              if (picolo3 ==5){
                            Service.gI().addEffectChar(player, 2201, 1, -1, -1, 0);} else 
                            {
                                Service.gI().removeEff(player, 2201);
                            }
                            break;
                        case 205:
                        case 43:
                            isActSet = true;
                            nappa3++;
                             if (nappa3 ==5){
                            Service.gI().addEffectChar(player, 2202, 1, -1, -1, 0);} else 
                            {
                                Service.gI().removeEff(player, 2202);
                            }
                            break;
                        case 203:
                        case 41:
                            isActSet = true;
                            kakarot3++;
                             if (kakarot3 ==5){
                            Service.gI().addEffectChar(player, 2202, 1, -1, -1, 0);} else 
                            {
                                Service.gI().removeEff(player, 2202);
                            }
                            break;
                        case 204:
                        case 42:
                            isActSet = true;
                            cadic3++;
                             if (cadic3 ==5){
                            Service.gI().addEffectChar(player, 2202, 1, -1, -1, 0);} else 
                            {
                                Service.gI().removeEff(player, 2202);
                            }
                            break;
//                        case 192:
//                        case 193:
//                            worldcup++;
//                            break;
                              case 21:
                            if (io.param > 39 && io.param < 80) {
                                setDHD++;
                            }
                            if (io.param > 14 && io.param < 21) {
                                setDTL++;
                            }
                            break;
                    }
                    if (isActSet) {
                        break;
                    }
                }
            } else {
                break;
            }
        }
    }
     private void setupSKTTL() {
        for (int i = 0; i < 5; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()&& item.template.id >= 555 && item.template.id <= 567) {
                boolean isActSet = false;
                for (Item.ItemOption io : item.itemOptions) {
                    switch (io.optionTemplate.id) {
                        case 129:
                        case 141:
                            isActSet = true;
                            songoku++;
                               if (songoku ==5){
                            Service.gI().addEffectChar(player, 2200, 1, -1, -1, 0);} else 
                            {
                                Service.gI().removeEff(player, 2200);
                            }
                             
                            break;
                        case 127:
                        case 139:
                            isActSet = true;
                            thienXinHang++;
                              if (thienXinHang ==5){
                            Service.gI().addEffectChar(player, 2200, 1, -1, -1, 0);} else 
                            {
                                Service.gI().removeEff(player, 2200);
                            }
                            break;
                        case 128:
                        case 140:
                            isActSet = true;
                           
                            kirin++;
                               if (kirin ==5){
                            Service.gI().addEffectChar(player, 2200, 1, -1, -1, 0);} else 
                            {
                                Service.gI().removeEff(player, 2200);
                            }
                            break;
                        case 131:
                        case 143:
                            isActSet = true;
                            ocTieu++;
                               if (ocTieu ==5){
                            Service.gI().addEffectChar(player, 2201, 1, -1, -1, 0);} else 
                            {
                                Service.gI().removeEff(player, 2201);
                            }
                            break;
                        case 132:
                        case 144:
                            isActSet = true;
                            pikkoroDaimao++;
                                 if (pikkoroDaimao ==5){
                            Service.gI().addEffectChar(player, 2201, 1, -1, -1, 0);} else 
                            {
                                Service.gI().removeEff(player, 2201);
                            }
                            break;
                        case 130:
                        case 142:
                            isActSet = true;
                            picolo++;
                               if (picolo ==5){
                            Service.gI().addEffectChar(player, 2201, 1, -1, -1, 0);} else 
                            {
                                Service.gI().removeEff(player, 2201);
                            }
                            break;
                        case 135:
                        case 138:
                            isActSet = true;
                            nappa++;
                              if (nappa ==5){
                            Service.gI().addEffectChar(player, 2202, 1, -1, -1, 0);} else 
                            {
                                Service.gI().removeEff(player, 2202);
                            }
                            break;
                        case 133:
                        case 136:
                            isActSet = true;
                            kakarot++;
                             if (kakarot ==5){
                            Service.gI().addEffectChar(player, 2202, 1, -1, -1, 0);} else 
                            {
                                Service.gI().removeEff(player, 2202);
                            }
                            break;
                        case 134:
                        case 137:
                            isActSet = true;
                            cadic++;
                             if (cadic ==5){
                            Service.gI().addEffectChar(player, 2202, 1, -1, -1, 0);} else 
                            {
                                Service.gI().removeEff(player, 2202);
                            }
                            break;
                        case 188:
                        case 213:
                            isActSet = true;
                            tiemnang++;
                            break;
                        case 220:
                        case 226:
                            isActSet = true;
                             
                            songoku2++;
                             if (songoku2 ==5){
                            Service.gI().addEffectChar(player, 2200, 1, -1, -1, 0);} else 
                            {
                                Service.gI().removeEff(player, 2200);
                            }
                            break;
                        case 218:
                        case 224:
                            isActSet = true;
                            thienXinHang2++;
                              if (thienXinHang2 ==5){
                            Service.gI().addEffectChar(player, 2200, 1, -1, -1, 0);} else 
                            {
                                Service.gI().removeEff(player, 2200);
                            }
                            break;
                        case 219:
                        case 225:
                            isActSet = true;
                            kirin2++;
                              if (kirin2 ==5){
                            Service.gI().addEffectChar(player, 2200, 1, -1, -1, 0);} else 
                            {
                                Service.gI().removeEff(player, 2200);
                            }
                            break;
                        case 222:
                        case 228:
                            isActSet = true;
                            ocTieu2++;
                              if (ocTieu2 ==5){
                            Service.gI().addEffectChar(player, 2201, 1, -1, -1, 0);} else 
                            {
                                Service.gI().removeEff(player, 2201);
                            }
                            break;
                        case 223:
                        case 229:
                            isActSet = true;
                            pikkoroDaimao2++;
                              if (pikkoroDaimao2 ==5){
                            Service.gI().addEffectChar(player, 2201, 1, -1, -1, 0);} else 
                            {
                                Service.gI().removeEff(player, 2201);
                            }
                            break;
                        case 221:
                        case 227:
                            isActSet = true;
                            picolo2++;
                              if (picolo2 ==5){
                            Service.gI().addEffectChar(player, 2201, 1, -1, -1, 0);} else 
                            {
                                Service.gI().removeEff(player, 2201);
                            }
                            break;
                        case 217:
                        case 232:
                            isActSet = true;
                            nappa2++;
                             if (nappa2 ==5){
                            Service.gI().addEffectChar(player, 2202, 1, -1, -1, 0);} else 
                            {
                                Service.gI().removeEff(player, 2202);
                            }
                            break;
                        case 215:
                        case 230:
                            isActSet = true;
                            kakarot2++;
                             if (kakarot2 ==5){
                            Service.gI().addEffectChar(player, 2202, 1, -1, -1, 0);} else 
                            {
                                Service.gI().removeEff(player, 2202);
                            }
                            break;
                        case 216:
                        case 231:
                            isActSet = true;
                            cadic2++;
                             if (cadic2 ==5){
                            Service.gI().addEffectChar(player, 2202, 1, -1, -1, 0);} else 
                            {
                                Service.gI().removeEff(player, 2202);
                            }
                            break;
                             case 199:
                        case 46:
                            isActSet = true;
                            songoku3++;
                              if (songoku3 ==5){
                            Service.gI().addEffectChar(player, 2200, 1, -1, -1, 0);} else 
                            {
                                Service.gI().removeEff(player, 2200);
                            }
                            break;
                        case 197:
                        case 44:
                            isActSet = true;
                            thienXinHang3++;
                              if (thienXinHang3 ==5){
                            Service.gI().addEffectChar(player, 2200, 1, -1, -1, 0);} else 
                            {
                                Service.gI().removeEff(player, 2200);
                            }
                            break;
                        case 198:
                        case 45:
                            isActSet = true;
                            kirin3++;
                              if (kirin3 ==5){
                            Service.gI().addEffectChar(player, 2200, 1, -1, -1, 0);} else 
                            {
                                Service.gI().removeEff(player, 2200);
                            }
                            break;
                        case 201:
                        case 190:
                            isActSet = true;
                            ocTieu3++;
                              if (ocTieu3 ==5){
                            Service.gI().addEffectChar(player, 2201, 1, -1, -1, 0);} else 
                            {
                                Service.gI().removeEff(player, 2201);
                            }
                            break;
                        case 202:
                        case 191:
                            isActSet = true;
                            pikkoroDaimao3++;
                              if (pikkoroDaimao3 ==5){
                            Service.gI().addEffectChar(player, 2201, 1, -1, -1, 0);} else 
                            {
                                Service.gI().removeEff(player, 2201);
                            }
                            break;
                        case 200:
                        case 189:
                            isActSet = true;
                            picolo3++;
                              if (picolo3 ==5){
                            Service.gI().addEffectChar(player, 2201, 1, -1, -1, 0);} else 
                            {
                                Service.gI().removeEff(player, 2201);
                            }
                            break;
                        case 205:
                        case 43:
                            isActSet = true;
                            nappa3++;
                             if (nappa3 ==5){
                            Service.gI().addEffectChar(player, 2202, 1, -1, -1, 0);} else 
                            {
                                Service.gI().removeEff(player, 2202);
                            }
                            break;
                        case 203:
                        case 41:
                            isActSet = true;
                            kakarot3++;
                             if (kakarot3 ==5){
                            Service.gI().addEffectChar(player, 2202, 1, -1, -1, 0);} else 
                            {
                                Service.gI().removeEff(player, 2202);
                            }
                            break;
                        case 204:
                        case 42:
                            isActSet = true;
                            cadic3++;
                             if (cadic3 ==5){
                            Service.gI().addEffectChar(player, 2202, 1, -1, -1, 0);} else 
                            {
                                Service.gI().removeEff(player, 2202);
                            }
                            break;
//                        case 192:
//                        case 193:
//                            worldcup++;
//                            break;
                        case 21:
                            if (io.param > 39 && io.param < 80) {
                                setDHD++;
                            }
                            if (io.param > 14 && io.param < 21) {
                                setDTL++;
                            }
                            break;
                    }
                    if (isActSet) {
                        break;
                    }
                }
            } else {
                break;
            }
        }
    }
    
    //checksetthanlinh
       public boolean setGod(){
        for (int i = 0; i < 6; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                if (item.template.id >= 555 && item.template.id <= 567) {
                    i++;
                } else if(i == 5) {
                    this.godClothes = true;
                    break;
                }
            } else {
                this.godClothes = false;
                break;
            }
        }
        return this.godClothes ? true : false;
       }
    
    
    // check set huy diet
    public boolean setGod14() {
        for (int i = 0; i < 6; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                if (item.template.id >= 650 && item.template.id <= 663) {
                    i++;
                } else if (i == 5) {
                    this.huydietClothers = true;
                    break;
                }
            } else {
                this.huydietClothers = false;
                break;
            }
        }
        return this.huydietClothers ? true : false;
    }
    
    private void setDefault() {
        this.songoku = 0;
        this.thienXinHang = 0;
        this.kirin = 0;
        this.ocTieu = 0;
        this.pikkoroDaimao = 0;
        this.picolo = 0;
        this.kakarot = 0;
        this.cadic = 0;
        this.nappa = 0;
        this.songoku2 = 0;
        this.thienXinHang2 = 0;
        this.kirin2 = 0;
        this.ocTieu2 = 0;
        this.pikkoroDaimao2 = 0;
        this.picolo2 = 0;
        this.kakarot2 = 0;
        this.cadic2 = 0;
        this.nappa2 = 0;
         this.songoku3 = 0;
        this.thienXinHang3 = 0;
        this.kirin3 = 0;
        this.ocTieu3 = 0;
        this.pikkoroDaimao3 = 0;
        this.picolo3 = 0;
        this.kakarot3 = 0;
        this.cadic3 = 0;
        this.nappa3 = 0;
        this.tiemnang = 0;
        this.setDHD = 0;
        this.setDTL = 0;
        this.worldcup = 0;
        this.godClothes = false;
        this.ctHaiTac = -1;
    }

    public void dispose() {
        this.player = null;
    }
}
