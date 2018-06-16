package z.xtreamiptv.playerv3.model.database;

public class PasswordStatusDBModel {
    private int idPaswordStaus;
    private String passwordStatus;
    private String passwordStatusCategoryId;
    private String passwordStatusUserDetail;

    public int getIdPaswordStaus() {
        return this.idPaswordStaus;
    }

    public void setIdPaswordStaus(int idPaswordStaus) {
        this.idPaswordStaus = idPaswordStaus;
    }

    public String getPasswordStatusCategoryId() {
        return this.passwordStatusCategoryId;
    }

    public void setPasswordStatusCategoryId(String passwordStatusCategoryId) {
        this.passwordStatusCategoryId = passwordStatusCategoryId;
    }

    public String getPasswordStatusUserDetail() {
        return this.passwordStatusUserDetail;
    }

    public void setPasswordStatusUserDetail(String passwordStatusUserDetail) {
        this.passwordStatusUserDetail = passwordStatusUserDetail;
    }

    public String getPasswordStatus() {
        return this.passwordStatus;
    }

    public void setPasswordStatus(String passwordStatus) {
        this.passwordStatus = passwordStatus;
    }

    public PasswordStatusDBModel(String passwordStatusCategoryId, String passwordStatusUserDetail, String passwordStatus) {
        this.passwordStatusCategoryId = passwordStatusCategoryId;
        this.passwordStatusUserDetail = passwordStatusUserDetail;
        this.passwordStatus = passwordStatus;
    }
}
