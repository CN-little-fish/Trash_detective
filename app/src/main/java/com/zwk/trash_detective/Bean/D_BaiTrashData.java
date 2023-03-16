//此代码要用

package com.zwk.trash_detective.Bean;

import java.io.Serializable;

public class D_BaiTrashData implements Serializable {
    private  String id = "";
    private String keyword = "";
    private String score = "";
    private String root = "";
    private String image_url = "";
    private String baike_url = "";
    private String description = "";


    public D_BaiTrashData(String keyword, String score, String root, String baike_info) {
        this.keyword = keyword;
        this.score = score;
        this.root = root;
        this.getInformation(baike_info);
    }

    public D_BaiTrashData(String keyword, String score, String root, String description, String id) {
        this.keyword = keyword;
        this.score = score;
        this.root = root;
        this.description = description ;
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getScore() {
        return score;
    }

    public String getRoot() {
        return root;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getDescription() {return description;}

    public String getBaike_url() {return baike_url;}

    public void setDescription(String description) {this.description = description;}

    public void getInformation(String info){
        int baikes = info.indexOf("baike_url");
        int images = info.indexOf("image_url");
        int descri = info.indexOf("description");
        if (baikes != -1 && images != -1 && descri != -1) {
            baike_url = info.substring(baikes+12,images-3);
            image_url = info.substring(images+12,descri-3);
            description = info.substring(descri,info.length()-2);
            description = description.substring(14,description.length());
            getBaike_url();
        }
    }

}
