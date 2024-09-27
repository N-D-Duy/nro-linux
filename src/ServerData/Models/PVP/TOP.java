package ServerData.Models.PVP;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TOP {
    private int id_player;
    private long power;
    private double ki;
    private double hp;
    private double sd;
    private byte nv;
    private int sk;
    private int pvp;
    private String info1;
    private String info2;
     public int rank;
  public String getName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getPower() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getNv() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getPvp() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getSk() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

