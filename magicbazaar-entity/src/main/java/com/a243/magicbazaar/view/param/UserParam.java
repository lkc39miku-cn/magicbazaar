package com.a243.magicbazaar.view.param;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserParam {
    private PageParam pageParam;
    private String id;
    private String username;
    private String nickname;
    private String password;
    private String gender;
    private String phone;
    private String email;
    private String publishStatus;
    private String deleteStatus;
    private String commentStatus;
}
