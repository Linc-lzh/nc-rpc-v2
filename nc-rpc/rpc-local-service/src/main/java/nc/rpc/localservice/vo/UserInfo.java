package nc.rpc.localservice.vo;

import java.io.Serializable;

public class UserInfo implements Serializable {
    private final String name;
    private final String phone;

    public UserInfo(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
}
