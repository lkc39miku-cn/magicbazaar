package com.a243.magicbazaar.dao.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class ChoosePermission implements Serializable {
    @Serial
    private static final long serialVersionUID = 1236881990301197807L;
    private String title;
    private Long id;
    private String field;
    private Boolean checked = false;
    private Boolean spread = false;
    private Boolean isPermission;
    private List<ChoosePermission> children;
}
