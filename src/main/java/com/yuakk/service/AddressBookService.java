package com.yuakk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yuakk.pojo.AddressBook;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author yuakk
 */
public interface AddressBookService extends IService<AddressBook> {
    /**
     * 更改默认地址
     * @param addressBook AddressBook
     * @return boolean
     */
    boolean changeDefaultAddress(@RequestBody AddressBook addressBook);
}
