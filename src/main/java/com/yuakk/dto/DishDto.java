package com.yuakk.dto;

import com.yuakk.pojo.Dish;
import com.yuakk.pojo.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yuakk
 */
@Data
public class DishDto extends Dish {
    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
