package com.mypackage.SpringStarter.util.constants;

public enum Prvilages {
    RESET_ANY_USER_PASSWORD(1L,"RESET_ANY_USER_PASSWORD"),
    ACCESS_ADMIN_PANEL(2L,"ACCESS_ADMIN_PANEL");
    
    private Long Id;
    private String prvilages;
    private Prvilages(Long Id, String prvilages) {
        this.Id = Id;
        this.prvilages = prvilages;
    }
    public Long getId() {
        return Id;
    }
    public String getprvilages() {
        return prvilages;
    }
    
}
