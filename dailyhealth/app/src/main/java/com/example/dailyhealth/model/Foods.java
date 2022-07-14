package com.example.dailyhealth.model;

public class Foods {
    private Long foodId;
    private String foodName;
    private Long foodSize; // 총내용량
    private Long kcal; // 칼로리
    private Long carbohydrate; // 탄수화물
    private Long protein; // 단백질
    private Long fat; // 지방
    private Long natrium; // 나트륨
    private Long cholesterol;  // 콜레스테롤


    public Foods(Long foodId, String foodName, Long foodSize, Long kcal, Long carbohydrate, Long protein, Long fat, Long natrium, Long cholesterol) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.foodSize = foodSize;
        this.kcal = kcal;
        this.carbohydrate = carbohydrate;
        this.protein = protein;
        this.fat = fat;
        this.natrium = natrium;
        this.cholesterol = cholesterol;
    }

    public Foods(String foodName,Long kcal, Long carbohydrate, Long protein, Long fat) {
        this.foodName = foodName;
        this.kcal = kcal;
        this.carbohydrate = carbohydrate;
        this.protein = protein;
        this.fat = fat;
    }



    public Long getFoodId() {
        return foodId;
    }

    public void setFoodId(Long foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public Long getFoodSize() {
        return foodSize;
    }

    public void setFoodSize(Long foodSize) {
        this.foodSize = foodSize;
    }

    public Long getKcal() {
        return kcal;
    }

    public void setKcal(Long kcal) {
        this.kcal = kcal;
    }

    public Long getCarbohydrate() {
        return carbohydrate;
    }

    public void setCarbohydrate(Long carbohydrate) {
        this.carbohydrate = carbohydrate;
    }

    public Long getProtein() {
        return protein;
    }

    public void setProtein(Long protein) {
        this.protein = protein;
    }

    public Long getFat() {
        return fat;
    }

    public void setFat(Long fat) {
        this.fat = fat;
    }

    public Long getNatrium() {
        return natrium;
    }

    public void setNatrium(Long natrium) {
        this.natrium = natrium;
    }

    public Long getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(Long cholesterol) {
        this.cholesterol = cholesterol;
    }
}

