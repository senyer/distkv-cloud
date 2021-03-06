package cloud.distkv.common.constants;

public enum Msg {

  OK("OK", 200),
  ERROR("Error", 400),
  AUTHORIZATION("Unauthorized", 401),
  AUTHENTICATION("Authentication failure", 402),
  HTTP_REQUEST_ERROR("Unsupported Request", 403),
  DISTKV_ERROR("Distkv operation Error", 404),
  INCORRECT_TYPE("Sorry, incorrect request type.", 801),
  INNER_ERROR("Inner Error", 500);

  private String desc;
  private Integer statusCode;

  private Msg(String desc, Integer statusCode) {
    this.desc = desc;
    this.statusCode = statusCode;
  }

  private Msg(String desc) {
    this.desc = desc;
  }

  private Msg(int statusCode) {
    this.statusCode = statusCode;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public Integer getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(Integer statusCode) {
    this.statusCode = statusCode;
  }

  @Override
  public String toString() {
    return this.desc;
  }

}
