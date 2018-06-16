package z.xtreamiptv.playerv3.model.database;

public class DatabaseUpdatedStatusDBModel {
    private String dbCategory;
    private String dbCategoryID;
    private String dbLastUpdatedDate;
    private String dbUpadatedStatusState;
    private int idAuto;

    public String getDbCategory() {
        return this.dbCategory;
    }

    public void setDbCategory(String dbCategory) {
        this.dbCategory = dbCategory;
    }

    public String getGetDbCategoryID() {
        return this.dbCategoryID;
    }

    public void setDbCategoryID(String dbCategoryID) {
        this.dbCategoryID = dbCategoryID;
    }

    public DatabaseUpdatedStatusDBModel(String dbUpadatedStatusState, String dbLastUpdatedDate, String dbCategory, String dbCategoryID) {
        this.dbUpadatedStatusState = dbUpadatedStatusState;
        this.dbLastUpdatedDate = dbLastUpdatedDate;
        this.dbCategory = dbCategory;
        this.dbCategoryID = dbCategoryID;
    }

    public int getIdAuto() {
        return this.idAuto;
    }

    public void setIdAuto(int idAuto) {
        this.idAuto = idAuto;
    }

    public String getDbUpadatedStatusState() {
        return this.dbUpadatedStatusState;
    }

    public void setDbUpadatedStatusState(String dbUpadatedStatusState) {
        this.dbUpadatedStatusState = dbUpadatedStatusState;
    }

    public String getDbLastUpdatedDate() {
        return this.dbLastUpdatedDate;
    }

    public void setDbLastUpdatedDate(String dbLastUpdatedDate) {
        this.dbLastUpdatedDate = dbLastUpdatedDate;
    }
}
