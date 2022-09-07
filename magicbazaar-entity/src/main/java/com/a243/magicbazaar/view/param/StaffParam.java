package com.a243.magicbazaar.view.param;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class StaffParam {
    private PageParam pageParam;
    private String id;
    private String username;
    private String password;
    private String phone;
    private String phoneCode;
    private String email;
    private String emailCode;
    private String publishStatus;
    private String deleteStatus;
    private String idList;
}
