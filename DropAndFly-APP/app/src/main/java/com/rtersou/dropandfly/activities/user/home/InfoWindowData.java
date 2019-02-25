package com.rtersou.dropandfly.activities.user.home;

import com.rtersou.dropandfly.models.Shop;

class InfoWindowData {
    private String title;
    private String description;

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    private Shop shop;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
