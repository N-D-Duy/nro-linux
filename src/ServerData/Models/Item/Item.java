package ServerData.Models.Item;

import ServerData.Models.Item.Template.ItemTemplate;
import ServerData.Services.ItemService;
import ServerData.Utils.Util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Item {

    public ItemTemplate template;

    public String info;

    public String content;

    public int quantity;

    public int quantityGD = 0;

    public List<ItemOption> itemOptions;

    public long createTime;

    public boolean isNotNullItem() {
        return this.template != null;
    }
public boolean isNullItem() {
        return this.template == null;
    }
    public Item() {
        this.itemOptions = new ArrayList<>();
        this.createTime = System.currentTimeMillis();
    }

    public Item(short itemId) {
        this.template = ItemService.gI().getTemplate(itemId);
        this.itemOptions = new ArrayList<>();
        this.createTime = System.currentTimeMillis();
    }

    public String getInfo() {
        String strInfo = "";
        for (ItemOption itemOption : itemOptions) {
            strInfo += itemOption.getOptionString();
        }
        return strInfo;
    }

    public String getContent() {
        return "Yêu cầu sức mạnh " + this.template.strRequire + " trở lên";
    }

    public void dispose() {
        this.template = null;
        this.info = null;
        this.content = null;
        if (this.itemOptions != null) {
            for (ItemOption io : this.itemOptions) {
                io.dispose();
            }
            this.itemOptions.clear();
        }
        this.itemOptions = null;
    }

    public String getInfoItem() {
        String strInfo = "|1|" + template.name + "\n|0|";
        for (ItemOption itemOption : itemOptions) {
            strInfo += itemOption.getOptionString() + "\n";
        }
        strInfo += "|2|" + template.description;
        return strInfo;
    }

    public static class ItemOption {

        private static Map<String, String> OPTION_STRING = new HashMap<String, String>();

        public int param;

        public Template.ItemOptionTemplate optionTemplate;

        public ItemOption() {
        }

        public ItemOption(ItemOption io) {
            this.param = io.param;
            this.optionTemplate = io.optionTemplate;
        }

        public ItemOption(int tempId, int param) {
            this.optionTemplate = ItemService.gI().getItemOptionTemplate(tempId);
            this.param = param;
        }

        public ItemOption(Template.ItemOptionTemplate temp, int param) {
            this.optionTemplate = temp;
            this.param = param;
        }

        public String getOptionString() {
            return Util.replace(this.optionTemplate.name, "#", String.valueOf(this.param));
        }

        public void dispose() {
            this.optionTemplate = null;
        }
        
        @Override
        public String toString() {
            final String n = "\"";
            return "{"
                    + n + "id" + n + ":" + n + optionTemplate.id + n + ","
                    + n + "param" + n + ":" + n + param + n
                    + "}";
        }
    }
    public boolean isSKH() {
        for (ItemOption itemOption : itemOptions) {
            if (itemOption.optionTemplate.id >= 127 && itemOption.optionTemplate.id <= 135 || itemOption.optionTemplate.id >= 136 && itemOption.optionTemplate.id <= 144 || itemOption.optionTemplate.id == 188) {
                return true;
            }
        }
        return false;
    }
public boolean isSKHLVL1() {
        for (ItemOption itemOption : itemOptions) {
            if (itemOption.optionTemplate.id >= 224 && itemOption.optionTemplate.id <= 232 || itemOption.optionTemplate.id >= 215 && itemOption.optionTemplate.id <= 232) {
                return true;
            }
        }
        return false;
    }
    public boolean isDoThue() {
        for (ItemOption itemOption : itemOptions) {
        if (this.template.id == 1230) {
            if (itemOption.optionTemplate.id == 93){
                return true;
            }
        }
    }
        return false;
    }

    public boolean isDTS() {
        if (this.template.id >= 1048 && this.template.id <= 1062) {
            return true;
        }
        return false;
    }

    public boolean isDTL() {
        if (this.template.id >= 555 && this.template.id <= 567) {
            return true;
        }
        return false;
    }
    public boolean isSKHTLLVL0() {
        for (ItemOption itemOption : itemOptions) {
            if (this.template.id >= 555 && this.template.id <= 567) {
                if (itemOption.optionTemplate.id >= 127 && itemOption.optionTemplate.id <= 135 
                || itemOption.optionTemplate.id >= 136 && itemOption.optionTemplate.id <= 144 
               ) {
                return true;
                }
            }
        }
        return false;
    }
     public boolean isSKHTLLVL1() {
        for (ItemOption itemOption : itemOptions) {
            if (this.template.id >= 555 && this.template.id <= 567) {
                if (itemOption.optionTemplate.id >= 224 && itemOption.optionTemplate.id <= 232 
                || itemOption.optionTemplate.id >= 215 && itemOption.optionTemplate.id <= 232 
            ) {
                return true;
                }
            }
        }
        return false;
    }
 public boolean isDNS() {
        if (this.template.id == 1465) {
            return true;
        }
        return false;
    }
 public boolean isDNS1() {
        if (this.template.id == 1466) {
            return true;
        }
        return false;
    }
    public boolean isDHD() {
        if (this.template.id >= 650 && this.template.id <= 662) {
            return true;
        }
        return false;
    }
    public boolean isDuiDuc() {
        if (this.template.id ==1503) {
            return true;
        }
        return false;
    }
    
    public boolean isDaHomet() {
        if (this.template.id ==1504) {
            return true;
        }
        return false;
    }
    
    
    public boolean isSKHHD() {
        for (ItemOption itemOption : itemOptions) {
            if (this.template.id >= 650 && this.template.id <= 662) {
                if (itemOption.optionTemplate.id >= 127 && itemOption.optionTemplate.id <= 135 
                || itemOption.optionTemplate.id >= 215 && itemOption.optionTemplate.id <= 232 
                || itemOption.optionTemplate.id == 212) {
                return true;
                }
            }
        }
        return false;
    }

    public boolean isManhTS() {
        if (this.template.id >= 1066 && this.template.id <= 1070) {
            return true;
        }
        return false;
    }
    
    public boolean isCongThucVip() {
        if (this.template.id >= 1084 && this.template.id <= 1086) {
            return true;
        }
        return false;
    }
    
    public boolean isDaNangCap() {
        if (this.template.id >= 1074 && this.template.id <= 1078) {
            return true;
        } else if (this.template.id == -1) {
    }
        return false;
    }
    
    public boolean isDaMayMan() {
        if (this.template.id >= 1079 && this.template.id <= 1083) {
            return true;
        }
        return false;
    }

    public String typeName() {
        switch (this.template.type) {
            case 0:
                return "Áo";
            case 1:
                return "Quần";
            case 2:
                return "Găng";
            case 3:
                return "Giày";
            case 4:
                return "Rada";
            default:
                return "";
        }
    }
    
    public String typeHanhTinh() {
        switch (this.template.id) {
            case 1071:
                return "Trái đất";
            case 1084:
                return "Trái đất";
            case 1072:
                return "Namếc";
            case 1085:
                return "Namếc";
            case 1073:
                return "Xayda";
            case 1086:
                return "Xayda";
            default:
                return "";
        }
    }

    public byte typeIdManh() {
        if (!isManhTS()) return -1;
        switch (this.template.id) {
            case 1066:
                return 0;
            case 1067:
                return 1;
            case 1070:
                return 2;
            case 1068:
                return 3;
            case 1069:
                return 4;
            default:
                return -1;
        }
    }

    public String typeNameManh() {
        switch (this.template.id) {
            case 1066:
                return "Áo";
            case 1067:
                return "Quần";
            case 1070:
                return "Găng";
            case 1068:
                return "Giày";
            case 1069:
                return "Nhẫn";
            default:
                return "";
        }
    }
    
    public String typeDanangcap() {
        switch (this.template.id) {
            case 1074:
                return "cấp 1";
            case 1075:
                return "cấp 2";
            case 1076:
                return "cấp 3";
            case 1077:
                return "cấp 4";
            case 1078:
                return "cấp 5";
            default:
                return "";
        }
    }
    
    public String typeDaMayman() {
        switch (this.template.id) {
            case 1079:
                return "cấp 1";
            case 1080:
                return "cấp 2";
            case 1081:
                return "cấp 3";
            case 1082:
                return "cấp 4";
            case 1083:
                return "cấp 5";
            default:
                return "";
        }
    }
}
