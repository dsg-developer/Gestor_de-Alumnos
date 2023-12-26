package models;

import main.Start;

/**
 * @author Work
 */

public class ClassIDs {
    private Integer id1;
    private Integer id2;
    private Integer id3;
    private Integer id4;
    private Integer id5;

    public ClassIDs() { Start.setConteo(Start.getConteo()+0.1f); }

    public ClassIDs(Integer id1) {
        this.id1 = id1;
    }

    public ClassIDs(Integer id1, Integer id2) {
        this.id1 = id1;
        this.id2 = id2;
    }

    public ClassIDs(Integer id1, Integer id2, Integer id3) {
        this.id1 = id1;
        this.id2 = id2;
        this.id3 = id3;
    }
   
    public ClassIDs(Integer id1, Integer id2, Integer id3, Integer id4) {
        this.id1 = id1;
        this.id2 = id2;
        this.id3 = id3;
        this.id4 = id4;
    }
    
    public ClassIDs(Integer id1, Integer id2, Integer id3, Integer id4, Integer id5) {
        this.id1 = id1;
        this.id2 = id2;
        this.id3 = id3;
        this.id4 = id4;
        this.id5 = id5;
    }

    public Integer getId1() {
        return id1;
    }

    public void setId1(Integer id1) {
        this.id1 = id1;
    }

    public Integer getId2() {
        return id2;
    }

    public void setId2(Integer id2) {
        this.id2 = id2;
    }

    public Integer getId3() {
        return id3;
    }

    public void setId3(Integer id3) {
        this.id3 = id3;
    }

    public Integer getId4() {
        return id4;
    }

    public void setId4(Integer id4) {
        this.id4 = id4;
    }

    public Integer getId5() {
        return id5;
    }

    public void setId5(Integer id5) {
        this.id5 = id5;
    }
    
}
