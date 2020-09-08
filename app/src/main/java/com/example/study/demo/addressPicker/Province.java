package com.example.study.demo.addressPicker;

public class Province {

    private String id;
    private String name = "";
    private String fid;
    private String level_id;

    public String getId() {
        return id;
    }

    public String getName() {
        if (name.equals("内蒙古自治区")) return "内蒙古";
        if (name.equals("广西壮族自治区")) return "广西";
        if (name.equals("西藏自治区")) return "西藏";
        if (name.equals("宁夏回族自治区")) return "宁夏";
        if (name.equals("新疆维吾尔自治区")) return "新疆";
        return name;
    }

    public String getFid() {
        return fid;
    }

    public String getLevel_id() {
        return level_id;
    }
}
