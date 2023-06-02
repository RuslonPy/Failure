package jpa.experiment.experimentjpa.model;

public enum Lang {
    UZ("UZ"),
    RU("RU"),
    EN("EN"),
    FR("FR"),
    DE("DE"),
    TR("TR"),
    TG("TG"),
    KAA("KAA");

    private String name;

    Lang(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Lang getByName(final String name) {
        for (Lang lang : Lang.values()) {
            if (lang.getName().equals(name)) {
                return lang;
            }
        }
        return EN;
    }
}
