package com.example.dailyhealth.model;

public class FoodsRecord {
    private Long foodRecordId;
    private Long foodId; // foods 기본키
    private String foodName;
//    private Foods food;
//    private Date oneDay;
    private int month; // 월
    private String day; // 일
    private Long eatTime; // 아침:1, 점심:2, 저녁:3
    private Long userid; // 유저 기본키
    private Long kcal;
//    private User user;
    public FoodsRecord(){

    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public Long getKcal() {
        return kcal;
    }

    public void setKcal(Long kcal) {
        this.kcal = kcal;
    }

    public FoodsRecord(Long foodId, int month, String day, Long eatTime, Long userid) {
        this.foodId = foodId;
        this.month = month;
        this.day = day;
        this.eatTime = eatTime;
        this.userid = userid;
    }

    public Long getFoodRecordId() {
        return foodRecordId;
    }

    public void setFoodRecordId(Long foodRecordId) {
        this.foodRecordId = foodRecordId;
    }

    public Long getFoodId() {
        return foodId;
    }

    public void setFoodId(Long foodId) {
        this.foodId = foodId;
    }


    public Long getEatTime() {
        return eatTime;
    }

    public void setEatTime(Long eatTime) {
        this.eatTime = eatTime;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
