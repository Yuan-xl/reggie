package com.yuakk.dto;

import com.yuakk.pojo.Setmeal;
import com.yuakk.pojo.SetmealDish;
import lombok.Data;
import java.util.List;

/**
 * @author yuakk
 */
@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
